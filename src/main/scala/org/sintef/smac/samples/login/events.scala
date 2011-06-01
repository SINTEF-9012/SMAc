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
package org.sintef.smac.samples.login

import org.sintef.smac._

case class LoginEvent(login : String, password : String) extends Event {}
case class AckEvent(key : String) extends Event {}						
case class SetLoginEvent(login : String) extends Event {}						
case class SetPasswordEvent(password : String) extends Event {}					
case class AckLoginEvent(key : String) extends Event {}						
case class AckPasswordEvent(key : String) extends Event {}						
