package it.polimi.ingsw.model.card;

import java.util.Map;

public class MotherNatureCharacterCard extends CharacterCard {

    private final int extraMovement;
    private final boolean extraResolving;

    public MotherNatureCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.extraMovement = ((Double) params.get("extra_movement")).intValue();
        this.extraResolving = (boolean) params.get("extra_resolving");
    }

    public int getExtraMovement() {
        return extraMovement;
    }

    public boolean isExtraResolving() {
        return extraResolving;
    }

}
