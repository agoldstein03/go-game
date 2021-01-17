package data;

import java.util.ArrayList;

public class GoString {

    private ArrayList<Position> positions = new ArrayList<>();
    private ArrayList<Position> territory = new ArrayList<>();
    private int eyes;

    public GoString(Position stone1, Position stone2) {
        positions.add(stone1);
        positions.add(stone2);
    }

    public void addStone(Position stone) {

    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public ArrayList<Position> getTerritory() {
        return territory;
    }

    private void evaluateTerritory() {

    }

    private void sortStones() {
    }

    public int getEyes() {
        return eyes;
    }

}
