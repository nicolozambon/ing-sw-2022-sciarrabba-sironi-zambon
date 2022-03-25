package it.polimi.ingsw.model.round.handler;

import it.polimi.ingsw.model.Player;

public class InfluenceModifier extends Handler {

    public InfluenceModifier(Player player) {
        super(player);
    }

    @Override
    public int calculateInfluence(){
        return 0;
    }
}
