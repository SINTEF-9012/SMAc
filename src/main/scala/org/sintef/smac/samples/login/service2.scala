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

class Service2Component(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) {
  
  val behavior = new Service2Logic(master, keepHistory, withGUI)

  case class WaitLoginService2(master : Orchestrator) extends State(master) {
    override def onEntry() = {
    }
  
    override def onExit() = {
    }
  }

  case class WaitLogin_Next_ValidateLoginService2(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  
    this.initEvent(SetLoginEvent(null))
  
    def executeActions() = {
    }
  }

  case class ValidateLoginService2(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Validating login...")

    }
  
    override def onExit() = {
    }
  }

  case class ValidateLogin_Next_WaitPasswordService2(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
    def executeActions() = {
      println("Service2: Login OK")
      master ! new AckLoginEvent("ok")

    }
  }

  case class WaitPasswordService2(master : Orchestrator) extends State(master) {
  
    override def onEntry() = {
    }
  
    override def onExit() = {
    }
  }

  case class WaitPassword_Next_ValidatePasswordService2(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  
    this.initEvent(SetPasswordEvent(null))
  
    def executeActions() = {
      println("Service2: password received")
      master ! new AckPasswordEvent("OK")
    }
  }

  case class Service2Logic(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, keepHistory) {

    //create sub-states
    val WaitLogin_state = WaitLoginService2(master)
    val ValidateLogin_state = ValidateLoginService2(master)
    val WaitPassword_state = WaitPasswordService2(master)
    addSubState(WaitLogin_state)
    addSubState(ValidateLogin_state)
    addSubState(WaitPassword_state)
    setInitial(WaitLogin_state)
  
    //create transitions among sub-states
    val WaitLogin_next_ValidateLogin_transition = WaitLogin_Next_ValidateLoginService2(WaitLogin_state, ValidateLogin_state, master)
    val ValidateLogin_next_WaitPassword_transition = ValidateLogin_Next_WaitPasswordService2(ValidateLogin_state, WaitPassword_state, master)
    val WaitPassword_next_ValidateLogin_transition = WaitPassword_Next_ValidatePasswordService2(WaitPassword_state, WaitLogin_state, master)
    addTransition(WaitLogin_next_ValidateLogin_transition)
    addTransition(ValidateLogin_next_WaitPassword_transition)
    addTransition(WaitPassword_next_ValidateLogin_transition)
  

    override def onEntry() = {
    }
  
    override def onExit() = {
    }
  
    override def startState() = {
      super.startState
      if (withGUI)
        Service2GUI.init
    }
  

    object Service2GUI extends ActionListener {

      val frame = new JFrame("Service2")
      val screen = new JTextPane()
	
      val sendsetLogin : JButton = new JButton("Send")
      val fieldsetLoginLogin = new JTextField("login")
      val sendsetPassword : JButton = new JButton("Send")
      val fieldsetPasswordPassword = new JTextField("password")
	
      def init {
        frame.setLayout(new GridBagLayout())
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        var c : GridBagConstraints = new GridBagConstraints()
        c.gridwidth = 1
        c.fill = GridBagConstraints.HORIZONTAL
        c.insets = new Insets(0,3,0,3)
      
        //GUI related to setLogin
        c.gridy = 0
        c.gridx = 0
        frame.add(new JLabel("setLogin"), c)
    
        c.gridy = 1
        c.gridx = 0
        frame.add(createsetLoginPanel(), c)
      
        c.gridy = 2
        c.gridx = 0
        frame.add(sendsetLogin, c)
        sendsetLogin.addActionListener(this)
    
        //GUI related to setPassword
        c.gridy = 0
        c.gridx = 1
        frame.add(new JLabel("setPassword"), c)
    
        c.gridy = 1
        c.gridx = 1
        frame.add(createsetPasswordPanel(), c)
      
        c.gridy = 2
        c.gridx = 1
        frame.add(sendsetPassword, c)
        sendsetPassword.addActionListener(this)
    

        frame.pack
        frame.setVisible(true)
      }
  
      def createPanel() : JPanel = {
        var c : GridBagConstraints = new GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.weightx = 0.5
        new JPanel(new GridBagLayout())
      }
  
      def createsetLoginPanel() : JPanel = {
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
        panel.add(fieldsetLoginLogin, c);
 	
			
        return panel    
      }
  
      def createsetPasswordPanel() : JPanel = {
        var c : GridBagConstraints = new GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.weightx = 0.5
        val panel = new JPanel(new GridBagLayout())
    
        val labelpassword = new JLabel();
        labelpassword.setText("password");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(labelpassword, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(fieldsetPasswordPassword, c);
 	
			
        return panel    
      }
  

      def actionPerformed(ae : ActionEvent) = {
        ae.getSource match {
          case b : JButton =>
            if (b == sendsetLogin) {
              master ! new SetLoginEvent(fieldsetLoginLogin.getText)
            }
            if (b == sendsetPassword) {
              master ! new SetPasswordEvent(fieldsetPasswordPassword.getText)
            }
        }
      }    
    }
  }
}