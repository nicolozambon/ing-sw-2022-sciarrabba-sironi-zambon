package it.polimi.ingsw.model.component.card;

public class MovementCharacterCard extends CharacterCard {

    private CharacterCard card;

    public MovementCharacterCard(CharacterCard card) {
        super(card);
        this.getParams();//TODO params parsing
    }

}
