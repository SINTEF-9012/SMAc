package org.sintef.smac.samples.generated

import org.sintef.smac._

case object AckEvent extends Event {}					
case class SetLoginEvent(login : String) extends Event {}						
case class SetPasswordEvent(password : String) extends Event {}						
case class LoginEvent(login : String, password : String) extends Event {}						
case object AckLoginEvent extends Event {}					
case object AckPasswordEvent extends Event {}		