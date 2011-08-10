/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Brice Morin
 * Company: SINTEF IKT, Oslo, Norway
 * Date: 2011
 */
package org.sintef.smac.samples.login

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.JTextPane
import org.sintef.smac._

class MediatorComponent2(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) {
  
  val behavior = new MediatorLogicMediator(master, keepHistory, withGUI)

  var login : String = _
  var password : String = _
  
  case class WaitForCredentialsMediator(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Waiting Credentials from Client...")

    }
  
    override def onExit() = {
    }
  }

  case class WaitForCredentials_Next_WaitForAckMediator(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  
    this.initEvent(LoginEvent(null, null))
  
    def executeActions() = {
      println("Extracting crendentials... Send credentials to Service2")
      eventsMap.keys.filter{e => e.isInstanceOf[LoginEvent]}.headOption match {
        case Some(t) => 
          println("Login: "+t.asInstanceOf[LoginEvent].login)
          login = t.asInstanceOf[LoginEvent].login
          password = t.asInstanceOf[LoginEvent].password
          master ! new SetLoginEvent(login)
          master ! new SetPasswordEvent(password)
        case None => true
      }
    }
  }

  case class WaitForAckMediator(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Waiting for login Ack from Service2...")

    }
  
    override def onExit() = {
    }
  }

/*  case class WaitForAck_Timeout_TimeoutMediator(previous : State, next : State, master : Orchestrator, delay : Long) extends TimedTransition(previous, next, master, delay) { 
    def executeActions() = {
    }
  }
*/
  case class WaitForAck_Next_WaitForCredentialsMediator(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  
    this.initEvent(AckLoginEvent(null))
    this.initEvent(AckPasswordEvent(null))
  
    def executeActions() = {
      println("Mediator: login ack received")
      master ! new AckEvent("OK")
    }
  }
    
    
    
  
  case class MediatorLogicMediator(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, keepHistory) {

    //create sub-states
    val WaitForCredentials_state = WaitForCredentialsMediator(master)
    val WaitForAck_state = WaitForAckMediator(master)
    addSubState(WaitForCredentials_state)
    addSubState(WaitForAck_state)
    setInitial(WaitForCredentials_state)
  
    //create transitions among sub-states
    val waitForCrendentials = WaitForCredentials_Next_WaitForAckMediator(WaitForCredentials_state, WaitForAck_state, master)
    val waitForAck = WaitForAck_Next_WaitForCredentialsMediator(WaitForAck_state, WaitForCredentials_state, master)
   // val timeout = WaitForAck_Timeout_TimeoutMediator(WaitForAck_state, WaitForCredentials_state, master, 10000)
    addTransition(waitForCrendentials)
    addTransition(waitForAck)
  //  addTransition(timeout)
  

    override def onEntry() = {
    }
  
    override def onExit() = {
    }
  
    override def startState() = {
      super.startState
      if (withGUI)
        MediatorGUI.init
    }
  

    object MediatorGUI extends ActionListener {

      val frame = new JFrame("Mediator")
      val screen = new JTextPane()
	
      val sendlogin : JButton = new JButton("Send")
      val fieldloginLogin = new JTextField("login")
      val fieldloginPassword = new JTextField("password")
      val sendackLogin : JButton = new JButton("Send")
      val fieldackLoginKey = new JTextField("key")
      val sendackPassword : JButton = new JButton("Send")
      val fieldackPasswordKey = new JTextField("key")
	
      def init {
        frame.setLayout(new GridBagLayout())
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        var c : GridBagConstraints = new GridBagConstraints()
        c.gridwidth = 1
        c.fill = GridBagConstraints.HORIZONTAL
        c.insets = new Insets(0,3,0,3)
      
        //GUI related to login
        c.gridy = 0
        c.gridx = 0
        frame.add(new JLabel("login"), c)
    
        c.gridy = 1
        c.gridx = 0
        frame.add(createloginPanel(), c)
      
        c.gridy = 2
        c.gridx = 0
        frame.add(sendlogin, c)
        sendlogin.addActionListener(this)
    
        //GUI related to ackLogin
        c.gridy = 0
        c.gridx = 1
        frame.add(new JLabel("ackLogin"), c)
    
        c.gridy = 1
        c.gridx = 1
        frame.add(createackLoginPanel(), c)
      
        c.gridy = 2
        c.gridx = 1
        frame.add(sendackLogin, c)
        sendackLogin.addActionListener(this)
    
        //GUI related to ackPassword
        c.gridy = 0
        c.gridx = 2
        frame.add(new JLabel("ackPassword"), c)
    
        c.gridy = 1
        c.gridx = 2
        frame.add(createackPasswordPanel(), c)
      
        c.gridy = 2
        c.gridx = 2
        frame.add(sendackPassword, c)
        sendackPassword.addActionListener(this)
    

        frame.pack
        frame.setVisible(true)
      }
  
      def createPanel() : JPanel = {
        var c : GridBagConstraints = new GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.weightx = 0.5
        new JPanel(new GridBagLayout())
      }
  
      def createloginPanel() : JPanel = {
        var c : GridBagConstraints = new GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.weightx = 0.5
        val panel = new JPanel(new GridBagLayout())
    
        val labellogin = new JLabel();
        labellogin.setText("login");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(labellogin, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(fieldloginLogin, c);
 	
        val labelpassword = new JLabel();
        labelpassword.setText("password");
        c.gridx = 0;
        c.gridy = 1;
        panel.add(labelpassword, c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(fieldloginPassword, c);
 	
			
        return panel    
      }
  
      def createackLoginPanel() : JPanel = {
        var c : GridBagConstraints = new GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.weightx = 0.5
        val panel = new JPanel(new GridBagLayout())
    
        val labelkey = new JLabel();
        labelkey.setText("key");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(labelkey, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(fieldackLoginKey, c);
 	
			
        return panel    
      }
  
      def createackPasswordPanel() : JPanel = {
        var c : GridBagConstraints = new GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.weightx = 0.5
        val panel = new JPanel(new GridBagLayout())
    
        val labelkey = new JLabel();
        labelkey.setText("key");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(labelkey, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(fieldackPasswordKey, c);
 	
			
        return panel    
      }
  

      def actionPerformed(ae : ActionEvent) = {
        ae.getSource match {
          case b : JButton =>
            if (b == sendlogin) {
              master ! new LoginEvent(fieldloginLogin.getText, fieldloginPassword.getText)
            }
            if (b == sendackLogin) {
              master ! new AckLoginEvent(fieldackLoginKey.getText)
            }
            if (b == sendackPassword) {
              master ! new AckPasswordEvent(fieldackPasswordKey.getText)
            }
        }
      }    
    }
  }

}