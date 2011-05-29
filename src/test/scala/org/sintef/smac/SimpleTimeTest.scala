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
package org.sintef.smac.test

import org.sintef.smac._
import org.sintef.smac.samples.timed._

import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit._

class SimpleTimeTest extends JUnitSuite with ShouldMatchersForJUnit {
  
  var master : Orchestrator = _
  var sm :HelloWorldStateMachine = _
  
  @Before def initialize() {
    master = new Orchestrator
    master.start
    
    sm = new HelloWorldStateMachine(master, null, false, false)

    sm.startState
  }
  
  @Test def verify() {   
    master ! LeftEvent
    Thread.sleep(100)
    //Should go to left state
    sm.current should equal (sm.substates.filter{s => s.isInstanceOf[Left]}.head)
    Thread.sleep(3000)
    //should automatically return to center state after a while
    sm.current should equal (sm.substates.filter{s => s.isInstanceOf[Center]}.head)
  }
}