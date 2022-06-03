package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.ThinModel;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class AssetsMapper {

    private Map<Integer, Integer> idMap = null;
    private Map<Integer, Map<String, Map<String, ImageView>>> schools = null;
    private Map<Integer, Map<String, Object>> islands = null;
    private Map<Integer, Map<String, Object>> clouds = null;
    private final Scene scene;
    private final Integer playersNumber;
    private final Integer currentId;

    public AssetsMapper(ThinModel model, Scene scene, Integer playersNumber, Integer currentId) {
        this.scene = scene;
        this.playersNumber = playersNumber;
        this.currentId = currentId;
        this.defineIslandsMap();
        this.defineSchoolsMap(model);
        this.defineCloudsMap();
        this.defineIdMap();
        if (playersNumber == 2) {
            HBox value = (HBox) scene.lookup("#board2");
            value.setVisible(false);
        }
    }

    public ImageView getTowerOnSchool(int schoolId, TowerColor color, int position) {
        String identifier = this.getTowerFXID(color, position);
        return this.schools.get(schoolId).get("towers").get(identifier);
    }

    public ImageView getProfessorOnSchool(int schoolId, Color color) {
        String identifier = this.getProfessorFXID(schoolId, color);
        return this.schools.get(schoolId).get("professors").get(identifier);
    }

    public ImageView getStudentInDiningRoom(int schoolId, int position, Color color) {
        String identifier = this.getStudentFXID(position, true, color, schoolId);
        return this.schools.get(schoolId).get("dining").get(identifier);
    }

    public ImageView getStudentInEntrance(int schoolId, int position) {
        String identifier = this.getStudentFXID(position, false, null, schoolId);
        return this.schools.get(schoolId).get("entrance").get(identifier);
    }

    public Map<String, ImageView> getStudentsInEntrance() {
        return this.schools.get(0).get("entrance");
    }

    public ImageView getMotherNature(int islandId) {
        String identifier = this.getMotherNatureFXID(islandId);
        return ((Map<String, ImageView>) this.islands.get(islandId).get("motherNature")).get(identifier);
    }

    public Text getStudentLabel(int islandId, Color color) {
        String identifier = this.getStudentNumberFXIDOnIsland(islandId, color);
        return ((Map<String, Text>) this.islands.get(islandId).get("studentsLabels")).get(identifier);
    }

    public ImageView getStudentPawn(int islandId, Color color) {
        String identifier = this.getStudentFXIDOnIsland(islandId, color);
        return ((Map<String, ImageView>) this.islands.get(islandId).get("studentsPawns")).get(identifier);
    }

    public ImageView getBridgeOnIsland(int startingIsland) {
        int destinationIsland = startingIsland+1;
        if (destinationIsland == 12) {
            destinationIsland = 0;
        }
        String identifier = this.getBridgeFXID(startingIsland, destinationIsland);
        return ((Map<String, ImageView>) this.islands.get(startingIsland).get("bridge")).get(identifier);
    }

    public ImageView getTowerOnIsland(int islandId) {
        String identifier = this.getTowerOnIslandFXID(islandId);
        return ((Map<String, ImageView>) this.islands.get(islandId).get("tower")).get(identifier);
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
            Map<String, Object> cloudMap = new HashMap<>();

            for (int j=0; j<4; j++) {
                String key = this.getStudentsOnCloudFXID(i, j);
                ImageView value = (ImageView) this.scene.lookup("#" + key);
                value.setVisible(false);
                if (i < cloudsNumber && j < maxStudentsOnCloud) {
                    cloudMap.put(key, value);
                }
            }

            this.islands.put(i, cloudMap);
        }
    }

    private void setupClouds() {

    }

    private void defineIslandsMap() {
        this.islands = new HashMap<>();
        for (int i=0; i<12; i++) {
            Map<String, Object> islandMap = new HashMap<>();

            Map<String, StackPane> island = new HashMap<>();
            Map<String, ImageView> motherNature = new HashMap<>();
            Map<String, ImageView> tower = new HashMap<>();
            Map<String, ImageView> studentsPawns = new HashMap<>();
            Map<String, Text> studentsLabels = new HashMap<>();
            Map<String, ImageView> bridge = new HashMap<>();

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
        // TODO check if model.getDiningRoomById(i).size() is the right convention

        this.schools = new HashMap<>();
        for (int i=0; i<this.playersNumber; i++) {
            Map<String, Map<String, ImageView>> schoolMap = new HashMap<>();

            Map<String, ImageView> dining = new HashMap<>();
            Map<String, ImageView> entrance = new HashMap<>();
            Map<String, ImageView> towers = new HashMap<>();
            Map<String, ImageView> professors = new HashMap<>();

            // Get all students in dining room
            for (Color color : Color.values()) {
                for (int j=0; j<10; j++) {
                    String key = this.getStudentFXID(j, true, color, i);
                    ImageView value = (ImageView) this.scene.lookup("#" + key);
                    dining.put(key, value);
                    value.setVisible(false);
                }
            }
            schoolMap.put("dining", dining);

            // Get all students in entrance
            for (int j=0; j<9; j++) {
                String key = this.getStudentFXID(j, false, null, i);
                ImageView value = (ImageView) this.scene.lookup("#" + key);
                if (j < model.getEntranceByPlayer(i).size()) {
                    entrance.put(key, value);
                }
                value.setVisible(false);
            }
            schoolMap.put("entrance", entrance);

            // Get all towers
            for (TowerColor color : TowerColor.values()) {
                for (int j = 0; j < 8; j++) {
                    if (color == model.getTowerColorByPlayer(i)) {
                        String key = this.getTowerFXID(color, j);
                        ImageView value = (ImageView) this.scene.lookup("#" + key);
                        if (j < model.getNumTowerByPlayer(i)) {
                            towers.put(key, value);
                        }
                        value.setVisible(false);
                    }
                }
            }
            schoolMap.put("towers", towers);

            // Get all professors
            for (Color color : Color.values()) {
                String key = this.getProfessorFXID(i, color);
                ImageView value = (ImageView) scene.lookup("#" + key);
                professors.put(key, value);
                value.setVisible(false);
            }
            schoolMap.put("professors", professors);

            this.schools.put(i, schoolMap);
        }
    }

    private void defineIdMap() {
        if (this.idMap == null) {
            this.idMap = new HashMap<>();
            this.idMap.put(this.currentId, 0);
            for (int i = 1; i < this.playersNumber; i++) {
                int nextPlayerId = (currentId + i) % this.playersNumber;
                this.idMap.put(nextPlayerId, i);
            }
        }
    }

    public Map<Integer, Integer> getIdMap() {
        return this.idMap;
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
        String id = "cloud" + IDCloud;
        return id;
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
     * @param color TowerColor
     * @param position position of the pawn.
     * @return
     */
    private String getTowerFXID(TowerColor color, int position) {
        String id = "";
        switch (color) {
            case BLACK -> id = "black";
            case GREY -> id = "gray";
            case WHITE -> id = "white";
        }
        id = id + "Tower" + position;

        return id;
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

    public String getProfessorPath (Color color) {
        String path = "/assets/gui/images/pawns/";
        switch (color) {
            case GREEN -> path = path + "greenProfessor.png";
            case RED -> path = path + "redProfessor.png";
            case YELLOW -> path = path + "yellowProfessor.png";
            case PINK -> path = path + "pinkProfessor.png";
            case BLUE -> path = path + "blueProfessor.png";
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
        String id = "scloud" + cloudID + "_" + pawnID;
        return id;
    }

    private String getMotherNatureFXID (int positionID) {
        String id = "mn" + positionID;
        return id;
    }

    public String getAssistantCardPath (int id) {
        String path = "/assets/gui/images/cards/AssistantCard/Assistente_" + id + ".png";
        return path;
    }

    public String getCharacterCardPath (int id) {
        String path = "/assets/gui/images/cards/CharacterCard/CharacterCard_" + id + ".jpg";
        return path;
    }

    public String getVolumeIconPath (boolean muted) {
        if (muted) return "/assets/gui/images/utils/muted.png";
        else return "/assets/gui/images/utils/unmuted.png";
    }

    private String getAssistantCardPlayedFXID (int IDPlayer) {
        IDPlayer = this.idMap.get(IDPlayer);
        String id = "assistantCard" + IDPlayer;
        return id;
    }

    ImageView getImageViewFromFXID(String fxid) {
        try {
            return (ImageView) this.scene.lookup(fxid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTowerOnIslandFXID (int islandID) {
        return "island" + islandID + "Tower";
    }

    private String getBridgeFXID (int islandID1, int islandID2) {
        return "bridge" + islandID1 + "_" + islandID2;
    }

    private void changeCoinSizeByPlayer (int IDPlayer, int newCoinSize) {
        IDPlayer = this.idMap.get(IDPlayer);
        String ID = "coinPlayer" + IDPlayer;
        String coinSize = String.valueOf(newCoinSize);
        ((Text) this.scene.lookup(ID)).setText(coinSize);
    }

    private void changeNumOfPawnsInIslandByColor (int numOfPawns, Color color, int islandID) {
        String ID = colorIDHelper(color) + islandID + "Num";
        ((Text) this.scene.lookup(ID)).setText(String.valueOf(numOfPawns));
    }


    String getCardFXID(int id) {
        String string = Integer.toString(id);
        if (string.length() < 2) string = "0" + string;
        return "acard" + string;
    }

}
