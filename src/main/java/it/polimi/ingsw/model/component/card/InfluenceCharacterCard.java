package it.polimi.ingsw.model.component.card;

public class InfluenceCharacterCard extends CharacterCard {

    private CharacterCard card;

    public InfluenceCharacterCard(CharacterCard card) {
        super(card);
        this.getParams();//TODO params parsing
    }



}
