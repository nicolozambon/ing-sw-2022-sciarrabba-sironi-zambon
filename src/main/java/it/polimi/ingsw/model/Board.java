package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board<T> {

    /**
     * List of pawns
     */
    private final List<T> pawns;

    /**
     * Constructor of Board
     */
    protected Board() {
        pawns = new ArrayList<>();
    }

    /**
     * Constructor of Board
     * @param pawns list of pawns
     */
    protected Board(List<T> pawns) {
        this.pawns = new ArrayList<>(pawns);
    }

    /**
     * Getter for pawns
     * @return the list containing all the pawns
     */
    protected List<T> getPawns() {
        return new ArrayList<>(pawns);
    }

    /**
     * Getter for the number of pawns
     * @return the number of pawns
     */
    protected int getNumPawns() {
        return this.pawns.size();
    }

    /**
     * Move a given pawn from a given board to this board
     * @param pawn given pawn
     * @param src source board
     */
    protected void moveInPawn(T pawn, Board<T> src) {
        this.pawns.add(pawn);
        src.pawns.remove(pawn);
    }

}

