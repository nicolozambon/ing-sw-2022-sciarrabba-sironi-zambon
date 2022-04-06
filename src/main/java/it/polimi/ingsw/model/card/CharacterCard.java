package it.polimi.ingsw.model.card;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CharacterCard {

    private int id;
    private String res_path;
    private int coins;
    private String effect;
    private String category;
    private Map<String, Object> params;
    private boolean hasExtraAction;

    public CharacterCard(int id, String res_path, int coins, String effect, String category, Map<String, Object> params, Boolean hasExtraAction) {
        this.id = id;
        this.res_path = res_path;
        this.coins = coins;
        this.effect = effect;
        this.category = category;
        this.params = params;
        this.hasExtraAction = hasExtraAction;
    }

    public CharacterCard(CharacterCard card) {
        this.id = card.id;
        this.res_path = card.res_path;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterCard)) return false;
        CharacterCard that = (CharacterCard) o;
        return coins == that.coins && Objects.equals(effect, that.effect) && Objects.equals(category, that.category) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coins, effect, category, params);
    }

}
