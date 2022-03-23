package it.polimi.ingsw.model.component;

public class NoEntryTile {
    Island island;

    public NoEntryTile(Island island) {
        this.island = island;
    }

    /**
     *
     * @return the island on which the blocktile is on.
     */
    public Island getIsland() {
        return island;
    }
}
