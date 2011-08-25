/**
 * File generated by the ThingML IDE
 * /!\Do not edit this file/!\
 * In case of a bug in the generated code,
 * please submit an issue on our GitHub
 **/

package org.thingml.generated.potentiometer
import org.sintef.smac._
import org.thingml.devices._
object TestFailure{ def getName = "testFailure" }
case class TestFailure(override val name : String = TestFailure.getName) extends Event(name)
object Led_on{ def getName = "led_on" }
case class Led_on(override val name : String = Led_on.getName) extends Event(name)
object Led_toggle{ def getName = "led_toggle" }
case class Led_toggle(override val name : String = Led_toggle.getName) extends Event(name)
object Timer_timeout{ def getName = "timer_timeout" }
case class Timer_timeout(override val name : String = Timer_timeout.getName) extends Event(name)
object NewValue{ def getName = "newValue" }
case class NewValue(value : Int, override val name : String = NewValue.getName) extends Event(name)
object Poll{ def getName = "poll" }
case class Poll(override val name : String = Poll.getName) extends Event(name)
object TestIn{ def getName = "testIn" }
case class TestIn(c : Char, override val name : String = TestIn.getName) extends Event(name)
object Timer_start{ def getName = "timer_start" }
case class Timer_start(delay : Int, override val name : String = Timer_start.getName) extends Event(name)
object TestOut{ def getName = "testOut" }
case class TestOut(c : Char, override val name : String = TestOut.getName) extends Event(name)
object Timer_cancel{ def getName = "timer_cancel" }
case class Timer_cancel(override val name : String = Timer_cancel.getName) extends Event(name)
object Update{ def getName = "update" }
case class Update(override val name : String = Update.getName) extends Event(name)
object Led_off{ def getName = "led_off" }
case class Led_off(override val name : String = Led_off.getName) extends Event(name)

/**
 * Definitions for type : Potentiometer
 **/
class Potentiometer(var BrickSensor_lastValue_var : Int, val Brick_device_var : org.thingml.devices.Device) extends Component with org.thingml.devices.Observer{

//Companion object
  object Potentiometer{
    object PotentiometerPort{
      def getName = "Potentiometer"
      object in {
      }
      object out {
        val newValue = NewValue.getName
      }
    }

    object SensorMockUpPort{
      def getName = "SensorMockUp"
      object in {
      }
      object out {
        val update = Update.getName
      }
    }

    object SensorPort{
      def getName = "Sensor"
      object in {
        val update = Update.getName
      }
      object out {
      }
    }

  }

  new Port(Potentiometer.PotentiometerPort.getName, List(), List(Potentiometer.PotentiometerPort.out.newValue), this).start
  new Port(Potentiometer.SensorMockUpPort.getName, List(), List(Potentiometer.SensorMockUpPort.out.update), this).start
  new Port(Potentiometer.SensorPort.getName, List(Potentiometer.SensorPort.in.update), List(), this).start
  override def newValue(BrickSensor_newValue_v_var : Int) : Unit = {
    val handler = this
    BrickSensor_lastValue_var = BrickSensor_newValue_v_var
    handler.getPort("SensorMockUp") match{
      case Some(p) => p.send(new Update())
      case None => println("Warning: no port SensorMockUp You may consider revising your ThingML model.")
    }
  }
  def register() : Unit = {
    val handler = this
    Brick_device_var.asInstanceOf[org.thingml.devices.Observable].register(this)
  }
  this.behavior ++= List(new BehaviorStateMachine(false, this).getBehavior)
  case class BehaviorStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
      register()
    }

    override def onExit() = {
//No exit action defined for this state
    }

    val t_self_1051938330 = new InternalTransition(getBehavior, new InternalTransition1051938330(), List((Potentiometer.SensorPort.getName, Potentiometer.SensorPort.in.update)))
    case class InternalTransition1051938330 extends InternalTransitionAction {
      override def executeActions() = {
        handler.getPort("Potentiometer") match{
          case Some(p) => p.send(new NewValue(BrickSensor_lastValue_var))
          case None => println("Warning: no port Potentiometer You may consider revising your ThingML model.")
        }
      }

    }
//create sub-states
    val Ready_state = new State(ReadyState(), root)
    parent.addSubState(Ready_state)
    case class ReadyState extends StateAction {
      override def onEntry() = {
//No entry action defined for this state
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    parent.setInitial(Ready_state)

//create transitions among sub-states
  }
}

/**
 * Definitions for type : SoftTimer
 **/
class SoftTimer(var SoftTimer_javaTimer_var : java.util.Timer, var SoftTimer_lastTask_var : java.util.TimerTask) extends Component with org.thingml.utils.TimerTaskTrait{

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

  new Port(SoftTimer.PollingPort.getName, List(SoftTimer.PollingPort.in.poll), List(), this).start
  new Port(SoftTimer.timerPort.getName, List(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.in.timer_cancel), List(SoftTimer.timerPort.out.timer_timeout), this).start
  override def run() : Unit = {
    val handler = this
    handler.getPort("timer") match{
      case Some(p) => p.send(new Timer_timeout())
      case None => println("Warning: no port timer You may consider revising your ThingML model.")
    }
  }
  def cancel() : Unit = {
    val handler = this
    try {
      SoftTimer_lastTask_var.cancel
      SoftTimer_javaTimer_var.purge
    } catch {
      case _ =>
        SoftTimer_javaTimer_var = new java.util.Timer()
    }
  }
  def start(SoftTimer_start_delay_var : Int) : Unit = {
    val handler = this
    if( !(SoftTimer_lastTask_var == null)) cancel()
    SoftTimer_lastTask_var = this.newTimerTask
    try
    SoftTimer_javaTimer_var.schedule(SoftTimer_lastTask_var, SoftTimer_start_delay_var)
    catch {
      case _ =>
        SoftTimer_javaTimer_var = new java.util.Timer()
        SoftTimer_javaTimer_var.schedule(SoftTimer_lastTask_var, SoftTimer_start_delay_var)
    }
  }
  this.behavior ++= List(new SoftTimerStateMachine(false, this).getBehavior)
  case class SoftTimerStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

    val t_self_1299606373 = new InternalTransition(getBehavior, new InternalTransition1299606373(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_start)))
    val t_self_1320412744 = new InternalTransition(getBehavior, new InternalTransition1320412744(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_cancel)))
    case class InternalTransition1299606373 extends InternalTransitionAction {
      override def checkGuard() : Boolean = {
        try {
          getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay > 0}
        catch {
          case nse : java.util.NoSuchElementException => return false
          case e : Exception => return false
        }

      }
      override def executeActions() = {
        start(getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay)
      }

    }
    case class InternalTransition1320412744 extends InternalTransitionAction {
      override def executeActions() = {
        cancel()
      }

    }
//create sub-states
    val default_state = new State(DefaultState(), root)
    parent.addSubState(default_state)
    case class DefaultState extends StateAction {
      override def onEntry() = {
//No entry action defined for this state
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    parent.setInitial(default_state)

//create transitions among sub-states
  }
}

/**
 * Definitions for type : Led
 **/
class Led(val Brick_device_var : org.thingml.devices.Device) extends Component {

//Companion object
  object Led{
    object LedPort{
      def getName = "Led"
      object in {
        val led_on = Led_on.getName
        val led_off = Led_off.getName
        val led_toggle = Led_toggle.getName
      }
      object out {
      }
    }

  }

  new Port(Led.LedPort.getName, List(Led.LedPort.in.led_on, Led.LedPort.in.led_off, Led.LedPort.in.led_toggle), List(), this).start
  this.behavior ++= List(new LedImplStateMachine(false, this).getBehavior)
  case class LedImplStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    val LedOff_state = new State(LedOffState(), root)
    parent.addSubState(LedOff_state)
    case class LedOffState extends StateAction {
      override def onEntry() = {
        Brick_device_var.asInstanceOf[org.thingml.devices.LedDemo].off()
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    val LedOn_state = new State(LedOnState(), root)
    parent.addSubState(LedOn_state)
    case class LedOnState extends StateAction {
      override def onEntry() = {
        Brick_device_var.asInstanceOf[org.thingml.devices.LedDemo].on()
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    parent.setInitial(LedOff_state)

//create transitions among sub-states
    val t_LedOff2LedOn_693539515 = new Transition(LedOff_state, LedOn_state, TransitionLedOff2LedOn_693539515(), List((Led.LedPort.getName, Led.LedPort.in.led_on), (Led.LedPort.getName, Led.LedPort.in.led_toggle)))
    parent.addTransition(t_LedOff2LedOn_693539515)
    val t_LedOn2LedOff_267527339 = new Transition(LedOn_state, LedOff_state, TransitionLedOn2LedOff_267527339(), List((Led.LedPort.getName, Led.LedPort.in.led_off), (Led.LedPort.getName, Led.LedPort.in.led_toggle)))
    parent.addTransition(t_LedOn2LedOff_267527339)
    case class TransitionLedOff2LedOn_693539515 extends TransitionAction {
      override def executeActions() = {
//No action defined for this transition
      }

    }
    case class TransitionLedOn2LedOff_267527339 extends TransitionAction {
      override def executeActions() = {
//No action defined for this transition
      }

    }
  }
}

/**
 * Definitions for type : ChangingFrequencyLed
 **/
class ChangingFrequencyLed(var ChangingFrequencyLed_frequency_var : Int) extends Component {

//Companion object
  object ChangingFrequencyLed{
    object LedPort{
      def getName = "Led"
      object in {
      }
      object out {
        val led_toggle = Led_toggle.getName
      }
    }

    object PotentiometerPort{
      def getName = "Potentiometer"
      object in {
        val newValue = NewValue.getName
      }
      object out {
      }
    }

    object TimerPort{
      def getName = "Timer"
      object in {
        val timer_timeout = Timer_timeout.getName
      }
      object out {
        val timer_start = Timer_start.getName
      }
    }

  }

  new Port(ChangingFrequencyLed.LedPort.getName, List(), List(ChangingFrequencyLed.LedPort.out.led_toggle), this).start
  new Port(ChangingFrequencyLed.PotentiometerPort.getName, List(ChangingFrequencyLed.PotentiometerPort.in.newValue), List(), this).start
  new Port(ChangingFrequencyLed.TimerPort.getName, List(ChangingFrequencyLed.TimerPort.in.timer_timeout), List(ChangingFrequencyLed.TimerPort.out.timer_start), this).start
  this.behavior ++= List(new BehaviorStateMachine(false, this).getBehavior)
  case class BehaviorStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    val Running_state = new State(RunningState(), root)
    parent.addSubState(Running_state)
    case class RunningState extends StateAction {
      override def onEntry() = {
        handler.getPort("Timer") match{
          case Some(p) => p.send(new Timer_start(ChangingFrequencyLed_frequency_var))
          case None => println("Warning: no port Timer You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    parent.setInitial(Running_state)

//create transitions among sub-states
    val t_Running2Running_1389311915 = new Transition(Running_state, Running_state, TransitionRunning2Running_1389311915(), List((ChangingFrequencyLed.TimerPort.getName, ChangingFrequencyLed.TimerPort.in.timer_timeout)))
    parent.addTransition(t_Running2Running_1389311915)
    val t_Running2Running_1757193537 = new Transition(Running_state, Running_state, TransitionRunning2Running_1757193537(), List((ChangingFrequencyLed.PotentiometerPort.getName, ChangingFrequencyLed.PotentiometerPort.in.newValue)))
    parent.addTransition(t_Running2Running_1757193537)
    case class TransitionRunning2Running_1389311915 extends TransitionAction {
      override def executeActions() = {
        handler.getPort("Led") match{
          case Some(p) => p.send(new Led_toggle())
          case None => println("Warning: no port Led You may consider revising your ThingML model.")
        }
      }

    }
    case class TransitionRunning2Running_1757193537 extends TransitionAction {
      override def executeActions() = {
        ChangingFrequencyLed_frequency_var = getEvent(ChangingFrequencyLed.PotentiometerPort.in.newValue, ChangingFrequencyLed.PotentiometerPort.getName).get.asInstanceOf[NewValue].value
      }

    }
  }
}