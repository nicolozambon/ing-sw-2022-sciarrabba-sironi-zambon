package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.ThinModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardController implements GUIController {
    private ViewGUI gui;
    private Map<Integer, Integer> idMap = null;
    private Map<Integer, Map<String, ImageView>> schools = null;

    public BoardController() {

    }

    @Override
    public void optionsHandling(List<String> options) {

    }

    @Override
    public void onWaitEvent(String name) {

    }

    @Override
    public void onWaitEvent() {

    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateModel(ThinModel model) {
        this.defineIdMap(model);
        this.defineSchoolsMap(model);
        //TODO
    }

    private void defineSchoolsMap(ThinModel model) {
        this.schools = new HashMap<>();
        for (int i=0; i<model.getNicknames().size(); i++) {
            Map<String, ImageView> schoolMap = new HashMap<>();
            for (Color color : Color.values()) {
                for (int j=0; j<10; j++) {
                    String key = this.getStudentFXID(j, true, color, i);
                    ImageView value = (ImageView) this.gui.getStage().getScene().lookup("#" + key);
                    schoolMap.put(key, value);
                    System.out.println(key);
                    //value.setVisible(false);
                }
            }
            System.out.println("\n");
            this.schools.put(i, schoolMap);
        }
    }

    private void defineIdMap(ThinModel model) {
        if (this.idMap == null) {
            this.idMap = new HashMap<>();
            int currentId = this.gui.getId();
            this.idMap.put(currentId, 0);
            for (int i = 1; i < model.getNicknames().size(); i++) {
                int nextPlayerId = (currentId + i) % model.getNicknames().size();
                this.idMap.put(nextPlayerId, i);
            }
        }
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
        String id;
        id = colorIDHelper(color);
        id = id + "s";
        if (!diningRoom) { //if pawn is in entrance
            id = id + "e";
        }
        id = id + position + "_" + boardID;
        return id;
    }

    /**
     * Generate FX:ID of a professor.
     * @param boardID 0: own board, bottom; 1: board left to player; 2: board right to player.
     * @param color Color value
     * @return FX:ID of a professor in scene.
     */
    private String getProfessorFXID (int boardID, Color color) {
        String id;
        id = colorIDHelper(color);
        id = id + "p" + "_" + boardID;
        return id;
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
        id = id + position;

        return id;
    }

    private void hidePawn (String id) {
        gui.getStage().getScene().lookup(id).setVisible(false);
    }

    private void showPawn (String id) {
        gui.getStage().getScene().lookup(id).setVisible(true);
    }

    private void modifyImage (String path, ImageView imageView) {
        Image image = new Image(path);
        imageView.setImage(image);
    }

    private String getStudentPath(Color color) {
        String path = "../images/pawns/";
        switch (color) {
            case GREEN -> path = path + "greenStudent.png";
            case RED -> path = path + "redStudent.png";
            case YELLOW -> path = path + "yellowStudent.png";
            case PINK -> path = path + "pinkStudent.png";
            case BLUE -> path = path + "blueStudent.png";
        }
        return path;
    }

    private String getProfessorPath (Color color) {
        String path = "../images/pawns/";
        switch (color) {
            case GREEN -> path = path + "greenProfessor.png";
            case RED -> path = path + "redProfessor.png";
            case YELLOW -> path = path + "yellowProfessor.png";
            case PINK -> path = path + "pinkProfessor.png";
            case BLUE -> path = path + "blueProfessor.png";
        }
        return path;
    }

    private String getTowerPath (TowerColor color) {
        String path = "../images/pawns/";
        switch (color) {
            case BLACK -> path = path + "blackTower.png";
            case WHITE -> path = path + "whiteTower.png";
            case GREY -> path = path + "grayTower.png";
        }
        return path;
    }

    private String getStudentsOnCloudFXID (int cloudID, int pawnID) {
        String id = "scloud" + cloudID + "_" + pawnID;
        return id;
    }

    private void moveMotherNature (int islandStartID, int islandDestID) {
        hidePawn(getMotherNatureFXID(islandStartID));
        showPawn(getMotherNatureFXID(islandDestID));
    }

    private String getMotherNatureFXID (int positionID) {
        String id = "mn" + positionID;
        return id;
    }

    private String getAssistantCardPath (int id) {
        String path = "../images/cards/AssistantCard/Assistente_" + id + ".png";
        return path;
    }

    private String getCharacterCardPath (int id) {
        String path = "../images/cards/CharacterCard/CharacterCard_" + id + ".jpg";
        return path;
    }

    private void changePlayedAssistantCard(int IDPlayer, int IDAssistantCard) {
        IDPlayer = this.idMap.get(IDPlayer);
        modifyImage(getAssistantCardPath(IDAssistantCard), getImageViewFromFXID(getAssistantCardPlayedFXID(IDPlayer)));
    }

    private String getAssistantCardPlayedFXID (int IDPlayer) {
        IDPlayer = this.idMap.get(IDPlayer);
        String id = "assistantCard" + IDPlayer;
        return id;
    }

    private ImageView getImageViewFromFXID (String fxid) {
        try {
            return (ImageView) gui.getStage().getScene().lookup(fxid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTowerOnIslandFXID (int islandID) {
        return "island" + islandID + "Tower";
    }

    /**
     * Shows bridge to unify islands.
     * @param islandID1 left island to unify.
     * @param islandID2 right island to unify.
     */
    private void unifyIslands (int islandID1, int islandID2) {
        showPawn(getBridgeFXID(islandID1, islandID2));
    }

    private String getBridgeFXID (int islandID1, int islandID2) {
        return "bridge" + islandID1 + "_" + islandID2;
    }

    private void changeCoinSizeByPlayer (int IDPlayer, int newCoinSize) {
        IDPlayer = this.idMap.get(IDPlayer);
        String ID = "coinPlayer" + IDPlayer;
        String coinSize = String.valueOf(newCoinSize);
        ((Text) gui.getStage().getScene().lookup(ID)).setText(coinSize);
    }

    private void changeNumOfPawnsInIslandByColor (int numOfPawns, Color color, int islandID) {
        String ID = colorIDHelper(color) + islandID + "Num";
        ((Text) gui.getStage().getScene().lookup(ID)).setText(String.valueOf(numOfPawns));
    }

    @FXML
    private void exitGame(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void glowElement (ActionEvent event) {
        //((Node) event.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 21.0, 21.0, 10.0, 0.0, 0.0, 0.0)");
        System.out.println("Mouse hovered over " + ((Node)event.getSource()));
        Platform.exit();
    }

}
