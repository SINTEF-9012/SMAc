package org.sintef.smac

import scala.actors.Actor


/**
 * Atomic states should extend State
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 */
abstract class State(master : Orchestrator) extends Actor {
  
  var isCurrent = false
  
  override def act(){}
 
  def executeOnEntry(){
    isCurrent = true
    onEntry
  }
  
  def executeOnExit(){
    isCurrent = false
    onExit
  }
 
  def onEntry
  
  def onExit  
 
}

/**
 * Composite states should extend Composite State
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 * They should be initialized with the list of their sub-states,
 * the list of transitions among these sub-states and an initial state
 */
abstract class CompositeState(master : Orchestrator, substates : List[State], initial : State, outGoingTransitions : List[Transition]) extends State(master) {
  
  override def act() = {
    loop {
      react {
        case e : Event =>
          //println("Dispatching: "+e)
          substates.foreach{s => 
            //println("  to "+s)
            s ! e
          }
          outGoingTransitions.foreach{t => 
            //println("  to "+t)
            t ! e
          }
        case e : Any => 
          println("Discarded: "+e)
      }
    }
  }
  
  def init() = {
    this.start
    outGoingTransitions.foreach{t => t.start}
    substates.foreach{s => s.start}
    initial.isCurrent = true
  }
  
  override def executeOnEntry(){
    super.executeOnEntry
    initial.onEntry
  }
  
  override def executeOnExit(){
    super.executeOnExit
    substates.filter{case s : State => s.isCurrent}.foreach{s : State => s.onExit}
  }
  
}

/**
 * State machines should extend StateMachine
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 * State machine are basically top-level composite states.
 * They should be initialized with the list of their sub-states,
 * the list of transitions among these sub-states and an initial state
 */
abstract class StateMachine(master : Orchestrator, substates : List[State], initial : State, outGoingTransitions : List[Transition]) extends CompositeState(master, substates, initial, outGoingTransitions) {
  override def init() = {
    master.register(this)
    super.init
  }
}

/**
 * Builder to create instances of your state machines
 */
abstract class StateMachineBuilder(master : Orchestrator) {
  def createStateMachine : StateMachine
}

/**
 * Transitions between two states
 */
abstract class Transition(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Actor {
  
  def checkGuard : Boolean = true
  
  var checkEvents : Boolean = false

  var eventsMap = scala.collection.mutable.Map[Event, Boolean]()
  
  final def evaluateEvent() = {
    var eval = true
    eventsMap.values.foreach{v => eval = v && eval}
    checkEvents = eval
  }
  
  def executeActions()  
  
  final override def start() : Actor = {
    super.start
    clearEvents
    return this
  }
  
  final def clearEvents() = {
    var newCheckEvents = scala.collection.mutable.Map[Event, Boolean]()
    events.foreach{ k => newCheckEvents.put(k,false)}
    eventsMap = newCheckEvents
  }
  
  final def execute() = {
    evaluateEvent
    if (previous.isCurrent && checkEvents && checkGuard) {
      clearEvents
      previous.executeOnExit
      executeActions()
      next.executeOnEntry
    }
  }
  
  final def act() = {
    loop {
      react {
        case e : Event =>
          if (events.contains(e)) {
            //println("OK "+e)
            eventsMap.put(e, true)
            execute
          }
          /*else {
            println("NOK "+e)
          }*/
      }
    }
  }
}


abstract class Event {}


/**
 * Orchestrator allows connecting different state machines together
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
class Orchestrator() extends Actor {
  
  var stateMachines = List[StateMachine]()
  
  def register(sm : StateMachine) = {
    //println("Register "+sm)
    stateMachines ::= sm
  }
  
  override def act() = {
    loop {
      react {
        case e : Event =>
          stateMachines.foreach{sm => 
            sm ! e
          }
        case e : Any =>
          println("Orchestrator_Any: "+e)
      }
    }
  }
}
