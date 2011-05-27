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
abstract class State(master: Orchestrator, parent: CompositeState) extends Actor {

  def getOutgoingTransitions(): List[Transition] = {
    if (parent != null) {
      parent.outGoingTransitions.filter {
        t => t.getPrevious == this
      }
    }
    else {
      List()
    }
  }

  def isCurrent = parent.current == this


  def checkForTransition: Option[Transition] = {
    getOutgoingTransitions()
      .filter(t => {
      t.evaluateEvents && t.checkGuard
    })
      .sortWith((t, r) => t.getScore > r.getScore).headOption
  }

  def dispatchEvent(e: Event) {
    getOutgoingTransitions().filter(t => t.getEvents.exists(ev => ev.getClass == e.getClass))
      .foreach(t => {
      t.eventsMap.put(e, true)
    })
  }

  override def act() {
    loop {
      react {
        case e: Event =>
          dispatchEvent(e)
          checkForTransition match {
            case Some(t) => t.execute
            case None =>
          }
      }
    }
  }

  def executeOnEntry() {
    parent.current = this
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
  }

}

abstract class CompositeState(master: Orchestrator, parent: CompositeState, keepHistory: Boolean) extends State(master, parent) {

  val substates: List[State]

  val outGoingTransitions: List[Transition]

  val initial: State

  var current: State = null

  var history: State = null

  override def dispatchEvent(e: Event) {
    //println("Dispatching: "+e)
    if (current != null) {
      current ! e
    }
    super.dispatchEvent(e)
  }

  override def act() = {
    loop {
      react {
        case e: Event =>
          dispatchEvent(e)
        case e: Any =>
          println("Discarded: " + e)
      }
    }
  }

  override def startState(): Unit = {
    super.startState()
    if (parent == null || parent == this) {
      //root composite
      //println("Root.current = "+initial)
      current = initial
      master.register(this)
    }
    else if (parent.current == this) {
      //println("Composite.current = "+initial)
      current = initial
    }
    substates.foreach {
      s =>
      //println("  debug "+s)
        s.startState
    }
  }

  override def executeOnEntry() {
    super.executeOnEntry
    if (keepHistory && history != null) {
      history.executeOnEntry
    }
    else {
      initial.executeOnEntry
    }
  }

  override def executeOnExit() {
    if (keepHistory) {
      history = current
    }
    current.executeOnExit
    super.executeOnExit
  }

}

/**
 * Transitions between two states
 */
abstract class Transition(previous: State, next: State, master: Orchestrator, events: List[Event]) {

  def getPrevious = previous

  def getEvents = events

  def checkGuard: Boolean = true

  def getScore: Double = 1

  var eventsMap = scala.collection.mutable.Map[Event, Boolean]()
  clearEvents()

  def evaluateEvents(): Boolean = {
    eventsMap.keys.forall(k => {
      eventsMap.getOrElse(k, false)
    })
  }

  def executeActions()

  final def clearEvents() {
    getEvents.foreach {
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

abstract class TimedTransition(previous: State, next: State, master: Orchestrator, events: List[Event], delay: Long) extends Transition(previous, next, master, events ) {

  var timer: Actor = null;

  override def getEvents = List(TIMEOUT_EVENT(this)) ++events

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

abstract case class Event() {}
case class TIMEOUT_EVENT(t: Transition) extends Event


/**
 * Orchestrator allows connecting different state machines together
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
class Orchestrator() extends Actor {

  var stateMachines = List[CompositeState]()

  def register(sm: CompositeState) = {
    //println("Register "+sm)
    stateMachines ::= sm
  }

  override def act() = {
    loop {
      react {
        case e: Event =>
          stateMachines.foreach {
            sm =>
              sm ! e
          }
        case e: Any =>
          println("Orchestrator_Any: " + e)
      }
    }
  }
}
