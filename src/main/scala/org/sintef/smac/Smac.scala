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
import scala.actors.TIMEOUT

/**
 * Atomic states should extend State
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 */
abstract class State(master: Orchestrator, parent: Option[CompositeState]) extends Actor {

  def getOutgoingTransitions(): List[Transition] = {
    parent match {
      case Some(p) =>
        p.outGoingTransitions.filter(t => t.getPrevious == this)
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
    
    


  def checkForTransition: Option[Transition] = {
    //println(this+".checkForTransition: ")
    getOutgoingTransitions()
    .filter(t => {
        t.evaluateEvents && t.checkGuard
      })
    .sortWith((t, r) => t.getScore > r.getScore).headOption
  }

  def dispatchEvent(e: Event) {
    //println(this+".Dispatching: "+e)
    getOutgoingTransitions().filter(t => t.getEvents.exists(ev => ev.getClass == e.getClass))
    .foreach(t => {
        //println("  Dispatching event to transition "+t)
        t.addEvent(e)
      })
    checkForTransition match {
      case Some(t) => 
        //println("Transition: "+t)
        t.execute
      case None =>
        //println("No Transition")
    }
  }

  override def act() {
    loop {
      react {
        case e: Event => {
            //println("dispacth "+e)
            if(isCurrent) {
              dispatchEvent(e)
            }
            else {
              parent match {
                case Some(p) => p.current ! e
                case None =>
              }
            }
          }
      }
    }
  }

  def executeOnEntry() {
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
    start
    /*if (parent == null || parent == this || parent.current == this)
     executeOnEntry*/
  }

}

abstract class CompositeState(master: Orchestrator, parent: Option[CompositeState], keepHistory: Boolean) extends State(master, parent) {

  val substates: List[State]

  val outGoingTransitions: List[Transition]

  val initial: State

  var current: State = _

  override def dispatchEvent(e: Event) {
    super.dispatchEvent(e)
    current ! e
  }

  override def startState(): Unit = {
    current = initial
    super.startState()
    substates.foreach {
      s =>
      //println("  debug "+s)
      s.startState
    }
    parent match {
      case Some(p) =>
      case None => //Root composite
        master.register(this)
        executeOnEntry
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
abstract class Transition(previous: State, next: State, master: Orchestrator) {

  def getPrevious = previous

  def getEvents = eventsMap.keys

  def checkGuard: Boolean = true

  def getScore: Double = 1

  val eventsMap = scala.collection.mutable.Map[Event, Boolean]()
  
  def initEvent(e : Event) {
    eventsMap.put(e, false)
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
    eventsMap.keys.forall(k => {
        eventsMap.getOrElse(k, false)
      })
  }

  def executeActions()

  final def clearEvents() {
    eventsMap.keys.foreach {
      k => eventsMap.put(k, false)
    }
  }

  def execute() = {
    clearEvents
    previous.executeOnExit
    executeActions()
    next.executeOnEntry
  }
}

abstract class TimedTransition(previous: State, next: State, master: Orchestrator, delay: Long) extends Transition(previous, next, master) {

  var timer: Actor = null;

  initEvent(TIMEOUT_EVENT(this))

  case class STOP_T_TIMER()

  def initTimer {
    timer = new Actor {
      def act =
        loop {
          reactWithin(delay) {
            case STOP_T_TIMER() => { reply(true);exit }
            case TIMEOUT => {
                if (previous.isCurrent) {
                  getEvents.filter(p => p.isInstanceOf[TIMEOUT_EVENT]).foreach {
                    event => previous ! event
                  }
                }
              }
          }
        }
    }.start
  }

  override def evaluateEvents(): Boolean = {
    if (timer == null) {
      initTimer
    }
    super.evaluateEvents
  }

  override def execute() = {
    if (timer != null) {
      timer !? STOP_T_TIMER()
      timer = null
    }
    super.execute
  }
}

abstract case class Event()
case class TIMEOUT_EVENT(t: Transition) extends Event


/**
 * Orchestrator allows connecting different state machines together
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
class Orchestrator() extends Actor {

  var stateMachines = List[Actor]()

  def register(sm: Actor) = {
    //println("Register "+sm)
    stateMachines ::= sm
  }

  override def act() = {
    loop {
      react {
        case e: Event =>
          stateMachines.foreach {
            //println("Orchestrator_Event: " + e)
            sm =>
            sm ! e
          }
        case e: Any =>
          //println("Orchestrator_Any: " + e)
      }
    }
  }
}
