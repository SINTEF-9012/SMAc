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
package org.sintef.smac.samples.pingpong

import org.sintef.smac._
import org.sintef.smac.samples.pingpong._

class PongStateMachine(keepHistory : Boolean) extends StateAction {
    
  /*def start = {
    master.register(sm)
    sm.start
  }*/
  
  val sm : StateMachine = new StateMachine(this, keepHistory)
  //create sub-states
  val pong = new State(Pong(), sm)
  sm.addSubState(pong)
  sm.setInitial(pong)
  
  //create transitions among sub-states
  val pingTransition = new InternalTransition(pong, PingTransition(), sm)
  pingTransition.initEvent(PingEvent)
  sm.addInternalTransition(pingTransition)
  
  def onEntry() = {}
  
  def onExit() = {}
}

case class Pong extends StateAction {
  override def onEntry() = {
    println("Pong.onEntry")
    handler.root.getPort("pong").get ! PongEvent
  }
  
  override def onExit() = {
    println("Pong.onExit")
  }
  
}

//Messages defined in the state machine
case class PingTransition extends InternalTransitionAction {
  //this.initEvent(PingEvent)
  def executeActions() = {
    println("PingTransition")
  }
}