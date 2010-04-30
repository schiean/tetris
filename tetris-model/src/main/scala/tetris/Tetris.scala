package tetris


 /**
  * field after %100 (the /100 determines color %100 determines filled/empty)
  *
  *
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
   * 2 = collision
   */

// TODO does not support game over :)

object Tetris  {
    val rnd = new scala.util.Random(78945489)

    // define blocks (TODO should be in brickstate)
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

    // stateless methods for game manipulation
    def getRandomBlock(): List[List[Int]]={
      return blockTypes(rnd.nextInt(blockTypes.size));
    }   


  def initGame():GameState = {
    return GameStateFactory.createNew(registerNewBlock,Field.createEmptyField)
  }

  // get aview of the applied brick on the current field
  // but the field will not be changed ofcourse
  def getCurrentView(state:GameState):String = {
    return Field.printFieldToString(applyBlock(state.field,state.brickState))
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

  // checks for collisions (an empty cell means the value %100 == 0, when filled this is 1)
  // so when we apply another block on the same cell this would become 2 which
  // makes it easy to check for collisions
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

  // check if next turn is valid, we check whether the new brickstate and current
  // view create any collisions
  def isValidNewState(pField:Array[Array[Int]],newState:BrickState): Boolean ={
    val newCurrentField = applyBlock(pField,newState)
    return hasCollision(newCurrentField)
  }

  // if it doesn't create a collision we rotate the brick
  def rotate(state:GameState):GameState={
    val newState = BrickStateFactory.rotate(state.brickState)
    if(hasCollision(applyBlock(state.field,newState))){
        return state
    }else{
        return GameStateFactory.createNew(newState, state.field, state.score)
    }
  }

  // while a block is falling it is not part of the view
  // when it is stuck on some other block then we freeze it and
  // make it part of the view
  def freezeBlock(state:GameState):GameState={
    val newField = applyBlock(state.field,state.brickState)
    // if top row middle is filled gamer is game-over
    val newBrickState = registerNewBlock
    return GameStateFactory.createNew(newBrickState,newField, state.score)
  }

  // fixes the brick into the view
  def applyBlock( pView: Array[Array[Int]], curBrickState:BrickState):Array[Array[Int]]={
    var pViewCopy = Field.copyField(pView)
    val y = curBrickState.curentPosition._1
    val x = curBrickState.curentPosition._2
    for (i <- 0 to curBrickState.curentBrick.length -1 ){
      for (j <- 0 to curBrickState.curentBrick(i).length -1 ){
        val posX = i+x
        val posY = j+y
        if(posX < Field.columns && posY < Field.rows){
          pViewCopy(posY)(posX) = pViewCopy(posY)(posX)+curBrickState.curentBrick(i)(j)
        }
      }
    }
    return pViewCopy
  }

  // see if the line is a tetris(all columns filled)
  def checkLine(line:Array[Int]): Boolean = {
    val sum = line.foldLeft(0)(_%100+_%100)
    return sum==Field.columns;
  }

  // remove lines that used to be a tetris line
  def cleanUpLines(state:GameState): GameState = {
    var field = state.field
    for(i <- (1 to Field.rows -2).reverse){
      if(field(i)(2)==1000){
        for(j <- (2 to i).reverse){
          field = Field.replaceLine(field, field(j-1), j)
        }
      }
    }
    return GameStateFactory.createNew(state.brickState,field, state.score)
  }

  // creates an line with special values indicating it was a tetris line
  def getScoreLine():Array[Int]={
    var line = new Array[Int](Field.columns)
    line(0)=1
    line(Field.columns-1)=1
     for (i <- 1 to Field.columns -2 ){
       line(i)=1000
     }
     return line
  }

  // process one line filled with blocks
  def processTetris(nr:Int, state:GameState): GameState = {
    return GameStateFactory.createNew(state.brickState, Field.replaceLine(state.field, getScoreLine, nr), state.score)
  }

  // move along all lines and process a tetris(filled line) if exists
  def processTetrisses(state:GameState): GameState = {
    var newState = state
    for(row <- 1 to Field.rows-2){
      var field = state.field
      if(checkLine(field(row))){
         newState = processTetris(row,state)
         newState = GameStateFactory.score(newState)
      }
    }
    return newState
  }


}

