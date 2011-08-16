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
package org.thingml.generated.region.simple

import java.util.Timer
import java.util.TimerTask
import org.sintef.smac._

class PollTask(p : Port) extends TimerTask{
  override def run {
    //println("poll")
    p ! new Poll()
  }
}

// Initialize instance variables and states
object Main {

  def main(args: Array[String]): Unit = {
//Connectors
    val null_70552537 = new Channel
    null_70552537.start
    val null_97979571 = new Channel
    null_97979571.start
    val null_675501707 = new Channel
    null_675501707.start
//Things
    val TestRegion_632640515 = new TestRegion()
    val Arduino_740362245 = new Arduino()
    val LED_283487041 = new LED()
    val ArduinoHarness_782756695 = new ArduinoHarness()
//Bindings
    null_70552537.connect(
      TestRegion_632640515.getPort("harness").get,
      ArduinoHarness_782756695.getPort("test").get
    )
    null_70552537.connect(
      ArduinoHarness_782756695.getPort("test").get,
      TestRegion_632640515.getPort("harness").get
    )

    null_97979571.connect(
      LED_283487041.getPort("DigitalIO").get,
      Arduino_740362245.getPort("DigitalIO").get
    )
    null_97979571.connect(
      Arduino_740362245.getPort("DigitalIO").get,
      LED_283487041.getPort("DigitalIO").get
    )

    null_675501707.connect(
      ArduinoHarness_782756695.getPort("led").get,
      LED_283487041.getPort("LED").get
    )
    null_675501707.connect(
      LED_283487041.getPort("LED").get,
      ArduinoHarness_782756695.getPort("led").get
    )

//Starting Things
    TestRegion_632640515.start
    
    
    Thread.sleep(500)
    TestRegion_632640515.getPort("harness").get ! new TestIn('t')
    Thread.sleep(500)
    TestRegion_632640515.getPort("harness").get ! new TestIn('t')
    Thread.sleep(500)
    TestRegion_632640515.getPort("harness").get ! new TestIn('t')
  }

}

