package it.polimi.ingsw.model.round.handler;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.ArrayList;
import java.util.List;


public class Handler {
    private Player actualPlayer;

    public Handler(Player player) {
        this.actualPlayer = player;
    }

    public void resolveIsland(MotherNature motherNature, List<Player> othersPlayer) {

    }

    /*public Player getInfluencer(List<Player> players, Island island){
        Player influencer = null;
        List<Integer> values = new ArrayList<Integer>();
        Tower tower = island.getTower();

        int highestInfluence = 0;
        int influence;

        if (tower != null) influencer = tower.owner;

        for (Player player : players) {
            influence = 0;
            if (influencer == player) influence++;

            for(Professor prof : player.getSchool().getProfessorsTable().getPawns()){
               // influence = influence + island.countByColor(prof.getColor());
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
    }*/

    public void professorControl(List<Player> otherPlayers) {
        for (Player player : otherPlayers) {
            for (Professor prof : player.getSchool().getProfessorsTable().getPawns()) {
                if (    actualPlayer.getSchool().getDiningRoomByColor(prof.getColor()).getNumPawns() >
                        player.getSchool().getDiningRoomByColor(prof.getColor()).getNumPawns()
                ) {
                    actualPlayer.getSchool().controlProfessor(prof, player.getSchool().getProfessorsTable());
                }
            }
        }
    }

    public void motherNatureMovement(Player player, MotherNature motherNature, int steps_choice) {
        Island destination = null;
        if (steps_choice <= player.lastAssistantCard().getSteps()) {
            motherNature.stepsToMove(steps_choice);
        }
    }

    public void extraAction() {

    }

}