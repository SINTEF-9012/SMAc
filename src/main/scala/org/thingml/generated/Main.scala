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
package org.thingml.generated

import java.util.Timer
import java.util.TimerTask
import org.sintef.smac._

class PollTask(p : Port) extends TimerTask {
  override def run() {
    //println("PollTask")
    p ! new Poll()
  }
}

// Initialize instance variables and states
object Main {

  def main(args: Array[String]): Unit = {
//Connectors
    val null_948074856 = new Channel
    null_948074856.start
    val null_311092314 = new Channel
    null_311092314.start
    val null_1524582596 = new Channel
    null_1524582596.start
//Things
    val Arduino_154082288 = new Arduino()
    val LED_763555691 = new LED()
    val SoftTimer_1913537093 = new SoftTimer()
    val Blink_1595847065 = new Blink()
//Bindings
    null_948074856.connect(
      LED_763555691.getBehavior("LEDImpl").get.getPort("DigitalIO").get,
      Arduino_154082288.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get
    )
    null_948074856.connect(
      Arduino_154082288.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get,
      LED_763555691.getBehavior("LEDImpl").get.getPort("DigitalIO").get
    )

    null_311092314.connect(
      Blink_1595847065.getBehavior("BlinkImpl").get.getPort("HW").get,
      LED_763555691.getBehavior("LEDImpl").get.getPort("LED").get
    )
    null_311092314.connect(
      LED_763555691.getBehavior("LEDImpl").get.getPort("LED").get,
      Blink_1595847065.getBehavior("BlinkImpl").get.getPort("HW").get
    )

    null_1524582596.connect(
      Blink_1595847065.getBehavior("BlinkImpl").get.getPort("HW").get,
      SoftTimer_1913537093.getBehavior("SoftTimer").get.getPort("timer").get
    )
    null_1524582596.connect(
      SoftTimer_1913537093.getBehavior("SoftTimer").get.getPort("timer").get,
      Blink_1595847065.getBehavior("BlinkImpl").get.getPort("HW").get
    )
    
//Starting Things
    Arduino_154082288.getBehaviors.foreach{sm => sm.start}
    LED_763555691.getBehaviors.foreach{sm => sm.start}
    SoftTimer_1913537093.getBehaviors.foreach{sm => sm.start}
    Blink_1595847065.getBehaviors.foreach{sm => sm.start}
    
    val pollPort : Port = SoftTimer_1913537093.getBehavior("SoftTimer").get.getPort("Polling").get
    val t : Timer = new Timer()
    t.scheduleAtFixedRate(new PollTask(pollPort), 5, 5)
  }

}