package it.polimi.ingsw.model.component.card;

import java.util.Map;

public class InfluenceCharacterCard extends CharacterCard {

    private CharacterCard card;

    private int towerInfluence;
    private int extraInfluence;
    private boolean colorWithoutInfluence;

    public InfluenceCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.towerInfluence = (int) params.get("tower_influence");
        this.extraInfluence = (int) params.get("extra_influence");
        this.colorWithoutInfluence = (boolean) params.get("color_without_influence");
    }

    public int getTowerInfluence() {
        return towerInfluence;
    }

    public int getExtraInfluence() {
        return extraInfluence;
    }

    public boolean isColorWithoutInfluence() {
        return colorWithoutInfluence;
    }
}
