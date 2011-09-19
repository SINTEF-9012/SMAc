/**
 * File generated by the ThingML IDE
 * /!\Do not edit this file/!\
 * In case of a bug in the generated code,
 * please submit an issue on our GitHub
 **/

package eu.remics.modernized
import org.sintef.smac._
import org.thingml.devices._
object Login{ def getName = "login" }
case class Login(login : String, override val name : String = Login.getName) extends Event(name)
object Log{ def getName = "log" }
case class Log(login : String, password : String, override val name : String = Log.getName) extends Event(name)
object Ack{ def getName = "ack" }
case class Ack(access : Boolean, override val name : String = Ack.getName) extends Event(name)
object Password{ def getName = "password" }
case class Password(pwd : String, override val name : String = Password.getName) extends Event(name)
object Access{ def getName = "access" }
case class Access(result : Boolean, override val name : String = Access.getName) extends Event(name)

/**
 * Definitions for type : Server
 **/
class Server(val Server_login_var : String, val Server_password_var : String) extends Component {

//Companion object
  object Server{
    object LoginClientPort{
      def getName = "LoginClient"
      object in {
        val log = Log.getName
      }
      object out {
        val ack = Ack.getName
      }
    }

  }

  new Port(Server.LoginClientPort.getName, List(Server.LoginClientPort.in.log), List(Server.LoginClientPort.out.ack), this).start
  this.behavior ++= List(new AuthenticationStateMachine(false, this).getBehavior)
  case class AuthenticationStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    val Ready_state = new State(ReadyState(), root)
    parent.addSubState(Ready_state)
    case class ReadyState extends StateAction {
      override def onEntry() = {
        println( "Ready: Waiting for credentials...")
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    val LoggedOn_state = new State(LoggedOnState(), root)
    parent.addSubState(LoggedOn_state)
    case class LoggedOnState extends StateAction {
      override def onEntry() = {
        println( "Your are logged in")
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    parent.setInitial(Ready_state)

//create transitions among sub-states
    val t_Ready2LoggedOn_1756933294 = new Transition(Ready_state, LoggedOn_state, new TransitionReady2LoggedOn_1756933294(), List((Server.LoginClientPort.getName, Server.LoginClientPort.in.log)))
    parent.addTransition(t_Ready2LoggedOn_1756933294)
    val t_Ready2LoggedOn_1625303265 = new Transition(Ready_state, LoggedOn_state, new TransitionReady2LoggedOn_1625303265(), List((Server.LoginClientPort.getName, Server.LoginClientPort.in.log)))
    parent.addTransition(t_Ready2LoggedOn_1625303265)
    case class TransitionReady2LoggedOn_1756933294 extends TransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(Server.LoginClientPort.in.log, Server.LoginClientPort.getName).get.asInstanceOf[Log].password == Server_password_var && getEvent(Server.LoginClientPort.in.log, Server.LoginClientPort.getName).get.asInstanceOf[Log].login == Server_login_var
      }
      override def executeActions() = {
        handler.getPort("LoginClient") match{
          case Some(p) => p.send(new Ack(true))
          case None => println("Warning: no port LoginClient You may consider revising your ThingML model.")
        }
      }

    }
    case class TransitionReady2LoggedOn_1625303265 extends TransitionAction {
      override def checkGuard() : Boolean = {
        !((getEvent(Server.LoginClientPort.in.log, Server.LoginClientPort.getName).get.asInstanceOf[Log].password == Server_password_var && getEvent(Server.LoginClientPort.in.log, Server.LoginClientPort.getName).get.asInstanceOf[Log].login == Server_login_var))
      }
      override def executeActions() = {
        handler.getPort("LoginClient") match{
          case Some(p) => p.send(new Ack(false))
          case None => println("Warning: no port LoginClient You may consider revising your ThingML model.")
        }
      }

    }
  }
}

/**
 * Definitions for type : Client
 **/
class Client(val Client_login_var : String, val Client_pwd_var : String) extends Component {

//Companion object
  object Client{
    object LoginServerPort{
      def getName = "LoginServer"
      object in {
        val ack = Ack.getName
      }
      object out {
        val log = Log.getName
      }
    }

  }

  new Port(Client.LoginServerPort.getName, List(Client.LoginServerPort.in.ack), List(Client.LoginServerPort.out.log), this).start
  this.behavior ++= List(new AuthenticationStateMachine(false, this).getBehavior)
  case class AuthenticationStateMachine(keepHistory : Boolean, root : Component) extends StateAction {
    def getBehavior = parent
    val parent : StateMachine = new StateMachine(this, keepHistory, root)
    override def onEntry() = {
//No entry action defined for this state
    }

    override def onExit() = {
//No exit action defined for this state
    }

//create sub-states
    val Waiting_state = new State(WaitingState(), root)
    parent.addSubState(Waiting_state)
    case class WaitingState extends StateAction {
      override def onEntry() = {
        handler.getPort("LoginServer") match{
          case Some(p) => p.send(new Log(Client_login_var, Client_pwd_var))
          case None => println("Warning: no port LoginServer You may consider revising your ThingML model.")
        }
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    val LoggedOn_state = new State(LoggedOnState(), root)
    parent.addSubState(LoggedOn_state)
    case class LoggedOnState extends StateAction {
      override def onEntry() = {
        println( "Your are logged in")
      }

      override def onExit() = {
//No exit action defined for this state
      }

    }

    parent.setInitial(Waiting_state)

//create transitions among sub-states
    val t_Waiting2LoggedOn_1400193374 = new Transition(Waiting_state, LoggedOn_state, new TransitionWaiting2LoggedOn_1400193374(), List((Client.LoginServerPort.getName, Client.LoginServerPort.in.ack)))
    parent.addTransition(t_Waiting2LoggedOn_1400193374)
    case class TransitionWaiting2LoggedOn_1400193374 extends TransitionAction {
      override def checkGuard() : Boolean = {
        getEvent(Client.LoginServerPort.in.ack, Client.LoginServerPort.getName).get.asInstanceOf[Ack].access
      }
      override def executeActions() = {
//No action defined for this transition
      }

    }
  }
}