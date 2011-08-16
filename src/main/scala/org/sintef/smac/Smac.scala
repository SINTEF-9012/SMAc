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
  
  final def getStateMachine = handler.getRoot
  
  final def getEvent(e : Event) : Option[Event] = handler.getEvent(e)
  
  final def getEvent(e : String) : Option[Event] = handler.getEvent(e)
  
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
  
  protected[smac] def getRoot = root
  
  action.handler = this
  
  protected[smac] var parent: Option[CompositeState] = Option(null)
  
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
            result = result ::: c.transitions.filter(t => t.getPrevious == this)
          case _ =>
        }
        
        ////println(result.size)
      case None =>
        result
    }
    ////println(result.size)
    return result
  }
  
  protected[smac] def clearEvents(goBack : Boolean) {
    allTransitions.foreach{t => t.clearEvents}
    if (goBack){
      parent match {
        case Some(p) =>
          p.clearEvents(this)
        case None =>
      }
    }
  }
      
  protected[smac] def checkForTransition: Option[Handler] = {
    ////println(this+".checkForTransition: ")  
    allTransitions.filter(t => { t.evaluateEvents && t.getAction.checkGuard})
    .sortWith((t, r) => (t.isInstanceOf[InternalTransition] && r.isInstanceOf[Transition]) || (t.getAction.getScore > r.getAction.getScore))
    .headOption match {
      case Some(in) => 
        ////println("  A transition can be triggered: "+in)
        return Option(in)
      case None => 
        return None
    }
  }


  protected[smac] def dispatchEvent(e: Event) : Boolean = {
    //println(this + "dispatch event "+e)
    allTransitions().foreach{t => 
      //println("  "+t)
      t.addEvent(e)
    }
    checkForTransition match {
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
    checkForTransition match {
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
          case e: Event =>
            //println("Region " + this + " dispatching event "+e + " to "+current)
            current.dispatchEvent(e)
          case e: Any =>
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

  override def dispatchEvent(e: Event) : Boolean = {
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
    /*else {
     clearEvents
     }*/
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
  
  protected[smac] def clearEvents(s : State) {
    substates.filter{sub => sub != s}.foreach{sub => sub.clearEvents(false)}
    super.clearEvents(true)
  }
}


/**
 * Transitions between two states
 */

abstract sealed class Handler(val root : Component) {
  
  final def getPort(name : String) : Option[Port] = {
    root.ports.get(name)
  }
  
  protected[smac] def isInterestedIn(e : Event) : Boolean = {
    ////println(this+" is interested in "+e.getClass.toString+"?")
    events.keys.exists(k => k.equals(e.name))
  }
  
  protected[smac] def getRoot = root
  
  protected[smac] def getAction: HandlerAction
 
  protected[smac] def getEvents = events.keys

  protected[smac] val events = scala.collection.mutable.Map[String, Pair[Event, Boolean]]()
  
  final def initEvent(e : String) {
    ////println(this+" init event "+e)
    events.put(e, (null, false))
  }
  
  final def initEvent(e : Event) {
    initEvent(e.name)
  }
  
  final def getEvent(e : Event) : Option[Event] = {
    getEvent(e.name)
  }
  
  final def getEvent(e : String) : Option[Event] = {
    events.get(e) match {
      case Some(p) =>
        if (p._2) {
          return Option(p._1)
        }
        else {
          return None
        }
      case None =>
        return None
    }
  }
  
  protected[smac] def addEvent(e : Event) {
    if (isInterestedIn(e)){
      ////println("  "+this+" is interested in "+e.getClass.toString+"!!!")
      events.put(e.name, (e, true))
    }
  }

  protected[smac] def evaluateEvents(): Boolean = {
    events.size == 0 || events.values.exists(p => p._2)
  }

  protected[smac] def clearEvents() {
    //println(this + ".clearEvents")
    events.keys.foreach {
      k => events.put(k, (null, false))
    }
  }

  /**
   * Describe the overall execution of the transition
   */
  protected[smac] def execute {
    clearEvents
  }
}

sealed class Transition(previous: State, next: State, action: TransitionAction, root : Component) extends Handler(root) {

  action.handler = this
  override def getAction = action
  
  protected[smac] def getPrevious = previous

  override def execute() = {
    action.executeBeforeActions
    previous.executeOnExit
    action.executeActions()
    next.executeOnEntry
    action.executeAfterActions
    previous.clearEvents(true)
  }
}

sealed class InternalTransition(self: State, action: InternalTransitionAction, root : Component) extends Handler(root) {
  
  action.handler = this
  override def getAction = action
  
  override def execute() = {
    ////println("EXECUTE")
    action.executeActions()
    self.clearEvents(true)
  }
}


abstract class Event(val name : String, val to : Option[Component] = None){
  private var source : Option[Component] = None
  def setSender(c : Component) { 
    source match {
      case Some(s) => println("Cannot change the sender of a message")
      case None => source = Option(c)
    }
  }
  def getSender = source
}



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
        case e: Event =>
          e.getSender match {
            case Some(c) => if (canReceive(e))
              //println("Port " + this + " dispatches to state machine")
              cpt.behavior.foreach{sm => 
                //println("  "+sm)
                sm.getActor ! e}
            case None =>
              println("This event does not come from a component")
          }
          
        case e: Any =>
      }
    }
  }
  
  def send(e : Event) {
    if (canSend(e)) {
      //println("Port " + this + " sending to channels")
      e.setSender(cpt)
      out.foreach{c =>
        //println("Port " + this + " sending to channel "+c)
        c ! e
      }
    }
  }
  
  protected[smac] def canSend(e : Event) = {
    send.exists(p => p.equals(e.name))
  }
  
  protected[smac] def canReceive(e : Event) = {
    receive.exists(p => p.equals(e.name))
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
        case e: Event =>
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