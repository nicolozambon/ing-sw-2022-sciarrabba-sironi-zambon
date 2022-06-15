package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.cli.CLIBuilder;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.model.ThinModel;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;

import java.lang.reflect.Method;
import java.util.List;

public class BoardController implements GUIController {

    private ViewGUI gui;

    private GUIBuilder GUIBuilder = null;

    private ThinModel model;

    @FXML private BorderPane wizardBorderPane;
    @FXML private BorderPane assistantBorderPane;

    @FXML private Button diningRoom;
    @FXML private Node dgGreen;
    @FXML private Node dgRed;
    @FXML private Node dgYellow;
    @FXML private Node dgBlue;
    @FXML private Node dgPink;
    @FXML private Node endActionButton;
    @FXML private Node characterCardButton;

    private String sourceID;
    private String destinationID;

    @Override
    public void optionsHandling(List<String> options) {
        for (String option : options) {
            try {
                Method method = BoardController.class.getDeclaredMethod(option);
                method.setAccessible(true);
                method.invoke(this);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if (options.size() == 1 && options.contains("endAction")) endActionOnClick(null);
    }

    @Override
    public void onWaitEvent(String name) {
        assistantBorderPane.setVisible(false);
        wizardBorderPane.setVisible(false);
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateModel(ThinModel model) {
        this.model = model;
        if (GUIBuilder == null) {
            GUIBuilder = new GUIBuilder(model, this.gui.getStage().getScene(), this.gui.getNickname());
        }

        disableAll();
        GUIBuilder.updateGUI(model);
        new CLIBuilder().buildCLI(model, this.gui.getNickname()).showGameBoard();
    }

    @FXML
    private void exitGame(Event event) {
        Platform.exit();
    }

    @FXML
    private void chooseWizard(Event event) {
        String id = ((Node) event.getSource()).getId();
        id = id.substring(0, id.length() - 5);
        this.gui.fireRequest(new RequestEvent("chooseWizard", this.gui.getId(), Wizard.valueOf(id.toUpperCase()).ordinal()));
        gui.playPopEffect();
        //System.out.println(Wizard.valueOf(id.toUpperCase()));
    }

    @FXML
    private void chooseAssistantCard(Event event) {
        String id = ((Node) event.getSource()).getId();
        id = id.substring(id.length() - 2);
        //System.out.println(id);
        assistantBorderPane.setVisible(false); //remove from here
        gui.fireRequest(new RequestEvent("playAssistantCard", gui.getId(), Integer.parseInt(id)));
        gui.playPopEffect();
    }

    @FXML
    private void chooseCharacterCard (Event event) {
        String id = ((ImageView) event.getSource()).getImage().getUrl();
        id = Character.toString(id.charAt(id.length() - 5));
        gui.fireRequest(new RequestEvent("playCharacterCard", gui.getId(), Integer.parseInt(id)));
        closeCharacterCardSelector();
        gui.playPopEffect();
    }

    @FXML
    private void endActionOnClick(Event event) {
        this.gui.fireRequest(new RequestEvent("endAction", this.gui.getId()));
    }

    private void chooseWizard() {
        wizardBorderPane.setVisible(true);
    }

    private void playAssistantCard() {
        assistantBorderPane.setVisible(true);
    }

    private void moveStudentToDiningRoom() {
        GUIBuilder.getStudentsInEntrance(this.gui.getId()).values().forEach(n -> n.setDisable(false));
    }

    private void moveStudentToIsland() {
        GUIBuilder.getStudentsInEntrance(this.gui.getId()).values().forEach(n -> n.setDisable(false));
    }

    private void moveMotherNature() {
        GUIBuilder.getMotherNature(model.getMNPosition()).setDisable(false);
    }

    private void playCharacterCard() {
        this.gui.getScenes().get("boardScene").lookup("#characterCardButton").setDisable(false);
    }

    private void takeStudentsFromCloud() {
        GUIBuilder.getClouds().forEach(n -> n.setDisable(false));
    }

    private void endAction() {
        endActionButton.setDisable(false);
    }

    private void card2() {

    }

    private void card6() {

    }
    private void card7() {

    }

    private void card8() {

    }

    private void disableAll() {
        //Hide other pane
        wizardBorderPane.setVisible(false);
        assistantBorderPane.setVisible(false);

        //Disable button not needed
        endActionButton.setDisable(true);
        characterCardButton.setDisable(true);

        //Disable all clickable components
        diningRoom.setDisable(true);
        GUIBuilder.getStudentsInEntrance(gui.getId()).values().forEach(n -> n.setDisable(true));
        GUIBuilder.getIslands().forEach(n -> n.setDisable(true));
        GUIBuilder.getClouds().forEach(n -> n.setDisable(true));
    }

    @FXML
    private void playStopMusic(Event event) {
        if (gui.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            gui.mediaPlayer.pause();
            ((ImageView) event.getSource()).setImage(new Image(this.GUIBuilder.getVolumeIconPath(true)));
        }
        else {
            gui.mediaPlayer.play();
            ((ImageView) event.getSource()).setImage(new Image(this.GUIBuilder.getVolumeIconPath(false)));
        }
    }

    @FXML
    private void openCharacterCardSelector () {
        gui.getStage().getScene().lookup("#characterBorderPane").setVisible(true);
        gui.playPopEffect();
    }

    @FXML
    private void closeCharacterCardSelector () {
        gui.getStage().getScene().lookup("#characterBorderPane").setVisible(false);
        gui.playPopEffect();
    }

    @FXML
    private void entranceStudentOnClick(Event event) {

    }

    @FXML
    private void cloudOnClick(Event event) {

    }

    @FXML
    private void islandOnClick(Event event) {

    }

    @FXML
    private void dgOnClick(Event event) {

    }
}
