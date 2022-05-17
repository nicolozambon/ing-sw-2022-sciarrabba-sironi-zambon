package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.MotherNatureStepsException;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;

import java.util.List;

public class MotherNatureHandler extends Handler {

    private final MotherNatureCharacterCard card;

    protected MotherNatureHandler(List<Player> players, MotherNatureCharacterCard card) {
        super(players);
        this.card = card;
    }

    @Override
    protected void motherNatureMovement(Player currentPlayer, MotherNature motherNature, int stepsChoice) throws MotherNatureStepsException {
        Player mostInfluentialPlayer = null;
        if(stepsChoice > 0 && stepsChoice <= currentPlayer.getLastAssistantCard().getSteps() + card.getExtraMovement()) {
            motherNature.stepsToMove(stepsChoice);
            mostInfluentialPlayer = getMostInfluentialPlayer(currentPlayer, motherNature.getPosition());
            if (mostInfluentialPlayer != null) {
                switchTowers(motherNature.getPosition(), mostInfluentialPlayer);
                unifyIsland(motherNature.getPosition());
            }
        } else {
            throw new MotherNatureStepsException();
        }
    }

    @Override
    protected void extraAction(Player currentPlayer, Model model, int ...values) {
        if (card.isExtraResolving()) {
            Island island = null;
            for (Island island1 : model.getIslands()) {
                if (island1.getId() == values[0]) island = island1;
            }

            if (getMostInfluentialPlayer(currentPlayer, island) != null) {
                switchTowers(island, getMostInfluentialPlayer(currentPlayer, island));
                unifyIsland(island);
            }
        }
    }

    @Override
    public int getCardId() {
        return this.card.getId();
    }
}
