package it.polimi.ingsw.model.component.card;

import it.polimi.ingsw.model.Player;

import java.util.Map;

public class MotherNatureCharacterCard extends CharacterCard {

    private CharacterCard card;

    private int extraMovement;
    private boolean extraResolving;

    public MotherNatureCharacterCard(CharacterCard card) {
        super(card);
        Map<String, Object> params = this.getParams();
        this.extraMovement = (int) params.get("extra_movement");
        this.extraResolving = (boolean) params.get("extra_resolving");
    }

    public int getExtraMovement() {
        return extraMovement;
    }

    public boolean isExtraResolving() {
        return extraResolving;
    }

}
