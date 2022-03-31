package it.polimi.ingsw.model.component;

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

    public int getNumPawns() {
        return this.pawns.size();
    }

    public void moveInPawn(T pawn, Board<T> src) {
        this.pawns.add(pawn);
        src.pawns.remove(pawn);
    }

}

