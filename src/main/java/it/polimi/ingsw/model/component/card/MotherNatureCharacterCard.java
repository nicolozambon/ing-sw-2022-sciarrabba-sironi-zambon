package it.polimi.ingsw.model.component.card;

import java.util.Map;

public class MotherNatureCharacterCard extends CharacterCard {

    private CharacterCard card;

    public MotherNatureCharacterCard(CharacterCard card) {
        super(card);
        this.getParams();//TODO params parsing
    }

}
