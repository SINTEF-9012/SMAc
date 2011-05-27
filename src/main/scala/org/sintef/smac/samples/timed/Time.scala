/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sintef.smac.samples.timed

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

case object LeftEvent extends Event {}
case object RightEvent extends Event {}					
case object CenterEvent extends Event {}									


case class HelloWorldStateMachine(master : Orchestrator, parent : CompositeState, keepHistory : Boolean) extends CompositeState(master, parent, keepHistory) {

  //create sub-states
  val center = Center(master, this)
  val right = Right(master, this)
  val left = Left(master, this)
  override val substates = List(center, right, left)
  override val initial = center
  
  //create transitions among sub-states
  val centerToLeft = CenterToLeft(center, left, master, List(LeftEvent))
  val centerToRight = CenterToRight(center, right, master, List(RightEvent))
  
  val leftToCenter = LeftToCenter(left, center, master, List(CenterEvent))
  val leftToCenterTimeout = LeftToCenterTimeout(left, center, master, List(), 2000)
  
  val rightToCenter = RightToCenter(right, center, master, List(CenterEvent))
  val rightToCenterTimeout = RightToCenterTimeout(right, center, master, List(), 2000)
  
  override val outGoingTransitions = List(centerToLeft, centerToRight, leftToCenter, leftToCenterTimeout, rightToCenter, rightToCenterTimeout)    
  
  override def startState() = {
    super.startState
    TimeGUI.init
  }
  
  override def onEntry() = {
    println("start")
  }
  
  override def onExit() = {
    //TODO: define actions here
  }
  
  //This is just an ugly GUI...
  object TimeGUI extends ActionListener {
    val frame = new JFrame("Time Client")
    val screen = new JTextPane()
	
    val sendLeft : JButton = new JButton("Send")
    val sendCenter : JButton = new JButton("Send")
    val sendRight : JButton = new JButton("Send")
    

    def init {
      frame.setLayout(new GridBagLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      var c : GridBagConstraints = new GridBagConstraints();
      c.gridwidth = 1;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.insets = new Insets(0,3,0,3);
    
      //GUI related to Left
      c.gridy = 0;
      c.gridx = 0;
      frame.add(new JLabel("Left"), c);
    
      c.gridy = 1;
      c.gridx = 0;
      frame.add(createPanel(), c);
      
      c.gridy = 2;
      c.gridx = 0;
      frame.add(sendLeft, c);
      sendLeft.addActionListener(this)
      
      //GUI related to Center
      c.gridy = 0;
      c.gridx = 1;
      frame.add(new JLabel("Center"), c);
    
      c.gridy = 1;
      c.gridx = 1;
      frame.add(createPanel(), c);
      
      c.gridy = 2;
      c.gridx = 1;
      frame.add(sendCenter, c);
      sendCenter.addActionListener(this)
      
      //GUI related to Right
      c.gridy = 0;
      c.gridx = 2;
      frame.add(new JLabel("Right"), c);
    
      c.gridy = 1;
      c.gridx = 2;
      frame.add(createPanel(), c);
      
      c.gridy = 2;
      c.gridx = 2;
      frame.add(sendRight, c);
      sendRight.addActionListener(this)
      
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
          if (b == sendLeft) {
            master ! LeftEvent
          }
          else if (b == sendCenter) {
            master ! CenterEvent
          }
          else if (b == sendRight) {
            master ! RightEvent
          }
      }
    }
    
  }
}

case class Center(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("CENTER")
  }
  
  override def onExit() = {
    println("leaving CENTER")
  }
}

case class CenterToLeft(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class CenterToRight(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class Left( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("LEFT")
  }
  
  override def onExit() = {
    println("Leaving LEFT")
  }
}

case class LeftToCenter(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class LeftToCenterTimeout(previous : State, next : State, master : Orchestrator, events : List[Event], delay : Long) extends TimedTransition(previous, next, master, events, delay) { 
  def executeActions() = {
    println("TIMEOUT")
  }
}

case class Right( master : Orchestrator,  parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("RIGHT")
  }
  
  override def onExit() = {
    println("Leaving RIGHT")
  }
}

case class RightToCenter(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    //TODO: define actions here
  }
}

case class RightToCenterTimeout(previous : State, next : State, master : Orchestrator, events : List[Event], delay : Long) extends TimedTransition(previous, next, master, events, delay) { 
  def executeActions() = {
    println("TIMEOUT")
  }
}