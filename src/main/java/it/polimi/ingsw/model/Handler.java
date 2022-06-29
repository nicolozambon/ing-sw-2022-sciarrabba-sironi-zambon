package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.MotherNatureStepsException;
import it.polimi.ingsw.exceptions.WinnerException;

import java.util.*;

/**
 * Class to handle part of business logic that depends on CharacterCard played
 */
public class Handler {

    /**
     * Category of the handler
     */
    private final String category;

    /**
     * List of Player
     * @see Player
     */
    protected transient List<Player> players;

    /**
     * Default constructor with list of players
     * @param players the list of players
     */
    public Handler(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.category = "default";
    }

    /**
     * Constructor with custom category, useful in subclasses
     * @param players the list of players
     * @param category the category to be assigned
     */
    protected Handler(List<Player> players, String category) {
        this.players = new ArrayList<>(players);
        this.category = category;
    }

    /**
     * Find and assign the professor to the right player
     * @param currentPlayer the current player
     * @param color the color of the professor
     * @param startingProfBoard the starting board where every professor stay at the beginning
     */
    protected void professorControl(Player currentPlayer, Color color, Board<Professor> startingProfBoard) {
        Professor professor = getProfessor(color, startingProfBoard);
        Board<Professor> professorBoard = getProfessorBoard(color, startingProfBoard);

        List<Player> players =
                this.players
                .stream()
                .sorted(Comparator.comparingInt(x -> getNumStudentsDR(x, color)))
                .toList();

        int last = players.size() - 1;

        if (getNumStudentsDR(players.get(last), color) > getNumStudentsDR(players.get(last - 1), color)) {
            players.get(last).getSchool().setProfessor(professor, professorBoard);
        }
    }

    /**
     * Move mother nature by the specified steps
     * @param currentPlayer the current player
     * @param motherNature the mother nature to be moved
     * @param stepsChoice the steps chosen by the player
     * @throws MotherNatureStepsException thrown if the amount of steps is invalid
     * @throws WinnerException thrown if there is a winner
     */
    protected void motherNatureMovement(Player currentPlayer, MotherNature motherNature, int stepsChoice) throws MotherNatureStepsException, WinnerException {
        Player mostInfluentialPlayer = null;
        if(stepsChoice > 0 && stepsChoice <= currentPlayer.getLastAssistantCard().getSteps()) {
            motherNature.stepsToMove(stepsChoice);
            mostInfluentialPlayer = getMostInfluentialPlayer(currentPlayer, motherNature.getPosition());
            if (mostInfluentialPlayer != null) {
                switchTowers(motherNature.getPosition(), mostInfluentialPlayer);
                unifyIsland(motherNature.getPosition());
            }
        } else {
            throw new MotherNatureStepsException();
        }
    }

    /**
     * Returns the influence of the specified player on the specified island or group of islands
     * @param island the island
     * @param player the player
     * @return the influence of the specified player on the specified island or group of islands
     */
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

    /**
     * Returns the influence of the specified player on the specified island
     * @param island the island
     * @param player the player
     * @param influence the influence of the player in islands linked to the specified
     * @return the influence of the specified player on the specified island
     */
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
     * Returns the most influential player on the specified island or group of islands
     * @param currentPlayer the current player who has moved mother nature
     * @param island the Island where mother nature has ended her movement
     * @return the most influential player on the specified island, the one who builds the tower
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
     * @param island the Island where switch towers
     * @param newOwner the new Player that has more influence on the specified island
     * @see Island
     * @see Player
     */
    protected void switchTowers (Island island, Player newOwner) throws WinnerException {
        while (island.isUnifyPrev()) {
            island = island.getPrevIsland();
        }

        island.setTower(newOwner);

        while (island.isUnifyNext()) {
            island = island.getNextIsland();
            island.setTower(newOwner);
        }
    }

    /**
     * Unifies islands recursively
     * @param island the first island to begin unify
     */
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

    /**
     * Method to be overridden in subclasses
     * @param currentPlayer the current player
     * @param model the model of the game
     * @param values various argument
     * @throws Exception thrown if there is some error
     */
    protected void extraAction(Player currentPlayer, Model model, int ... values) throws Exception {
        // @Override
    }


    /**
     * Returns the professor of the specified color wherever it is
     * @param color the color of the professor
     * @param startingProfBoard the starting board where every professor stay at the beginning
     * @return
     */
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

    /**
     * Returns the board where the professor of the specified color is
     * @param color the color of the professor
     * @param startingProfBoard the starting board where every professor stay at the beginning
     * @return
     */
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

    /**
     * Returns the number of student in the dining room for the specified player and color
     * @param player the player
     * @param color the color of the dining room
     * @return
     */
    protected int getNumStudentsDR(Player player, Color color) {
        return player.getSchool().getDiningRoomByColor(color).getNumPawns();
    }

    /**
     * Returns the id of the CharacterCard associated to the Handler
     * @return
     */
    public int getCardId() {
        return -1;
    }

    /**
     * Returns the category
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the list of players
     * @param players the new list of players
     */
    protected void setPlayers(List<Player> players) {
        this.players = new ArrayList<>(players);
    }
}