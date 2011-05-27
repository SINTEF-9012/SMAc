package org.sintef.smac.samples.pingpong

import org.sintef.smac._
import org.sintef.smac.samples.pingpong._

class PongStateMachine(master : Orchestrator, parent : CompositeState, keepHistory : Boolean) extends CompositeState(master, parent, keepHistory) {
  
  //create sub-states
  val pong = Pong(master, this)
  override val substates = List(pong)
  override val initial = pong
  
  //create transitions among sub-states
  val pingTransition = PingTransition(pong, pong, master, List(PingEvent))
  override val outGoingTransitions = List(pingTransition)
  
  def onEntry() = {}
  
  def onExit() = {}
}

case class Pong(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("Pong.onEntry")
    master ! PongEvent
  }
  
  override def onExit() = {
    println("Pong.onExit")
  }
  
}

//Messages defined in the state machine
case class PingTransition(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) {
  def executeActions() = {
    println("PingTransition")
  }
}