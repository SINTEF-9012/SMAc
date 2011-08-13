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
sealed class Port(val name : String, val receive : List[String], val send : List[String], sm : StateMachine) extends Actor {
  
  sm.ports += (name -> this)
  
  protected[smac] var out : List[Channel] = List()
  
  override def act() = {
    loop {
      react {
        case e: Event =>
          if (canReceive(e))
            //println("Port " + this + " dispatches to state machine")
            sm.dispatchEvent(e)
        case e: Any =>
          ////println("Orchestrator_Any: " + e)
      }
    }
  }
  
  def send(e : Event) {
    if (canSend(e)) {
      //println("Port " + this + " sending to channels")
      out.foreach{c =>
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

sealed class State(action : StateAction, val root : StateMachine) {
  
  protected[smac] def getRoot = root
  
  final def getPort(name : String) : Option[Port] = {
    getRoot.ports.get(name)
  }
  
  action.handler = this
  
  protected[smac] var parent: Option[CompositeState] = Option(null)
  
  protected[smac] var internal: List[InternalTransition] = List()
  
  final def addInternalTransition(t : InternalTransition) {
    internal ++= List(t)
  }
  
  //TODO: Union with internal
  protected[smac] def allTransitions(): List[Handler] = {
    var result : List[Handler] = List()
    result :: internal
    parent match {
      case Some(p) =>
        result ::: p.transitions.filter(t => t.getPrevious == this)
      case None =>
        List()
    }
  }

  protected[smac] def isCurrent : Boolean = {
    parent match {
      case Some(p) => p.current == this
      case None => true
    }
  }
  
  protected[smac] def clearEvents() {
    allTransitions.foreach{t => t.clearEvents}
    parent match {
      case Some(p) =>
        p.clearEvents
      case None =>
    }
  }
      
  protected[smac] def checkForTransition: Option[Handler] = {
    //println(this+".checkForTransition: ")  
    allTransitions.filter(t => { t.evaluateEvents && t.getAction.checkGuard})
    .sortWith((t, r) => (t.isInstanceOf[InternalTransition] && r.isInstanceOf[Transition]) || (t.getAction.getScore > r.getAction.getScore))
    .headOption match {
      case Some(in) => 
        //println("  An internal transition can be triggered: "+in)
        return Option(in)
      case None => 
        return None
    }
  }


  protected[smac] def dispatchEvent(e: Event) : Boolean = {
    allTransitions().foreach(t => t.addEvent(e))
    checkForTransition match {
      case Some(t) => 
        //println(this + ".Transition: " + t)
        t.execute
        return true
      case None =>
        //println(this + "No Transition")
        return false
    }
  }

  protected[smac] def executeOnEntry() {
    clearEvents
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

sealed class StateMachine(action : StateAction, keepHistory: Boolean, root : StateMachine = null) extends CompositeState(action, keepHistory, root) {
  
  protected[smac] override def getRoot = this
  
  var ports : Map[String, Port] = Map()
  
  final def start { current.executeOnEntry }
}

sealed class CompositeState(action : StateAction, keepHistory: Boolean, root : StateMachine) extends State(action, root) {
  
  def addSubState(sub : State) {
    substates ++= List(sub)
    sub.parent = Option(this)
  }
  
  def addTransition(t : Transition) {
    transitions ++= List(t)
  }
  
  def setInitial(i : State) {
    initial = i
    current = initial
  }

  protected[smac] var substates: List[State] = List()

  protected[smac] var transitions: List[Transition] = List()

  protected[smac] var initial: State = _

  protected[smac] var current: State = _

  override def dispatchEvent(e: Event) : Boolean = {
    if (!current.dispatchEvent(e)) { //composite dispatch event to sub-states, which might consume the event
      return super.dispatchEvent(e)//before checking if a transition is valid
    }
    else {
      return false
    }
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
}


/**
 * Transitions between two states
 */

abstract sealed class Handler(val root : StateMachine) {
  
  final def getPort(name : String) : Option[Port] = {
    getRoot.ports.get(name)
  }
  
  protected[smac] def isInterestedIn(e : Event) : Boolean = {
    //println(this+" is interested in "+e.getClass.toString+"?")
    events.keys.exists(k => k.equals(e.name))
  }
  
  protected[smac] def getRoot = root
  
  protected[smac] def getAction: HandlerAction
 
  protected[smac] def getEvents = events.keys

  protected[smac] val events = scala.collection.mutable.Map[String, Pair[Event, Boolean]]()
  
  final def initEvent(e : String) {
    //println(this+" init event "+e)
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
      //println("  "+this+" is interested in "+e.getClass.toString+"!!!")
      events.put(e.name, (e, true))
    }
  }

  protected[smac] def evaluateEvents(): Boolean = {
    events.size == 0 || events.values.exists(p => p._2)
  }

  protected[smac] def clearEvents() {
    events.keys.foreach {
      k => events.put(k, (null, false))
    }
  }

  /**
   * Describe the overall execution of the transition
   */
  protected[smac] def execute   
}

sealed class Transition(previous: State, next: State, action: TransitionAction, root : StateMachine) extends Handler(root) {

  action.handler = this
  override def getAction = action
  
  protected[smac] def getPrevious = previous

  override def execute() = {
    clearEvents
    action.executeBeforeActions
    previous.executeOnExit
    action.executeActions()
    next.executeOnEntry
    action.executeAfterActions
  }
}

sealed class InternalTransition(self: State, action: InternalTransitionAction, root : StateMachine) extends Handler(root) {
  
  action.handler = this
  override def getAction = action
  
  override def execute() = {
    clearEvents
    action.executeActions()
  }
}


abstract case class Event(val name : String){}



/**
 * Channel allows connecting different state machines together via ports
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
sealed class Channel() extends Actor {

  protected var out = List[Port]()

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
          out.foreach {
            p =>
            //println("Channel dispatching " + e + " to " + p)
            p ! e
          }
        case e: Any =>
          ////println("Orchestrator_Any: " + e)
      }
    }
  }
}
