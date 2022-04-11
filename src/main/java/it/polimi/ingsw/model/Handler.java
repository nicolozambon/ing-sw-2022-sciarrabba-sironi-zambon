package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.*;

import java.util.*;


public class Handler {

    List<Player> players;

    protected Handler(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    protected void professorControl(Player currentPlayer, Color color, Board<Professor> startingProfBoard) {
        Professor professor = getProfessor(color, startingProfBoard);
        Board<Professor> professorBoard = getProfessorBoard(color, startingProfBoard);

        List<Player> players =
                this.players
                .stream()
                .sorted(Comparator.comparingInt(x -> getPlayersNumStudentsDR(x, color)))
                .toList();

        int last = players.size() - 1;

        if (getPlayersNumStudentsDR(players.get(last), color) > getPlayersNumStudentsDR(players.get(last - 1), color)) {
            players.get(last).getSchool().setProfessor(professor, professorBoard);
        }
    }

    protected void motherNatureMovement(Player currentPlayer, MotherNature motherNature, int stepsChoice) {
        Player mostInfluentialPlayer = null;
        if(stepsChoice > 0 && stepsChoice <= currentPlayer.getLastAssistantCard().getSteps()) {
            motherNature.stepsToMove(stepsChoice);
            mostInfluentialPlayer = getMostInfluentialPlayer(currentPlayer, motherNature.getPosition());
            if (mostInfluentialPlayer != null) {
                switchTowers(motherNature.getPosition(), mostInfluentialPlayer);
                unifyIsland(motherNature.getPosition());
            }
        }
    }

    protected int resolveIsland(Island island, Player player) {
        int influence = 0;

        while (island.isUnifyPrev()) {
            island = island.getPrevIsland();
        }

        influence += resolveIslandHelper(island, player, influence);

        while (island.isUnifyNext()) {
            island = island.getNextIsland();
            influence += resolveIslandHelper(island, player, influence);
        }
        return influence;
    }

    protected int resolveIslandHelper (Island island, Player player, int influence) {
        for (Professor professor : player.getSchool().getProfessorsTable().getPawns()) {
            influence += island.countStudentsByColor(professor.getColor());
        }

        if (island.getTower() != null && island.getTower().getOwner().equals(player)) {
            influence += 1;
        }
        return influence;
    }

    /**
     * Calculates for every player the influence on each (group of) island.
     * @param currentPlayer The current player who has moved mother nature
     * @param island The Island where mother nature has ended her movement
     * @return The most influential player on the specified island, the one who builds the tower
     */
    protected Player getMostInfluentialPlayer(Player currentPlayer, Island island) {
        Map<Player, Integer> playerInfluence = new HashMap<>();
        int maxInfluence = -1;
        Player playerMaxInfluence = null;
        boolean isValid = false;

        for (Player player : players) {
            playerInfluence.put(player, resolveIsland(island, player));
        }

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

    /**
     * Switches the ownership of all island linked together, changing the towers.
     * @param island
     * @param newOwner
     */
    protected void switchTowers (Island island, Player newOwner) {
        while (island.isUnifyPrev()) {
            island = island.getPrevIsland();
        }

        island.setTower(newOwner);

        while (island.isUnifyNext()) {
            island = island.getNextIsland();
            island.setTower(newOwner);
        }
    }

    protected void unifyIsland(Island island) {
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

    protected void extraAction(Player currentPlayer, Model model, int ... values) {
        // @Override
    }


    protected Professor getProfessor(Color color, Board<Professor> startingProfBoard) {

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

    protected Board<Professor> getProfessorBoard(Color color, Board<Professor> startingProfBoard) {

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

    protected int getPlayersNumStudentsDR(Player player, Color color) {
        return player.getSchool().getDiningRoomByColor(color).getNumPawns();
    }

}