package it.polimi.ingsw.model.component.card;

import java.util.Objects;

public class CharacterCard extends Card {

    private final int id;
    private final int cost;

    private final String name;
    private final String path;
    private final String setup;
    private final String effect;

    private boolean hasBeenPlayed = false;



    public CharacterCard(int id, int cost, String name, String path, String setup, String effect) {
        this.id = id;
        this.cost = cost;
        this.name = name;
        this.path = path;
        this.setup = setup;
        this.effect = effect;
    }

    public int getCost(){
        return this.cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterCard)) return false;
        CharacterCard that = (CharacterCard) o;
        return cost == that.cost && Objects.equals(setup, that.setup) && Objects.equals(effect, that.effect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, setup, effect);
    }
}
