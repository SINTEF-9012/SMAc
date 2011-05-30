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

class PongStateMachine(master : Orchestrator, parent : CompositeState, keepHistory : Boolean) extends CompositeState(master, parent, keepHistory) {
  
  //create sub-states
  val pong = Pong(master, this)
  override val substates = List(pong)
  override val initial = pong
  
  //create transitions among sub-states
  val pingTransition = PingTransition(pong, pong, master)
  override val outGoingTransitions = List(pingTransition)
  
  def onEntry() = {}
  
  def onExit() = {}
}

case class Pong(master : Orchestrator, parent : CompositeState) extends State(master, parent) {
  override def onEntry() = {
    println("Pong.onEntry")
    master ! PongEvent
  }
  
  override def onExit() = {
    println("Pong.onExit")
  }
  
}

//Messages defined in the state machine
case class PingTransition(previous : State, next : State, master : Orchestrator) extends Transition(previous, next, master) {
  this.initEvent(PingEvent)
  def executeActions() = {
    println("PingTransition")
  }
}