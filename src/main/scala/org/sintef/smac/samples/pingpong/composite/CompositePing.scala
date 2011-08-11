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
package org.sintef.smac.samples.pingpong.composite

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

class PingStateMachine(master : Orchestrator, keepHistory : Boolean, withGUI : Boolean) extends StateAction(master){

  def start = {
    master.register(sm)
    sm.start
  }
  
  val sm : StateMachine = new StateMachine(master, this, keepHistory)
  //create sub-states
  val fast = Fast(master, true).getComposite
  val slow = Slow(master, true).getComposite
  sm.addSubState(fast)
  sm.addSubState(slow)
  sm.setInitial(slow)
    
  //create transitions among sub-states
  val slowTransition = new Transition(fast, slow, master, SlowTransition(master))
  slowTransition.initEvent(SlowEvent)
  val fastTransition = new Transition(slow, fast, master, FastTransition(master))
  fastTransition.initEvent(FastEvent)
  sm.addTransition(slowTransition)
  sm.addTransition(fastTransition)
    
  /*override def startState() = {
   super.startState*/
  if (withGUI)
    PingGUI.init
  //}
  
  def onEntry = {}
  
  def onExit = {}

  //This is just an ugly GUI...
  object PingGUI extends ActionListener {
    val frame = new JFrame("PingGUI")
    val screen = new JTextPane()
	
    val sendButtonPong : JButton = new JButton("Send")
    val sendButtonStop : JButton = new JButton("Send")
    val sendButtonStart : JButton = new JButton("Send")
    val sendButtonSlow : JButton = new JButton("Send")
    val sendButtonFast : JButton = new JButton("Send")

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

      //GUI related to slow
      c.gridy = 0;
      c.gridx = 3;
      frame.add(new JLabel("slow"), c);
    
      c.gridy = 1;
      c.gridx = 3;
      frame.add(createSlowPanel(), c);
      
      c.gridy = 2;
      c.gridx = 3;
      frame.add(sendButtonSlow, c);
      sendButtonSlow.addActionListener(this)
      
      //GUI related to fast
      c.gridy = 0;
      c.gridx = 4;
      frame.add(new JLabel("fast"), c);
    
      c.gridy = 1;
      c.gridx = 4;
      frame.add(createFastPanel(), c);
      
      c.gridy = 2;
      c.gridx = 4;
      frame.add(sendButtonFast, c);
      sendButtonFast.addActionListener(this)
      
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
    
    def createFastPanel() : JPanel = {
      var c : GridBagConstraints = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 0.5;
      new JPanel(new GridBagLayout());
    }
    
    def createSlowPanel() : JPanel = {
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
          else if (b == sendButtonSlow) {
            master ! SlowEvent
          }
          else if (b == sendButtonFast) {
            master ! FastEvent
          }
      }
    }
  }    
}

case class Fast(master : Orchestrator, keepHistory : Boolean) extends StateAction(master) {
  
  def getComposite : CompositeState = c
  
  val c : CompositeState = new CompositeState(master, this, keepHistory)
  //create sub-states
  val ping = new State(master, Ping(master, 25))
  val stop = new State(master, Stop(master))
  c.addSubState(ping)
  c.addSubState(stop)
  c.setInitial(stop)
    
  //create transitions among sub-states
  val pongTransition = new InternalTransition(ping, master, PongTransition(master))
  pongTransition.initEvent(PongEvent)
  val stopTransition = new Transition(ping, stop, master, StopTransition(master))
  stopTransition.initEvent(StopEvent)
  val startTransition = new Transition(stop, ping, master, StartTransition(master))
  startTransition.initEvent(StartEvent)
  c.addInternalTransition(pongTransition)
  c.addTransition(stopTransition)
  c.addTransition(startTransition)
    
  override def onEntry() = {
    println("Fast.onEntry")
  }
 
  override def onExit() = {
    println("Fast.onExit")
  }
}

case class Slow(master : Orchestrator, keepHistory : Boolean) extends StateAction(master) {
  
  def getComposite : CompositeState = c
  
  val c : CompositeState = new CompositeState(master, this, keepHistory)
//create sub-states
  val ping = new State(master, Ping(master, 1000))
  val stop = new State(master, Stop(master))
  c.addSubState(ping)
  c.addSubState(stop)
  c.setInitial(stop)
    
  //create transitions among sub-states
  val pongTransition = new InternalTransition(ping, master, PongTransition(master))
  pongTransition.initEvent(PongEvent)
  val stopTransition = new Transition(ping, stop, master, StopTransition(master))
  stopTransition.initEvent(StopEvent)
  val startTransition = new Transition(stop, ping, master, StartTransition(master))
  startTransition.initEvent(StartEvent)
  c.addInternalTransition(pongTransition)
  c.addTransition(stopTransition)
  c.addTransition(startTransition)
  
  override def onEntry() = {
    println("Slow.onEntry")
  }
 
  override def onExit() = {
    println("Slow.onExit")
  }
}

case class Ping(master : Orchestrator, delay : Long) extends StateAction(master) {
  val max = 10000
  var count = 0
    
  override def onEntry() = {
    println("Ping.onEntry")
    if (count < max){
      //Thread.sleep(delay)
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

case class Stop(master : Orchestrator) extends StateAction(master) {
  
  override def onEntry() = {
    println("Stop.onEntry")
  }
  
  override def onExit() = {
    println("Stop.onExit")
  } 

}


//Messages defined in the state machine
case class PongTransition(master : Orchestrator) extends InternalTransitionAction(master) {
  //this.initEvent(PongEvent)
  def executeActions() = {
    println("PongTransition")
  }
}

case class StopTransition(master : Orchestrator) extends TransitionAction(master) { 
  //this.initEvent(StopEvent)
  def executeActions() = {
    println("StopTransition")
  }
}


case class StartTransition(master : Orchestrator) extends TransitionAction(master) {
  //this.initEvent(StartEvent)
  def executeActions() = {
    println("StartTransition")
  }
}

case class FastTransition(master : Orchestrator) extends TransitionAction(master) {
  //this.initEvent(FastEvent)
  def executeActions() = {
    println("FastTransition")
  }
}

case class SlowTransition(master : Orchestrator) extends TransitionAction(master) {
  //this.initEvent(SlowEvent)
  def executeActions() = {
    println("SlowTransition")
  }
}