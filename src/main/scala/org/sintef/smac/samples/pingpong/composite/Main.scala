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

import org.sintef.smac._
import org.sintef.smac.samples.pingpong._
import org.sintef.smac.samples.pingpong.composite._

object Main {

  /**
   * This main allows executing the ping pong in two modes:
   * 1/ Ping state machine + GUI: You can manually send event to the Ping state
   *    machine. For example, start, some pong (it should reply some ping), 
   *    stop, some pong again (it should do nothing), start again, etc.
   * 2/ Ping state machine + GUI + Pong state machine: This time, pong events are
   *    sent by the pong state machine. Just send the start event and wait until
   *    the 10 000 ping pong exchanges are done... or send the stop event at any
   *    moment. To enable this mode, just uncomment the 3 commented lines.
   */
  def main(args: Array[String]): Unit = {       
    val selfChannel : Channel = new Channel().start.asInstanceOf[Channel]
    val sm = new PingComponent(true, true)
    selfChannel.connect(sm.getPort("ping").get, sm.getPort("ping").get)
    sm.start
  }
}
