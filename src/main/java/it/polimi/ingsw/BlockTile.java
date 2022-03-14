package it.polimi.ingsw;

public class BlockTile {
    Island island;

    public BlockTile(Island island) {
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
