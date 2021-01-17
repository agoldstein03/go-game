package data;

public class ComputerPlayer extends Player{

    private boolean black;

    public ComputerPlayer(boolean isBlack) {
        black = isBlack;
    }

    public boolean isBlack(){
        return black;
    }

    @Override
    public Action chooseAction() {
        return null;
    }
}
