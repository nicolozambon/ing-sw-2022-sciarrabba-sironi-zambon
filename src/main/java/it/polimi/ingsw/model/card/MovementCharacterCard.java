package it.polimi.ingsw.model.card;

import java.util.Map;

/**
 * Specific implementation of CharacterCard with Movement category
 * @see CharacterCard
 */
public class MovementCharacterCard extends CharacterCard {

    /**
     * How many additional control of professor
     */
    private final int extraControl;

    /**
     * How many exchange between entrance and dining room
     */
    private final int possibleExchange;

    /**
     * How many students every player has to return to the bag
     */
    private final int numOfStudentsToReturn;

    /**
     * Constructor, from a generic CharacterCard return a MovementCharacterCard
     * @param card the generic CharacterCard
     */
    public MovementCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.extraControl = ((Double) params.get("extra_control")).intValue();
        this.possibleExchange = ((Double) params.get("possible_exchange")).intValue();
        this.numOfStudentsToReturn = ((Double) params.get("num_of_students_to_return")).intValue();
    }

    /**
     * Returns the extra control of the card
     * @return the extra control of the card
     */
    public int getExtraControl() {
        return extraControl;
    }

    /**
     * Returns the number of exchange between entrance and dining room
     * @return the number of exchange between entrance and dining room
     */
    public int getPossibleExchange() {
        return possibleExchange;
    }

    /**
     * Returns the number of students every player has to return to the bag
     * @return the number of students every player has to return to the bag
     */
    public int getNumOfStudentsToReturn() {
        return numOfStudentsToReturn;
    }
}
