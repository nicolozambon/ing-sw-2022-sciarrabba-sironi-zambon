package it.polimi.ingsw.model.card;

import java.util.Objects;

public class CharacterCard extends Card {

    private final int id;
    private final int coins;
    private final String name;
    private final String path;
    private final String setup;
    private final String effect;

    public CharacterCard(int id, int coins, String name, String path, String setup, String effect) {
        this.id = id;
        this.coins = coins;
        this.name = name;
        this.path = path;
        this.setup = setup;
        this.effect = effect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterCard)) return false;
        CharacterCard that = (CharacterCard) o;
        return coins == that.coins && Objects.equals(setup, that.setup) && Objects.equals(effect, that.effect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coins, setup, effect);
    }
}
