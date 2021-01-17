package data;

public class HumanPlayer extends Player {

    private boolean black;

    public HumanPlayer(boolean isBlack){
        black = isBlack;
    }

    @Override
    public boolean isBlack() {
        return black;
    }

    @Override
    public Action chooseAction(State state) {
        return null;
    }

    public Action chooseAction(int x, int y) {
        if(x>=0)
            return new PlaceStoneAction(new Position(x, y, black ? Stone.BLACK:Stone.WHITE));
        else
            return null;
    }

    public Action pass() {return new PassAction();}
}
