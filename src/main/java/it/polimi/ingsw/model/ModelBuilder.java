package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.json.HandlerDeserializer;
import it.polimi.ingsw.json.HandlerSerializer;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.Deck;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class ModelBuilder {

    private final Map<Integer, Integer> numStudentToMoveMap = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        this.put(4, 3);
    }};

    private boolean allCharacterCards;

    public Model buildModel(InputStream inputStream) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Handler.class, new HandlerSerializer());
        gsonBuilder.registerTypeAdapter(Handler.class, new HandlerDeserializer());
        Gson gson = gsonBuilder.create();

        Model model = gson.fromJson(new InputStreamReader(inputStream), Model.class);

        Map<TowerColor, Player> towerColorPlayerMap = new HashMap<>();

        for (Player p : model.getPlayers()) {
            towerColorPlayerMap.put(p.getTowerColor(), p);
            for (Tower t : p.getSchool().getTowersBoard().getPawns()) {
                t.setOwner(p);
            }
        }

        model.resumeFromDisk();
        model.getHandler().setPlayers(model.getPlayers());
        model.getController().restoreAfterDeserialization(model);

        List<Island> islands = model.getIslands();
        for (Island island : islands) {
            island.setNextIsland(islands.get((island.getId() + 1) % islands.size()));
            island.setPrevIsland(islands.get((island.getId() + islands.size() - 1) % islands.size()));
            if (island.getTower() != null) island.getTower().setOwner(towerColorPlayerMap.get(island.getTower().getColor()));
        }

        model.getMotherNature().setPosition(islands.get(model.getMotherNature().getPosition().getId()));

        return model;
    }

    public Model buildModel(List<String> names, boolean allCharacterCards, boolean completeRule) {

        this.allCharacterCards = allCharacterCards;

        List<Island> islands = buildIslands();
        List<Cloud> clouds = buildClouds(names.size());
        StudentBag bag = buildStudentBag();

        MotherNature motherNature = new MotherNature(islands.get(new Random().nextInt(islands.size())));

        Island mnPosition = motherNature.getPosition();
        for (Island island : islands) {
            if (island.getId() != mnPosition.getId() && island.getId() != (mnPosition.getId() + 6) % 12) {
                bag.extractStudentAndMove(island);
            }
        }

        int numStudentToMove = numStudentToMoveMap.get(clouds.size());

        return new Model(buildPlayers(names,bag), islands, clouds, motherNature, buildCharacterCards(), 20 - names.size(), buildProfessorBoard(), bag, numStudentToMove, completeRule);
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
        List<AssistantCard> assistants;
        Gson gson = new Gson();

        InputStream inputStream = getClass().getResourceAsStream("/config/assistant_cards.json");
        assistants = Arrays.asList(gson.fromJson(new InputStreamReader(inputStream), AssistantCard[].class));

        return new ArrayList<>(assistants);
    }

    private List<CharacterCard> buildCharacterCards() {
        List<CharacterCard> characters;
        Gson gson = new Gson();

        InputStream inputStream = getClass().getResourceAsStream("/config/character_cards.json");
        characters = Arrays.asList(gson.fromJson(new InputStreamReader(inputStream), CharacterCard[].class));

        if (allCharacterCards) {
            return new ArrayList<>(characters);
        } else {
            List<CharacterCard> characterCards = new ArrayList<>();
            Random generator = new Random();
            int index1;
            int index2;
            int index3;

            index1 = generator.nextInt(characters.size());

            do {
                index2 = generator.nextInt(characters.size());
            } while (index1 == index2);

            do {
                index3 = generator.nextInt(characters.size());
            } while (index1 == index3 || index2 == index3);

            characterCards.add(characters.get(index1));
            characterCards.add(characters.get(index2));
            characterCards.add(characters.get(index3));

            return new ArrayList<>(characterCards);
        }

    }
}
