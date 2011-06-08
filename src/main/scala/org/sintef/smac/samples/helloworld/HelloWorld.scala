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
package org.sintef.smac.samples.helloworld

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
import org.sintef.smac._

case object HEvent extends Event {}					
case object EEvent extends Event {}					
case object LEvent extends Event {}									
case object OEvent extends Event {}					

case class HelloWorldStateMachine(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, keepHistory) {

  //create sub-states
  val INIT_state = INIT(master)
  val H_state = H(master)
  val E_state = E(master)
  val L1_state = L1(master)
  val L2_state = L2(master)
  val O_state = O(master)
  val STOP_state = STOP(master)
  addSubState(INIT_state)
  addSubState(H_state)
  addSubState(E_state)
  addSubState(L1_state)
  addSubState(L2_state)
  addSubState(O_state)
  addSubState(STOP_state)
  setInitial(INIT_state)
  
  //create transitions among sub-states
  val INIT_next_H_transition = INIT_Next_H(INIT_state, H_state, master)
  val H_next_E_transition = H_Next_E(H_state, E_state, master)
  val E_next_L1_transition = E_Next_L1(E_state, L1_state, master)
  val L1_next_L2_transition = L1_Next_L2(L1_state, L2_state, master)
  val L2_next_O_transition = L2_Next_O(L2_state, O_state, master)
  val O_next_STOP_transition = O_Next_STOP(O_state, STOP_state, master)
  
  addTransition(INIT_next_H_transition)
  addTransition(H_next_E_transition)
  addTransition(E_next_L1_transition)
  addTransition(L1_next_L2_transition)
  addTransition(L2_next_O_transition)
  addTransition(O_next_STOP_transition)
  
  override def startState() = {
    super.startState
    if (withGUI)
      HelloWorldGUI.init
  }
  
  override def onEntry() = {
    println("start")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
  
  //This is just an ugly GUI...
  object HelloWorldGUI extends ActionListener {
    val frame = new JFrame("HelloWorld Client")
    val screen = new JTextPane()
	
    val sendH : JButton = new JButton("Send")
    val sendE : JButton = new JButton("Send")
    val sendL : JButton = new JButton("Send")
    val sendO : JButton = new JButton("Send")

    def init {
      frame.setLayout(new GridBagLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      var c : GridBagConstraints = new GridBagConstraints();
      c.gridwidth = 1;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.insets = new Insets(0,3,0,3);
    
      //GUI related to H
      c.gridy = 0;
      c.gridx = 0;
      frame.add(new JLabel("H"), c);
    
      c.gridy = 1;
      c.gridx = 0;
      frame.add(createPanel(), c);
      
      c.gridy = 2;
      c.gridx = 0;
      frame.add(sendH, c);
      sendH.addActionListener(this)
      
      //GUI related to E
      c.gridy = 0;
      c.gridx = 1;
      frame.add(new JLabel("E"), c);
    
      c.gridy = 1;
      c.gridx = 1;
      frame.add(createPanel(), c);
      
      c.gridy = 2;
      c.gridx = 1;
      frame.add(sendE, c);
      sendE.addActionListener(this)
      
      //GUI related to L
      c.gridy = 0;
      c.gridx = 2;
      frame.add(new JLabel("L"), c);
    
      c.gridy = 1;
      c.gridx = 2;
      frame.add(createPanel(), c);
      
      c.gridy = 2;
      c.gridx = 2;
      frame.add(sendL, c);
      sendL.addActionListener(this)

      //GUI related to O
      c.gridy = 0;
      c.gridx = 3;
      frame.add(new JLabel("O"), c);
    
      c.gridy = 1;
      c.gridx = 3;
      frame.add(createPanel(), c);
      
      c.gridy = 2;
      c.gridx = 3;
      frame.add(sendO, c);
      sendO.addActionListener(this)
            
      frame.pack
      frame.setVisible(true)
    }
    
    def createPanel() : JPanel = {
      var c : GridBagConstraints = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 0.5;
      new JPanel(new GridBagLayout());
    }
    
    def actionPerformed(ae : ActionEvent) = {
      ae.getSource match {
        case b : JButton =>
          if (b == sendH) {
            master ! HEvent
          }
          else if (b == sendE) {
            master ! EEvent
          }
          else if (b == sendL) {
            master ! LEvent
          }
          else if (b == sendO) {
            master ! OEvent
          }
      }
    }
    
  }
}

case class INIT(master : Orchestrator) extends State(master) {
  override def onEntry() = {
    println("init")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class INIT_Next_H(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(HEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class H( master : Orchestrator) extends State(master) {
  override def onEntry() = {
    println("W")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class H_Next_E(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(EEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class E( master : Orchestrator) extends State(master) {
  override def onEntry() = {
    println("O")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class E_Next_L1(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(LEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class L1( master : Orchestrator) extends State(master) {
  override def onEntry() = {
    println("R")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L1_Next_L2(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(LEvent)
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class L2( master : Orchestrator) extends State(master) {
  override def onEntry() = {
    println("L")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L2_Next_O(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(OEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class O( master : Orchestrator) extends State(master) {
  override def onEntry() = {
    println("D")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class O_Next_STOP(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class STOP( master : Orchestrator) extends State(master) {
  override def onEntry() = {
    println("stop")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}
