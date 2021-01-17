package data.exceptions;

import data.Group;
import data.Position;
import data.State;

import java.util.HashSet;

public class SelfCaptureException extends IllegalArgumentException {

    private static Object HashSet;

    public SelfCaptureException(Position pos) {
        super("A move of "+pos.toString()+" would cause a self-capture");
    }

    public static Group assertValid(Position pos, State state) throws SelfCaptureException {
        Group group = new Group(pos, state);
        assertValidGroup(pos, group);
        return group;
    }

    public static Group assertValid(Position pos, State state, HashSet<Position> processedPositions) throws SelfCaptureException {
        Group group = new Group(pos, state, processedPositions);
        assertValidGroup(pos, group);
        return group;
    }

    private static void assertValidGroup(Position pos, Group group) {
        if (group.countLiberties() == 0) {
            throw new SelfCaptureException(pos);
        }
    }

}
