package it.polimi.ingsw.model.round.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.MotherNature;
import it.polimi.ingsw.model.component.card.InfluenceCharacterCard;

import java.util.List;

public class InfluenceModifier extends Handler {

    /* Card has this params:
    private int towerInfluence;
    private int extraInfluence;
    private boolean colorWithoutInfluence;
    */

    public InfluenceModifier(Player player, InfluenceCharacterCard card) {
        super(player);
    }

    @Override
    public void resolveIsland(MotherNature motherNature, List<Player> otherPlayers) {
        super.resolveIsland(motherNature, otherPlayers);
    }
}
