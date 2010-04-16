package tetris


 /**
   *     0 1 2 3 4 5  col
   *
   * 0   0 0 0 0 0 0
   * 1   0 0 0 0 0 0
   * 2   0 0 0 0 0 0
   * 3   0 0 0 0 0 0
   * row
   *
   * 0 = empty
   * 1 = filled
   *
   */

//
// TODO check for game_over
// sommige kleuren kloppen niet, rotate klopt niet helemaal

object Tetris  {
    val rnd = new scala.util.Random(78945489)
    val columns = 10
    val rows =  20

    val i = List(List(101,101,101,101))
    val j = List(List(201,201,201),
                 List(0  ,  0,201))
    val l = List(List(301,301,301),
                 List(301,  0,  0))
    val o = List(List(401,401),
                 List(401,401))
    val s = List(List(0  ,401,401),
                 List(401,401,  0))
    val t = List(List(501,501,501),
                 List(  0,501,  0))
    val z = List(List(601,601,  0),
                 List(  0,601,601))

    val blockTypes = List(i,j,l,o,s,t,z)

    def getRandomBlock(): List[List[Int]]={
      return blockTypes(rnd.nextInt(blockTypes.size));
    }   

    def createField(): Array[Array[Int]] = {
      return new Array[Array[Int]](rows, columns)
    }

  def initGame():GameState = {
    return GameStateFactory.createNew(registerNewBlock,createEmptyField)
  }

  def main(args: Array[String]): Unit = {
    val x = 1;
    var state = initGame()
    for(i <- 0 to 50){
      println(i)
      state = rotate(state)
      state = GameStateFactory.score(state)
      println("score:"+state.score)
      state = moveBlock(state,x)
      printField(applyBlock(state.field,state.brickState) )     
      Thread.sleep(1000)
    }
  }

  def getCurrentView(state:GameState):String = {
    return printFieldToString(applyBlock(state.field,state.brickState))
  }

  def registerNewBlock():BrickState={
    return BrickStateFactory.createNew(getRandomBlock)
  }

  def moveBlock (state:GameState,div_x:Int): GameState = {
    val state1 = moveBlock(state , 0     ,1)  // move down
    val state2 = moveBlock(state1, div_x ,0)  // move horizontal
    if(state2.brickState.curentPosition._1==0){ // horizontal move was illegal
      return state1;
    }else{
      return state2;
    }
  }

  def moveBlock (state:GameState,div_x:Int, div_y:Int):GameState = {
    val oldPos = state.brickState.curentPosition
    val y = state.brickState.curentPosition._1 + div_y
    val x = state.brickState.curentPosition._2 + div_x
    val newBrickState  = BrickStateFactory.move(state.brickState, Tuple2(y,x))
    if( isValidNewState(state.field, newBrickState) ){
      return freezeBlock(state)
    }else{
      return GameStateFactory.createNew(newBrickState, state.field, state.score)
    }    
  }


  def hasCollision(pField:Array[Array[Int]]): Boolean={
    for( row <- pField ){
      for( cell <- row ){
        if( cell % 100 > 1 ){
          return true;
        }
      }
    }
    return false;
  }

  def isValidNewState(pField:Array[Array[Int]],newState:BrickState): Boolean ={
    val newCurrentField = applyBlock(pField,newState)
    return hasCollision(newCurrentField)
  }

  def rotate(state:GameState):GameState={
    val newState = BrickStateFactory.rotate(state.brickState)
    if(hasCollision(applyBlock(state.field,newState))){
        return state
    }else{
        return GameStateFactory.createNew(newState, state.field, state.score)
    }
  }

  def freezeBlock(state:GameState):GameState={
    val newField = applyBlock(state.field,state.brickState)
    // if top row middle is filled gamer is game-over
    val newBrickState = registerNewBlock
    return GameStateFactory.createNew(newBrickState,newField, state.score)
  }

  def applyBlock( pView: Array[Array[Int]], curBrickState:BrickState):Array[Array[Int]]={
    var pViewCopy = copyField(pView)
    val y = curBrickState.curentPosition._1
    val x = curBrickState.curentPosition._2
    for (i <- 0 to curBrickState.curentBrick.length -1 ){
      for (j <- 0 to curBrickState.curentBrick(i).length -1 ){
        val posX = i+x
        val posY = j+y
        if(posX<columns && posY<rows){
          pViewCopy(posY)(posX) = pViewCopy(posY)(posX)+curBrickState.curentBrick(i)(j)
        }
      }
    }
    return pViewCopy
  }

  def copyField( pField: Array[Array[Int]]):Array[Array[Int]]={
    var newField = createField
    copyIntoField(newField, pField)
    return newField
  }

  def replaceLine(oldField:Array[Array[Int]], line:Array[Int], nr:Int): Array[Array[Int]] ={
    val newField = copyField(oldField)
    for(i <- 0 to columns-1){
      newField(nr)(i) = line(i)
    }
    return newField
  }

  def copyIntoField(newField: Array[Array[Int]],oldField: Array[Array[Int]]):Unit={
   for (i <- 0 to columns -1 ){
      for (j <- 0 to rows -1 ){
        newField(j)(i) = oldField(j)(i)
      }
    }
  }
 
  def createEmptyField():Array[Array[Int]] = {
    var field = createField
    // we want a border with 1's and for the rest 0's
    for (i <- 0 to columns -1 )
      for (j <- 0 to rows -1 )
        field(j)(i) = 1
    for (i <- 1 to columns -2 )
      for (j <- 0 to rows -2 )
        field(j)(i) = 0
    return field
  }

  def printField( pField: Array[Array[Int]]):Unit = {
    for(row <- pField){
      for(spot <- row){
        if(spot>0)
          print('#')
        else{
          print(' ')
        }
        print(" ")
      }
      println()
    }
    println()
  }

  def checkLine(line:Array[Int]): Boolean = {
    val sum = line.foldLeft(0)(_%100+_%100)
    return sum==columns;
  }

  def cleanUpLines(state:GameState): GameState = {
    var field = state.field
    for(i <- (1 to rows -2).reverse){
      if(field(i)(2)==1000){
        for(j <- (2 to i).reverse){
          field = replaceLine(field, field(j-1), j)
        }
      }
    }
    return GameStateFactory.createNew(state.brickState,field, state.score)
  }

  def getScoreLine():Array[Int]={
    var line = new Array[Int](columns)
    line(0)=1
    line(columns-1)=1
     for (i <- 1 to columns -2 ){
       line(i)=1000
     }
     return line
  }
  
  def processTetris(nr:Int, state:GameState): GameState = {
    return GameStateFactory.createNew(state.brickState, replaceLine(state.field, getScoreLine, nr), state.score)
  }
  def processTetrisses(state:GameState): GameState = {
    var newState = state
    for(row <- 1 to rows-2){
      var field = state.field
      if(checkLine(field(row))){
         newState = processTetris(row,state)
         newState = GameStateFactory.score(newState)
      }
    }
    return newState
  }

  def printFieldToString( pField: Array[Array[Int]]):String = {
    var fieldStr="";
    for(row <- pField){
      for(spot <- row){
        if(spot==1)
          fieldStr +="#"
        else if(spot==1000)
          fieldStr +="X"
        else if(spot/100==1)
          fieldStr +="a"
        else if(spot/100==2)
          fieldStr +="b"
        else if(spot/100==3)
          fieldStr +="c"
        else if(spot/100==4)
          fieldStr +="d"
        else if(spot/100==5)
          fieldStr +="e"
        else if(spot/100==6)
          fieldStr +="f"
        else
          fieldStr +=" "        
      }
    }
    return fieldStr;
  }

}

