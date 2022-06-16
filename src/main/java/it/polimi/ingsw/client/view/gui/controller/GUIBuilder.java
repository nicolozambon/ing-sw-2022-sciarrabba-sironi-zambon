package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.ThinModel;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.*;

public class GUIBuilder {

    private final Map<Integer, Integer> idMap;

    private final int id;

    private Map<Integer, Map<String, Map<String, ImageView>>> schools = null;
    private Map<Integer, Map<String, Map<String, Node>>> islands = null;
    private Map<Integer, Map<String, Node>> clouds = null;
    private List<Node> assistantCards;
    private List<ImageView> lastPlayedAssistantCards;
    private List<Text> characterCardsCost;
    private final Scene scene;
    private final Integer playersNumber;

    public GUIBuilder(ThinModel model, Scene scene, String nickname) {
        this.scene = scene;

        //Map from 'real' id to 'gui' id
        idMap = new HashMap<>();
        List<String> nicknames = model.getNicknames();
        id = nicknames.indexOf(nickname);
        idMap.put(id, 0);
        for (int i = 1; i < nicknames.size(); i++) {
            int nextPlayerId = (id + i) % nicknames.size();
            idMap.put(nextPlayerId, i);
        }
        this.playersNumber = idMap.size();

        if (playersNumber == 2) {
            scene.lookup("#board2").setVisible(false);
            scene.lookup("#nickname2").setVisible(false);
            scene.lookup("#assistantNickname2").setVisible(false);
        }

        this.defineLastPlayedAssistantCards(model.getNicknames());
        this.defineIslandsMap();
        this.defineSchoolsMap(model);
        this.defineCloudsMap();
        this.defineAssistantsCards();
        if (model.isCompleteRule()) this.defineCharacterCards(model.getCharacterCards());
    }

    protected void updateGUI(ThinModel model) {
        setupAssistantCards(model.getAssistantCardsByPlayer(id));
        setupIslands(model);
        setupSchools(model);
        setupClouds(model);
        setupLastPlayedAssistantCard(model);
        setupCoins(model);
        if (model.isCompleteRule()) setupCharacterCardsCost(model.getCharacterCards());
    }

    private void setupCharacterCardsCost(List<CharacterCard> cards) {
        for (int i = 0; i < 3; i++) {
            CharacterCard card = cards.get(i);
            String string;
            if (card.getCoins() < 10) string = "x0" + card.getCoins();
            else string = "x" + card.getCoins();
            characterCardsCost.get(i).setText(string);
        }
    }

    private void setupCoins(ThinModel model) {
        for(Integer i : idMap.keySet()) {
            Text coin = (Text) scene.lookup("#coin" + idMap.get(i));
            int value = model.getCoinByPlayer(i);
            String string;
            if (value < 10) string = "x0" + value;
            else string = "x" + value;
            coin.setText(string);
        }

        Text coinReserve = (Text) scene.lookup("#coinReserve");
        int value = model.getCoinReserve();
        String string;
        if (value < 10) string = "x0" + value;
        else string = "x" + value;
        coinReserve.setText(string);
    }

    private void setupLastPlayedAssistantCard(ThinModel model) {
        for(Integer i : idMap.keySet()) {
            if (model.getLastAssistantCardByPlayer(i) != null) {
                Image image = new Image(getAssistantCardPath(model.getLastAssistantCardByPlayer(i).getValue()));
                lastPlayedAssistantCards.get(idMap.get(i)).setImage(image);
                lastPlayedAssistantCards.get(idMap.get(i)).setVisible(true);
            }
        }
    }

    private void setupAssistantCards(List<AssistantCard> cards) {
        for (Node assistantCard : assistantCards) {
            assistantCard.getStyleClass().clear();
            assistantCard.getStyleClass().add("cardInvalid");
            assistantCard.setDisable(true);
        }
        for (AssistantCard card : cards) {
            Node node = assistantCards.get(card.getValue() - 1);
            node.getStyleClass().clear();
            node.getStyleClass().add("cardValid");
            node.setDisable(false);
        }
    }

    private void setupSchools(ThinModel model) {
        int maxEntrance = 7;

        if (model.getNicknames().size() == 3) {
            maxEntrance = 9;
        }

        for (int i = 0; i < model.getNicknames().size(); i++ ) {

            // Add towers
            for (ImageView tower : this.getTowerOnSchool(i)) {
                tower.setVisible(this.getTowerOnSchool(i).indexOf(tower) < model.getNumTowerByPlayer(i));
            }

            // Add professors
            for (Color professorColor : Color.values()) {
                ImageView professor = this.getProfessorOnSchool(i, professorColor);
                professor.setVisible(model.getProfessorsByPlayer(i).contains(professorColor));
            }

            // Add dining room
            for (Color color : Color.values()) {
                for (int j = 0; j < 10; j++) {
                    ImageView student = this.getStudentInDiningRoom(i, j, color);
                    student.setVisible(j < model.getDiningRoomByPlayer(i).get(color));
                }
            }

            // Add entrance
            for (int j = 0; j < maxEntrance; j++) {
                ImageView student = this.getStudentInEntrance(i, j);
                if (j < model.getEntranceByPlayer(i).size()) {
                    String path = this.getStudentPath(model.getEntranceByPlayer(i).get(j));
                    Image studentPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                    student.setImage(studentPawn);
                    student.setVisible(true);
                } else {
                    student.setVisible(false);
                }
            }

        }
    }

    private void setupIslands(ThinModel model) {
        for (int i=0; i<12; i++) {

            // Show mother nature
            ImageView motherNature = this.getMotherNature(i);
            motherNature.setDisable(false);
            motherNature.setVisible(model.getMNPosition() == i);

            // Show students
            for (Color color : Color.values()) {
                Integer numberOfStudents = model.getStudentOnIsland(i).get(color);
                Text label = this.getStudentLabel(i, color);
                ImageView pawn = this.getStudentPawn(i, color);
                if (numberOfStudents > 0) {
                    // Set label
                    label.setText("x" + numberOfStudents);
                    // Show pawn
                    pawn.setVisible(true);
                } else {
                    label.setText(null);
                    pawn.setVisible(false);
                }
            }

            // Show bridge
            ImageView bridge = this.getBridgeOnIsland(i);
            bridge.setVisible(model.isIslandLinkedNext(i));

            // Show tower
            TowerColor towerColor = model.getTowerColorOnIsland(i);
            ImageView tower = this.getTowerOnIsland(i);
            if (towerColor != null) {
                Image towerImage = new Image(this.getTowerPath(towerColor));
                tower.setImage(towerImage);
                tower.getStyleClass().clear();
                tower.getStyleClass().add(towerColor.toString().toLowerCase() + "Tower");
                tower.setVisible(true);
            } else {
                tower.setVisible(false);
            }

            // TODO add elements here

        }
    }

    private void setupClouds(ThinModel model) {
        for (Integer i : clouds.keySet()) {
            List<Color> students = model.getStudentOnCloud(i);
            Collection<Node> studentsImages = clouds.get(i).values();
            for (Node node : studentsImages) {
                if (students.isEmpty()) node.setVisible(false);
                else {
                    Color c = students.remove(0);
                    ImageView studentImageView = (ImageView) node;
                    studentImageView.setImage(new Image(getStudentPath(c)));
                    node.setVisible(true);
                }
            }
        }
    }

    private void defineCharacterCards(List<CharacterCard> cards) {
        characterCardsCost = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            characterCardsCost.add(defineCharacterCard(cards.get(i), i));
        }
    }

    private Text defineCharacterCard(CharacterCard card, int id) {
        ImageView imageView = (ImageView) scene.lookup("#" + getCharacterCardSelectorID(id, false, false, false));
        ImageView mainImageView = (ImageView) scene.lookup("#" + getCharacterCardSelectorID(id, false, false, true));
        Text text = (Text) scene.lookup("#" + getCharacterCardSelectorID(id, false, true, false));
        Text cost = (Text) scene.lookup("#" + getCharacterCardSelectorID(id, true, false, false));

        Bindings.bindBidirectional(imageView.imageProperty(), mainImageView.imageProperty());
        imageView.setImage(new Image(getCharacterCardPath(card.getId())));

        text.setText(card.getEffect());

        String string;
        if (card.getCoins() < 10) string = "x0" + card.getCoins();
        else string = "x" + card.getCoins();
        cost.setText(string);

        return cost;
    }

    private void defineLastPlayedAssistantCards(List<String> nicknames) {
        lastPlayedAssistantCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Text text = (Text) scene.lookup("#nicknamePlayer" + i);
            Text text1 = (Text) scene.lookup("#nickname" + i);
            Text text2 = (Text) scene.lookup("#assistantNickname" + i);
            Bindings.bindBidirectional(text.textProperty(), text1.textProperty());
            Bindings.bindBidirectional(text.textProperty(), text2.textProperty());

            ImageView card = (ImageView) scene.lookup("#assistantCard" + i);
            ImageView card1 = (ImageView) scene.lookup("#assistantCard1_" + i);
            Bindings.bindBidirectional(text1.visibleProperty(), card.visibleProperty());
            Bindings.bindBidirectional(text2.visibleProperty(), card1.visibleProperty());

            Bindings.bindBidirectional(card.visibleProperty(), card1.visibleProperty());
            Bindings.bindBidirectional(card.imageProperty(), card1.imageProperty());

            if (i < playersNumber) {
                int finalI = i;
                int currentId = idMap.entrySet().stream().filter(e -> e.getValue() == finalI).findAny().get().getKey();
                text.setText(nicknames.get(currentId));
                lastPlayedAssistantCards.add(card);
            }
            card.setVisible(false);
        }
    }

    private void defineAssistantsCards() {
        this.assistantCards = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Node card = scene.lookup("#" + this.getCardFXID(i));
            this.assistantCards.add(card);
        }
    }

    private void defineIslandsMap() {
        this.islands = new HashMap<>();
        for (int i=0; i<12; i++) {
            Map<String, Map<String, Node>> islandMap = new HashMap<>();

            Map<String, Node> island = new HashMap<>();
            Map<String, Node> motherNature = new HashMap<>();
            Map<String, Node> tower = new HashMap<>();
            Map<String, Node> studentsPawns = new HashMap<>();
            Map<String, Node> studentsLabels = new HashMap<>();
            Map<String, Node> bridge = new HashMap<>();

            String islandKey = this.getIslandFXID(i);
            StackPane islandValue = (StackPane) this.scene.lookup("#" + islandKey);
            island.put(islandKey, islandValue);

            String motherNatureKey = this.getMotherNatureFXID(i);
            ImageView motherNatureValue = (ImageView) this.scene.lookup("#" + motherNatureKey);
            motherNatureValue.setVisible(false);
            motherNature.put(motherNatureKey, motherNatureValue);

            String towerKey = this.getTowerOnIslandFXID(i);
            ImageView towerValue = (ImageView) this.scene.lookup("#" + towerKey);
            towerValue.setVisible(false);
            tower.put(towerKey, towerValue);

            for (Color color: Color.values()) {
                String studentPawnKey = this.getStudentFXIDOnIsland(i, color);
                ImageView studentPawnValue = (ImageView) this.scene.lookup("#" + studentPawnKey);
                studentPawnValue.setVisible(false);
                studentsPawns.put(studentPawnKey, studentPawnValue);
            }

            for (Color color: Color.values()) {
                String studentLabelKey = this.getStudentNumberFXIDOnIsland(i, color);
                Text studentLabelValue = (Text) this.scene.lookup("#" + studentLabelKey);
                studentLabelValue.setText(null);
                studentsLabels.put(studentLabelKey, studentLabelValue);
            }

            int destinationIsland = i+1;
            if (destinationIsland == 12) {
                destinationIsland = 0;
            }
            String bridgeKey = this.getBridgeFXID(i, destinationIsland);
            ImageView bridgeValue = (ImageView) this.scene.lookup("#" + bridgeKey);
            bridgeValue.setVisible(false);
            bridge.put(bridgeKey, bridgeValue);

            islandMap.put("island", island);
            islandMap.put("motherNature", motherNature);
            islandMap.put("tower", tower);
            islandMap.put("studentsPawns", studentsPawns);
            islandMap.put("studentsLabels", studentsLabels);
            islandMap.put("bridge", bridge);

            this.islands.put(i, islandMap);
        }
    }

    private void defineSchoolsMap(ThinModel model) {

        this.schools = new HashMap<>();
        for (Integer i : idMap.keySet()) {
            Map<String, Map<String, ImageView>> schoolMap = new HashMap<>();

            Map<String, ImageView> dining = new HashMap<>();
            Map<String, ImageView> entrance = new HashMap<>();
            Map<String, ImageView> towers = new HashMap<>();
            Map<String, ImageView> professors = new HashMap<>();

            // Get all students in dining room
            for (Color color : Color.values()) {
                for (int j=0; j<10; j++) {
                    String key = this.getStudentFXID(j, true, color, idMap.get(i));
                    ImageView value = (ImageView) this.scene.lookup("#" + key);
                    dining.put(key, value);
                    value.setVisible(false);
                }
            }
            schoolMap.put("dining", dining);

            // Get all students in entrance
            for (int j=0; j<9; j++) {
                String key = this.getStudentFXID(j, false, null, idMap.get(i));
                ImageView value = (ImageView) this.scene.lookup("#" + key);

                if (idMap.get(i) == 0) {
                    Node node = this.scene.lookup("#studentEntrance" + j);
                    Bindings.bindBidirectional(node.visibleProperty(), value.visibleProperty());
                }

                if (j < model.getEntranceByPlayer(i).size()) {
                    entrance.put(key, value);
                }
                value.setVisible(false);
            }
            schoolMap.put("entrance", entrance);

            // Get all towers
            TowerColor color = model.getTowerColorByPlayer(i);
            Image tower = new Image(getTowerPath(color));
            String style = color.toString().toLowerCase() + "Tower";
            for (int j = 0; j < 8; j++) {
                String key = this.getTowerFXID(idMap.get(i), j);
                ImageView towerImage = (ImageView) this.scene.lookup("#" + key);
                towerImage.getStyleClass().clear();
                towerImage.getStyleClass().add(style);
                towerImage.setImage(tower);
                if (j < model.getNumTowerByPlayer(i)) {
                    towers.put(key, towerImage);
                }
                towerImage.setVisible(false);
            }
            schoolMap.put("towers", towers);

            // Get all professors
            for (Color c : Color.values()) {
                String key = this.getProfessorFXID(idMap.get(i), c);
                ImageView value = (ImageView) scene.lookup("#" + key);
                professors.put(key, value);
                value.setVisible(false);
            }
            schoolMap.put("professors", professors);

            this.schools.put(i, schoolMap);
        }
    }

    private void defineCloudsMap() {
        this.clouds = new HashMap<>();
        int cloudsNumber = 2;
        int maxStudentsOnCloud = 3;
        if (this.playersNumber == 3) {
            cloudsNumber = 3;
            maxStudentsOnCloud = 4;
        }

        for (int i=0; i<3; i++) {
            Map<String, Node> cloudMap = new HashMap<>();

            if (i < cloudsNumber) {
                for (int j = 0; j < 4; j++) {
                    String key = this.getStudentsOnCloudFXID(i, j);
                    Node value = this.scene.lookup("#" + key);
                    value.setVisible(false);
                    if (j < maxStudentsOnCloud) {
                        cloudMap.put(key, value);
                    }
                }
                this.clouds.put(i, cloudMap);
            }
            else {
                Node cloud = this.scene.lookup("#cloud" + i);
                cloud.setVisible(false);
            }
        }
    }

    protected List<Node> getIslands() {
        List<Node> islands = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            islands.add(this.scene.lookup("#" + this.getIslandFXID(i)));
        }
        return islands;
    }

    protected List<Node> getClouds() {
        List<Node> clouds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            clouds.add(this.scene.lookup("#" + this.getCloudFXID(i)));
        }
        return clouds;
    }

    private List<ImageView> getTowerOnSchool(int schoolId) {
        return this.schools.get(schoolId).get("towers").values().stream().toList();
    }

    private ImageView getProfessorOnSchool(int schoolId, Color color) {
        String identifier = this.getProfessorFXID(idMap.get(schoolId), color);
        return this.schools.get(schoolId).get("professors").get(identifier);
    }

    private ImageView getStudentInDiningRoom(int schoolId, int position, Color color) {
        String identifier = this.getStudentFXID(position, true, color, idMap.get(schoolId));
        return this.schools.get(schoolId).get("dining").get(identifier);
    }

    private ImageView getStudentInEntrance(int schoolId, int position) {
        String identifier = this.getStudentFXID(position, false, null, idMap.get(schoolId));
        return this.schools.get(schoolId).get("entrance").get(identifier);
    }

    protected ImageView getMotherNature(int islandId) {
        String identifier = this.getMotherNatureFXID(islandId);
        return (ImageView) this.islands.get(islandId).get("motherNature").get(identifier);
    }

    private Text getStudentLabel(int islandId, Color color) {
        String identifier = this.getStudentNumberFXIDOnIsland(islandId, color);
        return (Text) this.islands.get(islandId).get("studentsLabels").get(identifier);
    }

    private ImageView getStudentPawn(int islandId, Color color) {
        String identifier = this.getStudentFXIDOnIsland(islandId, color);
        return (ImageView) this.islands.get(islandId).get("studentsPawns").get(identifier);
    }

    private ImageView getBridgeOnIsland(int startingIsland) {
        int destinationIsland = startingIsland+1;
        if (destinationIsland == 12) {
            destinationIsland = 0;
        }
        String identifier = this.getBridgeFXID(startingIsland, destinationIsland);
        return  (ImageView) this.islands.get(startingIsland).get("bridge").get(identifier);
    }

    private ImageView getTowerOnIsland(int islandId) {
        String identifier = this.getTowerOnIslandFXID(islandId);
        return (ImageView) this.islands.get(islandId).get("tower").get(identifier);
    }

    /**
     * Generate FX:ID of a student.
     * @param position [0-9] Position in the board (Dining Room 0 is closer to entrance).
     * @param diningRoom True if student is in diningRoom; false if in entrance.
     * @param color Color value
     * @param boardID 0: own board, bottom; 1: board left to player; 2: board right to player.
     * @return FX:ID of a student in scene
     */
    private String getStudentFXID (int position, boolean diningRoom, Color color, int boardID) {
        String id = "";
        if (diningRoom) {
            id = colorIDHelper(color);
        }
        id = id + "s";
        if (!diningRoom) { //if pawn is in entrance
            id = id + "e";
        }
        id = id + position + "_" + boardID;
        return id;
    }

    /**
     * Generate FX:ID of a student on an island
     * @param IDIsland ID of the island.
     * @param color color of the student.
     * @return FX:ID of the student on the set island.
     */
    private String getStudentFXIDOnIsland(int IDIsland, Color color) {
        String id = colorIDHelper(color);
        id = id + "si_" + IDIsland;
        return id;
    }

    /**
     * Generate FX:ID of the text indicating the number of students on an island.
     * @param IDIsland ID of the island
     * @param color color of the student
     * @return FX:ID of the text on the island of that color.
     */
    private String getStudentNumberFXIDOnIsland (int IDIsland, Color color) {
        String id = colorIDHelper(color);
        id = id + IDIsland + "Num";
        return id;
    }

    private String getIslandFXID (int IDIsland) {
        return "island" + IDIsland;
    }

    /**
     * Generate FX:ID of a cloud.
     * @param IDCloud ID of the cloud
     * @return FX:ID of the cloud
     */
    private String getCloudFXID (int IDCloud) {
        return "cloud" + IDCloud;
    }

    /**
     * Generate FX:ID of a professor.
     * @param boardID 0: own board, bottom; 1: board left to player; 2: board right to player.
     * @param color Color value
     * @return FX:ID of a professor in scene.
     */
    private String getProfessorFXID (int boardID, Color color) {
        return colorIDHelper(color) + "p" + "_" + boardID;
    }

    /**
     * Internal helper method for first letter of color
     * @param color Color value
     * @return String with letter of color.
     */
    private String colorIDHelper(Color color) {
        String id = "";
        switch (color) {
            case GREEN -> id = "g";
            case RED -> id = "r";
            case YELLOW -> id = "y";
            case PINK -> id = "p";
            case BLUE -> id = "b";
        }
        return id;
    }

    /**
     * Generate FX:ID of a tower.
     * @param playerId player id in the gui
     * @param position position of the pawn.
     * @return
     */
    private String getTowerFXID(int playerId, int position) {
        return "tower" + playerId + "_" + position;
    }

    public String getStudentPath(Color color) {
        String path = "/assets/gui/images/pawns/";
        switch (color) {
            case GREEN -> path = path + "greenStudent.png";
            case RED -> path = path + "redStudent.png";
            case YELLOW -> path = path + "yellowStudent.png";
            case PINK -> path = path + "pinkStudent.png";
            case BLUE -> path = path + "blueStudent.png";
        }
        return path;
    }

    public String getTowerPath (TowerColor color) {
        String path = "/assets/gui/images/pawns/";
        switch (color) {
            case BLACK -> path = path + "blackTower.png";
            case WHITE -> path = path + "whiteTower.png";
            case GREY -> path = path + "grayTower.png";
        }
        return path;
    }

    public String getStudentsOnCloudFXID (int cloudID, int pawnID) {
        return "scloud" + cloudID + "_" + pawnID;
    }

    private String getMotherNatureFXID (int positionID) {
        return "mn" + positionID;
    }

    public String getAssistantCardPath (int id) {
        return "/assets/gui/images/cards/AssistantCard/Assistente_" + id + ".png";
    }

    public String getCharacterCardPath (int id) {
        return "/assets/gui/images/cards/CharacterCard/CharacterCard_" + id + ".jpg";
    }

    public String getVolumeIconPath (boolean muted) {
        if (muted) return "/assets/gui/images/utils/muted.png";
        else return "/assets/gui/images/utils/unmuted.png";
    }

    private String getTowerOnIslandFXID (int islandID) {
        return "island" + islandID + "Tower";
    }

    private String getBridgeFXID (int islandID1, int islandID2) {
        return "bridge" + islandID1 + "_" + islandID2;
    }

    private String getCardFXID(int id) {
        String string = Integer.toString(id);
        if (string.length() < 2) string = "0" + string;
        return "acard" + string;
    }

    /**
     * Generates FX:ID of CharacterCard in the selector pane.
     * @param ID 1, 2, 3 of the card in game
     * @return FX:ID generated.
     */
    private String getCharacterCardSelectorID (int ID, boolean cost, boolean text, boolean main) {
        String id = "";
        if (main) id = "main";
        if (cost) id = "cost";
        if (text) id = "text";
        return id + "CharacterCard" + ID;
    }

    /**
     * Generates FX:ID of the table of the dining room (by color) of the player (Board 0)
     * @param color Color of the dining room students.
     * @return FX:ID of the table of the dining room.
     */
    private String getDRSelectorID (Color color) {
        String id = "dg";
        switch (color) {
            case YELLOW -> id = id + "Yellow";
            case RED -> id = id + "Red";
            case BLUE -> id = id + "Blue";
            case GREEN -> id = id + "Green";
            case PINK -> id = id + "Pink";
        }
        return id;
    }

}
