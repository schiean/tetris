package bridge;


import tetris.Tetris;
import tetris.BrickState;
import tetris.BrickStateFactory;
import tetris.GameState;

public class Bridge {
    private GameState state;

    public Bridge(){
        state = Tetris.initGame();
    }
    
    public String getScore(){
        return "Score:"+String.valueOf(state.score());
    }

    public String getState(String key){
        state = Tetris.cleanUpLines(state);
        if( key.equals("S") ){
              state = Tetris.rotate(state);
        }
        if(key.equals("D")){
            state = Tetris.moveBlock(state,0,1);
        }
        Integer x = 0;
        if( key.equals("L")){
            x = -1;
        }
        if( key.equals("R")){
            x = 1;
        }
        state = Tetris.moveBlock(state,x);

        state = Tetris.processTetrisses(state);
        return Tetris.getCurrentView(state);
    }
}
