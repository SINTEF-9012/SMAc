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
package org.sintef.smac.samples.generated

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

case class Service1Logic(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, keepHistory) {

  //create sub-states
  val WaitCredentials_state = WaitCredentials(master)
  addSubState(WaitCredentials_state)
  val ValidateCredentials_state = ValidateCredentials(master)
  addSubState(ValidateCredentials_state)
  setInitial(WaitCredentials_state)
  
  //create transitions among sub-states
  addTransition(WaitCredentials_Next_ValidateCredentials(WaitCredentials_state, ValidateCredentials_state, master))
  addTransition(ValidateCredentials_Next_WaitCredentials(ValidateCredentials_state, WaitCredentials_state, master))
  

  override def onEntry() = {
  }
  
  override def onExit() = {
  }


  case class WaitCredentials(master : Orchestrator) extends State(master) {
    override def onEntry() = {
    }
  
    override def onExit() = {
    }
  }
  case class WaitCredentials_Next_ValidateCredentials(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
    initEvent(LoginEvent(null,null))
  
    def executeActions() = {
    }
  }
  case class ValidateCredentials(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Validating credentials...")

    }
  
    override def onExit() = {
    }
  }
  case class ValidateCredentials_Next_WaitCredentials(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  
    def executeActions() = {
      master ! AckEvent

    }
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
            master ! LoginEvent
          }
      }
    }    
  }
}