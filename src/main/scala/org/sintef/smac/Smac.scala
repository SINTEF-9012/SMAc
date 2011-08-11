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
 * Authors: Brice Morin and Francois Fouquet
 * Company: SINTEF IKT, Oslo, Norway
 *          INRIA, Rennes, France
 * Date: 2011
 */
package org.sintef.smac

import scala.actors.Actor


/**
 * These classes should be extended to define the actions of the state
 * and the transitions of a given state machine
 */
abstract sealed class HandlerAction(master: Orchestrator) {
  
  protected[smac] var handler : Handler = _
  
  def getEvent(e : Event) : Option[Event] = handler.getEvent(e)
  
  def checkGuard: Boolean = true
  def getScore: Double = 1
  
  def executeActions()
}

abstract class TransitionAction(master: Orchestrator) extends HandlerAction(master) {
  def executeBeforeActions(){}
  def executeAfterActions(){}
}

abstract class InternalTransitionAction(master: Orchestrator) extends HandlerAction(master) {
  
}

abstract class StateAction(master: Orchestrator) {
  def onEntry
  def onExit
}


/**
 * These classes define the SMAc framework and its execution semantics.
 * They should not, and cannot (sealed), be extended by user state machines
 */
sealed class State(master: Orchestrator, action : StateAction) {

  protected[smac] var parent: Option[CompositeState] = Option(null)
  
  protected[smac] var internal: List[InternalTransition] = List()
  
  def addInternalTransition(t : InternalTransition) {
    internal ++= List(t)
  }
  
  //TODO: Union with internal
  protected[smac] def getOutgoingTransitions(): List[Handler] = {
    parent match {
      case Some(p) =>
        p.transitions.filter(t => t.getPrevious == this)
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
    getOutgoingTransitions.foreach{t => t.clearEvents}
    internal.foreach{t => t.clearEvents}
    parent match {
      case Some(p) =>
        p.clearEvents
      case None =>
    }
  }
    
  protected[smac] def checkForTransition: Option[Handler] = {
    //println(this+".checkForTransition: ")  
    internal.filter(t => {
        t.evaluateEvents && t.getAction.checkGuard
      }).sortWith((t, r) => t.getAction.getScore > r.getAction.getScore).headOption match {
      case Some(in) => 
        //println("  An internal transition can be triggered: "+in)
        return Option(in)
      case None => 
        //println("  No valid internal transition. Checking outgoing transitions")
        getOutgoingTransitions()
        .filter(t => {
            t.evaluateEvents && t.getAction.checkGuard
          }).sortWith((t, r) => t.getAction.getScore > r.getAction.getScore).headOption match {
          case Some(ext) => 
            //println("  A transition can be triggered: "+ext)
            return Option(ext)
          case None => 
            //println("  No valid transition")
            return Option(null)
        }
    }
  }

  protected[smac] def dispatchEvent(e: Event) : Boolean = {
    //println(this + ".dispatchEvent: " + e)
    ////println(this+".Dispatching: "+e)
    //TODO avoid duplication
    getOutgoingTransitions().filter(t => t.getEvents.exists(ev => ev.getClass == e.getClass))
    .foreach(t => {
        //println(this + "  Dispatching event to transition "+t)
        t.addEvent(e)
      })
    internal.filter(t => t.getEvents.exists(ev => ev.getClass == e.getClass))
    .foreach(t => {
        //println(this + "  Dispatching event to transition "+t)
        t.addEvent(e)
      })
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

sealed class StateMachine(master: Orchestrator, action : StateAction, keepHistory: Boolean) extends CompositeState(master, action, keepHistory) with Actor {
  override def act() {
    loop {
      react {
        case e: Event => {
            //println("  " + this + " receives and dispaches event " + e)
            //if(isCurrent) {
            dispatchEvent(e)
            //}
          }
      }
    }
  }  
}

sealed class CompositeState(master: Orchestrator, action : StateAction, keepHistory: Boolean) extends State(master, action) {
  
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

abstract sealed class Handler(master: Orchestrator) {
  
  protected[smac] def getAction: HandlerAction
 
  protected[smac] def getEvents = eventsMap.keys

  protected[smac] val eventsMap = scala.collection.mutable.Map[Event, Boolean]()
  
  def initEvent(e : Event) {
    eventsMap.put(e, false)
  }
  
  def getEvent(e : Event) : Option[Event] ={
    getEvents.filter(ev => ev.getClass == e.getClass).headOption
  }
  
  protected[smac] def addEvent(e : Event) {
    eventsMap.keys.filter{k => k.getClass == e.getClass}.headOption match { 
      case Some(k) =>
        eventsMap.remove(k)
        eventsMap.put(e, true)
      case None =>
    }
  }

  protected[smac] def evaluateEvents(): Boolean = {
    eventsMap.size == 0 || eventsMap.values.exists(v => v)
  }

  protected[smac] def clearEvents() {
    eventsMap.keys.foreach {
      k => eventsMap.put(k, false)
    }
  }

  /**
   * Describe the overall execution of the transition
   */
  def execute   
}

sealed class Transition(previous: State, next: State, master: Orchestrator, action: TransitionAction) extends Handler(master) {

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

sealed class InternalTransition(self: State, master: Orchestrator, action: InternalTransitionAction) extends Handler(master) {
  
  action.handler = this
  override def getAction = action
  
  override def execute() = {
    clearEvents
    action.executeActions()
  }
}

abstract case class Event()

/**
 * Orchestrator allows connecting different state machines together
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
class Orchestrator() extends Actor {

  protected var stateMachines = List[StateMachine]()

  def register(sm: StateMachine) = {
    ////println("Register "+sm)
    stateMachines ::= sm
  }

  override def act() = {
    loop {
      react {
        case e: Event =>
          stateMachines.foreach {
            sm =>
            //println("Orchestrator: dispatching " + e + " to " + sm)
            sm ! e
          }
        case e: Any =>
          ////println("Orchestrator_Any: " + e)
      }
    }
  }
}
