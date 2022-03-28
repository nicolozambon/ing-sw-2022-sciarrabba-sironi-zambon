package it.polimi.ingsw.model.component;

import it.polimi.ingsw.enums.Color;

import java.util.List;
import java.util.Objects;

public class Island extends Board<Student> {

    private int ID;

    private Tower tower;

    private Island nextIsland;
    private Island prevIsland;

    private boolean unifyNext;
    private boolean unifyPrev;

    public Island(int ID, List<Student> students) {
        super(students);
        this.ID = ID;

        unifyPrev = false;
        unifyNext = false;
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

    public void setNextIsland(Island nextIsland) {
        this.nextIsland = nextIsland;
    }

    public void setPrevIsland(Island prevIsland) {
        this.prevIsland = prevIsland;
    }

    public boolean isUnifyNext() {
        return unifyNext;
    }

    public boolean isUnifyPrev() {
        return unifyPrev;
    }

    public void unifyToNext() {
        this.unifyNext = true;
    }

    public void unifyToPrev() {
        this.unifyPrev = true;
    }

    public int countStudentsByColor(Color color) {
        // TODO: implement the method that returns the num of students of the requested color on the island.
        // Maybe could be useful also in Board class.
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return ID == island.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
