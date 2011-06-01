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
import org.sintef.smac.samples.helloworld._

import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit._

class SimpleTest extends JUnitSuite with ShouldMatchersForJUnit {
  
  var master : Orchestrator = _
  var sm :HelloWorldStateMachine = _
  
  @Before def initialize() {
    master = new Orchestrator
    master.start
    sm = new HelloWorldStateMachine(master, Option(null), false, false)
    sm.startState
  }
  
  @Test def verify() {   
    master ! HEvent
    Thread.sleep(100)
    master ! EEvent
    Thread.sleep(100)
    master ! LEvent
    Thread.sleep(100)
    
    //LEvent should only trigger one transition
    //i.e., we should be in L1 state, not in L2 state
    sm.current should equal (sm.substates.filter{s => s.isInstanceOf[L1]}.head)
    
    master ! LEvent
    Thread.sleep(100)
    master ! OEvent
    Thread.sleep(100)
    
    //OEvent should trigger on transition from L to O
    //since stop transition has no associated event, it should also be triggered
    //so that we should end in the STOP state
    sm.current should equal (sm.substates.filter{s => s.isInstanceOf[STOP]}.head)
    
  }
}