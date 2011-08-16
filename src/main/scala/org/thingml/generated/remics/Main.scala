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
package org.thingml.generated.remics

import org.sintef.smac._

// Initialize instance variables and states
object Main {

  def main(args: Array[String]): Unit = {
//Connectors
    val null_1418513383 = new Channel
    null_1418513383.start
    val null_629428158 = new Channel
    null_629428158.start
//Things
    val Client_645749530 = new Client()
    val Server_1869790762 = new Server()
    val Mediator_266299511 = new Mediator()
//Bindings
    null_1418513383.connect(
      Client_645749530.getPort("LoginServer").get,
      Mediator_266299511.getPort("Client").get
    )
    null_1418513383.connect(
      Mediator_266299511.getPort("Client").get,
      Client_645749530.getPort("LoginServer").get
    )

    null_629428158.connect(
      Server_1869790762.getPort("LoginClient").get,
      Mediator_266299511.getPort("Server").get
    )
    null_629428158.connect(
      Mediator_266299511.getPort("Server").get,
      Server_1869790762.getPort("LoginClient").get
    )

//Starting Things
    Client_645749530.start
    Server_1869790762.start
    Mediator_266299511.start
  }

}