package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class MainController implements GUIController {
    private ViewGUI gui;

    public MainController() {

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
        hidePawn(getMotherNatureID(islandStartID));
        showPawn(getMotherNatureID(islandDestID));
    }

    private String getMotherNatureID (int positionID) {
        String id = "mn" + positionID;
        return id;
    }

}
