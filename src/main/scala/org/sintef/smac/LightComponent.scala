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
 * @Authors: Brice Morin and Francois Fouquet
 * @Copyright: SINTEF IKT, Oslo, Norway
 * @Contact: <brice.morin@sintef.no>
 */
package org.sintef.smac

import scala.actors.Actor

sealed protected[smac] case class SignedEvent(val sender : Component, val port : Port, val event : Event, val to : Option[Component] = None)

abstract class Event(val name : String){}

class FakeComponent extends Component {
  
}

abstract class Component {
  var ports : Map[String, Port] = Map()
  var behavior : List[StateMachine] = List()
  
  def start {
    ports.values.foreach{p => p.start}
    behavior.foreach{b => b.start}
  }
  
  final def getPort(name : String) : Option[Port] = {
    ports.get(name)
  }
}

sealed class Port(val name : String, val receive : List[String], val send : List[String], val cpt : Component) extends Actor {
  
  cpt.ports += (name -> this)
  
  protected[smac] var out : List[Channel] = List()
  
  override def act() = {
    loop {
      react {
        case e: SignedEvent =>
          if (canReceive(e))
            //println("Port " + this + " dispatches to state machine")
          cpt.behavior.foreach{sm => 
            //println("  "+sm)
            sm.getActor ! new SignedEvent(e.sender, this, e.event, e.to)
            //sm.dispatchEvent(new SignedEvent(e.sender, this, e.event, e.to))
          }
      }
    }
  }
  
  def send(e : Event) {
    if (canSend(e)) {
      //println("Port " + this + " sending to channels")
      out.foreach{c =>
        //println("Port " + this + " sending to channel "+c)
        c ! new SignedEvent(cpt, this, e)
      }
    }
  }
  
  protected[smac] def canSend(e : Event) = {
    send.exists(p => p.equals(e.name))
  }
  
  protected[smac] def canReceive(e : SignedEvent) = {
    receive.exists(p => p.equals(e.event.name))
  }
}

sealed class Channel() extends Actor {

  override def start : Actor = {
    super.start
    return this
  }
  
  protected var out = List[Port]()
  
  def connectIn(p : Port){
    p.out ::= this
  }
  
  def connectOut(p : Port){
    out ::= p
  }

  def connect(p: Port, p2: Port) = {
    p.out ::= this
    out ::= p2
  }
  
  def disconnect(p : Port) {
    p.out - this
    out - p
  }

  override def act() = {
    loop {
      react {
        case e: SignedEvent =>
          e.to match {
            case Some(to) => out.filter{p => p.cpt == to}.foreach {
                p =>
                ////println("Channel dispatching " + e + " to " + p)
                p forward e
              }
            case None => out.foreach {
                p =>
                ////println("Channel dispatching " + e + " to " + p)
                p forward e
              }
          }
      }
    }
  }
}

/**
 * Just a naive PoC
 * TODO: a lot
 */
sealed class SessionChannel extends Channel {
  
}