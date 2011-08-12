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

import org.sintef.smac.Event

object PingEvent {def getName = "PingEvent"}
object PongEvent {def getName = "PongEvent"}
object StopEvent {def getName = "StopEvent"}
object StartEvent {def getName = "StartEvent"}
object FastEvent {def getName = "FastEvent"}
object SlowEvent {def getName = "SlowEvent"}

case class PingEvent(override val name : String = PingEvent.getName) extends Event(name)
case class PongEvent(override val name : String = PongEvent.getName) extends Event(name)
case class StopEvent(override val name : String = StopEvent.getName) extends Event(name)
case class StartEvent(override val name : String = StartEvent.getName) extends Event(name)
case class FastEvent(override val name : String = FastEvent.getName) extends Event(name)
case class SlowEvent(override val name : String = SlowEvent.getName) extends Event(name)