/**
 * File generated by the ThingML IDE
 * /!\Do not edit this file/!\
 * In case of a bug in the generated code,
 * please submit an issue on our GitHub
 **/

package eu.remics.modernized
import org.sintef.smac._


// Initialize instance variables and states
object Main {

  def main(args: Array[String]): Unit = {
//Channels
    val c_1913311945 = new Channel
    c_1913311945.start
//Things
    val Server_RemicsOneClientOneServer_server = new Server(Server_login_var = "toto", Server_password_var = "pouet")
    val Client_RemicsOneClientOneServer_client = new Client(Client_login_var = "toto", Client_pwd_var = "pouet")
//Bindings
    c_1913311945.connect(
      Client_RemicsOneClientOneServer_client.getPort("LoginServer").get,
      Server_RemicsOneClientOneServer_server.getPort("LoginClient").get
    )
    c_1913311945.connect(
      Server_RemicsOneClientOneServer_server.getPort("LoginClient").get,
      Client_RemicsOneClientOneServer_client.getPort("LoginServer").get
    )

//Starting Things
    Server_RemicsOneClientOneServer_server.asInstanceOf[Component].start
    Client_RemicsOneClientOneServer_client.asInstanceOf[Component].start
  }

}