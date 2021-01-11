package views;

import data.State;
import javafx.scene.canvas.Canvas;

public class Board extends Canvas {

    private State state;
    private int size;

    public Board(State state){
        this.state=state;
        size=state.size;
    }



}
