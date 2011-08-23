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

sealed class State(action : StateAction, val root : Component) {

  final def getPort(name : String) : Option[Port] = {
    root.getPort(name) 
  }
  
  def getEvent(e : String, p : Port) : Option[Event] = {
    parent match {
      case Some(parent) => parent.getEvent(e, p)
      case None => None
    } 
  }
  
  protected[smac] def getRoot = root
  
  action.handler = this
  
  protected[smac] var parent: Option[CompositeState] = None
  
  protected[smac] var internal: List[InternalTransition] = List()
  
  final def addInternalTransition(t : InternalTransition) {
    internal ++= List(t)
  }
  
  protected[smac] def allTransitions(): List[Handler] = {
    var result : List[Handler] = List()
    result = result ::: internal
    ////println(result.size)
    parent match {
      case Some(p) =>
        p match {
          case c : CompositeState =>
            result = result ::: c.transitions.filter(t => t.source == this)
          case _ =>
        }
        
        ////println(result.size)
      case None =>
        result
    }
    ////println(result.size)
    return result
  }
    

  //TODO: avoid (almost) duplicating checkForTransition...  
  protected[smac] def checkForAutoTransition: Option[Handler] = {
    allTransitions.filter(t => { t.isAuto && t.getAction.checkGuard})
    .sortWith((t, r) => (t.isInstanceOf[InternalTransition] && r.isInstanceOf[Transition]) || (t.getAction.getScore > r.getAction.getScore))
    .headOption match {
      case Some(in) => 
        ////println("  A transition can be triggered: "+in)
        return Option(in)
      case None => 
        return None
    }
  }
  
  protected[smac] def checkForTransition(e : SignedEvent): Option[Handler] = {
    ////println(this+".checkForTransition: ")  
    allTransitions.filter(t => { t.isInterestedIn(e) && t.getAction.checkGuard})
    .sortWith((t, r) => (t.isInstanceOf[InternalTransition] && r.isInstanceOf[Transition]) || (t.getAction.getScore > r.getAction.getScore))
    .headOption match {
      case Some(in) => 
        ////println("  A transition can be triggered: "+in)
        return Option(in)
      case None => 
        return None
    }
  }

  protected[smac] def executeOnEntry() {
    //println("State.executeOnEntry")
    parent match {
      case Some(p) => p.current = this
      case None =>
    } 
    action.onEntry
    checkForAutoTransition match {//checks if a transition with no event can be triggered
      case Some(t) => {t.execute}
      case None =>
    }
  }

  protected[smac] def executeOnExit() {
    action.onExit
  }
}

sealed trait Region {
  var keepsHistory = false
  
  protected[smac] var initial: State = _

  protected[smac] var current: State = _
  
  protected[smac] var substates: List[State] = List()
  
  def dispatchEvent(e: SignedEvent) : Boolean
  
  class Dispatcher extends Actor {
    override def act() = {
     react {
       case e: SignedEvent =>
         dispatchEvent(e)
     } 
    }
  }
  
  val actor = new Actor{
    override def act() = {
      loop {
        react {
          case e: SignedEvent =>
            new Dispatcher().start ! e
        }
      }
    }
  }

  
  def getActor = actor
  
  def setHistory(h : Boolean) {keepsHistory = h}
  
  def setInitial(i : State) {
    initial = i
    current = initial
  }

  def addSubState(sub : State) {
    substates ++= List(sub)
    this match {
      case c : CompositeState => sub.parent = Option(c)
      case _ =>
    }
    
  }
  
  def start { 
    actor.start
  }
  
}

sealed class StateMachine(action : StateAction, keepHistory: Boolean, root : Component) extends CompositeState(action, keepHistory, root) {
  
  override def getEvent(e : String, p : Port) : Option[Event] = {
    //println("getEvent("+e+", "+p.name+")")
    root.getPort(p.name) match {
      case Some(port) =>
        //println("  "+port)
        port.getEvent(e)
      case None => None
    }
  }
}

sealed class CompositeState(action : StateAction, keepHistory: Boolean, root : Component) extends State(action, root) with Region {
  
  protected[smac] var regions : List[Region] = List()
  
  protected[smac] var transitions: List[Transition] = List()

  
  override def start { 
    super.start
    executeOnEntry
  }
  
  def addRegion(r : Region) {
    regions ++= List(r)
  }
  
  def addTransition(t : Transition) {
    transitions ++= List(t)
  }  
  
  protected[smac] override def checkForTransition(e : SignedEvent): Option[Handler] = {
    current.checkForTransition(e) match {
      case Some(t) => 
        return Option(t)
      case None =>
        return super.checkForTransition(e)
    }
  }

  override def dispatchEvent(e: SignedEvent) : Boolean = {
    //println(this + ".dispatchEvent "+e)
    var status = false
    regions.foreach{r => //events are dispatched to regions with no condition
      //println("  to region "+r)
      r.getActor ! e
    }
    
    checkForTransition(e) match {
      case Some(t) => 
        //println("  "+this + ".Transition: " + t)
        t.execute
        status = true
      case None =>
        //println("  "+this + "No Transition")
        status = false
    }
    
    return status
  }

  override def executeOnEntry() {
    //println("Composite.executeOnEntry")
    super.executeOnEntry
    current.executeOnEntry
  }

  override def executeOnExit() {
    if (!keepHistory) {
      current = initial
    } 
    current.executeOnExit
    super.executeOnExit
  }
}