package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board<T> {

    private final List<T> pawns;

    protected Board() {
        pawns = new ArrayList<>();
    }

    protected Board(List<T> pawns) {
        this.pawns = new ArrayList<>(pawns);
    }

    protected List<T> getPawns() {
        return new ArrayList<>(pawns);
    }

    protected int getNumPawns() {
        return this.pawns.size();
    }

    protected void moveInPawn(T pawn, Board<T> src) {
        this.pawns.add(pawn);
        src.pawns.remove(pawn);
    }

}

