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
 * These classes should be extended to define the actions of the state
 * and the transitions of a given state machine
 */
class EmptyHandlerAction extends HandlerAction{
  final override def executeActions {}
}

class EmptyStateAction extends StateAction{
  final override def onEntry {}
  final override def onExit {}
}

abstract sealed class HandlerAction {
  
  protected[smac] var handler : Handler = _
  
  final def getEvent(e : String, p : String) : Option[Event] = handler.getEvent(e, p)
  
  def checkGuard: Boolean = true
  def getScore: Double = 1
  
  def executeActions()
   
}

abstract class TransitionAction extends HandlerAction {
  def executeBeforeActions(){}
  def executeAfterActions(){}
}

abstract class InternalTransitionAction extends HandlerAction {
  
}

abstract class StateAction {
  
  protected[smac] var handler : State = _
  
  def getBehavior = handler
    
  def onEntry
  def onExit
}