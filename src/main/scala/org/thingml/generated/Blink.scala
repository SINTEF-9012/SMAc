/**
 * File generated by the ThingML IDE
 * /!\Do not edit this file/!\
 * In case of a bug in the generated code,
 * please submit an issue on our GitHub
 **/

package org.thingml.generated
import org.sintef.smac._
// Definition of Enumeration  PinMode
object PinMode extends Enumeration {
  type PinMode = Byte
  val PINMODE_INPUT : Byte = 0
  val PINMODE_OUTPUT : Byte = 1
}
// Definition of Enumeration  DigitalState
object DigitalState extends Enumeration {
  type DigitalState = Byte
  val DIGITALSTATE_LOW : Byte = 0
  val DIGITALSTATE_HIGH : Byte = 1
}
// Definition of Enumeration  DigitalPin
object DigitalPin extends Enumeration {
  type DigitalPin = Byte
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
object PWMPin extends Enumeration {
  type PWMPin = Byte
  val PWMPIN_PWM_PIN_3 : Byte = 3
  val PWMPIN_PWM_PIN_5 : Byte = 5
  val PWMPIN_PWM_PIN_6 : Byte = 6
  val PWMPIN_PWM_PIN_9 : Byte = 9
  val PWMPIN_PWM_PIN_10 : Byte = 10
  val PWMPIN_PWM_PIN_11 : Byte = 11
}
// Definition of Enumeration  AnalogPin
object AnalogPin extends Enumeration {
  type AnalogPin = Byte
  val ANALOGPIN_A_0 : Byte = 14
  val ANALOGPIN_A_1 : Byte = 15
  val ANALOGPIN_A_2 : Byte = 16
  val ANALOGPIN_A_3 : Byte = 17
  val ANALOGPIN_A_4 : Byte = 18
  val ANALOGPIN_A_5 : Byte = 19
}
// Definition of Enumeration  AnalogReference
object AnalogReference extends Enumeration {
  type AnalogReference = Byte
  val ANALOGREFERENCE_DEFAULT : Byte = 1
  val ANALOGREFERENCE_INTERNAL : Byte = 3
  val ANALOGREFERENCE_EXTERNAL : Byte = 0
}
// Definition of Enumeration  InterruptPin
object InterruptPin extends Enumeration {
  type InterruptPin = Byte
  val INTERRUPTPIN_PIN_2_INT0 : Byte = 0
  val INTERRUPTPIN_PIN_3_INT1 : Byte = 1
}
// Definition of Enumeration  InterruptTrigger
object InterruptTrigger extends Enumeration {
  type InterruptTrigger = Byte
  val INTERRUPTTRIGGER_CHANGE : Byte = 1
  val INTERRUPTTRIGGER_RISING : Byte = 3
  val INTERRUPTTRIGGER_FALLING : Byte = 2
  val INTERRUPTTRIGGER_LOW : Byte = 0
}
object led_off{ def getName = "led_off" }
case class led_off(override val name : String = led_off.getName) extends Event(name)
object digitalRead{ def getName = "digitalRead" }
case class digitalRead(pin : Byte, override val name : String = digitalRead.getName) extends Event(name)
object poll{ def getName = "poll" }
case class poll(override val name : String = poll.getName) extends Event(name)
object pinMode{ def getName = "pinMode" }
case class pinMode(pin : Byte, mode : Byte, override val name : String = pinMode.getName) extends Event(name)
object interruptNotification{ def getName = "interruptNotification" }
case class interruptNotification(interrupt : Byte, override val name : String = interruptNotification.getName) extends Event(name)
object testIn{ def getName = "testIn" }
case class testIn(c : Char, override val name : String = testIn.getName) extends Event(name)
object eeprom_value{ def getName = "eeprom_value" }
case class eeprom_value(value : Short, override val name : String = eeprom_value.getName) extends Event(name)
object digitalWrite{ def getName = "digitalWrite" }
case class digitalWrite(pin : Byte, value : Byte, override val name : String = digitalWrite.getName) extends Event(name)
object ping{ def getName = "ping" }
case class ping(override val name : String = ping.getName) extends Event(name)
object setup{ def getName = "setup" }
case class setup(override val name : String = setup.getName) extends Event(name)
object eeprom_sync_write{ def getName = "eeprom_sync_write" }
case class eeprom_sync_write(address : Int, value : Short, override val name : String = eeprom_sync_write.getName) extends Event(name)
object timer_timeout{ def getName = "timer_timeout" }
case class timer_timeout(override val name : String = timer_timeout.getName) extends Event(name)
object digitalReadResult{ def getName = "digitalReadResult" }
case class digitalReadResult(value : Byte, override val name : String = digitalReadResult.getName) extends Event(name)
object eeprom_write_ack{ def getName = "eeprom_write_ack" }
case class eeprom_write_ack(override val name : String = eeprom_write_ack.getName) extends Event(name)
object eeprom_read{ def getName = "eeprom_read" }
case class eeprom_read(address : Int, override val name : String = eeprom_read.getName) extends Event(name)
object timer_start{ def getName = "timer_start" }
case class timer_start(delay : Int, override val name : String = timer_start.getName) extends Event(name)
object tone{ def getName = "tone" }
case class tone(pin : Byte, frequency : Int, duration : Int, override val name : String = tone.getName) extends Event(name)
object analogWrite{ def getName = "analogWrite" }
case class analogWrite(pin : Byte, value : Short, override val name : String = analogWrite.getName) extends Event(name)
object led_toggle{ def getName = "led_toggle" }
case class led_toggle(override val name : String = led_toggle.getName) extends Event(name)
object testOut{ def getName = "testOut" }
case class testOut(c : Char, override val name : String = testOut.getName) extends Event(name)
object led_on{ def getName = "led_on" }
case class led_on(override val name : String = led_on.getName) extends Event(name)
object detachInterrupt{ def getName = "detachInterrupt" }
case class detachInterrupt(interrupt : Byte, override val name : String = detachInterrupt.getName) extends Event(name)
object analogReadResult{ def getName = "analogReadResult" }
case class analogReadResult(value : Int, override val name : String = analogReadResult.getName) extends Event(name)
object noTone{ def getName = "noTone" }
case class noTone(pin : Byte, override val name : String = noTone.getName) extends Event(name)
object testFailure{ def getName = "testFailure" }
case class testFailure(override val name : String = testFailure.getName) extends Event(name)
object pong{ def getName = "pong" }
case class pong(override val name : String = pong.getName) extends Event(name)
object analogRead{ def getName = "analogRead" }
case class analogRead(pin : Byte, override val name : String = analogRead.getName) extends Event(name)
object analogReference{ def getName = "analogReference" }
case class analogReference(`type` : Byte, override val name : String = analogReference.getName) extends Event(name)
object eeprom_write{ def getName = "eeprom_write" }
case class eeprom_write(address : Int, value : Short, override val name : String = eeprom_write.getName) extends Event(name)
object timer_cancel{ def getName = "timer_cancel" }
case class timer_cancel(override val name : String = timer_cancel.getName) extends Event(name)
object attachInterrupt{ def getName = "attachInterrupt" }
case class attachInterrupt(interrupt : Byte, mode : Byte, override val name : String = attachInterrupt.getName) extends Event(name)

/**
 * Definitions for type : Arduino
 **/
class Arduino {

  var behavior : Map[String, StateMachine] = Map()
  def getBehavior(sm : String) = behavior.get(sm)
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
  case class ArduinoStdlibImplStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior += ("ArduinoStdlibImpl" -> parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    case class RunningArduinoState extends StateAction {
      override def onEntry() = {
//No entry action defined for this state
      }

      override def onExit() = {
//No exit action defined for this state
      }

      case class InternalTransition952443392 extends InternalTransitionAction {
        override def executeActions() = {
          pinMode(/*before2*/getEvent("pinMode").get.asInstanceOf[pinMode].pin/*after2*/, /*before2*/getEvent("pinMode").get.asInstanceOf[pinMode].mode/*after2*/);
        }

      }
      val t_self_952443392 = new InternalTransition(RunningArduino_state, InternalTransition952443392(), parent)
      t_self_952443392.initEvent("pinMode")
      RunningArduino_state.addInternalTransition(t_self_952443392)
      case class InternalTransition1902059420 extends InternalTransitionAction {
        override def executeActions() = {
          digitalWrite(/*before2*/getEvent("digitalWrite").get.asInstanceOf[digitalWrite].pin/*after2*/, /*before2*/getEvent("digitalWrite").get.asInstanceOf[digitalWrite].value/*after2*/);
        }

      }
      val t_self_1902059420 = new InternalTransition(RunningArduino_state, InternalTransition1902059420(), parent)
      t_self_1902059420.initEvent("digitalWrite")
      RunningArduino_state.addInternalTransition(t_self_1902059420)
      case class InternalTransition572487432 extends InternalTransitionAction {
        override def executeActions() = {
          handler.getPort("DigitalIO") match{
            case Some(p) => p.send(new digitalReadResult(1))
            case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
          }
        }

      }
      val t_self_572487432 = new InternalTransition(RunningArduino_state, InternalTransition572487432(), parent)
      t_self_572487432.initEvent("digitalRead")
      RunningArduino_state.addInternalTransition(t_self_572487432)
      case class InternalTransition2112994712 extends InternalTransitionAction {
        override def executeActions() = {
          analogReference(/*before2*/getEvent("analogReference").get.asInstanceOf[analogReference].`type`/*after2*/);
        }

      }
      val t_self_2112994712 = new InternalTransition(RunningArduino_state, InternalTransition2112994712(), parent)
      t_self_2112994712.initEvent("analogReference")
      RunningArduino_state.addInternalTransition(t_self_2112994712)
      case class InternalTransition887699865 extends InternalTransitionAction {
        override def executeActions() = {
          handler.getPort("AnalogIO") match{
            case Some(p) => p.send(new analogReadResult(0))
            case None => println("Warning: no port AnalogIO You may consider revising your ThingML model.")
          }
        }

      }
      val t_self_887699865 = new InternalTransition(RunningArduino_state, InternalTransition887699865(), parent)
      t_self_887699865.initEvent("analogRead")
      RunningArduino_state.addInternalTransition(t_self_887699865)
      case class InternalTransition780638304 extends InternalTransitionAction {
        override def executeActions() = {
          analogWrite(/*before2*/getEvent("analogWrite").get.asInstanceOf[analogWrite].pin/*after2*/, /*before2*/getEvent("analogWrite").get.asInstanceOf[analogWrite].value/*after2*/);
        }

      }
      val t_self_780638304 = new InternalTransition(RunningArduino_state, InternalTransition780638304(), parent)
      t_self_780638304.initEvent("analogWrite")
      RunningArduino_state.addInternalTransition(t_self_780638304)
      case class InternalTransition1013462002 extends InternalTransitionAction {
        override def executeActions() = {
          tone(/*before2*/getEvent("tone").get.asInstanceOf[tone].pin/*after2*/, /*before2*/getEvent("tone").get.asInstanceOf[tone].frequency/*after2*/, /*before2*/getEvent("tone").get.asInstanceOf[tone].duration/*after2*/);
        }

      }
      val t_self_1013462002 = new InternalTransition(RunningArduino_state, InternalTransition1013462002(), parent)
      t_self_1013462002.initEvent("tone")
      RunningArduino_state.addInternalTransition(t_self_1013462002)
      case class InternalTransition919099148 extends InternalTransitionAction {
        override def executeActions() = {
          noTone(/*before2*/getEvent("noTone").get.asInstanceOf[noTone].pin/*after2*/);
        }

      }
      val t_self_919099148 = new InternalTransition(RunningArduino_state, InternalTransition919099148(), parent)
      t_self_919099148.initEvent("noTone")
      RunningArduino_state.addInternalTransition(t_self_919099148)
    }

    val RunningArduino_state = new State(RunningArduinoState(), parent)
    parent.addSubState(RunningArduino_state)
    parent.setInitial(RunningArduino_state)

//create transitions among sub-states
  }
}

/**
 * Definitions for type : LED
 **/
class LED {

  var behavior : Map[String, StateMachine] = Map()
  def getBehavior(sm : String) = behavior.get(sm)
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
//Port LED
  val LED_port_receive = List(led_on.getName, led_off.getName, led_toggle.getName)
  val LED_port_send = List()
  ports.put("LED", (LED_port_receive, LED_port_send))
//Port DigitalIO
  val DigitalIO_port_receive = List()
  val DigitalIO_port_send = List(pinMode.getName, digitalWrite.getName)
  ports.put("DigitalIO", (DigitalIO_port_receive, DigitalIO_port_send))
// Definition of the properties:

// Variables for the properties of the instance
  var LED_pin_var : Byte = _

  case class LEDImplStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior += ("LEDImpl" -> parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
    override def onEntry() = {
      handler.getPort("DigitalIO") match{
        case Some(p) => p.send(new pinMode(LED_pin_var, PinMode.PINMODE_OUTPUT))
        case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
      }
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    case class LED_OFFState extends StateAction {
      override def onEntry() = {
        handler.getPort("DigitalIO") match{
          case Some(p) => p.send(new digitalWrite(LED_pin_var, DigitalState.DIGITALSTATE_LOW))
          case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    val LED_OFF_state = new State(LED_OFFState(), parent)
    parent.addSubState(LED_OFF_state)
    case class LED_ONState extends StateAction {
      override def onEntry() = {
        handler.getPort("DigitalIO") match{
          case Some(p) => p.send(new digitalWrite(LED_pin_var, DigitalState.DIGITALSTATE_HIGH))
          case None => println("Warning: no port DigitalIO You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    val LED_ON_state = new State(LED_ONState(), parent)
    parent.addSubState(LED_ON_state)
    parent.setInitial(LED_OFF_state)

//create transitions among sub-states
    case class TransitionLED_OFF2LED_ON_1388314661 extends TransitionAction {
      override def executeActions() = {
//No action defined for this transition
      }

    }
    val t_LED_OFF2LED_ON_1388314661 = new Transition(LED_OFF_state, LED_ON_state, TransitionLED_OFF2LED_ON_1388314661(), parent)
    t_LED_OFF2LED_ON_1388314661.initEvent("led_on")
    parent.addTransition(t_LED_OFF2LED_ON_1388314661)
    case class TransitionLED_ON2LED_OFF_1364844884 extends TransitionAction {
      override def executeActions() = {
//No action defined for this transition
      }

    }
    val t_LED_ON2LED_OFF_1364844884 = new Transition(LED_ON_state, LED_OFF_state, TransitionLED_ON2LED_OFF_1364844884(), parent)
    t_LED_ON2LED_OFF_1364844884.initEvent("led_off")
    parent.addTransition(t_LED_ON2LED_OFF_1364844884)
  }
}

/**
 * Definitions for type : SoftTimer
 **/
class SoftTimer {

  var behavior : Map[String, StateMachine] = Map()
  def getBehavior(sm : String) = behavior.get(sm)
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
//Port Polling
  val Polling_port_receive = List(poll.getName)
  val Polling_port_send = List()
  ports.put("Polling", (Polling_port_receive, Polling_port_send))
// Definition of the properties:

// Variables for the properties of the instance
  var SoftTimer_SoftTimer_target_var : Long = _

  case class SoftTimerStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior += ("SoftTimer" -> parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    case class IdleState extends StateAction {
      override def onEntry() = {
//No entry action defined for this state
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    val Idle_state = new State(IdleState(), parent)
    parent.addSubState(Idle_state)
    case class CountingState extends StateAction {
      override def onEntry() = {
//No entry action defined for this state
      }

      override def onExit() = {
//No exit action defined for this state
      }

      case class InternalTransition1009920161 extends InternalTransitionAction {
        override def executeActions() = {
          SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + /*before2*/getEvent("timer_start").get.asInstanceOf[timer_start].delay/*after2*/
        }

      }
      val t_self_1009920161 = new InternalTransition(Counting_state, InternalTransition1009920161(), parent)
      t_self_1009920161.initEvent("timer_start")
      Counting_state.addInternalTransition(t_self_1009920161)
    }

    val Counting_state = new State(CountingState(), parent)
    parent.addSubState(Counting_state)
    parent.setInitial(Idle_state)

//create transitions among sub-states
    case class TransitionIdle2Counting_1617922859 extends TransitionAction {
      override def executeActions() = {
        SoftTimer_SoftTimer_target_var = System.currentTimeMillis() + /*before2*/getEvent("timer_start").get.asInstanceOf[timer_start].delay/*after2*/
      }

    }
    val t_Idle2Counting_1617922859 = new Transition(Idle_state, Counting_state, TransitionIdle2Counting_1617922859(), parent)
    t_Idle2Counting_1617922859.initEvent("timer_start")
    parent.addTransition(t_Idle2Counting_1617922859)
    case class TransitionCounting2Idle_953169274 extends TransitionAction {
      override def executeActions() = {
//No action defined for this transition
      }

      override def executeAfterActions() = {
        handler.getPort("timer") match{
          case Some(p) => p.send(new timer_timeout())
          case None => println("Warning: no port timer You may consider revising your ThingML model.")
        }
      }

    }
    val t_Counting2Idle_953169274 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_953169274(), parent)
    t_Counting2Idle_953169274.initEvent("poll")
    parent.addTransition(t_Counting2Idle_953169274)
    case class TransitionCounting2Idle_1056794277 extends TransitionAction {
      override def executeActions() = {
//No action defined for this transition
      }

    }
    val t_Counting2Idle_1056794277 = new Transition(Counting_state, Idle_state, TransitionCounting2Idle_1056794277(), parent)
    t_Counting2Idle_1056794277.initEvent("timer_cancel")
    parent.addTransition(t_Counting2Idle_1056794277)
  }
}

/**
 * Definitions for type : Blink
 **/
class Blink {

  var behavior : Map[String, StateMachine] = Map()
  def getBehavior(sm : String) = behavior.get(sm)
  var ports : scala.collection.mutable.Map[String, Pair[List[String], List[String]]] = scala.collection.mutable.Map()
//Port HW
  val HW_port_receive = List(timer_timeout.getName)
  val HW_port_send = List(led_toggle.getName, timer_start.getName)
  ports.put("HW", (HW_port_receive, HW_port_send))
  case class BlinkImplStateMachine(keepHistory : Boolean) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory)
    behavior += ("BlinkImpl" -> parent)
    ports.keys.foreach{k => 
      new Port(k, ports.get(k).get._1, ports.get(k).get._2, parent).start
    }
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    case class BlinkingState extends StateAction {
      override def onEntry() = {
        handler.getPort("HW") match{
          case Some(p) => p.send(new timer_start(1000))
          case None => println("Warning: no port HW You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    val Blinking_state = new State(BlinkingState(), parent)
    parent.addSubState(Blinking_state)
    parent.setInitial(Blinking_state)

//create transitions among sub-states
    case class TransitionBlinking2Blinking_451982499 extends TransitionAction {
      override def executeActions() = {
//No action defined for this transition
      }

      override def executeAfterActions() = {
        handler.getPort("HW") match{
          case Some(p) => p.send(new led_toggle())
          case None => println("Warning: no port HW You may consider revising your ThingML model.")
        }
      }

    }
    val t_Blinking2Blinking_451982499 = new Transition(Blinking_state, Blinking_state, TransitionBlinking2Blinking_451982499(), parent)
    t_Blinking2Blinking_451982499.initEvent("timer_timeout")
    parent.addTransition(t_Blinking2Blinking_451982499)
  }
}

// Initialize instance variables and states
object Main {

  def main(args: Array[String]): Unit = {
//Connectors
    val null_948074856 = new Channel
    null_948074856.start
    val null_311092314 = new Channel
    null_311092314.start
    val null_1524582596 = new Channel
    null_1524582596.start
//Things
    val Arduino_154082288 = new Arduino()
    val LED_763555691 = new LED()
    val SoftTimer_1913537093 = new SoftTimer()
    val Blink_1595847065 = new Blink()
//Bindings
    null_948074856.connect(
      LED_763555691.getBehavior("LEDImpl").get.getPort("DigitalIO").get,
      Arduino_154082288.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get
    )
    null_311092314.connect(
      Blink_1595847065.getBehavior("BlinkImpl").get.getPort("HW").get,
      LED_763555691.getBehavior("LEDImpl").get.getPort("LED").get
    )
    null_1524582596.connect(
      Blink_1595847065.getBehavior("BlinkImpl").get.getPort("HW").get,
      SoftTimer_1913537093.getBehavior("SoftTimer").get.getPort("timer").get
    )
  }

}

