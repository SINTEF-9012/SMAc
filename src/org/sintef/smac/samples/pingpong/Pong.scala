package org.sintef.smac.samples.pingpong

import org.sintef.smac._
import org.sintef.smac.samples.pingpong._


class PongMachineBuilder(master : Orchestrator) extends StateMachineBuilder(master) {
  def createStateMachine() : StateMachine = {
    //create sub-states
    val pong = Pong(master)

    var substates = List[State]()
    substates ::= pong

    
    //create transitions among sub-states
    val pingTransition = PingTransition(pong, pong, master)
  
    var outGoingTransitions = List[Transition]()
    outGoingTransitions ::= pingTransition

  
    //finally, create the state machine
    val pongSM : StateMachine = new PongStateMachine(master, substates, pong, outGoingTransitions)
  
    return pongSM
  }
}


class PongStateMachine(master : Orchestrator, substates : List[State], initial : State, outGoingTransitions : List[Transition]) extends StateMachine(master, substates, initial, outGoingTransitions) {
  
  def onEntry() = {}
  
  def onExit() = {}
}

case class Pong(master : Orchestrator) extends State(master) {
  override def onEntry() = {
    Thread.sleep(25)
    println("Pong.onEntry")
    master ! PongEvent
  }
  
  override def onExit() = {
    println("Pong.onExit")
  }
  
}

//Messages defined in the state machine
case class PingTransition(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) {
  def act() = {
    loop {
      react {
        case PingEvent =>
          execute
      }
    }
  }
  
  def executeActions() = {
    println("PingTransition")
  }
  
  def checkGuard : Boolean = true
}