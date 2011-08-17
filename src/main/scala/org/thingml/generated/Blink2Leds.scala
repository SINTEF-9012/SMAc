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
class PollTask(p : Port) extends TimerTask{
  override def run {
    p.send(new Poll())
  }
}
object Random1024{
  val r : Random = new Random()
  def randomInt() = r.nextInt(256).toByte
}
// Definition of Enumeration  PinMode
object PinMode_ENUM extends Enumeration {
  type PinMode_ENUM = Byte
  val PINMODE_INPUT : Byte = 0
  val PINMODE_OUTPUT : Byte = 1
}
// Definition of Enumeration  DigitalState
object DigitalState_ENUM extends Enumeration {
  type DigitalState_ENUM = Byte
  val DIGITALSTATE_LOW : Byte = 0
  val DIGITALSTATE_HIGH : Byte = 1
}
// Definition of Enumeration  DigitalPin
object DigitalPin_ENUM extends Enumeration {
  type DigitalPin_ENUM = Byte
  val DIGITALPIN_PIN_0 : Byte = 0
  val DIGITALPIN_PIN_1 : Byte = 1
  val DIGITALPIN_PIN_2 : Byte = 2
  val DIGITALPIN_PIN_3 : Byte = 3
  val DIGITALPIN_PIN_4 : Byte = 4
  val DIGITALPIN_PIN_5 : Byte = 5
  val DIGITALPIN_PIN_6 : Byte = 6
  val DIGITALPIN_PIN_7 : Byte = 7
  val DIGITALPIN_PIN_8 : Byte = 8
  val DIGITALPIN_PIN_9 : Byte = 9
  val DIGITALPIN_PIN_10 : Byte = 10
  val DIGITALPIN_PIN_11 : Byte = 11
  val DIGITALPIN_PIN_12 : Byte = 12
  val DIGITALPIN_PIN_13 : Byte = 13
  val DIGITALPIN_A_0 : Byte = 14
  val DIGITALPIN_A_1 : Byte = 15
  val DIGITALPIN_A_2 : Byte = 16
  val DIGITALPIN_A_3 : Byte = 17
  val DIGITALPIN_A_4 : Byte = 18
  val DIGITALPIN_A_5 : Byte = 19
}
// Definition of Enumeration  PWMPin
object PWMPin_ENUM extends Enumeration {
  type PWMPin_ENUM = Byte
  val PWMPIN_PWM_PIN_3 : Byte = 3
  val PWMPIN_PWM_PIN_5 : Byte = 5
  val PWMPIN_PWM_PIN_6 : Byte = 6
  val PWMPIN_PWM_PIN_9 : Byte = 9
  val PWMPIN_PWM_PIN_10 : Byte = 10
  val PWMPIN_PWM_PIN_11 : Byte = 11
}
// Definition of Enumeration  AnalogPin
object AnalogPin_ENUM extends Enumeration {
  type AnalogPin_ENUM = Byte
  val ANALOGPIN_A_0 : Byte = 14
  val ANALOGPIN_A_1 : Byte = 15
  val ANALOGPIN_A_2 : Byte = 16
  val ANALOGPIN_A_3 : Byte = 17
  val ANALOGPIN_A_4 : Byte = 18
  val ANALOGPIN_A_5 : Byte = 19
}
// Definition of Enumeration  AnalogReference
object AnalogReference_ENUM extends Enumeration {
  type AnalogReference_ENUM = Byte
  val ANALOGREFERENCE_DEFAULT : Byte = 1
  val ANALOGREFERENCE_INTERNAL : Byte = 3
  val ANALOGREFERENCE_EXTERNAL : Byte = 0
}
// Definition of Enumeration  InterruptPin
object InterruptPin_ENUM extends Enumeration {
  type InterruptPin_ENUM = Byte
  val INTERRUPTPIN_PIN_2_INT0 : Byte = 0
  val INTERRUPTPIN_PIN_3_INT1 : Byte = 1
}
// Definition of Enumeration  InterruptTrigger
object InterruptTrigger_ENUM extends Enumeration {
  type InterruptTrigger_ENUM = Byte
  val INTERRUPTTRIGGER_CHANGE : Byte = 1
  val INTERRUPTTRIGGER_RISING : Byte = 3
  val INTERRUPTTRIGGER_FALLING : Byte = 2
  val INTERRUPTTRIGGER_LOW : Byte = 0
}
object Setup{ def getName = "setup" }
case class Setup(override val name : String = Setup.getName) extends Event(name)
object Timer_timeout{ def getName = "timer_timeout" }
case class Timer_timeout(override val name : String = Timer_timeout.getName) extends Event(name)
object Led_toggle{ def getName = "led_toggle" }
case class Led_toggle(override val name : String = Led_toggle.getName) extends Event(name)
object AnalogReadResult{ def getName = "analogReadResult" }
case class AnalogReadResult(value : Int, override val name : String = AnalogReadResult.getName) extends Event(name)
object Eeprom_write{ def getName = "eeprom_write" }
case class Eeprom_write(address : Int, value : Short, override val name : String = Eeprom_write.getName) extends Event(name)
object AnalogRead{ def getName = "analogRead" }
case class AnalogRead(pin : Byte, override val name : String = AnalogRead.getName) extends Event(name)
object Led_off{ def getName = "led_off" }
case class Led_off(override val name : String = Led_off.getName) extends Event(name)
object Eeprom_value{ def getName = "eeprom_value" }
case class Eeprom_value(value : Short, override val name : String = Eeprom_value.getName) extends Event(name)
object AnalogWrite{ def getName = "analogWrite" }
case class AnalogWrite(pin : Byte, value : Short, override val name : String = AnalogWrite.getName) extends Event(name)
object InterruptNotification{ def getName = "interruptNotification" }
case class InterruptNotification(interrupt : Byte, override val name : String = InterruptNotification.getName) extends Event(name)
object DetachInterrupt{ def getName = "detachInterrupt" }
case class DetachInterrupt(interrupt : Byte, override val name : String = DetachInterrupt.getName) extends Event(name)
object Timer_start{ def getName = "timer_start" }
case class Timer_start(delay : Int, override val name : String = Timer_start.getName) extends Event(name)
object Eeprom_write_ack{ def getName = "eeprom_write_ack" }
case class Eeprom_write_ack(override val name : String = Eeprom_write_ack.getName) extends Event(name)
object TestOut{ def getName = "testOut" }
case class TestOut(c : Char, override val name : String = TestOut.getName) extends Event(name)
object PinMode{ def getName = "pinMode" }
case class PinMode(pin : Byte, mode : Byte, override val name : String = PinMode.getName) extends Event(name)
object Timer_cancel{ def getName = "timer_cancel" }
case class Timer_cancel(override val name : String = Timer_cancel.getName) extends Event(name)
object Poll{ def getName = "poll" }
case class Poll(override val name : String = Poll.getName) extends Event(name)
object Eeprom_read{ def getName = "eeprom_read" }
case class Eeprom_read(address : Int, override val name : String = Eeprom_read.getName) extends Event(name)
object DigitalRead{ def getName = "digitalRead" }
case class DigitalRead(pin : Byte, override val name : String = DigitalRead.getName) extends Event(name)
object Ping{ def getName = "ping" }
case class Ping(override val name : String = Ping.getName) extends Event(name)
object Eeprom_sync_write{ def getName = "eeprom_sync_write" }
case class Eeprom_sync_write(address : Int, value : Short, override val name : String = Eeprom_sync_write.getName) extends Event(name)
object DigitalReadResult{ def getName = "digitalReadResult" }
case class DigitalReadResult(value : Byte, override val name : String = DigitalReadResult.getName) extends Event(name)
object Pong{ def getName = "pong" }
case class Pong(override val name : String = Pong.getName) extends Event(name)
object DigitalWrite{ def getName = "digitalWrite" }
case class DigitalWrite(pin : Byte, value : Byte, override val name : String = DigitalWrite.getName) extends Event(name)
object AttachInterrupt{ def getName = "attachInterrupt" }
case class AttachInterrupt(interrupt : Byte, mode : Byte, override val name : String = AttachInterrupt.getName) extends Event(name)
object NoTone{ def getName = "noTone" }
case class NoTone(pin : Byte, override val name : String = NoTone.getName) extends Event(name)
object Tone{ def getName = "tone" }
case class Tone(pin : Byte, frequency : Int, duration : Int, override val name : String = Tone.getName) extends Event(name)
object Led_on{ def getName = "led_on" }
case class Led_on(override val name : String = Led_on.getName) extends Event(name)
object TestIn{ def getName = "testIn" }
case class TestIn(c : Char, override val name : String = TestIn.getName) extends Event(name)
object AnalogReference{ def getName = "analogReference" }
case class AnalogReference(`type` : Byte, override val name : String = AnalogReference.getName) extends Event(name)
object TestFailure{ def getName = "testFailure" }
case class TestFailure(override val name : String = TestFailure.getName) extends Event(name)

/**
 * Definitions for type : Arduino
 **/
class Arduino extends Component {

//Companion object
  object Arduino{
    object PollingPort{
      def getName = "Polling"
      object in {
      }
      object out {
        val poll = Poll.getName
      }
    }

    object DigitalIOPort{
      def getName = "DigitalIO"
      object in {
        val pinMode = PinMode.getName
        val digitalRead = DigitalRead.getName
        val digitalWrite = DigitalWrite.getName
      }
      object out {
        val digitalReadResult = DigitalReadResult.getName
      }
    }

    object AnalogIOPort{
      def getName = "AnalogIO"
      object in {
        val analogReference = AnalogReference.getName
        val analogRead = AnalogRead.getName
        val analogWrite = AnalogWrite.getName
      }
      object out {
        val analogReadResult = AnalogReadResult.getName
      }
    }

    object AdvancedIOPort{
      def getName = "AdvancedIO"
      object in {
        val tone = Tone.getName
        val noTone = NoTone.getName
      }
      object out {
      }
    }

    object PingPort{
      def getName = "Ping"
      object in {
        val ping = Ping.getName
      }
      object out {
        val pong = Pong.getName
      }
    }

    object InterruptsPort{
      def getName = "Interrupts"
      object in {
        val attachInterrupt = AttachInterrupt.getName
        val detachInterrupt = DetachInterrupt.getName
      }
      object out {
        val interruptNotification = InterruptNotification.getName
      }
    }

    object EEPROMPort{
      def getName = "EEPROM"
      object in {
        val eeprom_read = Eeprom_read.getName
        val eeprom_sync_write = Eeprom_sync_write.getName
        val eeprom_write = Eeprom_write.getName
      }
      object out {
        val eeprom_value = Eeprom_value.getName
        val eeprom_write_ack = Eeprom_write_ack.getName
      }
    }

  }

  new Port("Polling", List(), List(Arduino.PollingPort.out.poll), this).start
  new Port("DigitalIO", List(Arduino.DigitalIOPort.in.pinMode, Arduino.DigitalIOPort.in.digitalRead, Arduino.DigitalIOPort.in.digitalWrite), List(Arduino.DigitalIOPort.out.digitalReadResult), this).start
  new Port("AnalogIO", List(Arduino.AnalogIOPort.in.analogReference, Arduino.AnalogIOPort.in.analogRead, Arduino.AnalogIOPort.in.analogWrite), List(Arduino.AnalogIOPort.out.analogReadResult), this).start
  new Port("AdvancedIO", List(Arduino.AdvancedIOPort.in.tone, Arduino.AdvancedIOPort.in.noTone), List(), this).start
  new Port("Ping", List(Arduino.PingPort.in.ping), List(Arduino.PingPort.out.pong), this).start
  new Port("Interrupts", List(Arduino.InterruptsPort.in.attachInterrupt, Arduino.InterruptsPort.in.detachInterrupt), List(Arduino.InterruptsPort.out.interruptNotification), this).start
  new Port("EEPROM", List(Arduino.EEPROMPort.in.eeprom_read, Arduino.EEPROMPort.in.eeprom_sync_write, Arduino.EEPROMPort.in.eeprom_write), List(Arduino.EEPROMPort.out.eeprom_value, Arduino.EEPROMPort.out.eeprom_write_ack), this).start
  this.behavior ++= List(new ArduinoStdlibImplStateMachine(false, this).getBehavior)
  case class ArduinoStdlibImplStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
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
    val RunningArduino_state = new State(RunningArduinoState(), root)
    parent.addSubState(RunningArduino_state)
    val t_self_549855360 = new InternalTransition(RunningArduino_state, new InternalTransition549855360(), List((Arduino.DigitalIOPort.getName, Arduino.DigitalIOPort.in.pinMode)))
    RunningArduino_state.addInternalTransition(t_self_549855360)
    val t_self_1147055517 = new InternalTransition(RunningArduino_state, new InternalTransition1147055517(), List((Arduino.DigitalIOPort.getName, Arduino.DigitalIOPort.in.digitalWrite)))
    RunningArduino_state.addInternalTransition(t_self_1147055517)
    val t_self_1490360916 = new InternalTransition(RunningArduino_state, new InternalTransition1490360916(), List((Arduino.DigitalIOPort.getName, Arduino.DigitalIOPort.in.digitalRead)))
    RunningArduino_state.addInternalTransition(t_self_1490360916)
    val t_self_584004921 = new InternalTransition(RunningArduino_state, new InternalTransition584004921(), List((Arduino.AnalogIOPort.getName, Arduino.AnalogIOPort.in.analogReference)))
    RunningArduino_state.addInternalTransition(t_self_584004921)
    val t_self_1774239462 = new InternalTransition(RunningArduino_state, new InternalTransition1774239462(), List((Arduino.AnalogIOPort.getName, Arduino.AnalogIOPort.in.analogRead)))
    RunningArduino_state.addInternalTransition(t_self_1774239462)
    val t_self_58472846 = new InternalTransition(RunningArduino_state, new InternalTransition58472846(), List((Arduino.AnalogIOPort.getName, Arduino.AnalogIOPort.in.analogWrite)))
    RunningArduino_state.addInternalTransition(t_self_58472846)
    val t_self_1534516756 = new InternalTransition(RunningArduino_state, new InternalTransition1534516756(), List((Arduino.AdvancedIOPort.getName, Arduino.AdvancedIOPort.in.tone)))
    RunningArduino_state.addInternalTransition(t_self_1534516756)
    val t_self_1571558359 = new InternalTransition(RunningArduino_state, new InternalTransition1571558359(), List((Arduino.AdvancedIOPort.getName, Arduino.AdvancedIOPort.in.noTone)))
    RunningArduino_state.addInternalTransition(t_self_1571558359)
    case class RunningArduinoState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
        new Timer().scheduleAtFixedRate(new PollTask(getPort("Polling").get), 5, 5)
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(RunningArduino_state)

//create transitions among sub-states
    case class InternalTransition549855360 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        println("pinMode("+getEvent(Arduino.DigitalIOPort.in.pinMode, Arduino.DigitalIOPort.getName).get.asInstanceOf[PinMode].pin+", "+getEvent(Arduino.DigitalIOPort.in.pinMode, Arduino.DigitalIOPort.getName).get.asInstanceOf[PinMode].mode+");")
      }

    }
    case class InternalTransition1147055517 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        println("digitalWrite("+getEvent(Arduino.DigitalIOPort.in.digitalWrite, Arduino.DigitalIOPort.getName).get.asInstanceOf[DigitalWrite].pin+", "+getEvent(Arduino.DigitalIOPort.in.digitalWrite, Arduino.DigitalIOPort.getName).get.asInstanceOf[DigitalWrite].value+");")
      }

    }
    case class InternalTransition1490360916 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        handler.getPort("DigitalIO") match{
          case Some(p) => p.send(new DigitalReadResult(Random1024.randomInt))
          case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
        }
      }

    }
    case class InternalTransition584004921 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        println("analogReference("+getEvent(Arduino.AnalogIOPort.in.analogReference, Arduino.AnalogIOPort.getName).get.asInstanceOf[AnalogReference].`type`+");")
      }

    }
    case class InternalTransition1774239462 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        handler.getPort("AnalogIO") match{
          case Some(p) => p.send(new AnalogReadResult(Random1024.randomInt))
          case None => println("Warning: no port AnalogIO You may consider revising your ThingML model.")
        }
      }

    }
    case class InternalTransition58472846 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        println("analogWrite("+getEvent(Arduino.AnalogIOPort.in.analogWrite, Arduino.AnalogIOPort.getName).get.asInstanceOf[AnalogWrite].pin+", "+getEvent(Arduino.AnalogIOPort.in.analogWrite, Arduino.AnalogIOPort.getName).get.asInstanceOf[AnalogWrite].value+");")
      }

    }
    case class InternalTransition1534516756 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        println("tone("+getEvent(Arduino.AdvancedIOPort.in.tone, Arduino.AdvancedIOPort.getName).get.asInstanceOf[Tone].pin+", "+getEvent(Arduino.AdvancedIOPort.in.tone, Arduino.AdvancedIOPort.getName).get.asInstanceOf[Tone].frequency+", "+getEvent(Arduino.AdvancedIOPort.in.tone, Arduino.AdvancedIOPort.getName).get.asInstanceOf[Tone].duration+");")
      }

    }
    case class InternalTransition1571558359 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        println("noTone("+getEvent(Arduino.AdvancedIOPort.in.noTone, Arduino.AdvancedIOPort.getName).get.asInstanceOf[NoTone].pin+");")
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

    object DigitalIOPort{
      def getName = "DigitalIO"
      object in {
      }
      object out {
        val pinMode = PinMode.getName
        val digitalWrite = DigitalWrite.getName
      }
    }

  }


// Variables for the properties of the instance
  var LED_pin_var : Byte = _

  new Port("LED", List(LED.LEDPort.in.led_on, LED.LEDPort.in.led_off, LED.LEDPort.in.led_toggle), List(), this).start
  new Port("DigitalIO", List(), List(LED.DigitalIOPort.out.pinMode, LED.DigitalIOPort.out.digitalWrite), this).start
  this.behavior ++= List(new LEDImplStateMachine(false, this).getBehavior)
  case class LEDImplStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
      println(this + ".onEntry")
      handler.getPort("DigitalIO") match{
        case Some(p) => p.send(new PinMode(LED_pin_var, PinMode_ENUM.PINMODE_OUTPUT))
        case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
      }
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
        handler.getPort("DigitalIO") match{
          case Some(p) => p.send(new DigitalWrite(LED_pin_var, DigitalState_ENUM.DIGITALSTATE_LOW))
          case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
        }
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
        handler.getPort("DigitalIO") match{
          case Some(p) => p.send(new DigitalWrite(LED_pin_var, DigitalState_ENUM.DIGITALSTATE_HIGH))
          case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(LED_OFF_state)

//create transitions among sub-states
    val t_LED_OFF2LED_ON_1686365681 = new Transition(LED_OFF_state, LED_ON_state, TransitionLED_OFF2LED_ON_1686365681(), List((LED.LEDPort.getName, LED.LEDPort.in.led_on), (LED.LEDPort.getName, LED.LEDPort.in.led_toggle)))
    parent.addTransition(t_LED_OFF2LED_ON_1686365681)
    val t_LED_ON2LED_OFF_283788351 = new Transition(LED_ON_state, LED_OFF_state, TransitionLED_ON2LED_OFF_283788351(), List((LED.LEDPort.getName, LED.LEDPort.in.led_off), (LED.LEDPort.getName, LED.LEDPort.in.led_toggle)))
    parent.addTransition(t_LED_ON2LED_OFF_283788351)
    case class TransitionLED_OFF2LED_ON_1686365681 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
    case class TransitionLED_ON2LED_OFF_283788351 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
  }
}

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
    val t_self_2046274478 = new InternalTransition(Counting_state, new InternalTransition2046274478(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_start)))
    Counting_state.addInternalTransition(t_self_2046274478)
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
    val t_Idle2Counting_928729810 = new Transition(Idle_state, Counting_state, TransitionIdle2Counting_928729810(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_start)))
    parent.addTransition(t_Idle2Counting_928729810)
    val t_Counting2Idle_36987345 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_36987345(), List((SoftTimer.PollingPort.getName, SoftTimer.PollingPort.in.poll)))
    parent.addTransition(t_Counting2Idle_36987345)
    val t_Counting2Idle_1244375180 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_1244375180(), List((SoftTimer.timerPort.getName, SoftTimer.timerPort.in.timer_cancel)))
    parent.addTransition(t_Counting2Idle_1244375180)
    case class TransitionIdle2Counting_928729810 extends TransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay > 0
      }
      override def executeActions() = {
        println(this + ".executeActions")
        SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay
      }

    }
    case class InternalTransition2046274478 extends InternalTransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay > 0
      }
      override def executeActions() = {
        println(this + ".executeActions")
        SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + getEvent(SoftTimer.timerPort.in.timer_start, SoftTimer.timerPort.getName).get.asInstanceOf[Timer_start].delay
      }

    }
    case class TransitionCounting2Idle_36987345 extends TransitionAction {
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
    case class TransitionCounting2Idle_1244375180 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
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
    val t_self_312759349 = new InternalTransition(Blinking_state, new InternalTransition312759349(), List((Blink2Leds.timer1Port.getName, Blink2Leds.timer1Port.in.timer_timeout)))
    Blinking_state.addInternalTransition(t_self_312759349)
    val t_self_833041663 = new InternalTransition(Blinking_state, new InternalTransition833041663(), List((Blink2Leds.timer2Port.getName, Blink2Leds.timer2Port.in.timer_timeout)))
    Blinking_state.addInternalTransition(t_self_833041663)
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
    case class InternalTransition312759349 extends InternalTransitionAction {
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
    case class InternalTransition833041663 extends InternalTransitionAction {
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