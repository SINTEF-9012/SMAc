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

import scala.collection.mutable.{Map, HashMap, SynchronizedMap}

sealed class SignedEvent(override val name : String = "SignedEvent", val sender : Component, val port : Port, val event : Event, val to : Option[Component] = None) extends Event(name)

abstract class Event(val name : String){}

//This should be sub-classed to provide proper serialization of events
class RemoteEventManager {
  def fromBytes(bytes : Array[Byte]) : Option[Event] = None
  def toBytes(event : Event) : Option[Array[Byte]] = None
}

class FakeComponent extends Component {}

abstract class ReactiveComponent extends Component {
  def onIncomingMessage(e : SignedEvent)
  override protected[smac] def incomingMessage(e : SignedEvent) {
    //super.incomingMessage(e)
    onIncomingMessage(e)
  }
}

abstract class Component {
  var ports : Map[String, Port] = Map()
  var behavior : List[StateMachine] = List()
  
  def start {
    ports.values.par.foreach{p => p.start}
    behavior.par.foreach{b => 
      b.start
    }
  }
    
  protected[smac] def incomingMessage(e : SignedEvent) {
    behavior.par.foreach{sm => 
      //println("  "+sm)
      sm.getActor ! e
      //sm.dispatchEvent(new SignedEvent(e.sender, this, e.event, e.to))
    }    
  }
  
  final def getPort(name : String) : Option[Port] = {
    ports.get(name)
  }
}

sealed class Port(val name : String, val receive : List[String], val send : List[String], val cpt : Component) extends Actor {
  
  var lastEvents : Map[String, Event] = new HashMap[String, Event]() with SynchronizedMap[String, Event]
  
  cpt.ports += (name -> this)
  
  protected[smac] var out : List[Channel] = List()
  
  def getEvent(e : String) : Option[Event] = {
    synchronized {
      return lastEvents.get(e)
    }
  }
  
  def in(e: SignedEvent) {
    if (canReceive(e)) {
      synchronized {
        lastEvents += (e.event.name -> e.event)
      }
      cpt.incomingMessage(new SignedEvent(sender = e.sender, port = this, event = e.event, to = e.to))
    }
  
  }
  
  override def act() = {
    loop {
      react {
        case e: SignedEvent =>
          actor{in(e)}
      }
    }
  }
  
  def send(e : Event) {
    if (canSend(e)) {
      //println("Port " + this + " sending to channels")
      out.par.foreach{c =>
        //println("Port " + this + " sending to channel "+c)
        c ! new SignedEvent(sender = cpt, port = this, event = e)
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
  
  def dispatch(e: Event) = 
    e match {
      case s : SignedEvent =>
        s.to match {
          case Some(to) => out.filter{p => p.cpt == to}.par.foreach { p => p ! s }
          case None => out.par.foreach { p => p ! s }
        }
      case ev : Event => out.par.foreach { p => p ! ev }
    }

  override def act() = {
    loop {
      react {
        case e: Event => actor{dispatch(e)}
      }
    }
  }
  
}

sealed abstract class RemoteChannel(remoteManager : RemoteEventManager = new RemoteEventManager) extends Channel {

  def remoteDispatch(e : Event)
  
  override def act() = {
    loop {
      react {
        case e: Event =>
          actor{remoteDispatch(e)}
        case b: Array[Byte] =>
          actor{
            remoteManager.fromBytes(b) match {
              case Some(e) =>
                dispatch(e)
              case None =>
            }
          }
      }
    }
  }
  
}

//TODO: create an actor that manages the serial port. It should send incoming arrays of bytes to this channel and 
//sends the arrays it receives from this channel
sealed class RxTxChannel(remoteManager : RemoteEventManager = new RemoteEventManager) extends RemoteChannel {
  override def remoteDispatch(e : Event){} 
}