package it.polimi.ingsw.model.card;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A Class to represent the character cards
 */
public class CharacterCard {

    /**
     * A unique id for each card
     */
    private final int id;

    /**
     * The coins needed to play the card
     */
    private int coins;

    /**
     * The effect of the card
     */
    private final String effect;

    /**
     * The category of the card
     */
    private final String category;

    /**
     * Map of additional parameters that change between different category
     */
    private final Map<String, Object> params;

    /**
     * If the card has or not extra action
     */
    private final boolean hasExtraAction;

    /**
     * Constructor with all attributes
     * @param id the unique id
     * @param coins the coins
     * @param effect the effect
     * @param category the category
     * @param params the additional parameters
     * @param hasExtraAction whether tha card has extra action
     */
    public CharacterCard(int id, int coins, String effect, String category, Map<String, Object> params, boolean hasExtraAction) {
        this.id = id;
        this.coins = coins;
        this.effect = effect;
        this.category = category;
        this.params = params;
        this.hasExtraAction = hasExtraAction;
    }

    /**
     * Constructor to deep copy another card
     * @param card the other card
     */
    public CharacterCard(CharacterCard card) {
        this.id = card.id;
        this.coins = card.coins;
        this.effect = card.effect;
        this.category = card.category;
        this.params = card.params;
        this.hasExtraAction = card.hasExtraAction;
    }

    /**
     * Returns the map of additional parameters
     * @return the map of additional parameters
     */
    protected Map<String, Object> getParams() {
        return new HashMap<>(params);
    }

    /**
     * Returns the coin necessarily to play the card
     * @return the coin necessarily to play the card
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Returns whether the card has extra action
     * @return true if the card has extra action, false otherwise
     */
    public boolean getHasExtraAction() {
        return this.hasExtraAction;
    }

    /**
     * Returns the card category
     * @return the card category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Returns the card's unique id
     * @return the card's unique id
     */
    public int getId() {
        return id;
    }

    /**
     * Increment the card's coins by 1 after the usage
     */
    public void incrementCoinCost() {
        this.coins++;
    }

    /**
     * Returns the effect of the card
     * @return the effect of the card
     */
    public String getEffect() {
        return this.effect;
    }

    /**
     * Compares the card with the specified object
     * @param o the object the card is compared to
     * @return true if the two cards have all attributes equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterCard that)) return false;
        return id == that.id && coins == that.coins && Objects.equals(effect, that.effect) && Objects.equals(category, that.category) && Objects.equals(params, that.params);
    }

    /**
     * Returns hash of the card based on all attributes
     * @return hash of the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, coins, effect, category, params);
    }

}
