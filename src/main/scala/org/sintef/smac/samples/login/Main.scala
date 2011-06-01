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
package org.sintef.smac.samples.login

import org.sintef.smac._

object Main{
  def main(args: Array[String]): Unit = {   
    println("START")
    val master = new Orchestrator
    master.start
    
    
    /*
     val client = new ClientComponent(master, false, false)
     val service1 = new Service1Component(master, false, false)
     service1.behavior.startState
     client.behavior.startState
     */
    
    
    //client now communicate with service2, via the mediator
    val client = new ClientComponent(master, false, false)
    val service2 = new Service2Component(master, false, false)
    val mediator = new MediatorComponent(master, false, false)
    
    service2.behavior.startState
    mediator.behavior.startState
    client.behavior.startState
    
  }
}