package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.WinnerException;

import java.util.Objects;

public class Island extends Board<Student> {

    /**
     * Identifier of the island
     */
    private final int id;

    /**
     * Container of the towers
     */
    private final Board<Tower> towerBoard = new Board<>();

    /**
     * Instance of the next island
     */
    private transient Island nextIsland;

    /**
     * Instance of the previous island
     */
    private transient Island prevIsland;

    /**
     * Represent the connection to the next island
     */
    private boolean unifyNext;

    /**
     * Represent the connection to the previous island
     */
    private boolean unifyPrev;

    /**
     * Constructor of Island
     * @param id identifier
     */
    protected Island(int id) {
        super();
        this.id = id;

        unifyPrev = false;
        unifyNext = false;
    }

    /**
     * Set a tower to an island
     * @param player the owner of the tower
     * @throws WinnerException raised if there is a winner
     */
    protected void setTower(Player player) throws WinnerException {
        Board<Tower> sourceTowerBoard = player.getSchool().getTowersBoard();;
        if (sourceTowerBoard.getNumPawns() > 0) {
            if (towerBoard.getNumPawns() > 0) {
                Tower oldTower = this.getTower();
                Board<Tower> oldTowerBoard = oldTower.getOwner().getSchool().getTowersBoard();
                oldTowerBoard.moveInPawn(oldTower, towerBoard);
            }
            towerBoard.moveInPawn(sourceTowerBoard.getPawns().get(0), sourceTowerBoard);
            if (sourceTowerBoard.getNumPawns() < 1) {
                throw new WinnerException(player.getId());
            }
        } else throw new WinnerException(player.getId());
    }

    /**
     * Getter for tower
     * @return if present the tower else null
     */
    protected Tower getTower() {
        if (towerBoard.getNumPawns() > 0) {
            return towerBoard.getPawns().get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Getter of the next island
     * @return the next island
     */
    protected Island getNextIsland() {
        return this.nextIsland;
    }

    /**
     * Getter of the previous island
     * @return the previous island
     */
    protected Island getPrevIsland() {
        return this.prevIsland;
    }

    /**
     * Setter of the next island
     * @param nextIsland the next island
     */
    protected void setNextIsland(Island nextIsland) {
        this.nextIsland = nextIsland;
    }

    /**
     * Setter of the previous island
     * @param prevIsland the previous island
     */
    protected void setPrevIsland(Island prevIsland) {
        this.prevIsland = prevIsland;
    }

    /**
     * Getter for unifyNext
     * @return if is linked to next true else false
     */
    protected boolean isUnifyNext() {
        return unifyNext;
    }

    /**
     * Getter for unifyPrev
     * @return if is linked to previous true else false
     */
    protected boolean isUnifyPrev() {
        return unifyPrev;
    }

    /**
     * Setter for unifyNext
     */
    protected void unifyToNext() {
        this.unifyNext = true;
    }

    /**
     * Setter for unifyPrev
     */
    protected void unifyToPrev() {
        this.unifyPrev = true;
    }

    /**
     * Getter of the length of the students of a given color
     * @param color color of interest
     * @return the amount of students of the given color
     */
    protected int countStudentsByColor(Color color) {
        return (int) getPawns().stream().filter(x -> x.getColor() == color).count();
    }

    /**
     * Getter of the id
     * @return identifier
     */
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
