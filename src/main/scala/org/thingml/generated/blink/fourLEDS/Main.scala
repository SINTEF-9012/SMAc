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
package org.thingml.generated.blink.fourLEDS

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
    val null_1884932277 = new Channel
    null_1884932277.start
    val null_1960929157 = new Channel
    null_1960929157.start
    val null_1312568829 = new Channel
    null_1312568829.start
    val null_534000652 = new Channel
    null_534000652.start
    val null_1327161015 = new Channel
    null_1327161015.start
    val null_1208347121 = new Channel
    null_1208347121.start
    val null_1052520751 = new Channel
    null_1052520751.start
    val null_1780804346 = new Channel
    null_1780804346.start
    val null_1696522263 = new Channel
    null_1696522263.start
//Things
    val Arduino_273918421 = new Arduino()
    val LED_493939748 = new LED()
    val LED_1989256443 = new LED()
    val LED_1580255587 = new LED()
    val LED_2017895774 = new LED()
    val SoftTimer_305666716 = new SoftTimer()
    val Blink4Leds_1571153503 = new Blink4Leds()
//Bindings
    null_1884932277.connect(
      LED_493939748.getPort("DigitalIO").get,
      Arduino_273918421.getPort("DigitalIO").get
    )
    null_1884932277.connect(
      Arduino_273918421.getPort("DigitalIO").get,
      LED_493939748.getPort("DigitalIO").get
    )

    null_1960929157.connect(
      LED_1989256443.getPort("DigitalIO").get,
      Arduino_273918421.getPort("DigitalIO").get
    )
    null_1960929157.connect(
      Arduino_273918421.getPort("DigitalIO").get,
      LED_1989256443.getPort("DigitalIO").get
    )

    null_1312568829.connect(
      LED_1580255587.getPort("DigitalIO").get,
      Arduino_273918421.getPort("DigitalIO").get
    )
    null_1312568829.connect(
      Arduino_273918421.getPort("DigitalIO").get,
      LED_1580255587.getPort("DigitalIO").get
    )

    null_534000652.connect(
      LED_2017895774.getPort("DigitalIO").get,
      Arduino_273918421.getPort("DigitalIO").get
    )
    null_534000652.connect(
      Arduino_273918421.getPort("DigitalIO").get,
      LED_2017895774.getPort("DigitalIO").get
    )

    null_1327161015.connect(
      Blink4Leds_1571153503.getPort("leds").get,
      LED_493939748.getPort("LED").get
    )
    null_1327161015.connect(
      LED_493939748.getPort("LED").get,
      Blink4Leds_1571153503.getPort("leds").get
    )

    null_1208347121.connect(
      Blink4Leds_1571153503.getPort("leds").get,
      LED_1989256443.getPort("LED").get
    )
    null_1208347121.connect(
      LED_1989256443.getPort("LED").get,
      Blink4Leds_1571153503.getPort("leds").get
    )

    null_1052520751.connect(
      Blink4Leds_1571153503.getPort("leds").get,
      LED_1580255587.getPort("LED").get
    )
    null_1052520751.connect(
      LED_1580255587.getPort("LED").get,
      Blink4Leds_1571153503.getPort("leds").get
    )

    null_1780804346.connect(
      Blink4Leds_1571153503.getPort("leds").get,
      LED_2017895774.getPort("LED").get
    )
    null_1780804346.connect(
      LED_2017895774.getPort("LED").get,
      Blink4Leds_1571153503.getPort("leds").get
    )

    null_1696522263.connect(
      Blink4Leds_1571153503.getPort("timer").get,
      SoftTimer_305666716.getPort("timer").get
    )
    null_1696522263.connect(
      SoftTimer_305666716.getPort("timer").get,
      Blink4Leds_1571153503.getPort("timer").get
    )

//Starting Things
    Arduino_273918421.start
    LED_493939748.start
    LED_1989256443.start
    LED_1580255587.start
    LED_2017895774.start
    SoftTimer_305666716.start
    Blink4Leds_1571153503.start
    
    new Timer().scheduleAtFixedRate(new PollTask(SoftTimer_305666716.getPort("Polling").get), 5, 5)
    
  }

}
