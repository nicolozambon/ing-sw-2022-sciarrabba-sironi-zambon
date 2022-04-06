package it.polimi.ingsw.model.card;

import java.util.Map;

public class MovementCharacterCard extends CharacterCard {

    private CharacterCard card;

    private int extraControl;
    private int possibleExchange;
    private int numOfStudentsToReturn;

    public MovementCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.extraControl = ((Double) params.get("extra_control")).intValue();
        this.possibleExchange = ((Double) params.get("possible_exchange")).intValue();
        this.numOfStudentsToReturn = ((Double) params.get("num_of_students_to_return")).intValue();
    }

    public int getExtraControl() {
        return extraControl;
    }

    public int getPossibleExchange() {
        return possibleExchange;
    }

    public int getNumOfStudentsToReturn() {
        return numOfStudentsToReturn;
    }
}
