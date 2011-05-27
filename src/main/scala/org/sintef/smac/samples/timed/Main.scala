/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sintef.smac.samples.timed

import org.sintef.smac._

object Main{
  def main(args: Array[String]): Unit = {   
    println("START")
    val master = new Orchestrator
    master.start
    
    val sm = new HelloWorldStateMachine(master, null, false)

    sm.startState
  }
}