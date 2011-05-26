package org.sintef.smac

import scala.actors.Actor

/**
 * Atomic states should extend State
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 */
abstract class State(master : Orchestrator, parent : CompositeState) extends Actor {
  
  var isCurrent = false
  
  def getOutgoingTransitions() : List[Transition] = {
    if (parent != null) {
      parent.outGoingTransitions.filter{t => t.getPrevious == this}
    } 
    else {
      List()
    }
  }
  
  def dispatchEvent(e : Event) {
    println("State.react: "+e)
    getOutgoingTransitions.filter(t => t.getEvents.exists(ev => ev.getClass == e.getClass))
    .foreach(t => {
        println("  forward to: "+t)
        t.eventsMap.put(e, true)
        println(t.checkEvents)
        println(t.checkGuard)
        t.evaluateEvent
        if (t.checkEvents && t.checkGuard){
          println("    and execute")
          t.execute
        }
      })    
  }
  
  override def act(){
    loop {
      react {
        case e : Event =>
          dispatchEvent(e)
      }
    }
  }
 
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

abstract class CompositeState(master : Orchestrator, parent : CompositeState, keepHistory : Boolean) extends State(master, parent) {
    
  val substates : List[State]
  
  val outGoingTransitions : List[Transition]

  val initial : State
  
  var history = List[State]()
  
  override def dispatchEvent(e : Event) {
    println("Dispatching: "+e)
    substates.filter{s => s.isCurrent}.foreach{s => 
      println("  to "+s)
      s ! e
    }
    super.dispatchEvent(e)
  }
  
  override def act() = {
    loop {
      react {
        case e : Event =>
          if (isCurrent) {
            dispatchEvent(e)  
          }
        case e : Any => 
          println("Discarded: "+e)
      }
    }
  }
  
  override def startState() : Unit = {
    //println("Composite.initState "+this.isCurrent+" "+this)
    initial.isCurrent = this.isCurrent
    this.start
    //outGoingTransitions.foreach{t => t.start}
    substates.foreach{s => 
      //println("  debug "+s)
      s.startState}
  }
  
  override def executeOnEntry(){
    super.executeOnEntry
    if (keepHistory && history.size > 0) {
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

abstract class StateMachine(master : Orchestrator, parent : CompositeState, keepHistory : Boolean) extends CompositeState(master, parent, keepHistory) {
  override def startState() : Unit = {
    // println("StateMachine.initState "+this)
    master.register(this)
    this.isCurrent = true
    initial.isCurrent = true
    this.start
    substates.foreach{s => 
      //println("  debug "+s)
      s.startState}
  }
}

/**
 * Transitions between two states
 */
abstract class Transition(previous : State, next : State, master : Orchestrator, events : List[Event]) {
    
  def getPrevious = previous
  
  def getEvents = events
  
  def checkGuard : Boolean = true
  
  var checkEvents : Boolean = false

  var eventsMap = scala.collection.mutable.Map[Event, Boolean]()
  
  final def evaluateEvent() = {
    var eval = true
    eventsMap.keys.foreach(k => 
      {
        //println(k)
        eval = eventsMap.getOrElse(k,true) && eval
      })
    checkEvents = eval
  }
  
  def executeActions()  
  
  final def clearEvents() = {
    var newCheckEvents = scala.collection.mutable.Map[Event, Boolean]()
    events.foreach{ k => newCheckEvents.put(k,false)}
    eventsMap = newCheckEvents
  }
  
  final def execute() = {
    clearEvents
    previous.executeOnExit
    executeActions()
    next.executeOnEntry
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
