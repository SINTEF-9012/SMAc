/**
 * File generated by the ThingML IDE
 * /!\Do not edit this file/!\
 * In case of a bug in the generated code,
 * please submit an issue on our GitHub
 **/

package org.thingml.generated
import org.sintef.smac._
import java.util.TimerTask
import java.util.Timer
import scala.util.Random
import scala.swing.Dialog
object Timer_cancel{ def getName = "timer_cancel" }
case class Timer_cancel(override val name : String = Timer_cancel.getName) extends Event(name)
object Timer_start{ def getName = "timer_start" }
case class Timer_start(delay : Integer, override val name : String = Timer_start.getName) extends Event(name)
object TestOut{ def getName = "testOut" }
case class TestOut(c : Char, override val name : String = TestOut.getName) extends Event(name)
object Led_on{ def getName = "led_on" }
case class Led_on(override val name : String = Led_on.getName) extends Event(name)
object Timer_timeout{ def getName = "timer_timeout" }
case class Timer_timeout(override val name : String = Timer_timeout.getName) extends Event(name)
object Poll{ def getName = "poll" }
case class Poll(override val name : String = Poll.getName) extends Event(name)
object Led_off{ def getName = "led_off" }
case class Led_off(override val name : String = Led_off.getName) extends Event(name)
object TestFailure{ def getName = "testFailure" }
case class TestFailure(override val name : String = TestFailure.getName) extends Event(name)
object Led_toggle{ def getName = "led_toggle" }
case class Led_toggle(override val name : String = Led_toggle.getName) extends Event(name)
object TestIn{ def getName = "testIn" }
case class TestIn(c : Char, override val name : String = TestIn.getName) extends Event(name)

/**
 * Definitions for type : SoftTimer
 **/
class SoftTimer extends Component {

//Companion object
  object SoftTimer{
    object PollingPort{
      def getName = "Polling"
      object in {
        val poll = Poll.getName
      }
      object out {
      }
    }

    object timerPort{
      def getName = "timer"
      object in {
        val timer_start = Timer_start.getName
        val timer_cancel = Timer_cancel.getName
      }
      object out {
        val timer_timeout = Timer_timeout.getName
      }
    }

  }


// Variables for the properties of the instance
  var SoftTimer_SoftTimer_target_var : Long = _

  new Port("Polling", List(SoftTimer.PollingPort.in.poll), List(), this).start
  new Port("timer", List(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.in.timer_cancel), List(SoftTimer.timerPort.out.timer_timeout), this).start
  this.behavior ++= List(new SoftTimerStateMachine(false, this).getBehavior)
  case class SoftTimerStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
      println(this + ".onEntry")
//No entry action defined for this state
    }

    override def onExit() = {
      println(this + ".onExit")
//No exit action defined for this state
    }

//create sub-states
    val Idle_state = new State(IdleState(), root)
    parent.addSubState(Idle_state)
    case class IdleState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
//No entry action defined for this state
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    val Counting_state = new State(CountingState(), root)
    parent.addSubState(Counting_state)
    val t_self_905294532 = new InternalTransition(Counting_state, new InternalTransition905294532(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_start)))
    Counting_state.addInternalTransition(t_self_905294532)
    case class CountingState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
//No entry action defined for this state
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(Idle_state)

//create transitions among sub-states
    val t_Idle2Counting_451525351 = new Transition(Idle_state, Counting_state, TransitionIdle2Counting_451525351(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_start)))
    parent.addTransition(t_Idle2Counting_451525351)
    val t_Counting2Idle_496179757 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_496179757(), List((SoftTimer.PollingPort.getName, SoftTimer.PollingPort.in.poll)))
    parent.addTransition(t_Counting2Idle_496179757)
    val t_Counting2Idle_860052087 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_860052087(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_cancel)))
    parent.addTransition(t_Counting2Idle_860052087)
    case class TransitionIdle2Counting_451525351 extends TransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay > 0
      }
      override def executeActions() = {
        println(this + ".executeActions")
        SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay
      }

    }
    case class InternalTransition905294532 extends InternalTransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay > 0
      }
      override def executeActions() = {
        println(this + ".executeActions")
        SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay
      }

    }
    case class TransitionCounting2Idle_496179757 extends TransitionAction {
      override def checkGuard() : Boolean = {
        !(System.currentTimeMillis() < SoftTimer_SoftTimer_target_var)
      }
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

      override def executeAfterActions() = {
        println(this + ".executeAfterActions")
        handler.getPort("timer") match{
          case Some(p) => p.send(new Timer_timeout())
          case None => println("Warning: no port timer You may consider revising your ThingML model.")
        }
      }

    }
    case class TransitionCounting2Idle_860052087 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
  }
}

/**
 * Definitions for type : ThingMLScheduler
 **/
class ThingMLScheduler extends Component {

//Companion object
  object ThingMLScheduler{
    object PollingPort{
      def getName = "Polling"
      object in {
      }
      object out {
        val poll = Poll.getName
      }
    }

  }

  new Port("Polling", List(), List(ThingMLScheduler.PollingPort.out.poll), this).start
  this.behavior ++= List(new SchedulerStateMachine(false, this).getBehavior)
  case class SchedulerStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
      println(this + ".onEntry")
//No entry action defined for this state
    }

    override def onExit() = {
      println(this + ".onExit")
//No exit action defined for this state
    }

//create sub-states
    val default_state = new State(DefaultState(), root)
    parent.addSubState(default_state)
    case class DefaultState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
        class PollTask(p : Port) extends TimerTask{
          override def run {
            p.send(new Poll())
          }
        }
        new Timer().scheduleAtFixedRate(new PollTask(getPort("Polling").get), 5, 5)
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(default_state)

//create transitions among sub-states
  }
}

/**
 * Definitions for type : Blink2Leds
 **/
class Blink2Leds extends Component {

//Companion object
  object Blink2Leds{
    object timer1Port{
      def getName = "timer1"
      object in {
        val timer_timeout = Timer_timeout.getName
      }
      object out {
        val timer_start = Timer_start.getName
      }
    }

    object timer2Port{
      def getName = "timer2"
      object in {
        val timer_timeout = Timer_timeout.getName
      }
      object out {
        val timer_start = Timer_start.getName
      }
    }

    object led1Port{
      def getName = "led1"
      object in {
      }
      object out {
        val led_toggle = Led_toggle.getName
      }
    }

    object led2Port{
      def getName = "led2"
      object in {
      }
      object out {
        val led_toggle = Led_toggle.getName
      }
    }

  }

  new Port("timer1", List(Blink2Leds.timer1Port.in.timer_timeout), List(Blink2Leds.timer1Port.out.timer_start), this).start
  new Port("timer2", List(Blink2Leds.timer2Port.in.timer_timeout), List(Blink2Leds.timer2Port.out.timer_start), this).start
  new Port("led1", List(), List(Blink2Leds.led1Port.out.led_toggle), this).start
  new Port("led2", List(), List(Blink2Leds.led2Port.out.led_toggle), this).start
  this.behavior ++= List(new Blink2LedsImplStateMachine(false, this).getBehavior)
  case class Blink2LedsImplStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
      println(this + ".onEntry")
//No entry action defined for this state
    }

    override def onExit() = {
      println(this + ".onExit")
//No exit action defined for this state
    }

//create sub-states
    val Blinking_state = new State(BlinkingState(), root)
    parent.addSubState(Blinking_state)
    val t_self_672598628 = new InternalTransition(Blinking_state, new InternalTransition672598628(), List((Blink2Leds.timer1Port.getName, Blink2Leds.timer1Port.in.timer_timeout)))
    Blinking_state.addInternalTransition(t_self_672598628)
    val t_self_458796487 = new InternalTransition(Blinking_state, new InternalTransition458796487(), List((Blink2Leds.timer2Port.getName, Blink2Leds.timer2Port.in.timer_timeout)))
    Blinking_state.addInternalTransition(t_self_458796487)
    case class BlinkingState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
        handler.getPort("timer1") match{
          case Some(p) => p.send(new Timer_start(1000))
          case None => println("Warning: no port timer1 You may consider revising your ThingML model.")
        }
        handler.getPort("timer2") match{
          case Some(p) => p.send(new Timer_start(333))
          case None => println("Warning: no port timer2 You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(Blinking_state)

//create transitions among sub-states
    case class InternalTransition672598628 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        handler.getPort("led1") match{
          case Some(p) => p.send(new Led_toggle())
          case None => println("Warning: no port led1 You may consider revising your ThingML model.")
        }
        handler.getPort("timer1") match{
          case Some(p) => p.send(new Timer_start(1000))
          case None => println("Warning: no port timer1 You may consider revising your ThingML model.")
        }
      }

    }
    case class InternalTransition458796487 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        handler.getPort("led2") match{
          case Some(p) => p.send(new Led_toggle())
          case None => println("Warning: no port led2 You may consider revising your ThingML model.")
        }
        handler.getPort("timer2") match{
          case Some(p) => p.send(new Timer_start(333))
          case None => println("Warning: no port timer2 You may consider revising your ThingML model.")
        }
      }

    }
  }
}

/**
 * Definitions for type : LED
 **/
class LED extends Component {

//Companion object
  object LED{
    object LEDPort{
      def getName = "LED"
      object in {
        val led_on = Led_on.getName
        val led_off = Led_off.getName
        val led_toggle = Led_toggle.getName
      }
      object out {
      }
    }

  }


// Variables for the properties of the instance
  var LED_device_var : org.thingml.devices.LedDemo = _

  new Port("LED", List(LED.LEDPort.in.led_on, LED.LEDPort.in.led_off, LED.LEDPort.in.led_toggle), List(), this).start
  this.behavior ++= List(new LEDImplStateMachine(false, this).getBehavior)
  
  case class LEDImplStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
      println(this + ".onEntry")
      LED_device_var = new org.thingml.devices.LedDemo()
    }

    override def onExit() = {
      println(this + ".onExit")
//No exit action defined for this state
    }

//create sub-states
    val LED_OFF_state = new State(LED_OFFState(), root)
    parent.addSubState(LED_OFF_state)
    case class LED_OFFState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
        LED_device_var.off()
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    val LED_ON_state = new State(LED_ONState(), root)
    parent.addSubState(LED_ON_state)
    case class LED_ONState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
        LED_device_var.on()
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(LED_OFF_state)

//create transitions among sub-states
    val t_LED_OFF2LED_ON_150646056 = new Transition(LED_OFF_state, LED_ON_state, TransitionLED_OFF2LED_ON_150646056(), List((LED.LEDPort.getName, LED.LEDPort.in.led_on)))
    parent.addTransition(t_LED_OFF2LED_ON_150646056)
    val t_LED_OFF2LED_ON_176434979 = new Transition(LED_OFF_state, LED_ON_state, TransitionLED_OFF2LED_ON_176434979(), List((LED.LEDPort.getName, LED.LEDPort.in.led_toggle)))
    parent.addTransition(t_LED_OFF2LED_ON_176434979)
    val t_LED_ON2LED_OFF_1876612771 = new Transition(LED_ON_state, LED_OFF_state, TransitionLED_ON2LED_OFF_1876612771(), List((LED.LEDPort.getName, LED.LEDPort.in.led_off)))
    parent.addTransition(t_LED_ON2LED_OFF_1876612771)
    val t_LED_ON2LED_OFF_1499084783 = new Transition(LED_ON_state, LED_OFF_state, TransitionLED_ON2LED_OFF_1499084783(), List((LED.LEDPort.getName, LED.LEDPort.in.led_toggle)))
    parent.addTransition(t_LED_ON2LED_OFF_1499084783)
    case class TransitionLED_OFF2LED_ON_150646056 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
    case class TransitionLED_OFF2LED_ON_176434979 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
    case class TransitionLED_ON2LED_OFF_1876612771 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
    case class TransitionLED_ON2LED_OFF_1499084783 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
  }
}