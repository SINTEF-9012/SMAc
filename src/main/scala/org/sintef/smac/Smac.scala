/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Authors: Brice Morin and Francois Fouquet
 * @Copyright: SINTEF IKT, Oslo, Norway
 * @Contact: <brice.morin@sintef.no>
 */
package org.sintef.smac

import scala.actors.Actor


/**
 * These classes should be extended to define the actions of the state
 * and the transitions of a given state machine
 */
abstract sealed class HandlerAction {
  
  protected[smac] var handler : Handler = _
  
  final def getEvent(e : String, p : String) : Option[Event] = handler.getEvent(e, p)
  
  def checkGuard: Boolean = true
  def getScore: Double = 1
  
  def executeActions()
   
}

abstract class TransitionAction extends HandlerAction {
  def executeBeforeActions(){}
  def executeAfterActions(){}
}

abstract class InternalTransitionAction extends HandlerAction {
  
}

abstract class StateAction {
  
  final def getStateMachine = handler.getRoot
  
  protected[smac] var handler : State = _
    
  def onEntry
  def onExit
}

///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////


/**
 * These classes define the execution semantics of SMAc.
 * They should not, and cannot (sealed), be extended by user state machines
 */
class FakeComponent extends Component {
  
}

abstract class Component {
  var ports : Map[String, Port] = Map()
  var behavior : List[StateMachine] = List()
  
  def start {
    ports.values.foreach{p => p.start}
    behavior.foreach{b => b.start}
  }
  
  final def getPort(name : String) : Option[Port] = {
    ports.get(name)
  }
}

sealed class State(action : StateAction, val root : Component) {
  
  def getEvent(e : String, p : Port) : Option[Event] = {
    parent match {
      case Some(parent) => parent.getEvent(e, p)
      case None => None
    } 
  }
  
  protected[smac] def getRoot = root
  
  action.handler = this
  
  protected[smac] var parent: Option[CompositeState] = None
  
  protected[smac] var internal: List[InternalTransition] = List()
  
  final def addInternalTransition(t : InternalTransition) {
    internal ++= List(t)
  }
  
  protected[smac] def allTransitions(): List[Handler] = {
    var result : List[Handler] = List()
    result = result ::: internal
    ////println(result.size)
    parent match {
      case Some(p) =>
        p match {
          case c : CompositeState =>
            result = result ::: c.transitions.filter(t => t.source == this)
          case _ =>
        }
        
        ////println(result.size)
      case None =>
        result
    }
    ////println(result.size)
    return result
  }
    

  //TODO: avoid (almost) duplicating checkForTransition...  
  protected[smac] def checkForAutoTransition: Option[Handler] = {
     allTransitions.filter(t => { t.isAuto && t.getAction.checkGuard})
    .sortWith((t, r) => (t.isInstanceOf[InternalTransition] && r.isInstanceOf[Transition]) || (t.getAction.getScore > r.getAction.getScore))
    .headOption match {
      case Some(in) => 
        ////println("  A transition can be triggered: "+in)
        return Option(in)
      case None => 
        return None
    }
  }
  
  protected[smac] def checkForTransition(e : SignedEvent): Option[Handler] = {
    ////println(this+".checkForTransition: ")  
    allTransitions.filter(t => { t.isInterestedIn(e) && t.getAction.checkGuard})
    .sortWith((t, r) => (t.isInstanceOf[InternalTransition] && r.isInstanceOf[Transition]) || (t.getAction.getScore > r.getAction.getScore))
    .headOption match {
      case Some(in) => 
        ////println("  A transition can be triggered: "+in)
        return Option(in)
      case None => 
        return None
    }
  }


  protected[smac] def dispatchEvent(e: SignedEvent) : Boolean = {
    checkForTransition(e) match {
      case Some(t) => 
        //println("  "+this + ".Transition: " + t)
        t.execute
        return true
      case None =>
        //println("  "+this + "No Transition")
        return false
    }
  }

  protected[smac] def executeOnEntry() {
    parent match {
      case Some(p) => p.current = this
      case None =>
    } 
    action.onEntry
    checkForAutoTransition match {//checks if a transition with no event can be triggered
      case Some(t) => {t.execute}
      case None =>
    }
  }

  protected[smac] def executeOnExit() {
    action.onExit
  }
}

sealed trait Region {
  var keepsHistory = false
  
  protected[smac] var initial: State = _

  protected[smac] var current: State = _
  
  protected[smac] var substates: List[State] = List()
  
  val actor = new Actor{
    override def act() = {
      loop {
        react {
          case e: SignedEvent =>
            //println("Region " + this + " dispatching event "+e + " to "+current)
            current.dispatchEvent(e)
          case _ =>
        }
      }
    }
  }
  
  def getActor = actor
  
  def setHistory(h : Boolean) {keepsHistory = h}
  
  def setInitial(i : State) {
    initial = i
    current = initial
  }

  def addSubState(sub : State) {
    substates ++= List(sub)
    this match {
      case c : CompositeState => sub.parent = Option(c)
      case _ =>
    }
    
  }
  
  def start { 
    actor.start
    current.executeOnEntry 
  }
  
}

sealed class StateMachine(action : StateAction, keepHistory: Boolean, root : Component) extends CompositeState(action, keepHistory, root) {
  private var currentEvents : Map[Port, Event] = Map()
  
  override def dispatchEvent(e: SignedEvent) : Boolean = {
    currentEvents +=  (e.port -> e.event)
    super.dispatchEvent(e) 
  }
  
  override def getEvent(e : String, p : Port) : Option[Event] = {
    currentEvents.keys.filter{port => port == p}.headOption match {
      case Some(port) =>
        currentEvents.get(port)
      case None => None
    }
  }
}

sealed class CompositeState(action : StateAction, keepHistory: Boolean, root : Component) extends State(action, root) with Region {
  
  protected[smac] var regions : List[Region] = List()
  
  protected[smac] var transitions: List[Transition] = List()

  
  def addRegion(r : Region) {
    regions ++= List(r)
  }
  
  def addTransition(t : Transition) {
    transitions ++= List(t)
  }  

  override def dispatchEvent(e: SignedEvent) : Boolean = {
    //println(this + ".dispatchEvent "+e)
    var status = false
    regions.foreach{r => //events are dispatched to regions with no condition
      //println("  to region "+r)
      r.getActor ! e
    }
    if (!current.dispatchEvent(e)) { //composite dispatch event to sub-states, which might consume the event
      //println("  current has not been activated. Try to activate self")
      status = super.dispatchEvent(e)//if not, they might consume the event
    }
    return status
  }

  override def executeOnEntry() {
    super.executeOnEntry
    current.executeOnEntry
  }

  override def executeOnExit() {
    if (!keepHistory) {
      current = initial
    } 
    current.executeOnExit
    super.executeOnExit
  }
  
  /*protected[smac] def clearEvents(s : State) {
   substates.filter{sub => sub != s}.foreach{sub => sub.clearEvents(false)}
   super.clearEvents(true)
   }*/
}


/**
 * Transitions between two states
 * Transition are interested in event (String corresponding to Event.getName)
 * coming from specified ports
 */


//Port -> Event
abstract sealed class Handler(val source : State, val events : List[Pair[String, String]]) {
  
  final def getPort(name : String) : Option[Port] = {
    //TODO: check if handler can access the port
    source.root.getPort(name)
  }
  
  protected[smac] def isInterestedIn(p : String, e : String) : Boolean = {
    events.exists(k => k._1 == p && k._2 == e)
  }
  
  protected[smac] def isInterestedIn(e : SignedEvent) : Boolean = {
    isInterestedIn(e.port.name, e.event.name)
  }
  
  protected[smac] def isAuto = events.length == 0
  
  protected[smac] def getEvent(e : String, p : String) : Option[Event] = {
    getPort(p) match {
      case Some(port) => source.getEvent(e, port)
      case None => None
    }    
  }
  
  protected[smac] def getAction: HandlerAction
      
  /**
   * Describe the overall execution of the transition
   */
  protected[smac] def execute
}

sealed class Transition(override val source: State, val target: State, val action: TransitionAction, override val events : List[Pair[String, String]] = List()) extends Handler(source, events) {

  override protected[smac] def getAction = action
  
  action.handler = this

  def execute() = {
    action.executeBeforeActions
    source.executeOnExit
    action.executeActions()
    target.executeOnEntry
    action.executeAfterActions
  }
}

sealed class InternalTransition(override val source: State, val action: InternalTransitionAction, override val events : List[Pair[String, String]] = List()) extends Handler(source, events) {
  
  override protected[smac] def getAction = action
  
  action.handler = this
  
  def execute() = {
    action.executeActions()
  }
}

sealed protected[smac] class SignedEvent(val sender : Component, val port : Port, val event : Event, val to : Option[Component] = None)

abstract class Event(val name : String){}

/**
 * Channel allows connecting different state machines together via ports
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
sealed class Port(val name : String, val receive : List[String], val send : List[String], val cpt : Component) extends Actor {
  
  cpt.ports += (name -> this)
  
  protected[smac] var out : List[Channel] = List()
  
  override def act() = {
    loop {
      react {
        case e: SignedEvent =>
          if (canReceive(e))
            //println("Port " + this + " dispatches to state machine")
          cpt.behavior.foreach{sm => 
            //println("  "+sm)
            sm.getActor ! new SignedEvent(e.sender, this, e.event, e.to)}
        case _ =>
      }
    }
  }
  
  def send(e : Event) {
    if (canSend(e)) {
      //println("Port " + this + " sending to channels")
      out.foreach{c =>
        //println("Port " + this + " sending to channel "+c)
        c ! new SignedEvent(cpt, this, e)
      }
    }
  }
  
  protected[smac] def canSend(e : Event) = {
    send.exists(p => p.equals(e.name))
  }
  
  protected[smac] def canReceive(e : SignedEvent) = {
    receive.exists(p => p.equals(e.event.name))
  }
}

sealed class Channel() extends Actor {

  override def start : Actor = {
    super.start
    return this
  }
  
  protected var out = List[Port]()
  
  def connectIn(p : Port){
    p.out ::= this
  }
  
  def connectOut(p : Port){
    out ::= p
  }

  def connect(p: Port, p2: Port) = {
    p.out ::= this
    out ::= p2
  }
  
  def disconnect(p : Port) {
    p.out - this
    out - p
  }

  override def act() = {
    loop {
      react {
        case e: SignedEvent =>
          e.to match {
            case Some(to) => out.filter{p => p.cpt == to}.foreach {
                p =>
                ////println("Channel dispatching " + e + " to " + p)
                p forward e
              }
            case None => out.foreach {
                p =>
                ////println("Channel dispatching " + e + " to " + p)
                p forward e
              }
          }
        case e: Any =>
      }
    }
  }
}

/**
 * Just a naive PoC
 * TODO: a lot
 */
sealed class SessionChannel extends Channel {
  
}