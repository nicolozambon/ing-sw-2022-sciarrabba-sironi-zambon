package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ThinModel {

    //Players State
    private List<String> nicknames;
    private List<SchoolSerializable> schools;
    private List<Integer> coins;
    private List<List<AssistantCard>> assistantsCards;

    //Game State
    private int coinReserve;
    private List<IslandSerializable> islands;
    private List<CloudSerializable> clouds;
    private List<CharacterCardSerializable> characterCards;
    private List<Color> professors;

    public ThinModel(Model model) {
        nicknames = model.getPlayers().stream().map(Player::getNickname).toList();
        schools = new ArrayList<>();
        coins = new ArrayList<>();
        assistantsCards = new ArrayList<>();

        coinReserve = model.getCoinReserve();
        islands = new ArrayList<>();
        clouds = new ArrayList<>();
        characterCards = new ArrayList<>();
        professors = new ArrayList<>();



        for (Player player : model.getPlayers()) {
            schools.add(player.getId(), new SchoolSerializable(player.getSchool(), player.getTowerColor()));
            coins.add(player.getId(), player.getCoins());
            assistantsCards.add(player.getId(), player.getAssistantCards());
        }

        for (Island island : model.getIslands()) {
            islands.add(island.getId(), new IslandSerializable(island));
        }

        for (Cloud cloud : model.getClouds()) {
            clouds.add(new CloudSerializable(cloud));
        }

        for (CharacterCard card : model.getCharacterCards()) {
            characterCards.add(new CharacterCardSerializable(card));
        }

        for (Professor professor : model.getProfessors()) {
            professors.add(professor.getColor());
        }

        islands.get(model.getMotherNature().getPosition().getId()).motherNaturePresence = true;

    }

    @Override
    public String toString() {
        String first = "ModelSerializable{" +
                "\n\nschools = " + schools +
                ",\n\nplayer's coin = " + coins;
        String second = ",\n\nassistantsCards = ";
        for (List<AssistantCard> ac : assistantsCards) {
            second = second + "\n" + ac;
        }

        String third = ",\n\ncoinReserve = " + coinReserve +
                ",\n\nislands = " + islands +
                ",\n\nclouds = " + clouds +
                ",\n\ncharacterCards = " + characterCards +
                ",\n\nprofessors = " + professors +
                "\n}";
        return first + second + third;
    }

    public List<Color> getEntranceByPlayerId(int playerId) {
        return new ArrayList<>(schools.get(playerId).entrance);
    }

    public int getDRByPlayerAndColor(int playerId, Color color) {
        return schools.get(playerId).diningRoom.get(color);
    }

    public int getStudentOnIslandById(int islandId) {
        int value = 0;
        for (Color color : Color.values()) {
            value += islands.get(islandId).students.get(color);
        }
        return value;
    }

    public int getStudentOnCloud(int cloudId) {
        return clouds.get(cloudId).students.size();
    }

    public int getCharacterCardCost(int characterId) {
        return characterCards.stream().filter(x -> x.id == characterId).findAny().get().cost;
    }

    public int getMNPosition() {
        for (IslandSerializable i : islands) {
            if (i.motherNaturePresence) return islands.indexOf(i);
        }
        return -1;
    }

    public int getNumPlayers() {
        return schools.size();
    }

    public int getNumIslands() {
        return islands.size();
    }

    public Map<Color, Integer> getStudentOnIsland(int id) {
        return new HashMap<>(islands.get(id).students);
    }


    private class SchoolSerializable { //A SchoolSerializable for all players.

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
        boolean towerPresence;
        TowerColor color;
        boolean motherNaturePresence;
        boolean isLinkedPrev;
        boolean isLinkedNext;

        IslandSerializable(Island island) {
            this.students = new HashMap<>();
            this.towerPresence = false;
            this.motherNaturePresence = false;
            this.isLinkedPrev = island.isUnifyPrev();
            this.isLinkedNext = island.isUnifyNext();

            for (Color color : Color.values()) {
                students.put(color, island.countStudentsByColor(color));
            }

            if (island.getTower() != null) {
                towerPresence = true;
                color = island.getTower().getColor();
            }
        }

        @Override
        public String toString() {
            return "\n{" +
                    "students = " + students +
                    ", towerPresence = " + towerPresence +
                    ", color = " + color +
                    ", motherNaturePresence = " + motherNaturePresence +
                    ", linkedPrev = " + isLinkedPrev +
                    ", linkedNext = " + isLinkedNext +
                    "}";
        }
    }

    private static class CharacterCardSerializable {

        int id;
        String effect;
        int cost;

        CharacterCardSerializable(CharacterCard card) {
            this.id = card.getId();
            this.effect = card.getEffect();
            this.cost = card.getCoins();
        }

        @Override
        public String toString() {
            return "\n{" +
                    "id = " + id +
                    ", effect = " + effect + '\'' +
                    ", cost = " + cost +
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