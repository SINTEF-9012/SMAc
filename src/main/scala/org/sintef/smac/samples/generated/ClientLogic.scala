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
		

case class ClientLogic(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, keepHistory) {

  var myLogin : String = _
  var myPassword : String = _
  
  //create sub-states
  val INIT_state = INIT(master)
  addSubState(INIT_state)
  val WaitForAck_state = WaitForAck(master)
  addSubState(WaitForAck_state)
  val Timeout_state = Timeout(master)
  addSubState(Timeout_state)
  val LoggedIn_state = LoggedIn(master)
  addSubState(LoggedIn_state)
  setInitial(INIT_state)
  
  //create transitions among sub-states
  addTransition(INIT_Next_WaitForAck(INIT_state, WaitForAck_state, master))
  addTransition(WaitForAck_Next_LoggedIn(WaitForAck_state, LoggedIn_state, master))
  addTransition(WaitForAck_Timeout_Timeout(WaitForAck_state, Timeout_state, master, 10000))
  

  override def onEntry() = {
  }
  
  override def onExit() = {
  }


  case class INIT(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Init...")
      myLogin = "Brice"
      myPassword = "REMICS"

    }
  
    override def onExit() = {
      println("Done!")

    }
  }
  case class INIT_Next_WaitForAck(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  
    def executeActions() = {
      master ! new LoginEvent(myLogin, myPassword)
    }
  }
  case class WaitForAck(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Waiting Ack...")

    }
  
    override def onExit() = {
    }
  }
  case class WaitForAck_Next_LoggedIn(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
    initEvent(AckEvent)
  
    def executeActions() = {
    }
  }
  case class WaitForAck_Timeout_Timeout(previous : State, next : State, master : Orchestrator, delay : Long) extends TimedTransition(previous, next, master, delay) { 
  
    def executeActions() = {
    }
  }
  case class Timeout(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Client.TIMEOUT!")
      exit

    }
  
    override def onExit() = {
    }
  }
  case class LoggedIn(master : Orchestrator) extends State(master) {
    override def onEntry() = {
      println("Logged In")

    }
  
    override def onExit() = {
    }
  }   
  
  override def startState() = {
    super.startState
    if (withGUI)
      ClientGUI.init
  }
  

  object ClientGUI extends ActionListener {

    val frame = new JFrame("Client")
    val screen = new JTextPane()
	
    val sendack : JButton = new JButton("Send")
	
    def init {
      frame.setLayout(new GridBagLayout())
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

      var c : GridBagConstraints = new GridBagConstraints()
      c.gridwidth = 1
      c.fill = GridBagConstraints.HORIZONTAL
      c.insets = new Insets(0,3,0,3)
      
      //GUI related to ack
      c.gridy = 0
      c.gridx = 0
      frame.add(new JLabel("ack"), c)
    
      c.gridy = 1
      c.gridx = 0
      frame.add(createPanel(), c)
      
      c.gridy = 2
      c.gridx = 0
      frame.add(sendack, c)
      sendack.addActionListener(this)
    

      frame.pack
      frame.setVisible(true)
    }
  
    def createPanel() : JPanel = {
      var c : GridBagConstraints = new GridBagConstraints()
      c.fill = GridBagConstraints.HORIZONTAL
      c.weightx = 0.5
      new JPanel(new GridBagLayout())
    }
  

    def actionPerformed(ae : ActionEvent) = {
      ae.getSource match {
        case b : JButton =>
          if (b == sendack) {
            master ! AckEvent
          }
      }
    }    
  }
}
