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

import org.sintef.smac._

// Initialize instance variables and states
object Main {

  def main(args: Array[String]): Unit = {
//Connectors
    val null_242322373 = new Channel
    null_242322373.start
    val null_250464093 = new Channel
    null_250464093.start
    val null_1929631376 = new Channel
    null_1929631376.start
    val null_1115297295 = new Channel
    null_1115297295.start
//Things
    val TestRegion_366109464 = new TestRegion()
    val Arduino_1911727745 = new Arduino()
    val LED_234554472 = new LED()
    val ArduinoHarness_891984761 = new ArduinoHarness()
//Bindings
    null_242322373.connect(
      TestRegion_366109464.getPort("harness").get,
      ArduinoHarness_891984761.getPort("test").get
    )
    null_242322373.connect(
      ArduinoHarness_891984761.getPort("test").get,
      TestRegion_366109464.getPort("harness").get
    )

    null_250464093.connect(
      LED_234554472.getPort("DigitalIO").get,
      Arduino_1911727745.getPort("DigitalIO").get
    )
    null_250464093.connect(
      Arduino_1911727745.getPort("DigitalIO").get,
      LED_234554472.getPort("DigitalIO").get
    )

    null_1929631376.connect(
      ArduinoHarness_891984761.getPort("polling").get,
      Arduino_1911727745.getPort("Polling").get
    )
    null_1929631376.connect(
      Arduino_1911727745.getPort("Polling").get,
      ArduinoHarness_891984761.getPort("polling").get
    )

    null_1115297295.connect(
      ArduinoHarness_891984761.getPort("led").get,
      LED_234554472.getPort("LED").get
    )
    null_1115297295.connect(
      LED_234554472.getPort("LED").get,
      ArduinoHarness_891984761.getPort("led").get
    )

//Starting Things
    TestRegion_366109464.start
    Arduino_1911727745.start
    LED_234554472.start
    ArduinoHarness_891984761.start
  }

}
