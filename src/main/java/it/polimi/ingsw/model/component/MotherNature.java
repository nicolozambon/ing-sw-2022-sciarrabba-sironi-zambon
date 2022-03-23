package it.polimi.ingsw.model.component;

public class MotherNature {

    private Island position;

    public MotherNature(Island position){
        this.position = position;
    }

    public Island getPosition() {
        return position;
    }

    public void stepsToMove(int steps) {
        for (int i = 0; i < steps; i++) {
            position = position.getNextIsland();
        }
    }
}
