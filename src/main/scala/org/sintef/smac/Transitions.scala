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

/**
 * Transitions between two states
 * Transition are interested in event (String corresponding to Event.getName)
 * coming from specified ports
 */
//Port -> Event
abstract sealed class Handler(val source : State, val events : List[Pair[String, String]]) {
  
  final def getPort(name : String) : Option[Port] = {
    //TODO: check if handler can access the port
    source.root.getPort(name)
  }
  
  protected[smac] def isInterestedIn(p : String, e : String) : Boolean = {
    events.exists(k => k._1 == p && k._2 == e)
  }
  
  protected[smac] def isInterestedIn(e : SignedEvent) : Boolean = {
    isInterestedIn(e.port.name, e.event.name)
  }
  
  protected[smac] def isAuto = events.length == 0
  
  protected[smac] def getEvent(e : String, p : String) : Option[Event] = {
    source.root.getEvent(p, e)    
  }
  
  protected[smac] def getAction: HandlerAction
      
  /**
   * Describe the overall execution of the transition
   */
  protected[smac] def execute
}

sealed class Transition(override val source: State, val target: State, val action: TransitionAction, override val events : List[Pair[String, String]] = List()) extends Handler(source, events) {

  override protected[smac] def getAction = action
  
  action.handler = this

  def execute() = {
    action.executeBeforeActions
    source.executeOnExit
    action.executeActions()
    target.executeOnEntry
    action.executeAfterActions
  }
}

sealed class InternalTransition(override val source: State, val action: InternalTransitionAction, override val events : List[Pair[String, String]] = List()) extends Handler(source, events) {
  
  source.addInternalTransition(this)
  
  override protected[smac] def getAction = action
  
  action.handler = this
  
  def execute() = {
    action.executeActions()
  }
}