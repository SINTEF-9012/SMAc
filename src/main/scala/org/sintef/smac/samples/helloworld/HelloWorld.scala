/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

case class HelloWorldStateMachine(master : Orchestrator, parent : CompositeState, keepHistory : Boolean) extends CompositeState(master, parent, keepHistory) {

  //create sub-states
  val INIT_state = INIT(master, this)
  val H_state = H(master, this)
  val E_state = E(master, this)
  val L1_state = L1(master, this)
  val L2_state = L2(master, this)
  val O_state = O(master, this)
  val STOP_state = STOP(master, this)
  override val substates = List(INIT_state, H_state, E_state, L1_state, L2_state, O_state, STOP_state)
  override val initial = INIT_state
  
  //create transitions among sub-states
  val INIT_next_H_transition = INIT_Next_H(INIT_state, H_state, master, List(HEvent))
  val H_next_E_transition = H_Next_E(H_state, E_state, master, List(EEvent))
  val E_next_L1_transition = E_Next_L1(E_state, L1_state, master, List(LEvent))
  val L1_next_L2_transition = L1_Next_L2(L1_state, L2_state, master, List(LEvent))
  val L2_next_O_transition = L2_Next_O(L2_state, O_state, master, List(OEvent))
  val O_next_STOP_transition = O_Next_STOP(O_state, STOP_state, master, List())
  override val outGoingTransitions = List(INIT_next_H_transition, H_next_E_transition, E_next_L1_transition, L1_next_L2_transition, L2_next_O_transition, O_next_STOP_transition)    
  
  override def startState() = {
    super.startState
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

case class INIT(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("init")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class INIT_Next_H(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class H( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("W")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class H_Next_E(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class E( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("O")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class E_Next_L1(previous : State, next : State, master : Orchestrator, var events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class L1( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("R")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L1_Next_L2(previous : State, next : State, master : Orchestrator, var events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class L2( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("L")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class L2_Next_O(previous : State, next : State, master : Orchestrator, var events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class O( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("D")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}

case class O_Next_STOP(previous : State, next : State, master : Orchestrator, var events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }  
}

case class STOP( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("stop")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
}
