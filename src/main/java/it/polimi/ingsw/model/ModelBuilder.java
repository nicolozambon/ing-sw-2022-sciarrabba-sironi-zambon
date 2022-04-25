package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterCardFactory;
import it.polimi.ingsw.model.card.Deck;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class ModelBuilder {

    private final Map<Integer, Integer> numStudentToMoveMap = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        this.put(4, 3);
    }};

    public Model buildModel(List<String> names) {

        List<Island> islands = buildIslands();
        List<Cloud> clouds = buildClouds(names.size());
        StudentBag bag = buildStudentBag();

        MotherNature motherNature = new MotherNature(islands.get(new Random().nextInt(islands.size())));

        for (Island island : islands) {
            if (island.getId() != motherNature.getPosition().getId()) {
                bag.extractStudentAndMove(island);
                bag.extractStudentAndMove(island);
            }
        }

        int numStudentToMove = numStudentToMoveMap.get(clouds.size());

        return new Model(buildPlayers(names,bag), islands, clouds, motherNature, buildCharacterCards(), 20, buildProfessorBoard(), bag, numStudentToMove);
    }

    private List<Player> buildPlayers(List<String> names, StudentBag bag) {
        List<Player> players = new ArrayList<>();
        List<AssistantCard> assistants = buildAssistantCards();

        int id = 0;
        int numEntrance = 0;
        int numTower = 0;

        if (names.size() == 2) {
            numEntrance = 7;
            numTower = 8;
        } else if (names.size() == 3) {
            numEntrance = 9;
            numTower = 6;
        }

        for (String name : names) {
            Player p = new Player(id, name, buildEntrance(numEntrance, bag), buildTowers(numTower, TowerColor.values()[id]), new Deck<>(assistants));
            for (Tower tower : p.getSchool().getTowersBoard().getPawns()) {
                tower.setOwner(p);
            }
            players.add(p);
            id++;
        }

        return new ArrayList<>(players);
    }

    private List<Student> buildEntrance(int num, StudentBag bag) {
        Board<Student> students = new Board<>();
        for (int i = 0; i < num; i++) {
            bag.extractStudentAndMove(students);
        }
        return students.getPawns();
    }

    private List<Tower> buildTowers(int num, TowerColor color) {
        List<Tower> towers = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            towers.add(new Tower(color));
        }
        return new ArrayList<>(towers);
    }

    private StudentBag buildStudentBag() {
        List<Student> students = new ArrayList<>();

        for (Color c : Color.values()) {
            for (int i = 0; i < 26; i++) {
                students.add(new Student(c));
            }
        }
        return new StudentBag(students);
    }

    private List<Island> buildIslands() {
        List<Island> islands = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            islands.add(new Island(i));
        }
        for (Island island : islands) {
            island.setNextIsland(islands.get((island.getId() + 1) % islands.size()));
            island.setPrevIsland(islands.get((island.getId() + islands.size() - 1) % islands.size()));
        }

        return new ArrayList<>(islands);
    }

    private Board<Professor> buildProfessorBoard() {
        List<Professor> professors = new ArrayList<>();
        for (Color c : Color.values()) {
            professors.add(new Professor(c));
        }
        return new Board<>(professors);
    }

    private List<Cloud> buildClouds(int num) {
        List<Cloud> clouds = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            clouds.add(new Cloud(new ArrayList<>()));
        }
        return new ArrayList<>(clouds);
    }

    private List<AssistantCard> buildAssistantCards() {
        List<AssistantCard> assistants = new ArrayList<>();
        try {
            Gson gson = new Gson();
            assistants = Arrays.asList(gson.fromJson(new FileReader("src/main/resources/config/assistant_cards.json"), AssistantCard[].class));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>(assistants);
    }

    private List<CharacterCard> buildCharacterCards() {
        List<CharacterCard> characters = new ArrayList<>();
        try {
            Gson gson = new Gson();
            characters = Arrays.asList(gson.fromJson(new FileReader("src/main/resources/config/character_cards.json"), CharacterCard[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CharacterCardFactory factory = new CharacterCardFactory();
        List<CharacterCard> characterCards = new ArrayList<>();
        Random generator = new Random();
        int index;

        for (int i = 0; i < 3; i++) {
            index = generator.nextInt(characters.size());
            characterCards.add(factory.setSubclass(characters.get(index)));
        }

        return new ArrayList<>(characters); //TODO Only for testing purpose!
        //return new ArrayList<>(characterCards);
    }
}
