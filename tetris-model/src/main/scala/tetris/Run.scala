/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris

object Run {
  // alternative game engine (usefull for debugging without java/javafx)
  def main(args: Array[String]): Unit = {
    val x = 0;
    var state = Tetris.initGame()
    for(i <- 0 to 50){
      println(i)
      state = Tetris.rotate(state)
      state = GameStateFactory.score(state)
      println("score:"+state.score)
      state = Tetris.moveBlock(state,x)
      Field.printField(Tetris.applyBlock(state.field,state.brickState) )
      Thread.sleep(1000)
    }
  }
}
