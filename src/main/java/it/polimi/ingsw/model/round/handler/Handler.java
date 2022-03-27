package it.polimi.ingsw.model.round.handler;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;

import java.util.List;


public class Handler {
    private final Player actualPlayer;

    public Handler(Player player) {
        this.actualPlayer = player;
    }

    public Player getActualPlayer() {
        return this.actualPlayer;
    }

    public Player getMostInfluentialPlayerOnIsland(List<Player> otherPlayers, Island island) {
        Player mip = null;
        Tower tower = island.getTower();
        int actualPlayerInfluence = 0;
        int previousInfluence = 0;
        int influence = 0;

        // TODO add a feature: necessary to return null player if the players has equal influence

        if (tower != null) {
            if (actualPlayer.equals(tower.getOwner())) {
                influence++;
            }
        }

        for (Player player : otherPlayers) {
            influence = 0;
            if (tower != null) {
                if (player.equals(tower.getOwner())) {
                    influence++;
                }
            }
            for (Professor professor : player.getSchool().getProfessorsTable().getPawns()) {
                influence += island.countStudentsByColor(professor.getColor());
            }
            if (influence > previousInfluence) {
                mip = player;
            }
            previousInfluence = influence;
        }

        for (Professor professor : this.actualPlayer.getSchool().getProfessorsTable().getPawns()) {
            actualPlayerInfluence += island.countStudentsByColor(professor.getColor());
        }
        if (actualPlayerInfluence > influence) {
            mip = this.actualPlayer;
        }

        return mip;
    }

    // To be called after motherNatureMovement().
    // See "Controlling an Island" and "Conquering an Island" sections at page 6 of the rules.
    public void resolveIsland(MotherNature motherNature, List<Player> otherPlayers) {
        Island island = motherNature.getPosition();
        Player player = null;

        player = this.getMostInfluentialPlayerOnIsland(otherPlayers, island);
        Tower tower = new Tower(player.getTowerColor());
        island.setTower(tower);
    }

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
        if (steps_choice <= player.getLastAssistantCard().getSteps()) {
            motherNature.stepsToMove(steps_choice);
        }
    }

    public void extraAction() {

    }

}