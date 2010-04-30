/*
 * functions for manipulating a gamestate object
 */

package tetris

import tetris._

object GameStateFactory{
  def createNew(br:BrickState,fld:Array[Array[Int]]):GameState = {
    return new GameState(br,fld,0)
  }
  
  def createNew(br:BrickState,fld:Array[Array[Int]],scr:Int):GameState = {
    return new GameState(br,fld,scr)
  }

  def score(gs:GameState):GameState={
    return new GameState(gs.brickState,gs.field, gs.score+10)
  }
}

class GameState (br:BrickState,fld:Array[Array[Int]],scr:Int) {
  val brickState = br
  val field = fld
  val score = scr
}
