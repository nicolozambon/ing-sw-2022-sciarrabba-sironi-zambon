package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board<T> {

    final List<T> pawns;

    public Board() {
        pawns = new ArrayList<T>();
    }

    public Board(List<T> pawns) {
        this.pawns = new ArrayList<T>(pawns);
    }

    public List<T> getPawns() {
        return new ArrayList<T>(pawns);
    }

    public T getPawn(int index) {
        return pawns.remove(index);
    }

    public boolean moveInPawn(T pawn, Board<T> src) throws ArrayIndexOutOfBoundsException { //Is this the right exception?
        try {
            this.pawns.add(pawn);
            src.pawns.remove(pawn);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public boolean moveToPawn(T pawn, Board<T> dst) throws ArrayIndexOutOfBoundsException { //Is this the right exception?
        try {
            dst.pawns.add(pawn);
            this.pawns.remove(pawn);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public static boolean movePawn(T pawn, Board<T> src, Board<T> dst) throws ArrayIndexOutOfBoundsException {
        try {
            dst.pawns.add(pawn);
            src.pawns.remove(pawn);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

}

