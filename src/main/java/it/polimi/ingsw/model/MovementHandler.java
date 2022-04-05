package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.List;

//TODO remain card 2 and card 10
public class MovementHandler extends Handler {

    MovementCharacterCard card;

    protected MovementHandler(List<Player> players, MovementCharacterCard card) {
        super(players);
        this.card = card;
    }

    @Override
    protected void extraAction(Player currentPlayer, int value, Model model) {
        model.returnStudentsToBag(Color.values()[value], card.getNumOfStudentsToReturn());
    }

}
