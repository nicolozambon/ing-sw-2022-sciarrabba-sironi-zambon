package it.polimi.ingsw.model.card;

import java.util.Map;

/**
 * Specific implementation of CharacterCard with Influence category
 * @see CharacterCard
 */
public class InfluenceCharacterCard extends CharacterCard {

    /**
     * How many influence a tower gives
     */
    private final int towerInfluence;

    /**
     * Additional influence given by the card
     */
    private final int extraInfluence;

    /**
     * If there is a color without influence
     */
    private final boolean colorWithoutInfluence;

    /**
     * Constructor, from a generic CharacterCard return an InfluenceCard
     * @param card the generic CharacterCard
     */
    public InfluenceCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.towerInfluence = ((Double) params.get("tower_influence")).intValue();
        this.extraInfluence = ((Double) params.get("extra_influence")).intValue();
        this.colorWithoutInfluence = (boolean) params.get("color_without_influence");
    }

    /**
     * Returns the tower influence
     * @return the tower influence
     */
    public int getTowerInfluence() {
        return towerInfluence;
    }

    /**
     * Returns the extra influence
     * @return the extra influence
     */
    public int getExtraInfluence() {
        return extraInfluence;
    }

    /**
     * Returns if there will be a color without influence
     * @return true if there will be a color without influence, false otherwise
     */
    public boolean isColorWithoutInfluence() {
        return colorWithoutInfluence;
    }
}
