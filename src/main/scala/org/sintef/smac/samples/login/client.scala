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
import javax.swing.JTextPane
import javax.swing.JTextField
import org.sintef.smac._


class ClientComponent(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) {

  var myLogin : String = _
  var myPassword : String = _
  val behavior = ClientLogicClient(master, Option(null), keepHistory, withGUI)
  
  case class INITClient(master : Orchestrator, parent : Option[CompositeState]) extends State(master, parent) {
    override def onEntry() = {
      println("Init...")
      myLogin = "Brice"
      myPassword = "REMICS"
    }
  
    override def onExit() = {
      println("Done!")

    }
  }

  case class INIT_Next_WaitForAckClient(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
    def executeActions() = {
      master ! new LoginEvent(myLogin, myPassword)
    }
  }

  case class WaitForAckClient(master : Orchestrator, parent : Option[CompositeState]) extends State(master, parent) {
    override def onEntry() = {
      println("Waiting Ack...")

    }
  
    override def onExit() = {
    }
  }

  case class WaitForAck_Next_LoggedInClient(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
    
    this.initEvent(AckEvent(null))
    
    def executeActions() = {
      println("WaitForAck_Next_LoggedInClient")
    }
  }

  case class WaitForAck_Timeout_TimeoutClient(previous : State, next : State, master : Orchestrator, delay : Long) extends TimedTransition(previous, next, master, delay) { 
    def executeActions() = {
    }
  }

  case class TimeoutClient(master : Orchestrator, parent : Option[CompositeState]) extends State(master, parent) {
    override def onEntry() = {
      println("TIMEOUT!")
      exit

    }
  
    override def onExit() = {
    }
  }

  case class LoggedInClient(master : Orchestrator, parent : Option[CompositeState]) extends State(master, parent) {
    override def onEntry() = {
      println("Executing business logic")

    }
  
    override def onExit() = {
    }
  }

  case class ClientLogicClient(master : Orchestrator, parent : Option[CompositeState], keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, parent, keepHistory) {

    //create sub-states
    val INIT_state = INITClient(master, Option(this))
    val WaitForAck_state = WaitForAckClient(master, Option(this))
    val Timeout_state = TimeoutClient(master, Option(this))
    val LoggedIn_state = LoggedInClient(master, Option(this))
    override val substates = List(INIT_state, WaitForAck_state, Timeout_state, LoggedIn_state)
    override val initial = INIT_state
  
    //create transitions among sub-states
    val INIT_next_WaitForAck_transition = INIT_Next_WaitForAckClient(INIT_state, WaitForAck_state, master)
    val WaitForAck_next_LoggedIn_transition = WaitForAck_Next_LoggedInClient(WaitForAck_state, LoggedIn_state, master)
    //val WaitForAck_timeout_Timeout_transition = WaitForAck_Timeout_TimeoutClient(WaitForAck_state, Timeout_state, master, List(), 10000)
    override val outGoingTransitions = List(INIT_next_WaitForAck_transition, WaitForAck_next_LoggedIn_transition/*, WaitForAck_timeout_Timeout_transition*/)    
  

    override def onEntry() = {
    }
  
    override def onExit() = {
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
      val fieldackKey = new JTextField("key")
	
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
        frame.add(createackPanel(), c)
      
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
  
      def createackPanel() : JPanel = {
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
        panel.add(fieldackKey, c);
 	
			
        return panel    
      }
  

      def actionPerformed(ae : ActionEvent) = {
        ae.getSource match {
          case b : JButton =>
            if (b == sendack) {
              master ! new AckEvent(fieldackKey.getText)
            }
        }
      }    
    }
  }
}