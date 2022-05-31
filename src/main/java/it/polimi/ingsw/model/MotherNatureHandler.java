package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IslandException;
import it.polimi.ingsw.exceptions.MotherNatureStepsException;
import it.polimi.ingsw.exceptions.WinnerException;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;

import java.util.List;

public class MotherNatureHandler extends Handler {

    private MotherNatureCharacterCard card;

    protected MotherNatureHandler(List<Player> players, MotherNatureCharacterCard card) {
        super(players);
        this.card = card;
    }

    @Override
    protected void motherNatureMovement(Player currentPlayer, MotherNature motherNature, int stepsChoice) throws MotherNatureStepsException, WinnerException {
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
    protected void extraAction(Player currentPlayer, Model model, int ...values) throws IslandException, WinnerException {
        if (card.isExtraResolving()) {
            try {
                Island island = model.getIslands().get(values[0] - 1);
                if (getMostInfluentialPlayer(currentPlayer, island) != null) {
                    switchTowers(island, getMostInfluentialPlayer(currentPlayer, island));
                    unifyIsland(island);
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IslandException();
            }



        }
    }

    @Override
    public int getCardId() {
        return this.card.getId();
    }

    @Override
    public String getCategory() {
        return this.card.getCategory();
    }

}
