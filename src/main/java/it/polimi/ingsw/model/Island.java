package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class Island extends Board<Student>{

    private int ID;

    private Tower tower;

    private Island nextIsland;
    private Island prevIsland;

    private boolean linkNext;
    private boolean linkPrev;

    public Island(List<Student> students, Island prev, Island next) {
        super(students);
        this.prevIsland = prev;
        this.nextIsland = next;
        linkPrev = false;
        linkNext = false;
        tower = null;
    }

    public Tower setTower(Tower tower) {
        Tower temp = this.tower;
        this.tower = tower;
        return temp;
    }

    public Tower getTower(){
        return tower;
    }

    public Island getNextIsland() {
        return this.nextIsland;
    }

    public Island getPrevIsland() {
        return this.prevIsland;
    }

    public boolean isLinkNext() {
        return linkNext;
    }

    public boolean isLinkPrev() {
        return linkPrev;
    }

    public void linkToNext() {
        this.linkNext = true;
    }

    public void linkToPrev() {
        this.linkPrev = true;
    }
}
