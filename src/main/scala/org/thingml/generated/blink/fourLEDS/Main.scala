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
    val null_939842843 = new Channel
    null_939842843.start
    val null_1771872022 = new Channel
    null_1771872022.start
    val null_936197591 = new Channel
    null_936197591.start
    val null_549710183 = new Channel
    null_549710183.start
    val null_1210507212 = new Channel
    null_1210507212.start
    val null_439690959 = new Channel
    null_439690959.start
    val null_1162942631 = new Channel
    null_1162942631.start
    val null_1689446226 = new Channel
    null_1689446226.start
    val null_1250770274 = new Channel
    null_1250770274.start
//Things
    val Arduino_80531996 = new Arduino()
    val LED_1349896004 = new LED()
    val LED_143256665 = new LED()
    val LED_448511246 = new LED()
    val LED_491769242 = new LED()
    val SoftTimer_1233188165 = new SoftTimer()
    val Blink4Leds_1263643664 = new Blink4Leds()
//Bindings
    null_939842843.connect(
      LED_1349896004.getBehavior("LEDImpl").get.getPort("DigitalIO").get,
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get
    )
    null_939842843.connect(
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get,
      LED_1349896004.getBehavior("LEDImpl").get.getPort("DigitalIO").get
    )

    null_1771872022.connect(
      LED_143256665.getBehavior("LEDImpl").get.getPort("DigitalIO").get,
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get
    )
    null_1771872022.connect(
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get,
      LED_143256665.getBehavior("LEDImpl").get.getPort("DigitalIO").get
    )

    null_936197591.connect(
      LED_448511246.getBehavior("LEDImpl").get.getPort("DigitalIO").get,
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get
    )
    null_936197591.connect(
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get,
      LED_448511246.getBehavior("LEDImpl").get.getPort("DigitalIO").get
    )

    null_549710183.connect(
      LED_491769242.getBehavior("LEDImpl").get.getPort("DigitalIO").get,
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get
    )
    null_549710183.connect(
      Arduino_80531996.getBehavior("ArduinoStdlibImpl").get.getPort("DigitalIO").get,
      LED_491769242.getBehavior("LEDImpl").get.getPort("DigitalIO").get
    )

    null_1210507212.connect(
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get,
      LED_1349896004.getBehavior("LEDImpl").get.getPort("LED").get
    )
    null_1210507212.connect(
      LED_1349896004.getBehavior("LEDImpl").get.getPort("LED").get,
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get
    )

    null_439690959.connect(
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get,
      LED_143256665.getBehavior("LEDImpl").get.getPort("LED").get
    )
    null_439690959.connect(
      LED_143256665.getBehavior("LEDImpl").get.getPort("LED").get,
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get
    )

    null_1162942631.connect(
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get,
      LED_448511246.getBehavior("LEDImpl").get.getPort("LED").get
    )
    null_1162942631.connect(
      LED_448511246.getBehavior("LEDImpl").get.getPort("LED").get,
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get
    )

    null_1689446226.connect(
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get,
      LED_491769242.getBehavior("LEDImpl").get.getPort("LED").get
    )
    null_1689446226.connect(
      LED_491769242.getBehavior("LEDImpl").get.getPort("LED").get,
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("leds").get
    )

    null_1250770274.connect(
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("timer").get,
      SoftTimer_1233188165.getBehavior("SoftTimer").get.getPort("timer").get
    )
    null_1250770274.connect(
      SoftTimer_1233188165.getBehavior("SoftTimer").get.getPort("timer").get,
      Blink4Leds_1263643664.getBehavior("Blink4LedsImpl").get.getPort("timer").get
    )

//Starting Things
    Arduino_80531996.getBehaviors.foreach{sm => sm.start}
    LED_1349896004.getBehaviors.foreach{sm => sm.start}
    LED_143256665.getBehaviors.foreach{sm => sm.start}
    LED_448511246.getBehaviors.foreach{sm => sm.start}
    LED_491769242.getBehaviors.foreach{sm => sm.start}
    SoftTimer_1233188165.getBehaviors.foreach{sm => sm.start}
    Blink4Leds_1263643664.getBehaviors.foreach{sm => sm.start}

    val pollPort : Port = SoftTimer_1233188165.getBehavior("SoftTimer").get.getPort("Polling").get
    val t : Timer = new Timer()
    t.scheduleAtFixedRate(new PollTask(pollPort), 5, 5)
  }

}
