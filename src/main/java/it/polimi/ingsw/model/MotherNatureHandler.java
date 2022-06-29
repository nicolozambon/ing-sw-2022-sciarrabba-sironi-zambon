package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IslandException;
import it.polimi.ingsw.exceptions.MotherNatureStepsException;
import it.polimi.ingsw.exceptions.WinnerException;
import it.polimi.ingsw.model.card.MotherNatureCharacterCard;

import java.util.List;

/**
 * The Handler associated to MotherNatureCharacterCard
 * @see MotherNatureCharacterCard
 */
public class MotherNatureHandler extends Handler {

    /**
     * The mother nature character card associated to this Handler
     */
    private final MotherNatureCharacterCard card;

    /**
     * Constructor with the specified MotherNatureCharacterCard
     * @param players list of players
     * @param card the specified MotherNatureCharacterCard
     */
    protected MotherNatureHandler(List<Player> players, MotherNatureCharacterCard card) {
        super(players, card.getCategory());
        this.card = card;
    }

    /**
     * Move mother nature by the specified steps, following the MotherNatureCharacterCard rules
     * @param currentPlayer the current player
     * @param motherNature the mother nature to be moved
     * @param stepsChoice the steps chosen by the player
     * @throws MotherNatureStepsException thrown if the amount of steps is invalid
     * @throws WinnerException thrown if there is a winner
     */
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

    /**
     * Play the extra action associated to the MotherNatureCharacterCard if available
     * @param currentPlayer the current player
     * @param model the model of the game
     * @param values various argument
     */
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
        return super.getCategory();
    }

}
