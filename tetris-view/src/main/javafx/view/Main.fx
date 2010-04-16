/*
 * Main.fx
 *
 * Created on 2-apr-2010, 14:21:41
 */

package view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.ext.swing.SwingButton;
 
import bridge.Bridge;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.layout.Panel;
import javafx.scene.layout.HBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;


/**
 * @author schiean
 */

var cols = 10;
var rows = 20;

var br = new Bridge();
var colors:Color[]=[Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE] ;

function fillColors(key:String){
    var colstr = br.getState(key);
    for(i in [0..colstr.length()-1]){
        if(colstr.substring(i,i+1).equals("a")){
            colors[i] = Color.YELLOW;
        }else if(colstr.substring(i,i+1).equals("b")){
            colors[i] = Color.BLUE;
        }else if(colstr.substring(i,i+1).equals("c")){
            colors[i] = Color.GREEN;
        }else if(colstr.substring(i,i+1).equals("d")){
            colors[i] = Color.CORAL;
        }else if(colstr.substring(i,i+1).equals("d")){
            colors[i] = Color.BROWN;
        }else if(colstr.substring(i,i+1).equals("e")){
            colors[i] = Color.PURPLE;
        }else if(colstr.substring(i,i+1).equals("f")){
            colors[i] = Color.RED;
        }else if(colstr.substring(i,i+1).equals("#")){
            colors[i] = Color.BLACK;
        }else if(colstr.substring(i,i+1).equals("#")){
            colors[i] = Color.WHITE;
        }else{
            colors[i] = Color.LIGHTGRAY
        }
    }
}

var score = "Score";
bound function getColor(x:Integer ,y:Integer):Color{
    return colors[x+y*cols];
}


function getScore():String{
    score = br.getScore();
}


var timeline : Timeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
        KeyFrame {
            canSkip: true
            time: 1s
            action: function() {
                fillColors(" ");
                getScore();
            }
        }
    ]
};
timeline.play();

Stage {
      
    title: "tetris"
    scene: Scene {
        fill: LinearGradient {
            startX: 1.0, startY: 0.0, endX: 1.0, endY: 1.0
            proportional: true
            stops: [ Stop { offset: 0.0 color: Color.BLACK },
                     Stop { offset: 1.0 color: Color.RED } ]
        }
        width: 22*cols+30
        height: 24*rows+40
        content:

        [
            
            for(colnum in[0..cols-1]){
             for(rownum in [0..rows-1]){
                    Rectangle {
                            x: 15 + 22 * colnum
                            y: 40  + 22 * rownum
                            width: 20,
                            height: 20
                            fill: bind getColor(colnum,rownum)
                    }
                }
            }
            Text {
                font : Font {
                    size : 16
                }
                x: 40
                y: 40  + 22 * (rows+1)
                content: bind score
            }
            Panel {
               content: [            
                 HBox  {
                    content:[
                        SwingButton {
                            text: "left"
                            action: function() { fillColors("L");  }
                        }
                        SwingButton {
                            text: "down"
                            action: function() { fillColors("D");  }
                        }
                        SwingButton {
                            text: "rotate"
                            action: function() { fillColors("S");  }
                        }
                        SwingButton {
                            text: "right"
                            action: function() { fillColors("R");  }
                        }
                   ]
                 }
                 ]
            }

           
        ]
    }
}