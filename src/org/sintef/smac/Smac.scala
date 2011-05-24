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
  
  def startState() : Unit = {
    start
  }
 
}

/**
 * Composite states should extend Composite State
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 * They should be initialized with the list of their sub-states,
 * the list of transitions among these sub-states and an initial state
 */
abstract class CompositeState(master : Orchestrator, substates : List[State], initial : State, outGoingTransitions : List[Transition], keepHistory : Boolean) extends State(master) {
  
  var history = List[State]()
  
  override def act() = {
    //if (isCurrent){
    loop {
      react {
        case e : Event =>
          if (isCurrent) {
            //println("Dispatching: "+e)
            substates.filter{s => s.isCurrent}.foreach{s => 
              //println("  to "+s)
              s ! e
            }
            outGoingTransitions.foreach{t => 
              //println("  to "+t)
              t ! e
            }
          }
        case e : Any => 
          println("Discarded: "+e)
      }
    }
    //}
  }
  
  override def startState() : Unit = {
    //println("Composite.initState "+this.isCurrent+" "+this)
    initial.isCurrent = this.isCurrent
    this.start
    outGoingTransitions.foreach{t => t.start}
    substates.foreach{s => 
      //println("  debug "+s)
      s.startState}
  }
  
  override def executeOnEntry(){
    super.executeOnEntry
    if (keepHistory) {
      history.foreach{h => h.executeOnEntry}
    }
    else {
      initial.executeOnEntry
    }
  }
  
  override def executeOnExit(){
    if (keepHistory) {
      history = substates.filter{case s : State => s.isCurrent}
    }
    substates.filter{case s : State => s.isCurrent}.foreach{s : State => s.executeOnExit}
    super.executeOnExit
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
abstract class StateMachine(master : Orchestrator, substates : List[State], initial : State, outGoingTransitions : List[Transition], keepHistory : Boolean) extends CompositeState(master, substates, initial, outGoingTransitions, keepHistory) {
  override def startState() : Unit = {
    // println("StateMachine.initState "+this)
    master.register(this)
    this.isCurrent = true
    initial.isCurrent = true
    this.start
    outGoingTransitions.foreach{t => t.start}
    substates.foreach{s => 
      //println("  debug "+s)
      s.startState}
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


abstract case class Event {}

abstract case class InternalEvent(sm : StateMachine) extends Event {}


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
        case i : InternalEvent =>
          stateMachines.filter{sm => sm == i.sm}.foreach{sm => 
            sm ! i
          }
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
