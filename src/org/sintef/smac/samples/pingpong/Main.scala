/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sintef.smac.samples.pingpong

import org.sintef.smac._
import org.sintef.smac.samples.pingpong._

object Main {

  /**
   * This main allows executing the ping pong in two modes:
   * 1/ Ping state machine + GUI: You can manually send event to the Ping state
   *    machine. For example, start, some pong (it should reply some ping), 
   *    stop, some pong again (it should do nothing), start again, etc.
   * 2/ Ping state machine + GUI + Pong state machine: This time, pong events are
   *    sent by the pong state machine. Just send the start event and wait until
   *    the 10 000 ping pong exchanges are done... or send the stop event at any
   *    moment. To enable this mode, just uncomment the 3 commented lines.
   */
  def main(args: Array[String]): Unit = {   
    println("START")
    val master = new Orchestrator
    master.start
    
    val pingSM = new PingStateMachine(master, null, false)
    println(pingSM.initial)
    
    //val pongSM = new PongStateMachine(master, false)
    
    pingSM.startState
    //pongSM.startState
  }
}
