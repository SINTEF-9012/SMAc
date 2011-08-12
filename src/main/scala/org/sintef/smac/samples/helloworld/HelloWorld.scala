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

object HEvent {
  def getName = "HEvent"
}
object EEvent {
  def getName = "EEvent"
}
object LEvent {
  def getName = "LEvent"
}
object OEvent {
  def getName = "OEvent"
}

case class HEvent(override val name : String = HEvent.getName) extends Event(name)
case class EEvent(override val name : String = EEvent.getName) extends Event(name)
case class LEvent(override val name : String = LEvent.getName) extends Event(name)
case class OEvent(override val name : String = OEvent.getName) extends Event(name)

case class HelloWorldStateMachine(keepHistory : Boolean, withGUI : Boolean) extends StateAction {

  /*def start = {
    master.register(sm)
    sm.start
  }*/
  
  def getBehavior = sm
  
  val sm : StateMachine = new StateMachine(this, keepHistory)
  val hello = new Port("hello", List(HEvent.getName), List(HEvent.getName), sm).start
  //create sub-states
  val INIT_state = new State(INIT(), sm)
  val H_state = new State(H(), sm)
  val E_state = new State(E(), sm)
  val L1_state = new State(L1(), sm)
  val L2_state = new State(L2(), sm)
  val O_state = new State(O(), sm)
  val STOP_state = new State(STOP(), sm)
  sm.addSubState(INIT_state)
  sm.addSubState(H_state)
  sm.addSubState(E_state)
  sm.addSubState(L1_state)
  sm.addSubState(L2_state)
  sm.addSubState(O_state)
  sm.addSubState(STOP_state)
  sm.setInitial(INIT_state)
  
  //create transitions among sub-states
  val INIT_next_H_transition = new Transition(INIT_state, H_state, INIT_Next_H(), sm)
  INIT_next_H_transition.initEvent(HEvent.getName)
  val H_next_E_transition = new Transition(H_state, E_state, H_Next_E(), sm)
  H_next_E_transition.initEvent(EEvent.getName)
  val E_next_L1_transition = new Transition(E_state, L1_state, E_Next_L1(), sm)
  E_next_L1_transition.initEvent(LEvent.getName)
  val L1_next_L2_transition = new Transition(L1_state, L2_state, L1_Next_L2(), sm)
  L1_next_L2_transition.initEvent(LEvent.getName)
  val L2_next_O_transition = new Transition(L2_state, O_state, L2_Next_O(), sm)
  L2_next_O_transition.initEvent(OEvent.getName)
  val O_next_STOP_transition = new Transition(O_state, STOP_state, O_Next_STOP(), sm)
  
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
            sm.getPort("hello").get.send(new HEvent())
          }
          else if (b == sendE) {
            sm.getPort("hello").get.send(new EEvent())
          }
          else if (b == sendL) {
            sm.getPort("hello").get.send(new LEvent())
          }
          else if (b == sendO) {
            sm.getPort("hello").get.send(new OEvent())
          }
      }
    }
    
  }
}

case class INIT extends StateAction {
  override def onEntry() = {
    println("init")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class INIT_Next_H extends TransitionAction { 
  //this.initEvent(HEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class H extends StateAction {
  override def onEntry() = {
    println("W")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class H_Next_E extends TransitionAction { 
  //this.initEvent(EEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class E extends StateAction {
  override def onEntry() = {
    println("O")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class E_Next_L1 extends TransitionAction { 
  //this.initEvent(LEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class L1 extends StateAction {
  override def onEntry() = {
    println("R")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L1_Next_L2 extends TransitionAction { 
  //this.initEvent(LEvent)
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class L2 extends StateAction {
  override def onEntry() = {
    println("L")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L2_Next_O extends TransitionAction { 
  //this.initEvent(OEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class O extends StateAction {
  override def onEntry() = {
    println("D")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class O_Next_STOP extends TransitionAction { 
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class STOP extends StateAction {
  override def onEntry() = {
    println("stop")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}
