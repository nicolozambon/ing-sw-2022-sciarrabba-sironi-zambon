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
    protected void motherNatureMovement(Player currentPlayer, MotherNature motherNature, int stepsChoice) {
        if(stepsChoice > 0 && stepsChoice <= currentPlayer.getLastAssistantCard().getSteps() + card.getExtraMovement()) {
            motherNature.stepsToMove(stepsChoice);
            switchTowers(motherNature.getPosition(), getMostInfluentialPlayer(currentPlayer, motherNature.getPosition()));
            unifyIsland(motherNature.getPosition());
        }
    }

    @Override
    protected void extraAction(Player currentPlayer, Model model, int ...values) {
        if (card.isExtraResolving()) {
            Island island = null;
            for (Island i : model.getIslands()) {
                if (i.getId() == values[0]) island = i;
            }

            switchTowers(island, getMostInfluentialPlayer(currentPlayer, island));
            unifyIsland(island);
        }
    }
}
