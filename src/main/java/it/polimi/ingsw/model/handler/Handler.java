package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.enums.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.component.*;

import java.util.*;


public class Handler {

    private List<Player> players;

    public Handler(List<Player> players) {
        this.players = players;
    }

    //TODO this method works but not much scalable for character card effects
    public void professorControl(Player currentPlayer, Color color, Board<Professor> startingProfBoard) {
        Professor professor = getProfessor(color, startingProfBoard);
        Board<Professor> professorBoard = getProfessorBoard(color, startingProfBoard);

        Player playerWithMoreStudent =
                players
                .stream()
                .max(Comparator.comparingInt(x -> getPlayersNumStudentsDR(x, color)))
                .get();

        for (Player player : players) {
            if ( !player.equals(playerWithMoreStudent) &&
                 getPlayersNumStudentsDR(player, color) == getPlayersNumStudentsDR(playerWithMoreStudent, color))
            {
                playerWithMoreStudent = null;
            }
        }

        if (playerWithMoreStudent != null) {
            playerWithMoreStudent.getSchool().setProfessor(professor, professorBoard);
        }
    }

    public void motherNatureMovement(Player currentPlayer, MotherNature motherNature, int stepsChoice) {
        if(stepsChoice > 0 && stepsChoice <= currentPlayer.getLastAssistantCard().getSteps()) {
            motherNature.stepsToMove(stepsChoice);
            resolveIsland(motherNature.getPosition());
            unifyIsland(motherNature.getPosition());
        }
    }

    void resolveIsland(Island island) {

    }

    void getMostInfluentialPlayer(Island island) {

    }

    private void unifyIsland(Island island) {
        Tower tower = island.getTower();
        if (tower != null) {

            if (    island.getNextIsland().getTower() != null &&
                    !(island.isUnifyNext()) &&
                    island.getNextIsland().getTower().getColor() == tower.getColor())
            {
                island.unifyToNext();
                island.getNextIsland().unifyToPrev();
                unifyIsland(island.getNextIsland());
            }

            if (    island.getPrevIsland().getTower() != null &&
                    !(island.isUnifyPrev()) &&
                    island.getPrevIsland().getTower().getColor() == tower.getColor())
            {
                island.unifyToPrev();
                island.getPrevIsland().unifyToNext();
                unifyIsland(island.getPrevIsland());
            }

        }
    }

    public void extraAction(int value, Model model) {
    }


    private Professor getProfessor(Color color, Board<Professor> startingProfBoard) {

        for (Professor prof : startingProfBoard.getPawns()) {
            if (prof.getColor() == color) return prof;
        }

        for (Player player : players) {
            for (Professor prof : player.getSchool().getProfessorsTable().getPawns()) {
                if (prof.getColor() == color) return prof;
            }
        }
        return null;
    }

    private Board<Professor> getProfessorBoard(Color color, Board<Professor> startingProfBoard) {

        for (Professor prof : startingProfBoard.getPawns()) {
            if (prof.getColor() == color) return startingProfBoard;
        }

        for (Player player : players) {
            for (Professor prof : player.getSchool().getProfessorsTable().getPawns()) {
                if (prof.getColor() == color) return player.getSchool().getProfessorsTable();
            }
        }
        return null;
    }

    private int getPlayersNumStudentsDR(Player player, Color color) {
        return player.getSchool().getDiningRoomByColor(color).getNumPawns();
    }

}