package it.polimi.ingsw.model.card;

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

}
