package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.card.InfluenceCharacterCard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Handler associated to InfluenceCharacterCard
 * @see InfluenceCharacterCard
 */
public class InfluenceHandler extends Handler {

    /**
     * The influence character card associated to this Handler
     */
    private final InfluenceCharacterCard card;

    /**
     * The color with no influence during this action phase
     */
    private Color colorWithNoInfluence = null;

    /**
     * Constructor with the specified InfluenceCharacterCard
     * @param players list of players
     * @param card the specified InfluenceCharacterCard
     */
    protected InfluenceHandler(List<Player> players, InfluenceCharacterCard card) {
        super(players, card.getCategory());
        this.card = card;
    }

    /**
     * Returns the influence of the specified player on the specified island, following the InfluenceCharacterCard rules
     * @param island the island
     * @param player the player
     * @param influence the influence of the player in islands linked to the specified
     * @return the influence of the specified player on the specified island
     */
    @Override
    protected int resolveIslandHelper (Island island, Player player, int influence) {
        for (Professor professor : player.getSchool().getProfessorsTable().getPawns()) {
            if (!card.isColorWithoutInfluence()) {
                influence += island.countStudentsByColor(professor.getColor());
            }
            else if(!colorWithNoInfluence.equals(professor.getColor())) {
                influence += island.countStudentsByColor(professor.getColor());
            }
        }

        if (island.getTower() != null && island.getTower().getOwner().equals(player)) {
            influence += card.getTowerInfluence();
        }
        return influence;
    }

    /**
     * Returns the most influential player on the specified island or group of islands, following the InfluenceCharacterCard rules
     * @param currentPlayer the current player who has moved mother nature
     * @param island the Island where mother nature has ended her movement
     * @return the most influential player on the specified island, the one who builds the tower
     */
    @Override
    protected Player getMostInfluentialPlayer(Player currentPlayer, Island island) {
        Map<Player, Integer> playerInfluence = new HashMap<>();
        int maxInfluence = -1;
        Player playerMaxInfluence = null;
        boolean isValid = false;

        for (Player player : players) {
            playerInfluence.put(player, resolveIsland(island, player));
        }

        playerInfluence.put(currentPlayer, playerInfluence.get(currentPlayer) + card.getExtraInfluence());

        for (Player player : players) {
            if (playerInfluence.get(player) > maxInfluence) {
                maxInfluence = playerInfluence.get(player);
                playerMaxInfluence = player;
                isValid = true;
            } else if (playerInfluence.get(player) == maxInfluence) {
                isValid = false;
            }
        }

        if (isValid) return playerMaxInfluence;

        return null;
    }

    /**
     * Play the extra action associated to the InfluenceCharacterCard if available
     * @param currentPlayer the current player
     * @param model the model of the game
     * @param values various argument
     */
    @Override
    protected void extraAction(Player currentPlayer, Model model, int ... values) {
        colorWithNoInfluence = Arrays.stream(Color.values()).filter(c -> c.ordinal() == values[0]).findFirst().get();
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
