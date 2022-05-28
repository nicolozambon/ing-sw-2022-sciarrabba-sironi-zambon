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

public final class ThinModel {

    //Players State
    private final List<String> nicknames;
    private final List<Wizard> wizards;
    private final List<SchoolSerializable> schools;
    private final List<Integer> coins;
    private final Map<Integer, List<AssistantCard>> assistantsCards;
    private final List<AssistantCard> lastPlayedAssistantCard;

    //Game State
    private final int coinReserve;
    private final List<IslandSerializable> islands;
    private final List<CloudSerializable> clouds;
    private final List<CharacterCard> characterCards;
    private final List<Color> professors;

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

    public List<Color> getEntranceByPlayerId(int playerId) {
        return new ArrayList<>(schools.get(playerId).entrance);
    }

    public int getStudentOnIslandById(int islandId) {
        int value = 0;
        for (Color color : Color.values()) {
            value += islands.get(islandId).students.get(color);
        }
        return value;
    }

    public int getNumClouds() {
        return clouds.size();
    }

    public List<Color> getStudentOnCloud(int cloudId) {
        return new ArrayList<>(clouds.get(cloudId).students);
    }

    public int getMNPosition() {
        for (IslandSerializable i : islands) {
            if (i.motherNaturePresence) return islands.indexOf(i);
        }
        return -1;
    }

    public List<String> getNicknames() {
        return new ArrayList<>(nicknames);
    }

    public Map<Color, Integer> getStudentOnIsland(int id) {
        return new HashMap<>(islands.get(id).students);
    }

    public int getNumIslands() {
        return islands.size();
    }

    public List<Wizard> getWizards() {
        return new ArrayList<>(wizards);
    }

    public List<CharacterCard> getCharacterCards() {
        return new ArrayList<>(characterCards);
    }

    public TowerColor getTowerColorOnIsland(int islandId) {
        return islands.get(islandId).towerColor;
    }

    public List<Color> getEntranceById(int playerId) {
        return new ArrayList<>(schools.get(playerId).entrance);
    }

    public Map<Color, Integer> getDiningRoomById(int playerId) {
        return new HashMap<>(schools.get(playerId).diningRoom);
    }

    public int getNumTowerByPlayer(int playerId) {
        return schools.get(playerId).numTowers;
    }

    public TowerColor getTowerColorByPlayer(int playerId) {
        return schools.get(playerId).towerColor;
    }

    public boolean isIslandLinkedNext(int islandId) {
        return islands.get(islandId).isLinkedNext;
    }

    public List<Color> getProfessorsByPlayer(int playerId) {
        return new ArrayList<>(schools.get(playerId).profTable);
    }

    public List<AssistantCard> getAssistantCardsByPlayer(int playerId) {
        return new ArrayList<>(assistantsCards.get(playerId));
    }

    public AssistantCard getLastAssistantCardByPlayer(int playerId) {
        return lastPlayedAssistantCard.get(playerId);
    }

    public int getCoinByPlayer(int playerId) {
        return coins.get(playerId);
    }

    public int getCoinReserve() {
        return coinReserve;
    }

    @Override
    public String toString() {
        String first = "ModelSerializable{" +
                "\n\nschools = " + schools +
                ",\n\nplayer's coin = " + coins;
        String second = ",\n\nassistantsCards = ";
        for (Integer i : assistantsCards.keySet()) {
            second = second + "\n" + assistantsCards.get(i);
        }
        second = second + "\n" + lastPlayedAssistantCard;
        String third = ",\n\ncoinReserve = " + coinReserve +
                ",\n\nislands = " + islands +
                ",\n\nclouds = " + clouds +
                ",\n\ncharacterCards = " + characterCards +
                ",\n\nprofessors = " + professors +
                "\n}";
        return first + second + third;
    }

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

        @Override
        public String toString() {
            return "\n{" +
                    "diningRoom = " + diningRoom +
                    ", entrance = " + entrance +
                    ", profTable = " + profTable +
                    ", numTowers = " + numTowers +
                    ", towerColor = " + towerColor +
                    "}";
        }
    }


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

        @Override
        public String toString() {
            return "\n{" +
                    "students = " + students +
                    ", color = " + towerColor +
                    ", motherNaturePresence = " + motherNaturePresence +
                    ", linkedPrev = " + isLinkedPrev +
                    ", linkedNext = " + isLinkedNext +
                    "}";
        }
    }

    private static class CloudSerializable {

        List<Color> students;

        CloudSerializable(Cloud cloud) {
            this.students = new ArrayList<>();
            for (Student student : cloud.getPawns()) {
                students.add(student.getColor());
            }
        }

        @Override
        public String toString() {
            return "\n{" +
                    "students = " + students +
                    "}";
        }
    }
}