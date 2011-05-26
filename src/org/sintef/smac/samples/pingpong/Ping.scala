package org.sintef.smac.samples.pingpong

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
import org.sintef.smac.samples.pingpong._

class PingStateMachine(master : Orchestrator, parent : CompositeState, keepHistory : Boolean) extends CompositeState(master, parent, keepHistory){

  //create sub-states
  val ping = Ping(master, this)
  val stop = Stop(master, this)
  override val substates = List(ping, stop)
  override val initial = stop;

  //create transitions among sub-states
  val pongTransition = PongTransition(ping, ping, master, List(PongEvent))
  val stopTransition = StopTransition(ping, stop, master, List(StopEvent))
  val startTransition = StartTransition(stop, ping, master, List(StartEvent))
  override val outGoingTransitions = List(pongTransition, stopTransition, startTransition)
  
  override def startState() = {
    super.startState
    PingGUI.init
  }
  
  def onEntry = {}
  
  def onExit = {}

  //This is just an ugly GUI...
  object PingGUI extends ActionListener {
    val frame = new JFrame("PingGUI")
    val screen = new JTextPane()
	
    val sendButtonPong : JButton = new JButton("Send")
    val sendButtonStop : JButton = new JButton("Send")
    val sendButtonStart : JButton = new JButton("Send")

    def init {
      frame.setLayout(new GridBagLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      var c : GridBagConstraints = new GridBagConstraints();
      c.gridwidth = 1;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.insets = new Insets(0,3,0,3);
    
      //GUI related to pong
      c.gridy = 0;
      c.gridx = 0;
      frame.add(new JLabel("pong"), c);
    
      c.gridy = 1;
      c.gridx = 0;
      frame.add(createPongPanel(), c);
      
      c.gridy = 2;
      c.gridx = 0;
      frame.add(sendButtonPong, c);
      sendButtonPong.addActionListener(this)
      
      //GUI related to stop
      c.gridy = 0;
      c.gridx = 1;
      frame.add(new JLabel("stop"), c);
    
      c.gridy = 1;
      c.gridx = 1;
      frame.add(createStopPanel(), c);
      
      c.gridy = 2;
      c.gridx = 1;
      frame.add(sendButtonStop, c);
      sendButtonStop.addActionListener(this)
      
      //GUI related to start
      c.gridy = 0;
      c.gridx = 2;
      frame.add(new JLabel("start"), c);
    
      c.gridy = 1;
      c.gridx = 2;
      frame.add(createStartPanel(), c);
      
      c.gridy = 2;
      c.gridx = 2;
      frame.add(sendButtonStart, c);
      sendButtonStart.addActionListener(this)

      
      frame.pack
      frame.setVisible(true)
    }
  
    def createPongPanel() : JPanel = {
      var c : GridBagConstraints = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 0.5;
      new JPanel(new GridBagLayout());
    }
    
    def createStopPanel() : JPanel = {
      var c : GridBagConstraints = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 0.5;
      new JPanel(new GridBagLayout());
    }
    
    def createStartPanel() : JPanel = {
      var c : GridBagConstraints = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 0.5;
      new JPanel(new GridBagLayout());
    }
    
    def actionPerformed(ae : ActionEvent) = {
      ae.getSource match {
        case b : JButton =>
          if (b == sendButtonPong) {
            master ! PongEvent
          }
          else if (b == sendButtonStop) {
            master ! StopEvent
          }
          else if (b == sendButtonStart) {
            master ! StartEvent
          }
      }
    }
  }    
}

case class Ping(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
  val max = 10000
  var count = 0
  var delay = 25
    
  override def onEntry() = {
    println("Ping.onEntry")
    if (count < max){
      Thread.sleep(delay)
      master ! PingEvent
      count += 1
    }
    else {
      master ! StopEvent
      count = 0
    }
  }
  
  override def onExit() = {
    println("Ping.onExit")
  }
  
}

case class Stop(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
  
  override def onEntry() = {
    println("Stop.onEntry")
  }
  
  override def onExit() = {
    println("Stop.onExit")
  } 

}


//Messages defined in the state machine
case class PongTransition(previous : State, next : State, master : Orchestrator, var events : List[Event]) extends Transition(previous, next, master, events) {
  def executeActions() = {
    println("PongTransition")
  }
}

case class StopTransition(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) { 
  def executeActions() = {
    println("StopTransition")
  }
}


case class StartTransition(previous : State, next : State, master : Orchestrator, events : List[Event]) extends Transition(previous, next, master, events) {
  def executeActions() = {
    println("StartTransition")
  }
}