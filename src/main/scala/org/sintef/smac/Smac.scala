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
 * Atomic states should extend State
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 */
abstract class State(master: Orchestrator) {

  var parent: Option[CompositeState] = Option(null)
  
  var internal: List[InternalTransition] = List()
  
  def addInternalTransition(t : InternalTransition) {
    internal ++= List(t)
  }
  
  //TODO: Union with internal
  def getOutgoingTransitions(): List[Handler] = {
    parent match {
      case Some(p) =>
        p.transitions.filter(t => t.getPrevious == this)
      case None =>
        List()
    }
  }

  def isCurrent : Boolean = {
    parent match {
      case Some(p) => p.current == this
      case None => true
    }
  }
  
  final def clearEvents() {
    getOutgoingTransitions.foreach{t => t.clearEvents}
    internal.foreach{t => t.clearEvents}
    parent match {
      case Some(p) =>
        p.clearEvents
      case None =>
    }
  }
    
  def checkForTransition: Option[Handler] = {
    //println(this+".checkForTransition: ")  
    internal.filter(t => {
        t.evaluateEvents && t.checkGuard
      }).sortWith((t, r) => t.getScore > r.getScore).headOption match {
      case Some(in) => 
        //println("  An internal transition can be triggered: "+in)
        return Option(in)
      case None => 
        //println("  No valid internal transition. Checking outgoing transitions")
        getOutgoingTransitions()
        .filter(t => {
            t.evaluateEvents && t.checkGuard
          }).sortWith((t, r) => t.getScore > r.getScore).headOption match {
          case Some(ext) => 
            //println("  A transition can be triggered: "+ext)
            return Option(ext)
          case None => 
            //println("  No valid transition")
            return Option(null)
        }
    }
  }

  def dispatchEvent(e: Event) : Boolean = {
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

  def executeOnEntry() {
    clearEvents
    parent match {
      case Some(p) => p.current = this
      case None =>
    } 
    onEntry
    checkForTransition match {
      case Some(t) => {t.execute}
      case None =>
    }
  }

  def executeOnExit() {
    onExit
  }

  def onEntry

  def onExit

  def startState(): Unit = {
    //start
  }

}

abstract class StateMachine(master: Orchestrator, keepHistory: Boolean) extends CompositeState(master, keepHistory) with Actor {
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
  
  override def startState(): Unit = {
    master.register(this)
    start
    super.startState
  }
}

abstract class CompositeState(master: Orchestrator, keepHistory: Boolean) extends State(master) {
  
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

  var substates: List[State] = List()

  var transitions: List[Transition] = List()

  var initial: State = _

  var current: State = _

  override def dispatchEvent(e: Event) : Boolean = {
    if (!current.dispatchEvent(e)) { //composite dispatch event to sub-states, which might consume the event
      return super.dispatchEvent(e)//before checking if a transition is valid
    }
    else {
      return false
    }
  }

  override def startState(): Unit = {
    //current = initial
    super.startState()
    substates.foreach {
      s =>
      ////println("  debug "+s)
      s.startState
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

abstract class Handler(master: Orchestrator) {
 
  def getEvents = eventsMap.keys

  def checkGuard: Boolean = true

  def getScore: Double = 1

  val eventsMap = scala.collection.mutable.Map[Event, Boolean]()
  
  def initEvent(e : Event) {
    eventsMap.put(e, false)
  }
  
  def getEvent(e : Event) : Event ={
    getEvents.filter(ev => ev.getClass == e.getClass).head
  }
  
  def addEvent(e : Event) {
    eventsMap.keys.filter{k => k.getClass == e.getClass}.headOption match { 
      case Some(k) =>
        eventsMap.remove(k)
        eventsMap.put(e, true)
      case None =>
    }
  }

  def evaluateEvents(): Boolean = {
    eventsMap.size == 0 || eventsMap.values.exists(v => v)
    /*eventsMap.keys.forall(k => {
        eventsMap.getOrElse(k, false)
      })*/
  }

  //TODO: Once the event is consumed, it should be cleared for ALL the transition (?)
  final def clearEvents() {
    eventsMap.keys.foreach {
      k => eventsMap.put(k, false)
    }
  }

  /**
   * Describe the overall execution of the transition
   */
  def execute 
  
  /**
   * Only describes the execution of the actions associated with the transition
   */
  def executeActions()
}

abstract class Transition(previous: State, next: State, master: Orchestrator) extends Handler(master) {

  def getPrevious = previous

  def executeBeforeActions(){}
  def executeAfterActions(){}

  override def execute() = {
    clearEvents
    executeBeforeActions
    previous.executeOnExit
    executeActions()
    next.executeOnEntry
    executeAfterActions
  }
}

abstract class InternalTransition(self: State, master: Orchestrator) extends Handler(master) {
  override def execute() = {
    clearEvents
    executeActions()
  }
}

abstract case class Event()

/**
 * Orchestrator allows connecting different state machines together
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
class Orchestrator() extends Actor {

  var stateMachines = List[StateMachine]()

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
