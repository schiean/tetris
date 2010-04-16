/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris

object BrickStateFactory{

  def createNew(ori:List[List[Int]]):BrickState = {
      return new BrickState(ori,ori,new Tuple2(0,3),1)
  }


  def createNew(state:BrickState,ori:Int,newBrick:List[List[Int]]):BrickState = {
    return new BrickState(newBrick,state.originalCurentBrick,state.curentPosition,ori)
  }

  // move these to methods on a brick?
  def move(state:BrickState,pos:Tuple2[Int,Int]):BrickState = {
    return new BrickState(state.curentBrick,state.originalCurentBrick,pos,state.curentOrientation)
  }


  def rotate(state:BrickState):BrickState = {
    val originalCurBrick = state.originalCurentBrick;
    if (state.curentOrientation==1){
      //TODO return BrickStateFactory.createNew(state, 2, originalCurBrick.transpose)
      return BrickStateFactory.createNew(state, 4, originalCurBrick) // temp
    }else if (state.curentOrientation==2){
      if(originalCurBrick.size!=1){
          val newBrick = List(originalCurBrick(1),originalCurBrick(0))
          return BrickStateFactory.createNew(state, 3, newBrick)
      }else{
          return BrickStateFactory.createNew(state, 3, originalCurBrick)
      }
    }else if (state.curentOrientation==3){
        if(originalCurBrick.size!=1){        
          // TODO val newBrick = List(originalCurBrick(1),originalCurBrick(0)).transpose
          //return BrickStateFactory.createNew(state, 4, newBrick)
          return BrickStateFactory.createNew(state, 4, originalCurBrick) // temp
        }else{
          return BrickStateFactory.createNew(state, 4, originalCurBrick)
        }
      }else if (state.curentOrientation==4){
        return BrickStateFactory.createNew(state, 1, originalCurBrick)
      }else{
        println("HELP")
        return BrickStateFactory.createNew(state, 1, originalCurBrick)
      }
  }
}


class BrickState(current:List[List[Int]],original:List[List[Int]],pos:Tuple2[Int,Int],ori:Int) {
  
    val curentBrick = current;
    val originalCurentBrick = original ;
    val curentPosition = pos;
    val curentOrientation = ori;
  
}
