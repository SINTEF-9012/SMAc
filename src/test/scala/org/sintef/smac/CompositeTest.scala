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
import org.sintef.smac.samples.pingpong.StartEvent
import org.sintef.smac.samples.pingpong.FastEvent
import org.sintef.smac.samples.pingpong.SlowEvent
import org.sintef.smac.samples.pingpong.PongEvent
import org.sintef.smac.samples.pingpong.composite._

import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit._

class CompositeTest extends JUnitSuite with ShouldMatchersForJUnit {
  
  var master : Orchestrator = _
  var sm :PingStateMachine = _
  
  @Before def initialize() {
    master = new Orchestrator
    master.start
    sm = new PingStateMachine(master, null, true, false)
    sm.startState
  }
  
  @Test def verify() {   
    master ! StartEvent
    Thread.sleep(500)
    master ! PongEvent
    Thread.sleep(500)
    
    //LEvent should only trigger one transition
    //i.e., we should be in L1 state, not in L2 state
    val slow : Slow = sm.substates.filter{s => s.isInstanceOf[Slow]}.head.asInstanceOf[Slow]
    
    println(sm.current)
    
    sm.current should equal (slow)
    slow.current should equal (slow.substates.filter{s => s.isInstanceOf[Ping]}.head)
    
    master ! FastEvent
    Thread.sleep(500)
    
    println(sm.current)
    
    val fast : Fast = sm.substates.filter{s => s.isInstanceOf[Fast]}.head.asInstanceOf[Fast]
    sm.current should equal (fast)
    fast.current should equal (fast.substates.filter{s => s.isInstanceOf[Stop]}.head)
    
    master ! SlowEvent
    Thread.sleep(500)
    sm.current should equal (slow)
    slow.current should equal (slow.substates.filter{s => s.isInstanceOf[Ping]}.head)
  }
}
