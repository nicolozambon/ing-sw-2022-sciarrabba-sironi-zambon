package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board<T> {

    private final List<T> pawns;

    public Board() {
        pawns = new ArrayList<>();
    }

    public Board(List<T> pawns) {
        this.pawns = new ArrayList<>(pawns);
    }

    public List<T> getPawns() {
        return new ArrayList<>(pawns);
    }

    public boolean moveInPawn(int index, Board<T> src) throws ArrayIndexOutOfBoundsException { //Is this the right exception?
        try {
            this.pawns.add(src.pawns.get(index));
            src.pawns.remove(src.pawns.get(index));
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

}

