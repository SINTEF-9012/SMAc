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
import scala.actors.Actor._

import scala.actors.Future
import scala.actors.Futures._

import scala.collection.mutable.{Map, HashMap, SynchronizedMap}

sealed class SignedEvent(override val name : String = "SignedEvent", val sender : Component, val port : Port, val event : Event, val to : Option[Component] = None) extends Event(name) {
  override def toString() = "Signed Event [ " + event.name + "] via [" + port.name + "]"
}

abstract class Event(val name : String) extends java.io.Serializable {}

class FakeComponent extends Component {}

abstract class ReactiveComponent extends Component {
  def onIncomingMessage(e : SignedEvent)
  override protected[smac] def incomingMessage(e : SignedEvent) : Boolean = {
    //super.incomingMessage(e)
    onIncomingMessage(e)
    return true
  }
}

abstract trait Component {
  
  /*lazy val getActor = actor {
    loop {
      react {
        case e: SignedEvent =>
          incomingMessage(e)
      }
    }
  }*/
  
  var ports : Map[String, Port] = Map()
  var behavior : List[StateMachine] = List()
  var lastEvents : Map[String, Map[String, Event]] = new HashMap[String, Map[String, Event]]() with SynchronizedMap[String, Map[String, Event]]
  
  def start {
    ports.values.par.foreach{p => p.start}
    behavior.par.foreach{b => 
      b.start
    }
  }
    
  protected[smac] def incomingMessage(e : SignedEvent) : Boolean = {
    synchronized {
      lastEvents.get(e.port.name) match {
        case Some(map) => 
          map += (e.event.name -> e.event)
        case None => 
          val map = Map[String, Event]() 
          map += (e.event.name -> e.event)
          lastEvents += (e.port.name -> map)
      } 
    }
    
    
    var results = List[Future[Boolean]]()
    behavior.par.foreach{sm => 
      println("dispatching to " + sm)
      val f : Future[Boolean] = future{sm.dispatchEvent(new SignedEvent(sender = e.sender, port = e.port, event = e.event, to = e.to))}
      results = results ::: List(f)
    }

    val status = !results.filter{f => f()}.isEmpty
    return status
  }
  
  final def getPort(name : String) : Option[Port] = {
    ports.get(name)
  }
  
  final def getEvent(p : String, e : String) : Option[Event] = {
    synchronized {
      lastEvents.get(p).flatMap(_.get(e))
    }
  }
}

sealed class Port(val name : String, val receive : List[String], val send : List[String], val cpt : Component) extends Actor {
  cpt.ports += (name -> this)
  
  protected[smac] var out : List[Channel] = List()
    
  def in(e: SignedEvent) {
      if (canReceive(e)) {
        cpt.incomingMessage(new SignedEvent(sender = e.sender, port = this, event = e.event, to = e.to))
      }  
  }
  
  override def act() = {
    loop {
      react {
        case e: SignedEvent =>
          //println("Port " + this + " receiving " + e)
          in(e)
      }
    }
  }
  
  def send(e : Event) {
    //println("Port " + this + " trying to send " + e)
    if (canSend(e)) {
      //println("Port " + this + " sending to channels")
      out.par.foreach{c =>
        //println("Port " + this + " sending to channel "+c)
        c.dispatch(new SignedEvent(sender = cpt, port = this, event = e), this)
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

sealed class Channel(p1 : Port, p2 : Port) {

  connect(p1, p2)

  def connect(p: Port, p2: Port) = {
    this.p1.out ::= this
    this.p2.out ::= this
  }
  
  def disconnect() {
    p1.out - this
    p2.out - this
  }
  
  def dispatch(e: Event, p : Port) {
    if (p == p1)
      p2 ! e
    else if (p == p2)
      p1 ! e
  }
  
}
