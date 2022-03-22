package it.polimi.ingsw.model.manager.characters_effects;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.manager.Handler;

import java.util.ArrayList;
import java.util.List;

public class Card08 extends Handler {
    /*CARD EFFECT:
    "During the influence calculation this turn, you count as having 2 more influence."
    */
    //TODO make it work as described
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