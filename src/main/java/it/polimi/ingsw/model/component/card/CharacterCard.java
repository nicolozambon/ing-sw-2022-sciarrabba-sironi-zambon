package it.polimi.ingsw.model.component.card;

import java.util.Map;
import java.util.Objects;

public class CharacterCard extends Card {

    private int id;
    private String res_path;
    private int coins;
    private String setup;
    private String effect;
    private String category;
    private Map<String, Object> params;

    private boolean hasBeenPlayed = false;

    public CharacterCard(int id, String res_path, int coins, String setup, String effect, String category, Map<String, Object> params) {
        this.id = id;
        this.res_path = res_path;
        this.coins = coins;
        this.setup = setup;
        this.effect = effect;
        this.category = category;
        this.params = params;
    }

    public int getCoins() {
        return this.coins;
    }

    public String getCategory() {
        return this.category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterCard)) return false;
        CharacterCard that = (CharacterCard) o;
        return coins == that.coins && Objects.equals(setup, that.setup) && Objects.equals(effect, that.effect) && Objects.equals(category, that.category) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coins, setup, effect, category, params);
    }
}
