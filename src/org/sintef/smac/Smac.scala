package org.sintef.smac

import scala.actors.Actor

/**
 * Atomic states should extend State
 * and implements the onEntry and onExit methods
 * to define the actions to execute on entry and on exit.
 */
abstract class State(master : Orchestrator, parent : CompositeState) extends Actor {
    
  def getOutgoingTransitions() : List[Transition] = {
    if (parent != null) {
      parent.outGoingTransitions.filter{t => t.getPrevious == this}
    } 
    else {
      List()
    }
  }
  
  def dispatchEvent(e : Event) {
    //println("State.react: "+e)
    getOutgoingTransitions.filter(t => t.getEvents.exists(ev => ev.getClass == e.getClass))
    .foreach(t => {
        //println("  forward to: "+t)
        t.eventsMap.put(e, true)
        println(t.checkEvents)
        println(t.checkGuard)
        t.evaluateEvent
        if (t.checkEvents && t.checkGuard){
          //println("    and execute")
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
    parent.current = this
    onEntry
  }
  
  def executeOnExit(){
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
  
  var current : State = null
  
  var history : State = null
  
  override def dispatchEvent(e : Event) {
    //println("Dispatching: "+e)
    if (current != null) {
      current ! e
    }
    super.dispatchEvent(e)
  }
  
  override def act() = {
    loop {
      react {
        case e : Event =>
          dispatchEvent(e)  
        case e : Any => 
          println("Discarded: "+e)
      }
    }
  }
  
  override def startState() : Unit = {
    super.startState()
    if (parent == null || parent == this){//root composite
      //println("Root.current = "+initial)
      current = initial
      master.register(this)
    }
    else if (parent.current == this) {
      //println("Composite.current = "+initial)
      current = initial
    }
    substates.foreach{s => 
      //println("  debug "+s)
      s.startState
    }
  }
  
  override def executeOnEntry(){
    super.executeOnEntry
    if (keepHistory && history != null) {
      history.executeOnEntry
    }
    else {
      initial.executeOnEntry
    }
  }
  
  override def executeOnExit(){
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


/**
 * Orchestrator allows connecting different state machines together
 * All the events sent by one state machine, will be receive by all 
 * the others managed by the orchestrator
 */
class Orchestrator() extends Actor {
  
  var stateMachines = List[CompositeState]()
  
  def register(sm : CompositeState) = {
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
