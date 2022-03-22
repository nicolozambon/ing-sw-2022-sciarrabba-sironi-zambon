package it.polimi.ingsw.model.manager.characters_effects;

import it.polimi.ingsw.model.manager.Handler;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Card06 extends Handler {
    /*CARD EFFECT:
    "When resolving a Conquering on an Island, Towers do not count towards influence."
    */

    @Override
    public Player getInfluencer(List<Player> players, Island island){
        Player influencer = null;
        List<Integer> values = new ArrayList<Integer>();
        Tower tower = island.getTower();

        int highestInfluence = 0;
        int influence;

        if (tower != null) influencer = tower.owner;

        for (Player player : players) {
            influence = 0;
            //if (influencer == player) influence++; Tower do not count

            for(Professor prof : player.school.professorsTable.getPawns()){
                influence = influence + island.countByColor(prof.getColor());
            }

            if (influence > highestInfluence) {
                highestInfluence = influence;
                influencer = player;
            }
            values.add(influence);
        }

        final int streamVar = highestInfluence;

        if( values.stream().filter(x -> x == streamVar).count() > 1){
            influencer = tower.owner;
        }

        return influencer;
    }
}
