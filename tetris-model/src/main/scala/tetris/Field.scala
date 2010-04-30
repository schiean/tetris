
package tetris

/**
 * some helper functions for manipulating arrays/fields
 * 
 * might wanna replace it with List of List someday
 */

object Field {
  val columns = 10
  val rows =  20


    def createField(): Array[Array[Int]] = {
      return new Array[Array[Int]](rows, columns)
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

  // init a tetris field (empty (0's), but with border(1's))
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

  // debug method to print the field on the console
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


  // TODO change into functional style
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
