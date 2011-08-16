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
    val null_2112927699 = new Channel
    null_2112927699.start
    val null_1553324600 = new Channel
    null_1553324600.start
    val null_2032298615 = new Channel
    null_2032298615.start
//Things
    val Arduino_1943219781 = new Arduino()
    val LED_1865707812 = new LED()
    val SoftTimer_102824579 = new SoftTimer()
    val Blink_1478354072 = new Blink()
//Bindings
    null_2112927699.connect(
      LED_1865707812.getPort("DigitalIO").get,
      Arduino_1943219781.getPort("DigitalIO").get
    )
    null_2112927699.connect(
      Arduino_1943219781.getPort("DigitalIO").get,
      LED_1865707812.getPort("DigitalIO").get
    )

    null_1553324600.connect(
      Blink_1478354072.getPort("HW").get,
      LED_1865707812.getPort("LED").get
    )
    null_1553324600.connect(
      LED_1865707812.getPort("LED").get,
      Blink_1478354072.getPort("HW").get
    )

    null_2032298615.connect(
      Blink_1478354072.getPort("HW").get,
      SoftTimer_102824579.getPort("timer").get
    )
    null_2032298615.connect(
      SoftTimer_102824579.getPort("timer").get,
      Blink_1478354072.getPort("HW").get
    )

//Starting Things
    Arduino_1943219781.start
    LED_1865707812.start
    SoftTimer_102824579.start
    Blink_1478354072.start
    
    new Timer().scheduleAtFixedRate(new PollTask(SoftTimer_102824579.getPort("Polling").get), 5, 5)
  }
  
}
