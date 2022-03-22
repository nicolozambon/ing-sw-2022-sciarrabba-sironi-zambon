package it.polimi.ingsw.model.manager;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;

import java.util.ArrayList;
import java.util.List;


public class Handler {

    public Handler() {

    }

    //public int calculatePlayerInfluenceOnIsland(Player player, Island island) {

    public int calculatePlayerInfluenceOnIsland(List<Player> players, Player actual, Island island) {
        return 0;
    }

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

    public void professorControl() {

    }

    public Island motherNatureMovement(MotherNature motherNature, Island from, AssistantCard card, int steps_choice) {
        Island destination = null;
        if (steps_choice <= card.getSteps()) {
            while (steps_choice > 0) {
                destination = from.nextIsland();
                steps_choice--;
            }
        }
        motherNature.setPosition(destination);
        return destination;
    }



}