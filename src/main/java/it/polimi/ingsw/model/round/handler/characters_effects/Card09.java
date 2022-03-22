package it.polimi.ingsw.model.round.handler.characters_effects;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.round.handler.Handler;

import java.util.ArrayList;
import java.util.List;

public class Card09 extends Handler {

    /*CARD EFFECT:
    "Choose a color of Student: during the influence calculation this turn, that color adds no influence."
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
            if (influencer == player) influence++;

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