package it.polimi.ingsw.model;

public class MotherNature {

    /**
     * Island of mother nature
     */
    private Island position;

    /**
     * Constructor of MotherNature
     * @param position island of mother nature
     */
    protected MotherNature(Island position){
        this.position = position;
    }

    /**
     * Getter of the position
     * @return island of mother nature
     */
    public Island getPosition() {
        return position;
    }

    /**
     * Move mother nature of given steps
     * @param steps number of steps
     */
    public void stepsToMove(int steps) {
        for (int i = 0; i < steps; i++) {
            while(position.isUnifyNext()) {
                position = position.getNextIsland();
            }
            position = position.getNextIsland();
        }
    }

    /**
     * Setter of the position
     * @param island position
     */
    protected void setPosition(Island island) {
        this.position = island;
    }

}