package data.exceptions;

import data.State;
import views.GameScreen;
import views.Main;

import java.util.ArrayList;

public class KoException extends IllegalArgumentException {

    public KoException(String message) {
        super(message);
    }

    public static void assertValid(State state) throws KoException {
        ArrayList<State> states = GameScreen.getGame().states;
        int size = states.size();
        if (size > 1 && states.get(size - 2).equals(state)) {
            throw new KoException("New state would lead to Ko");
        }
    }

}
