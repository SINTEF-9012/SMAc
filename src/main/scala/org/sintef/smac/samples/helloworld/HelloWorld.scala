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

case class HelloWorldStateMachine(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) extends StateAction(master) {

  def start = {
    master.register(sm)
    sm.start
  }
  
  val sm : StateMachine = new StateMachine(master, this, keepHistory)
  //create sub-states
  val INIT_state = new State(master, INIT(master))
  val H_state = new State(master, H(master))
  val E_state = new State(master, E(master))
  val L1_state = new State(master, L1(master))
  val L2_state = new State(master, L2(master))
  val O_state = new State(master, O(master))
  val STOP_state = new State(master, STOP(master))
  sm.addSubState(INIT_state)
  sm.addSubState(H_state)
  sm.addSubState(E_state)
  sm.addSubState(L1_state)
  sm.addSubState(L2_state)
  sm.addSubState(O_state)
  sm.addSubState(STOP_state)
  sm.setInitial(INIT_state)
  
  //create transitions among sub-states
  val INIT_next_H_transition = new Transition(INIT_state, H_state, master, INIT_Next_H(master))
  INIT_next_H_transition.initEvent(HEvent)
  val H_next_E_transition = new Transition(H_state, E_state, master, H_Next_E(master))
  H_next_E_transition.initEvent(EEvent)
  val E_next_L1_transition = new Transition(E_state, L1_state, master, E_Next_L1(master))
  E_next_L1_transition.initEvent(LEvent)
  val L1_next_L2_transition = new Transition(L1_state, L2_state, master, L1_Next_L2(master))
  L1_next_L2_transition.initEvent(LEvent)
  val L2_next_O_transition = new Transition(L2_state, O_state, master, L2_Next_O(master))
  L2_next_O_transition.initEvent(OEvent)
  val O_next_STOP_transition = new Transition(O_state, STOP_state, master, O_Next_STOP(master))
  
  H_next_E_transition.addEvent(EEvent)
  
  sm.addTransition(INIT_next_H_transition)
  sm.addTransition(H_next_E_transition)
  sm.addTransition(E_next_L1_transition)
  sm.addTransition(L1_next_L2_transition)
  sm.addTransition(L2_next_O_transition)
  sm.addTransition(O_next_STOP_transition)
  
/*  override def startState() = {
    super.startState
*/    if (withGUI)
      HelloWorldGUI.init
/*  }
*/
  
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

case class INIT(master : Orchestrator) extends StateAction(master) {
  override def onEntry() = {
    println("init")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class INIT_Next_H(master : Orchestrator) extends TransitionAction(master) { 
  //this.initEvent(HEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class H( master : Orchestrator) extends StateAction(master) {
  override def onEntry() = {
    println("W")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class H_Next_E(master : Orchestrator) extends TransitionAction(master) { 
  //this.initEvent(EEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class E( master : Orchestrator) extends StateAction(master) {
  override def onEntry() = {
    println("O")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class E_Next_L1(master : Orchestrator) extends TransitionAction(master) { 
  //this.initEvent(LEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class L1( master : Orchestrator) extends StateAction(master) {
  override def onEntry() = {
    println("R")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L1_Next_L2(master : Orchestrator) extends TransitionAction(master) { 
  //this.initEvent(LEvent)
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class L2( master : Orchestrator) extends StateAction(master) {
  override def onEntry() = {
    println("L")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L2_Next_O(master : Orchestrator) extends TransitionAction(master) { 
  //this.initEvent(OEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class O( master : Orchestrator) extends StateAction(master) {
  override def onEntry() = {
    println("D")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class O_Next_STOP(master : Orchestrator) extends TransitionAction(master) { 
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class STOP( master : Orchestrator) extends StateAction(master) {
  override def onEntry() = {
    println("stop")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}
