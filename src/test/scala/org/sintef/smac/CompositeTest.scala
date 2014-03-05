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
import org.sintef.smac.samples.pingpong.StopEvent
import org.sintef.smac.samples.pingpong.FastEvent
import org.sintef.smac.samples.pingpong.SlowEvent
import org.sintef.smac.samples.pingpong.PongEvent
import org.sintef.smac.samples.pingpong.composite._

import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit._

class CompositeTest extends JUnitSuite with ShouldMatchersForJUnit {
  
  var channel : Channel = _
  var sm :PingComponent = _
  
  var testComponent : FakeComponent = _
    
  @Before def initialize() {
    testComponent = new FakeComponent()
    val p = new Port("ping", List(PongEvent.getName, StartEvent.getName, StopEvent.getName, FastEvent.getName, SlowEvent.getName), List(PongEvent.getName, StartEvent.getName, StopEvent.getName, FastEvent.getName, SlowEvent.getName), testComponent)
    sm = new PingComponent(true, false)
    channel = new Channel(p, sm.getPort("ping").get)
    sm.start
    testComponent.start
  }
  
  @Test def verify() {   
    println("1/ master ! StartEvent")
    testComponent.getPort("ping").get.send(new StartEvent())
    Thread.sleep(500)
    println("2/ master ! PongEvent")
    testComponent.getPort("ping").get.send(new PongEvent())
    Thread.sleep(500)
    
    val slow : Slow = sm.behavior.head.substates.filter{s => s.action.isInstanceOf[Slow]}.head.action.asInstanceOf[Slow]
    
     println(sm.behavior.head.current)

    sm.behavior.head.current.action should equal (slow)
     slow.getComposite.current should equal (slow.getComposite.substates.filter{s : State=> s.action.isInstanceOf[Ping]}.head)
    
    println("3/ master ! FastEvent")
    testComponent.getPort("ping").get.send(new FastEvent())
    Thread.sleep(500)
    
    println(sm.behavior.head.current)
    
     val fast : Fast = sm.behavior.head.substates.filter{s => s.action.isInstanceOf[Fast]}.head.action.asInstanceOf[Fast]
     sm.behavior.head.current.action should equal (fast)
     fast.getComposite.current should equal (fast.getComposite.substates.filter{s : State => s.action.isInstanceOf[Stop]}.head)
    
    println("4/ master ! SlowEvent")
    testComponent.getPort("ping").get.send(new SlowEvent())
    Thread.sleep(500)

    sm.behavior.head.current.action should equal (slow)
     slow.getComposite.current should equal (slow.getComposite.substates.filter{s => s.action.isInstanceOf[Ping]}.head)
  }
}
