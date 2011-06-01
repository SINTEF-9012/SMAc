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

class Service1Component(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) {
  
  var currentLogin : String = _
  var currentPassword : String = _
  
  val behavior = new Service1Logic(master, null, keepHistory, withGUI)

  case class WaitCredentialsService1(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
    override def onEntry() = {
    }
  
    override def onExit() = {
    }
  }

  case class WaitCredentials_Next_ValidateCredentialsService1(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  
    this.initEvent(LoginEvent(null, null))
  
    def executeActions() = {
      eventsMap.keys.filter{e => e.isInstanceOf[LoginEvent]}.headOption match {
        case Some(t) => 
          println("Login: "+t.asInstanceOf[LoginEvent].login)
          currentLogin = t.asInstanceOf[LoginEvent].login
          currentPassword = t.asInstanceOf[LoginEvent].password
        case None => true
      }
    }
  }

  case class ValidateCredentialsService1(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
    override def onEntry() = {
      println("Validating credentials...")

    }
  
    override def onExit() = {
    }
  }

  case class ValidateCredentials_Next_WaitCredentialsService1(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
    def executeActions() = {
      master ! new AckEvent("key")
    }
  }

  case class Service1Logic(master : Orchestrator, parent : CompositeState, keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, parent, keepHistory) {

    //create sub-states
    val WaitCredentials_state = WaitCredentialsService1(master, this)
    val ValidateCredentials_state = ValidateCredentialsService1(master, this)
    override val substates = List(WaitCredentials_state, ValidateCredentials_state)
    override val initial = WaitCredentials_state
  
    //create transitions among sub-states
    val WaitCredentials_next_ValidateCredentials_transition = WaitCredentials_Next_ValidateCredentialsService1(WaitCredentials_state, ValidateCredentials_state, master)
    val ValidateCredentials_next_WaitCredentials_transition = ValidateCredentials_Next_WaitCredentialsService1(ValidateCredentials_state, WaitCredentials_state, master)
    override val outGoingTransitions = List(WaitCredentials_next_ValidateCredentials_transition, ValidateCredentials_next_WaitCredentials_transition)    
  

    override def onEntry() = {
    }
  
    override def onExit() = {
    }
  
    override def startState() = {
      super.startState
      if (withGUI)
        Service1GUI.init
    }
  

    object Service1GUI extends ActionListener {

      val frame = new JFrame("Service1")
      val screen = new JTextPane()
	
      val sendlogin : JButton = new JButton("Send")
      val fieldloginLogin = new JTextField("login")
      val fieldloginPassword = new JTextField("password")
	
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
  

      def actionPerformed(ae : ActionEvent) = {
        ae.getSource match {
          case b : JButton =>
            if (b == sendlogin) {
              master ! new LoginEvent(fieldloginLogin.getText, fieldloginPassword.getText)
            }
        }
      }    
    }
  }
}

