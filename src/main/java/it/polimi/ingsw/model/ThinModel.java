package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model representation for serialization and deserialization. Holds the state of the match of the Model that is received
 * by the client, and saved by the server
 */
public final class ThinModel {

    //Players State
    /**
     * List of nicknames of the players
     */
    private final List<String> nicknames;

    /**
     * List of Wizards
     */
    private final List<Wizard> wizards;

    /**
     * List of Schools, serializable
     */
    private final List<SchoolSerializable> schools;

    /**
     * List of coins for all players
     */
    private final List<Integer> coins;

    /**
     * List of Assistant Cards for each player
     */
    private final Map<Integer, List<AssistantCard>> assistantsCards;

    /**
     * List of last played assistant cards
     */
    private final List<AssistantCard> lastPlayedAssistantCard;

    //Game State

    /**
     * Size of the coin reserve of the match
     */
    private final int coinReserve;

    /**
     * State of all the islands, serializable
     */
    private final List<IslandSerializable> islands;

    /**
     * State of all the clouds, serializable
     */
    private final List<CloudSerializable> clouds;

    /**
     * List of the Character Cards for the match
     */
    private final List<CharacterCard> characterCards;

    /**
     * List of professors
     */
    private final List<Color> professors;

    /**
     * Is the match being played with the complete rules
     */
    private final boolean completeRule;

    /**
     * Constructor, build the ThinModel representation, given the full Model
     * @param model state of the game to represent
     */
    public ThinModel(Model model) {
        nicknames = model.getPlayers().stream().map(Player::getNickname).toList();
        schools = new ArrayList<>();
        coins = new ArrayList<>();
        assistantsCards = new HashMap<>();
        wizards = new ArrayList<>();
        lastPlayedAssistantCard = new ArrayList<>();

        coinReserve = model.getCoinReserve();
        islands = new ArrayList<>();
        clouds = new ArrayList<>();
        characterCards = new ArrayList<>(model.getCharacterCards());
        professors = new ArrayList<>();

        completeRule = model.isCompleteRule();

        for (Player player : model.getPlayers()) {
            schools.add(player.getId(), new SchoolSerializable(player.getSchool(), player.getTowerColor()));
            coins.add(player.getId(), player.getCoins());
            assistantsCards.put(player.getId(), player.getAssistantCards());
            wizards.add(player.getId(), player.getWizard());
            lastPlayedAssistantCard.add(player.getId(), player.getLastAssistantCard());
        }

        for (Island island : model.getIslands()) {
            islands.add(island.getId(), new IslandSerializable(island));
        }

        for (Cloud cloud : model.getClouds()) {
            clouds.add(new CloudSerializable(cloud));
        }

        for (Professor professor : model.getProfessors()) {
            professors.add(professor.getColor());
        }

        islands.get(model.getMotherNature().getPosition().getId()).motherNaturePresence = true;
    }

    /**
     * @param islandId ID of the Island to select
     * @return the number of students on a given island
     */
    public int getNumStudentOnIsland(int islandId) {
        int value = 0;
        for (Color color : Color.values()) {
            value += islands.get(islandId).students.get(color);
        }
        return value;
    }

    /**
     * @return the number of clouds in the match
     */
    public int getNumClouds() {
        return clouds.size();
    }

    /**
     * @param cloudId ID of the Cloud to select
     * @return an ArrayList of Students present on a given cloud
     */
    public List<Color> getStudentOnCloud(int cloudId) {
        return new ArrayList<>(clouds.get(cloudId).students);
    }

    /**
     * @return the ID of the island on which Mother Nature is on
     */
    public int getMNPosition() {
        for (IslandSerializable i : islands) {
            if (i.motherNaturePresence) return islands.indexOf(i);
        }
        return -1;
    }

    /**
     * @return an ArrayList of the nicknames of the players
     */
    public List<String> getNicknames() {
        return new ArrayList<>(nicknames);
    }

    /**
     * @param id ID of the Island selected
     * @return a Map with each color of the students and the relative number of pawns on the given island
     */
    public Map<Color, Integer> getStudentOnIsland(int id) {
        return new HashMap<>(islands.get(id).students);
    }

    /**
     * @return The number of Islands
     */
    public int getNumIslands() {
        return islands.size();
    }

    /**
     * @return an ArrayList of the selection of the wizards, ordered by Player ID
     */
    public List<Wizard> getWizards() {
        return new ArrayList<>(wizards);
    } //ordered by player id

    /**
     * @return an ArrayList of the Character Cards for the match
     */
    public List<CharacterCard> getCharacterCards() {
        return new ArrayList<>(characterCards);
    }

    /**
     * @param islandId ID of the island selected
     * @return the color of the tower on the given island
     */
    public TowerColor getTowerColorOnIsland(int islandId) {
        return islands.get(islandId).towerColor;
    }

    /**
     * @param playerId ID of the player selected
     * @return an ArrayList of the students present on a given player's school entrance
     */
    public List<Color> getEntranceByPlayer(int playerId) {
        return new ArrayList<>(schools.get(playerId).entrance);
    }

    /**
     * @param playerId ID of the player selected
     * @return a Map of the students present on a given player's school dining room. For each color, the number of the pawns present.
     */
    public Map<Color, Integer> getDiningRoomByPlayer(int playerId) {
        return new HashMap<>(schools.get(playerId).diningRoom);
    }

    /**
     * @param playerId ID of the player selected
     * @return the number of towers present in the school of the given player
     */
    public int getNumTowerByPlayer(int playerId) {
        return schools.get(playerId).numTowers;
    }

    /**
     * @param playerId ID of the player selected
     * @return the color of the towers for the given player
     */
    public TowerColor getTowerColorByPlayer(int playerId) {
        return schools.get(playerId).towerColor;
    }

    /**
     * @param islandId ID of the island selected
     * @return true if the given island is linked to the next one
     */
    public boolean isIslandLinkedNext(int islandId) {
        return islands.get(islandId).isLinkedNext;
    }

    /**
     * @param playerId ID of the player selected
     * @return an ArrayList of the colors of the professors held by the given player
     */
    public List<Color> getProfessorsByPlayer(int playerId) {
        return new ArrayList<>(schools.get(playerId).profTable);
    }

    /**
     * @param playerId ID of the player selected
     * @return an ArrayList of the Assistant Cards of the given player
     */
    public List<AssistantCard> getAssistantCardsByPlayer(int playerId) {
        return new ArrayList<>(assistantsCards.get(playerId));
    }

    /**
     * @param playerId ID of the player selected
     * @return the last Assistant Card played by the given player
     */
    public AssistantCard getLastAssistantCardByPlayer(int playerId) {
        return lastPlayedAssistantCard.get(playerId);
    }

    /**
     * @param playerId ID of the player selected
     * @return the number of coins for the given player
     */
    public int getCoinByPlayer(int playerId) {
        return coins.get(playerId);
    }

    /**
     * @return the size of the Coin Reserve
     */
    public int getCoinReserve() {
        return coinReserve;
    }

    /**
     * @return true if the match is being played with the complete rules
     */
    public boolean isCompleteRule() {
        return completeRule;
    }

    /**
     * Class for representing the schools in a serializable manner
     */
    private static class SchoolSerializable { //A SchoolSerializable for all players.

        Map<Color, Integer> diningRoom = new HashMap<>();
        List<Color> entrance = new ArrayList<>(); //Ordered by #Position of Entrance in the board.
        List<Color> profTable = new ArrayList<>();
        int numTowers;
        TowerColor towerColor;

        SchoolSerializable(School school, TowerColor towerColor) {
            for (Color color : Color.values()) {
                diningRoom.put(color, school.getDiningRoomByColor(color).getNumPawns());
            }
            for (Student student : school.getEntrance().getPawns()) {
                entrance.add(student.getColor());
            }
            numTowers = school.getTowersBoard().getNumPawns();
            this.towerColor = towerColor;
            for (Professor professor : school.getProfessorsTable().getPawns()) {
                profTable.add(professor.getColor());
            }
        }
    }

    /**
     * Class for representing the islands in a serializable manner
     */
    private static class IslandSerializable {

        Map<Color, Integer> students;

        TowerColor towerColor;
        boolean motherNaturePresence;
        boolean isLinkedPrev;
        boolean isLinkedNext;

        IslandSerializable(Island island) {
            this.students = new HashMap<>();
            this.motherNaturePresence = false;
            this.isLinkedPrev = island.isUnifyPrev();
            this.isLinkedNext = island.isUnifyNext();

            for (Color color : Color.values()) {
                students.put(color, island.countStudentsByColor(color));
            }

            if (island.getTower() != null) {
                towerColor = island.getTower().getColor();
            }
        }
    }

    /**
     * Class for representing the clouds in a serializable manner
     */
    private static class CloudSerializable {

        List<Color> students;

        CloudSerializable(Cloud cloud) {
            this.students = new ArrayList<>();
            for (Student student : cloud.getPawns()) {
                students.add(student.getColor());
            }
        }
    }
}