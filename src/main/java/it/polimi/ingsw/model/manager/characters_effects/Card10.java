package it.polimi.ingsw.model.manager.characters_effects;

import it.polimi.ingsw.model.manager.Manager;
import it.polimi.ingsw.model.*;

import it.polimi.ingsw.model.Board;

public class Card10 extends Manager {

    public Card10() {

    }

    public void Card10Effect (Player p) {
        Board<T> src;
        Board<T> dst;
        Pawn<T> pawn;

        for (int i = 0; i < 2; i++) {
            // TODO: get pawn to move, get choice of Board to move from and destination.
            Board.movePawn (pawn, src, dst);
        }
    }
}
