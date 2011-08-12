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
  
  var channel : Channel = _
  var sm :PingStateMachine = _
  
  @Before def initialize() {
    channel = new Channel
    channel.start
    sm = new PingStateMachine(true, false)
    channel.connect(sm.getStateMachine.getPort("ping").get, sm.getStateMachine.getPort("ping").get)
  }
  
  @Test def verify() {   
    println("1/ master ! StartEvent")
    sm.getStateMachine.getPort("ping").get ! new StartEvent()
    Thread.sleep(500)
    println("2/ master ! PongEvent")
    sm.getStateMachine.getPort("ping").get ! new PongEvent()
    Thread.sleep(500)
    
    /*val slow : Slow = sm.substates.filter{s => s.isInstanceOf[Slow]}.head.asInstanceOf[Slow]
    
    println(sm.current)
    
    sm.current should equal (slow)
    slow.current should equal (slow.substates.filter{s => s.isInstanceOf[Ping]}.head)*/
    
    println("3/ master ! FastEvent")
    sm.getStateMachine.getPort("ping").get ! new FastEvent()
    Thread.sleep(500)
    
    /*println(sm.current)
    
    val fast : Fast = sm.substates.filter{s => s.isInstanceOf[Fast]}.head.asInstanceOf[Fast]
    sm.current should equal (fast)
    fast.current should equal (fast.substates.filter{s => s.isInstanceOf[Stop]}.head)*/
    
    println("4/ master ! SlowEvent")
    sm.getStateMachine.getPort("ping").get ! new SlowEvent()
    Thread.sleep(500)
    /*sm.current should equal (slow)
    slow.current should equal (slow.substates.filter{s => s.isInstanceOf[Ping]}.head)*/
  }
}
