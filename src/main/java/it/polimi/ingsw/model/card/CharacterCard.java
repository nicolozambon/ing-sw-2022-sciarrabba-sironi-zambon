package it.polimi.ingsw.model.card;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CharacterCard {

    private final int id;
    private int coins;
    private final String effect;
    private final String category;
    private final Map<String, Object> params;
    private final boolean hasExtraAction;

    public CharacterCard(int id, int coins, String effect, String category, Map<String, Object> params, boolean hasExtraAction) {
        this.id = id;
        this.coins = coins;
        this.effect = effect;
        this.category = category;
        this.params = params;
        this.hasExtraAction = hasExtraAction;
    }

    public CharacterCard(CharacterCard card) {
        this.id = card.id;
        this.coins = card.coins;
        this.effect = card.effect;
        this.category = card.category;
        this.params = card.params;
        this.hasExtraAction = card.hasExtraAction;
    }

    protected Map<String, Object> getParams() {
        return new HashMap<>(params);
    }

    public int getCoins() {
        return this.coins;
    }

    public boolean getHasExtraAction() {
        return this.hasExtraAction;
    }

    public String getCategory() {
        return this.category;
    }

    public int getId() {
        return id;
    }

    public void incrementCoinCost() {
        this.coins++;
    }

    public String getEffect() {
        return this.effect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterCard that)) return false;
        return coins == that.coins && Objects.equals(effect, that.effect) && Objects.equals(category, that.category) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coins, effect, category, params);
    }

}
