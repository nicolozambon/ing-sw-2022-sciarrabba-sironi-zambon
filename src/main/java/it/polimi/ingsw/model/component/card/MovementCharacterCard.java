package it.polimi.ingsw.model.component.card;

import java.util.Map;

public class MovementCharacterCard extends CharacterCard {

    private CharacterCard card;

    private int extraControl;
    private int possibleExchange;
    private int numOfStudentsToReturn;

    public MovementCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.extraControl = (int) params.get("extra_control");
        this.possibleExchange = (int) params.get("possible_exchange");
        this.numOfStudentsToReturn = (int) params.get("num_of_students_to_return");
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
