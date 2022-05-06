package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;

import java.util.List;
import java.util.Objects;

public class Island extends Board<Student> {

    private final int id;

    private final Board<Tower> towerBoard = new Board<>();

    private transient Island nextIsland;
    private transient Island prevIsland;

    private boolean unifyNext;
    private boolean unifyPrev;

    protected Island(int id) {
        super();
        this.id = id;

        unifyPrev = false;
        unifyNext = false;
    }

    protected void setTower(Player player) {
        Board<Tower> sourceTowerBoard = player.getSchool().getTowersBoard();;
        if (towerBoard.getNumPawns() > 0) {
            Tower oldTower = this.getTower();
            Board<Tower> oldTowerBoard = oldTower.getOwner().getSchool().getTowersBoard();

            oldTowerBoard.moveInPawn(oldTower, towerBoard);
        }
        if (sourceTowerBoard.getNumPawns() > 0) towerBoard.moveInPawn(sourceTowerBoard.getPawns().get(0), sourceTowerBoard);
    }

    protected Tower getTower() {
        if (towerBoard.getNumPawns() > 0) {
            return towerBoard.getPawns().get(0);
        }
        else {
            return null;
        }
    }

    protected Island getNextIsland() {
        return this.nextIsland;
    }

    protected Island getPrevIsland() {
        return this.prevIsland;
    }

    protected void setNextIsland(Island nextIsland) {
        this.nextIsland = nextIsland;
    }

    protected void setPrevIsland(Island prevIsland) {
        this.prevIsland = prevIsland;
    }

    protected boolean isUnifyNext() {
        return unifyNext;
    }

    protected boolean isUnifyPrev() {
        return unifyPrev;
    }

    protected void unifyToNext() {
        this.unifyNext = true;
    }

    protected void unifyToPrev() {
        this.unifyPrev = true;
    }

    protected int countStudentsByColor(Color color) {
        return (int) getPawns().stream().filter(x -> x.getColor() == color).count();
    }

    protected int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return id == island.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
