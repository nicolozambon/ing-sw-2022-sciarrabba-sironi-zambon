package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.card.InfluenceCharacterCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfluenceHandler extends Handler {

    private InfluenceCharacterCard card;
    private Color colorWithNoInfluence = null;

    protected InfluenceHandler(List<Player> players, InfluenceCharacterCard card) {
        super(players);
        this.card = card;
    }

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

        if (island.getTower().getOwner().equals(player)) {
            influence += 1* card.getTowerInfluence() ;
        }
        return influence;
    }

    @Override
    protected Player getMostInfluentialPlayer(Player currentPlayer, Island island) {
        Map<Player, Integer> playerInfluence = new HashMap<Player, Integer>();
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

    @Override
    protected void extraAction(int value, Model model) {
        colorWithNoInfluence = Color.values()[value];
    }
}