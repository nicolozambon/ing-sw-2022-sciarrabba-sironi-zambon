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

/**
 * ModelBuilder class populates a Model instance with the correct number of pawns for each player.
 * If a match is resumed from a saved JSON file, this class correctly setups the Model instance
 */
public class ModelBuilder {
    /**
     * Maps the number of students to move with the number of players
     */
    private final Map<Integer, Integer> numStudentToMoveMap = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        this.put(4, 3);
    }};

    /**
     * Boolean, true if the ModelBuilder has to load all the Character Cards
     */
    private boolean allCharacterCards;

    /**
     * Builds the model using a GSON Builder from a JSON file, resuming from a saved match
     * @param inputStream Input containing the JSON file
     * @return
     */
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

    /**
     * Builds a new Model, populating it with all the right pawns, according to the rules and settings given
     * @param names List of nicknames
     * @param allCharacterCards Boolean, true if the game supports all the Character Cards
     * @param completeRule Boolean, true if the match is to be created with the complete rules
     * @return
     */
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

    /**
     * Builds the players instances, given their nicknames and the student bag to get the student pawns from
     * @param names List of Nicknames for the players
     * @param bag Student Bag to extract the pawns from
     * @return
     */
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

    /**
     * Builds the entrance of the schools, extracting from the student bag "num" pawns and returning them
     * @param num Number of students to extract
     * @param bag Student Bag to extract the pawns from
     * @return an ArrayList containing the pawns
     */
    private List<Student> buildEntrance(int num, StudentBag bag) {
        Board<Student> students = new Board<>();
        for (int i = 0; i < num; i++) {
            bag.extractStudentAndMove(students);
        }
        return students.getPawns();
    }

    /**
     * Builds a set of towers of the desired color
     * @param num Number of towers to generate
     * @param color Color of the towers to generate
     * @return an ArrayList containing the created Tower objects
     */
    private List<Tower> buildTowers(int num, TowerColor color) {
        List<Tower> towers = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            towers.add(new Tower(color));
        }
        return new ArrayList<>(towers);
    }

    /**
     * Builds a new Student Bag, containing all the pawns of each color
     * @return a new Student Bag
     */
    private StudentBag buildStudentBag() {
        List<Student> students = new ArrayList<>();

        for (Color c : Color.values()) {
            for (int i = 0; i < 26; i++) {
                students.add(new Student(c));
            }
        }
        return new StudentBag(students);
    }

    /**
     * Builds all the islands, linking them together accordingly to their position
     * @return an ArrayList containing all the islands
     */
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

    /**
     * Builds a Starting Professor's board, containing a professor for each color.
     * @return a new Board of professors
     */
    private Board<Professor> buildProfessorBoard() {
        List<Professor> professors = new ArrayList<>();
        for (Color c : Color.values()) {
            professors.add(new Professor(c));
        }
        return new Board<>(professors);
    }

    /**
     * Builds all the cloud instances
     * @param num Number of clouds to create
     * @return an ArrayList containing all the created clouds
     */
    private List<Cloud> buildClouds(int num) {
        List<Cloud> clouds = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            clouds.add(new Cloud(new ArrayList<>()));
        }
        return new ArrayList<>(clouds);
    }

    /**
     * Builds all the Assistant Card instances, reading their values from a JSON file
     * @return an ArrayList containing all the Assistant Cards created
     */
    private List<AssistantCard> buildAssistantCards() {
        List<AssistantCard> assistants;
        Gson gson = new Gson();

        InputStream inputStream = getClass().getResourceAsStream("/config/assistant_cards.json");
        assistants = Arrays.asList(gson.fromJson(new InputStreamReader(inputStream), AssistantCard[].class));

        return new ArrayList<>(assistants);
    }

    /**
     * Builds all the Character Card instances for the match, reading their values from a JSON file and choosing them randomly
     * @return an ArrayList containing all the Character Cards created
     */
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
