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


case class HelloWorldStateMachine(master : Orchestrator, parent : Option[CompositeState], keepHistory : Boolean, withGUI : Boolean) extends CompositeState(master, parent, keepHistory) {

  //create sub-states
  val center = Center(master, Option(this))
  val right = Right(master, Option(this))
  val left = Left(master, Option(this))
  override val substates = List(center, right, left)
  override val initial = center
  
  //create transitions among sub-states
  val centerToLeft = CenterToLeft(center, left, master)
  val centerToRight = CenterToRight(center, right, master)
  
  val leftToCenter = LeftToCenter(left, center, master)
  val leftToCenterTimeout = LeftToCenterTimeout(left, center, master, 2000)
  
  val rightToCenter = RightToCenter(right, center, master)
  val rightToCenterTimeout = RightToCenterTimeout(right, center, master, 2000)
  
  override val outGoingTransitions = List(centerToLeft, centerToRight, leftToCenter, leftToCenterTimeout, rightToCenter, rightToCenterTimeout)    
  
  override def startState() = {
    super.startState
    if (withGUI)
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

case class Center(master : Orchestrator, parent : Option[CompositeState]) extends State(master, parent) {
  override def onEntry() = {
    println("CENTER")
  }
  
  override def onExit() = {
    println("leaving CENTER")
  }
}

case class CenterToLeft(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(LeftEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class CenterToRight(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(RightEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class Left( master : Orchestrator,  parent : Option[CompositeState]) extends State(master, parent) {
  override def onEntry() = {
    println("LEFT")
  }
  
  override def onExit() = {
    println("Leaving LEFT")
  }
}

case class LeftToCenter(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(CenterEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class LeftToCenterTimeout(previous : State, next : State, master : Orchestrator, delay : Long) extends TimedTransition(previous, next, master, delay) { 
  
  def executeActions() = {
    println("TIMEOUT")
  }
}

case class Right( master : Orchestrator,  parent : Option[CompositeState]) extends State(master, parent) {
  override def onEntry() = {
    println("RIGHT")
  }
  
  override def onExit() = {
    println("Leaving RIGHT")
  }
}

case class RightToCenter(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) { 
  this.initEvent(CenterEvent)
  def executeActions() = {
    //TODO: define actions here
  }
}

case class RightToCenterTimeout(previous : State, next : State, master : Orchestrator, delay : Long) extends TimedTransition(previous, next, master, delay) { 
  def executeActions() = {
    println("TIMEOUT")
  }
}