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

import org.sintef.smac._

// Initialize instance variables and states
object Main {

  def main(args: Array[String]): Unit = {
//Connectors
    val null_2034408626 = new Channel
    null_2034408626.start
    val null_1762380181 = new Channel
    null_1762380181.start
    val null_1590114020 = new Channel
    null_1590114020.start
    val null_1300857109 = new Channel
    null_1300857109.start
    val null_493730314 = new Channel
    null_493730314.start
    val null_894261275 = new Channel
    null_894261275.start
    val null_13817227 = new Channel
    null_13817227.start
    val null_1841438955 = new Channel
    null_1841438955.start
//Things
    val Arduino_701179797 = new Arduino()
    val LED_1262837020 = new LED()
    val LED_948129019 = new LED()
    val SoftTimer_647098590 = new SoftTimer()
    val SoftTimer_513595611 = new SoftTimer()
    val Blink2Leds_1440914329 = new Blink2Leds()
//Bindings
    null_2034408626.connect(
      LED_1262837020.getPort("DigitalIO").get,
      Arduino_701179797.getPort("DigitalIO").get
    )
    null_2034408626.connect(
      Arduino_701179797.getPort("DigitalIO").get,
      LED_1262837020.getPort("DigitalIO").get
    )

    null_1762380181.connect(
      LED_948129019.getPort("DigitalIO").get,
      Arduino_701179797.getPort("DigitalIO").get
    )
    null_1762380181.connect(
      Arduino_701179797.getPort("DigitalIO").get,
      LED_948129019.getPort("DigitalIO").get
    )

    null_1590114020.connect(
      SoftTimer_647098590.getPort("Polling").get,
      Arduino_701179797.getPort("Polling").get
    )
    null_1590114020.connect(
      Arduino_701179797.getPort("Polling").get,
      SoftTimer_647098590.getPort("Polling").get
    )

    null_1300857109.connect(
      SoftTimer_513595611.getPort("Polling").get,
      Arduino_701179797.getPort("Polling").get
    )
    null_1300857109.connect(
      Arduino_701179797.getPort("Polling").get,
      SoftTimer_513595611.getPort("Polling").get
    )

    null_493730314.connect(
      Blink2Leds_1440914329.getPort("led1").get,
      LED_1262837020.getPort("LED").get
    )
    null_493730314.connect(
      LED_1262837020.getPort("LED").get,
      Blink2Leds_1440914329.getPort("led1").get
    )

    null_894261275.connect(
      Blink2Leds_1440914329.getPort("timer1").get,
      SoftTimer_647098590.getPort("timer").get
    )
    null_894261275.connect(
      SoftTimer_647098590.getPort("timer").get,
      Blink2Leds_1440914329.getPort("timer1").get
    )

    null_13817227.connect(
      Blink2Leds_1440914329.getPort("led2").get,
      LED_948129019.getPort("LED").get
    )
    null_13817227.connect(
      LED_948129019.getPort("LED").get,
      Blink2Leds_1440914329.getPort("led2").get
    )

    null_1841438955.connect(
      Blink2Leds_1440914329.getPort("timer2").get,
      SoftTimer_513595611.getPort("timer").get
    )
    null_1841438955.connect(
      SoftTimer_513595611.getPort("timer").get,
      Blink2Leds_1440914329.getPort("timer2").get
    )

//Starting Things
    Arduino_701179797.start
    LED_1262837020.start
    LED_948129019.start
    SoftTimer_647098590.start
    SoftTimer_513595611.start
    Blink2Leds_1440914329.start
    
  }

}