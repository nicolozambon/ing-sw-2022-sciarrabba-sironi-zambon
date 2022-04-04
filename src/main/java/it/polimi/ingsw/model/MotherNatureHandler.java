package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.MotherNatureCharacterCard;

import java.util.List;

public class MotherNatureHandler extends Handler {

    MotherNatureCharacterCard card;

    protected MotherNatureHandler(List<Player> players, MotherNatureCharacterCard card) {
        super(players);
        this.card = card;
    }

    @Override
    protected void extraAction(int value, Model model) {
        Island island = null;
        for (Island i : model.getIslands()) {
            if (i.getId() == value) island = i;
        }

        //TODO fix switchTowers(island, getMostInfluentialPlayer(, island));
        unifyIsland(island);
    }
}
