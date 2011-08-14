/**
 * File generated by the ThingML IDE
 * /!\Do not edit this file/!\
 * In case of a bug in the generated code,
 * please submit an issue on our GitHub
 **/

package org.thingml.generated.blink.fourLEDS
import org.sintef.smac._
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
object AnalogReadResult{ def getName = "analogReadResult" }
case class AnalogReadResult(value : Int, override val name : String = AnalogReadResult.getName) extends Event(name)
object Ping{ def getName = "ping" }
case class Ping(override val name : String = Ping.getName) extends Event(name)
object Timer_timeout{ def getName = "timer_timeout" }
case class Timer_timeout(override val name : String = Timer_timeout.getName) extends Event(name)
object AnalogRead{ def getName = "analogRead" }
case class AnalogRead(pin : Byte, override val name : String = AnalogRead.getName) extends Event(name)
object PinMode{ def getName = "pinMode" }
case class PinMode(pin : Byte, mode : Byte, override val name : String = PinMode.getName) extends Event(name)
object Timer_cancel{ def getName = "timer_cancel" }
case class Timer_cancel(override val name : String = Timer_cancel.getName) extends Event(name)
object Poll{ def getName = "poll" }
case class Poll(override val name : String = Poll.getName) extends Event(name)
object Eeprom_sync_write{ def getName = "eeprom_sync_write" }
case class Eeprom_sync_write(address : Int, value : Short, override val name : String = Eeprom_sync_write.getName) extends Event(name)
object Eeprom_write{ def getName = "eeprom_write" }
case class Eeprom_write(address : Int, value : Short, override val name : String = Eeprom_write.getName) extends Event(name)
object AttachInterrupt{ def getName = "attachInterrupt" }
case class AttachInterrupt(interrupt : Byte, mode : Byte, override val name : String = AttachInterrupt.getName) extends Event(name)
object NoTone{ def getName = "noTone" }
case class NoTone(pin : Byte, override val name : String = NoTone.getName) extends Event(name)
object Eeprom_value{ def getName = "eeprom_value" }
case class Eeprom_value(value : Short, override val name : String = Eeprom_value.getName) extends Event(name)
object Led_on{ def getName = "led_on" }
case class Led_on(override val name : String = Led_on.getName) extends Event(name)
object Led_toggle{ def getName = "led_toggle" }
case class Led_toggle(override val name : String = Led_toggle.getName) extends Event(name)
object AnalogReference{ def getName = "analogReference" }
case class AnalogReference(`type` : Byte, override val name : String = AnalogReference.getName) extends Event(name)
object Eeprom_write_ack{ def getName = "eeprom_write_ack" }
case class Eeprom_write_ack(override val name : String = Eeprom_write_ack.getName) extends Event(name)
object DigitalReadResult{ def getName = "digitalReadResult" }
case class DigitalReadResult(value : Byte, override val name : String = DigitalReadResult.getName) extends Event(name)
object Timer_start{ def getName = "timer_start" }
case class Timer_start(delay : Int, override val name : String = Timer_start.getName) extends Event(name)
object DigitalRead{ def getName = "digitalRead" }
case class DigitalRead(pin : Byte, override val name : String = DigitalRead.getName) extends Event(name)
object Setup{ def getName = "setup" }
case class Setup(override val name : String = Setup.getName) extends Event(name)
object Pong{ def getName = "pong" }
case class Pong(override val name : String = Pong.getName) extends Event(name)
object DigitalWrite{ def getName = "digitalWrite" }
case class DigitalWrite(pin : Byte, value : Byte, override val name : String = DigitalWrite.getName) extends Event(name)
object InterruptNotification{ def getName = "interruptNotification" }
case class InterruptNotification(interrupt : Byte, override val name : String = InterruptNotification.getName) extends Event(name)
object Led_off{ def getName = "led_off" }
case class Led_off(override val name : String = Led_off.getName) extends Event(name)
object TestFailure{ def getName = "testFailure" }
case class TestFailure(override val name : String = TestFailure.getName) extends Event(name)
object Tone{ def getName = "tone" }
case class Tone(pin : Byte, frequency : Int, duration : Int, override val name : String = Tone.getName) extends Event(name)
object TestOut{ def getName = "testOut" }
case class TestOut(c : Char, override val name : String = TestOut.getName) extends Event(name)
object TestIn{ def getName = "testIn" }
case class TestIn(c : Char, override val name : String = TestIn.getName) extends Event(name)
object AnalogWrite{ def getName = "analogWrite" }
case class AnalogWrite(pin : Byte, value : Short, override val name : String = AnalogWrite.getName) extends Event(name)
object Eeprom_read{ def getName = "eeprom_read" }
case class Eeprom_read(address : Int, override val name : String = Eeprom_read.getName) extends Event(name)
object DetachInterrupt{ def getName = "detachInterrupt" }
case class DetachInterrupt(interrupt : Byte, override val name : String = DetachInterrupt.getName) extends Event(name)

/**
 * Definitions for type : Arduino
 **/
class Arduino {

  var behavior : scala.collection.mutable.Map[String, StateMachine] = scala.collection.mutable.Map()
  def getBehavior(sm : String) = behavior.get(sm)
  def getBehaviors = behavior.values
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
//Port DigitalIO
  val DigitalIO_port_receive = List(PinMode.getName, DigitalRead.getName, DigitalWrite.getName)
  val DigitalIO_port_send = List(DigitalReadResult.getName)
  ports.put("DigitalIO", (DigitalIO_port_receive, DigitalIO_port_send))
//Port AnalogIO
  val AnalogIO_port_receive = List(AnalogReference.getName, AnalogRead.getName, AnalogWrite.getName)
  val AnalogIO_port_send = List(AnalogReadResult.getName)
  ports.put("AnalogIO", (AnalogIO_port_receive, AnalogIO_port_send))
//Port AdvancedIO
  val AdvancedIO_port_receive = List(Tone.getName, NoTone.getName)
  val AdvancedIO_port_send = List()
  ports.put("AdvancedIO", (AdvancedIO_port_receive, AdvancedIO_port_send))
//Port Ping
  val Ping_port_receive = List(Ping.getName)
  val Ping_port_send = List(Pong.getName)
  ports.put("Ping", (Ping_port_receive, Ping_port_send))
//Port Interrupts
  val Interrupts_port_receive = List(AttachInterrupt.getName, DetachInterrupt.getName)
  val Interrupts_port_send = List(InterruptNotification.getName)
  ports.put("Interrupts", (Interrupts_port_receive, Interrupts_port_send))
//Port EEPROM
  val EEPROM_port_receive = List(Eeprom_read.getName, Eeprom_sync_write.getName, Eeprom_write.getName)
  val EEPROM_port_send = List(Eeprom_value.getName, Eeprom_write_ack.getName)
  ports.put("EEPROM", (EEPROM_port_receive, EEPROM_port_send))
  new ArduinoStdlibImplStateMachine(false)
  case class ArduinoStdlibImplStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior.put("ArduinoStdlibImpl", parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
    override def onEntry() = {
      println(this + ".onEntry")
//No entry action defined for this state
    }

    override def onExit() = {
      println(this + ".onExit")
//No exit action defined for this state
    }

//create sub-states
    val RunningArduino_state = new State(RunningArduinoState(), parent)
    parent.addSubState(RunningArduino_state)
    case class InternalTransition834496157 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        /*println("pinMode(getEvent(PinMode.getName).get.asInstanceOf[PinMode].pin, getEvent(PinMode.getName).get.asInstanceOf[PinMode].mode");*/
      }

    }
    case class InternalTransition1999186855 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        /*println("digitalWrite(getEvent(DigitalWrite.getName).get.asInstanceOf[DigitalWrite].pin, getEvent(DigitalWrite.getName).get.asInstanceOf[DigitalWrite].value");*/
      }

    }
    case class InternalTransition1950702248 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        handler.getPort("DigitalIO") match{
          case Some(p) => p.send(new DigitalReadResult(0))
          case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
        }
      }

    }
    case class InternalTransition607551681 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        /*println("analogReference(getEvent(AnalogReference.getName).get.asInstanceOf[AnalogReference].`type`");*/
      }

    }
    case class InternalTransition565415132 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        handler.getPort("AnalogIO") match{
          case Some(p) => p.send(new AnalogReadResult(0))
          case None => println("Warning: no port AnalogIO You may consider revising your ThingML model.")
        }
      }

    }
    case class InternalTransition1069969095 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        /*println("analogWrite(getEvent(AnalogWrite.getName).get.asInstanceOf[AnalogWrite].pin, getEvent(AnalogWrite.getName).get.asInstanceOf[AnalogWrite].value");*/
      }

    }
    case class InternalTransition764954107 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        /*println("tone(getEvent(Tone.getName).get.asInstanceOf[Tone].pin, getEvent(Tone.getName).get.asInstanceOf[Tone].frequency, getEvent(Tone.getName).get.asInstanceOf[Tone].duration");*/
      }

    }
    case class InternalTransition1632961370 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        /*println("noTone(getEvent(NoTone.getName).get.asInstanceOf[NoTone].pin");*/
      }

    }
    val t_self_834496157 = new InternalTransition(RunningArduino_state, new InternalTransition834496157(), parent)
    t_self_834496157.initEvent(PinMode.getName)
    RunningArduino_state.addInternalTransition(t_self_834496157)
    val t_self_1999186855 = new InternalTransition(RunningArduino_state, new InternalTransition1999186855(), parent)
    t_self_1999186855.initEvent(DigitalWrite.getName)
    RunningArduino_state.addInternalTransition(t_self_1999186855)
    val t_self_1950702248 = new InternalTransition(RunningArduino_state, new InternalTransition1950702248(), parent)
    t_self_1950702248.initEvent(DigitalRead.getName)
    RunningArduino_state.addInternalTransition(t_self_1950702248)
    val t_self_607551681 = new InternalTransition(RunningArduino_state, new InternalTransition607551681(), parent)
    t_self_607551681.initEvent(AnalogReference.getName)
    RunningArduino_state.addInternalTransition(t_self_607551681)
    val t_self_565415132 = new InternalTransition(RunningArduino_state, new InternalTransition565415132(), parent)
    t_self_565415132.initEvent(AnalogRead.getName)
    RunningArduino_state.addInternalTransition(t_self_565415132)
    val t_self_1069969095 = new InternalTransition(RunningArduino_state, new InternalTransition1069969095(), parent)
    t_self_1069969095.initEvent(AnalogWrite.getName)
    RunningArduino_state.addInternalTransition(t_self_1069969095)
    val t_self_764954107 = new InternalTransition(RunningArduino_state, new InternalTransition764954107(), parent)
    t_self_764954107.initEvent(Tone.getName)
    RunningArduino_state.addInternalTransition(t_self_764954107)
    val t_self_1632961370 = new InternalTransition(RunningArduino_state, new InternalTransition1632961370(), parent)
    t_self_1632961370.initEvent(NoTone.getName)
    RunningArduino_state.addInternalTransition(t_self_1632961370)
    case class RunningArduinoState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
//No entry action defined for this state
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(RunningArduino_state)

//create transitions among sub-states
  }
}

/**
 * Definitions for type : LED
 **/
class LED {

  var behavior : scala.collection.mutable.Map[String, StateMachine] = scala.collection.mutable.Map()
  def getBehavior(sm : String) = behavior.get(sm)
  def getBehaviors = behavior.values
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
//Port LED
  val LED_port_receive = List(Led_on.getName, Led_off.getName, Led_toggle.getName)
  val LED_port_send = List()
  ports.put("LED", (LED_port_receive, LED_port_send))
//Port DigitalIO
  val DigitalIO_port_receive = List()
  val DigitalIO_port_send = List(PinMode.getName, DigitalWrite.getName)
  ports.put("DigitalIO", (DigitalIO_port_receive, DigitalIO_port_send))

// Variables for the properties of the instance
  var LED_pin_var : Byte = _

  new LEDImplStateMachine(false)
  case class LEDImplStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior.put("LEDImpl", parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
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
    val LED_OFF_state = new State(LED_OFFState(), parent)
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

    val LED_ON_state = new State(LED_ONState(), parent)
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
    case class TransitionLED_OFF2LED_ON_559612188 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
    val t_LED_OFF2LED_ON_559612188 = new Transition(LED_OFF_state, LED_ON_state, TransitionLED_OFF2LED_ON_559612188(), parent)
    t_LED_OFF2LED_ON_559612188.initEvent(Led_on.getName)
    parent.addTransition(t_LED_OFF2LED_ON_559612188)
    case class TransitionLED_ON2LED_OFF_640632851 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
    val t_LED_ON2LED_OFF_640632851 = new Transition(LED_ON_state, LED_OFF_state, TransitionLED_ON2LED_OFF_640632851(), parent)
    t_LED_ON2LED_OFF_640632851.initEvent(Led_off.getName)
    parent.addTransition(t_LED_ON2LED_OFF_640632851)
  }
}

/**
 * Definitions for type : SoftTimer
 **/
class SoftTimer {

  var behavior : scala.collection.mutable.Map[String, StateMachine] = scala.collection.mutable.Map()
  def getBehavior(sm : String) = behavior.get(sm)
  def getBehaviors = behavior.values
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
//Port Polling
  val Polling_port_receive = List(Poll.getName)
  val Polling_port_send = List()
  ports.put("Polling", (Polling_port_receive, Polling_port_send))
//Port timer
  val timer_port_receive = List(Timer_start.getName, Timer_cancel.getName)
  val timer_port_send = List(Timer_timeout.getName)
  ports.put("timer", (timer_port_receive, timer_port_send))

// Variables for the properties of the instance
  var SoftTimer_SoftTimer_target_var : Long = _

  new SoftTimerStateMachine(false)
  case class SoftTimerStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior.put("SoftTimer", parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
    override def onEntry() = {
      println(this + ".onEntry")
//No entry action defined for this state
    }

    override def onExit() = {
      println(this + ".onExit")
//No exit action defined for this state
    }

//create sub-states
    val Idle_state = new State(IdleState(), parent)
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

    val Counting_state = new State(CountingState(), parent)
    parent.addSubState(Counting_state)
    case class InternalTransition1762842542 extends InternalTransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(Timer_start.getName).get.asInstanceOf[Timer_start].delay > 0}
      override def executeActions() = {
        println(this + ".executeActions")
        SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + getEvent(Timer_start.getName).get.asInstanceOf[Timer_start].delay
      }

    }
    val t_self_1762842542 = new InternalTransition(Counting_state, new InternalTransition1762842542(), parent)
    t_self_1762842542.initEvent(Timer_start.getName)
    Counting_state.addInternalTransition(t_self_1762842542)
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
    case class TransitionIdle2Counting_1104445273 extends TransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(Timer_start.getName).get.asInstanceOf[Timer_start].delay > 0}
      override def executeActions() = {
        println(this + ".executeActions")
        SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + getEvent(Timer_start.getName).get.asInstanceOf[Timer_start].delay
      }

    }
    val t_Idle2Counting_1104445273 = new Transition(Idle_state, Counting_state, TransitionIdle2Counting_1104445273(), parent)
    t_Idle2Counting_1104445273.initEvent(Timer_start.getName)
    parent.addTransition(t_Idle2Counting_1104445273)
    case class TransitionCounting2Idle_797952442 extends TransitionAction {
      override def checkGuard() : Boolean = {
        !(System.currentTimeMillis() < SoftTimer_SoftTimer_target_var)}
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
    val t_Counting2Idle_797952442 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_797952442(), parent)
    t_Counting2Idle_797952442.initEvent(Poll.getName)
    parent.addTransition(t_Counting2Idle_797952442)
    case class TransitionCounting2Idle_43984264 extends TransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
//No action defined for this transition
      }

    }
    val t_Counting2Idle_43984264 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_43984264(), parent)
    t_Counting2Idle_43984264.initEvent(Timer_cancel.getName)
    parent.addTransition(t_Counting2Idle_43984264)
  }
}

/**
 * Definitions for type : Blink4Leds
 **/
class Blink4Leds {

  var behavior : scala.collection.mutable.Map[String, StateMachine] = scala.collection.mutable.Map()
  def getBehavior(sm : String) = behavior.get(sm)
  def getBehaviors = behavior.values
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
//Port timer
  val timer_port_receive = List(Timer_timeout.getName)
  val timer_port_send = List(Timer_start.getName)
  ports.put("timer", (timer_port_receive, timer_port_send))
//Port leds
  val leds_port_receive = List()
  val leds_port_send = List(Led_toggle.getName)
  ports.put("leds", (leds_port_receive, leds_port_send))
  new Blink4LedsImplStateMachine(false)
  case class Blink4LedsImplStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior.put("Blink4LedsImpl", parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
    override def onEntry() = {
      println(this + ".onEntry")
//No entry action defined for this state
    }

    override def onExit() = {
      println(this + ".onExit")
//No exit action defined for this state
    }

//create sub-states
    val Blinking_state = new State(BlinkingState(), parent)
    parent.addSubState(Blinking_state)
    case class InternalTransition228832089 extends InternalTransitionAction {
      override def executeActions() = {
        println(this + ".executeActions")
        handler.getPort("leds") match{
          case Some(p) => p.send(new Led_toggle())
          case None => println("Warning: no port leds You may consider revising your ThingML model.")
        }
        handler.getPort("timer") match{
          case Some(p) => p.send(new Timer_start(500))
          case None => println("Warning: no port timer You may consider revising your ThingML model.")
        }
      }

    }
    val t_self_228832089 = new InternalTransition(Blinking_state, new InternalTransition228832089(), parent)
    t_self_228832089.initEvent(Timer_timeout.getName)
    Blinking_state.addInternalTransition(t_self_228832089)
    case class BlinkingState extends StateAction {
      override def onEntry() = {
        println(this + ".onEntry")
        handler.getPort("timer") match{
          case Some(p) => p.send(new Timer_start(500))
          case None => println("Warning: no port timer You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
        println(this + ".onExit")
//No exit action defined for this state
      }

    }

    parent.setInitial(Blinking_state)

//create transitions among sub-states
  }
}