package it.polimi.ingsw.model.component;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.Objects;

public class Island extends Board<Student> {

    private final int ID;

    private final Board<Tower> towerBoard = new Board<>();

    private Island nextIsland;
    private Island prevIsland;

    private boolean unifyNext;
    private boolean unifyPrev;

    public Island(int ID, List<Student> students) {
        super(students);
        this.ID = ID;

        unifyPrev = false;
        unifyNext = false;
    }

    public void setTower(Player player) {
        if (towerBoard.getNumPawns() == 0) {
            Board<Tower> sourceTowerBoard = player.getSchool().getTowersBoard();
            Tower oldTower = this.getTower();
            Board<Tower> oldTowerBoard = oldTower.getOwner().getSchool().getTowersBoard();

            oldTowerBoard.moveInPawn(oldTower, towerBoard);
            towerBoard.moveInPawn(sourceTowerBoard.getPawns().get(0), sourceTowerBoard);
        }
    }

    public Tower getTower() {
        return towerBoard.getPawns().get(0);
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
        return (int) getPawns().stream().filter(x -> x.getColor() == color).count();
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
