package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board<T> {

    private List<T> pawns;

    public Board (){
        pawns = new ArrayList<T>();
    }

    public Board(List<T> pawns){
        this.pawns = new ArrayList<T>(pawns);
    }

    public List<T> getPawns(){
        return new ArrayList<T>(pawns);
    }

    public T getPawn(int index){
        return pawns.remove(index);
    }

    public boolean addPawns(List<T> pawns){
        return this.pawns.addAll(pawns);
    }

    public boolean addPawn(T pawn){
        return this.pawns.add(pawn);
    }

}
