package org.sintef.smac.samples.pingpong

import org.sintef.smac.Event

case object PingEvent extends Event {}
case object PongEvent extends Event {}
case object StopEvent extends Event {}
case object StartEvent extends Event {}

case object FastEvent extends Event {}
case object SlowEvent extends Event {}