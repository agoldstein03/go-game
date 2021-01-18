package data;

import java.util.ArrayList;

public class ComputerPlayer extends Player {

    private boolean black;

    public ComputerPlayer(boolean isBlack) {
        black = isBlack;
    }

    public boolean isBlack(){
        return black;
    }

    @Override
    public Action chooseAction(State state) {
        return new AlmostRandomPlayer(black).chooseAction(state);
    }

}
