package it.polimi.ingsw.model.card;

import java.util.Map;

/**
 * Specific implementation of CharacterCard with MotherNature category
 * @see CharacterCard
 */
public class MotherNatureCharacterCard extends CharacterCard {

    /**
     * How many additional steps mother nature has
     */
    private final int extraMovement;

    /**
     * If is it possible to extra resolve an island
     */
    private final boolean extraResolving;

    /**
     * Constructor, from a generic CharacterCard return a MotherNatureCharacterCard
     * @param card the generic CharacterCard
     */
    public MotherNatureCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.extraMovement = ((Double) params.get("extra_movement")).intValue();
        this.extraResolving = (boolean) params.get("extra_resolving");
    }

    /**
     * Returns the mother nature additional steps
     * @return the mother nature additional steps
     */
    public int getExtraMovement() {
        return extraMovement;
    }

    /**
     * Returns whether is possible to extra resolve an island
     * @return true if is possible to extra resolve an island, false otherwise
     */
    public boolean isExtraResolving() {
        return extraResolving;
    }

}
