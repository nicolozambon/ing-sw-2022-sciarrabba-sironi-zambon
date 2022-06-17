package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.cli.CLIBuilder;
import it.polimi.ingsw.client.view.cli.OptionLister;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.model.ThinModel;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BoardController implements GUIController {

    private ViewGUI gui;
    private GUIBuilder guiBuilder = null;

    private ThinModel model;
    private final OptionLister optionLister;

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
    @FXML private Text messages;
    private List<Node> entranceStudents;

    private Node sourceNode;

    public BoardController() {
        optionLister = new OptionLister();
    }

    @Override
    public void optionsHandling(List<String> options) {
        messages.setText(optionLister.list(options).substring(1));
        if (options.size() == 1 && options.contains("endAction")) endActionOnClick(null);
        else {
            for (String option : options) {
                try {
                    Method method = BoardController.class.getDeclaredMethod(option);
                    method.setAccessible(true);
                    method.invoke(this);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onWaitEvent(String name) {
        assistantBorderPane.setVisible(false);
        wizardBorderPane.setVisible(false);
        disableAll();

        String string;
        if (name == null) string = "Please wait it is not your turn!";
        else string = "Please wait, it is " + name + "'s turn!";

        messages.setText(string);
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateModel(ThinModel model) {
        this.model = model;
        if (guiBuilder == null) {
            guiBuilder = new GUIBuilder(model, this.gui.getStage().getScene(), this.gui.getNickname());
            entranceStudents = new ArrayList<>();
            for (int i = 0; i < model.getEntranceByPlayer(gui.getId()).size(); i++) {
                entranceStudents.add(this.gui.getStage().getScene().lookup("#studentEntrance" + i));
            }
            if (!model.isCompleteRule()) {
                characterCardButton.setVisible(false);
            }
        }
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Character Card");
        disableAll();
        guiBuilder.updateGUI(model);
        new CLIBuilder().buildCLI(model, this.gui.getNickname()).showGameBoard(); //TODO only debugging purpose
    }

    private void chooseWizard() {
        wizardBorderPane.setVisible(true);
    }

    private void playAssistantCard() {
        assistantBorderPane.setVisible(true);
    }

    private void moveStudentToDiningRoom() {
        entranceStudents.forEach(n -> n.setDisable(false));
    }

    private void moveStudentToIsland() {
        entranceStudents.forEach(n -> n.setDisable(false));
    }

    private void moveMotherNature() {
        guiBuilder.getMotherNature(model.getMNPosition()).setDisable(false);
    }

    private void playCharacterCard() {
        this.gui.getScenes().get("boardScene").lookup("#characterCardButton").setDisable(false);
    }

    private void takeStudentsFromCloud() {
        guiBuilder.getClouds().forEach(n -> n.setDisable(false));
    }

    private void endAction() {
        endActionButton.setDisable(false);
        endActionButton.setVisible(true);
    }

    private void card2() {
        //Island
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 2 Effect");
        characterCardButton.setDisable(false);
    }

    private void card6() {
        //Color
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 6 Effect");
        characterCardButton.setDisable(false);
    }

    private void card7() {
        //Imprecazioni
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 7 Effect");
        characterCardButton.setDisable(false);
    }

    private void card8() {
        //Color
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 8 Effect");
        characterCardButton.setDisable(false);
    }

    private void disableAll() {
        //Hide other pane
        wizardBorderPane.setVisible(false);
        assistantBorderPane.setVisible(false);

        //Disable button not needed
        endActionButton.setVisible(false);
        characterCardButton.setDisable(true);

        //Disable 'full' dining room, entrance student, islands, clouds
        diningRoom.setDisable(true);
        entranceStudents.forEach(n -> n.setDisable(true));
        for (Node island : guiBuilder.getIslands()) {
            island.getStyleClass().clear();
            island.setCursor(Cursor.DEFAULT);
        }
        //guiBuilder.getIslands().forEach(n -> n.setDisable(true));
        guiBuilder.getClouds().forEach(n -> n.setDisable(true));

        //Disable colors dining room
        dgGreen.setDisable(true);
        dgPink.setDisable(true);
        dgBlue.setDisable(true);
        dgYellow.setDisable(true);
        dgRed.setDisable(true);
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
        if (sourceNode == null) {
            String id = ((ImageView) event.getSource()).getImage().getUrl();
            id = Character.toString(id.charAt(id.length() - 5));
            gui.fireRequest(new RequestEvent("playCharacterCard", gui.getId(), Integer.parseInt(id)));
            closeCharacterCardSelector();
            gui.playPopEffect();
        }
    }

    @FXML
    private void openCharacterCardSelector(Event event) {
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        if (text.getText().equals("Character Card")) {
            gui.getStage().getScene().lookup("#characterBorderPane").setVisible(true);
            gui.playPopEffect();
        } else if (text.getText().startsWith("Use")){
            if (sourceNode == null) {
                disableAll();
                sourceNode = (Node) event.getSource();
                characterCardButton.setDisable(false);
                characterCardButton.getStyleClass().add("selected");
                int value = Integer.parseInt(text.getText().substring(9,10));
                switch(value) {
                    case 2 -> {
                        for (Node island : guiBuilder.getIslands()) {
                            island.getStyleClass().add("borderOnHover");
                            island.setCursor(Cursor.HAND);
                        }
                    }
                    case 6 -> System.out.println("card 6");
                    case 7 -> System.out.println("card 7");
                    case 8 -> System.out.println("card 8");
                }
            } else if (sourceNode.equals(event.getSource())) {
                disableAll();
                characterCardButton.getStyleClass().remove("selected");
                sourceNode = null;
                optionsHandling(gui.getOptions());
            }

        }
    }

    @FXML
    private void closeCharacterCardSelector () {
        gui.getStage().getScene().lookup("#characterBorderPane").setVisible(false);
        gui.playPopEffect();
    }

    @FXML
    private void entranceStudentOnClick(Event event) {
        if (sourceNode == null) {
            entranceStudents.forEach(n -> n.setDisable(true));
            sourceNode = ((Node)event.getSource());
            sourceNode.getStyleClass().add("selected");
            sourceNode.setDisable(false);
            for (Node island : guiBuilder.getIslands()) {
                island.getStyleClass().add("borderOnHover");
                island.setCursor(Cursor.HAND);
            }
            diningRoom.setDisable(false);
            characterCardButton.setDisable(true);
        } else if (sourceNode.equals(event.getSource())) {
            entranceStudents.forEach(n -> n.setDisable(false));
            for (Node island : guiBuilder.getIslands()) {
                island.getStyleClass().clear();
                island.setCursor(Cursor.DEFAULT);
            }
            diningRoom.setDisable(true);
            characterCardButton.setDisable(false);
            sourceNode.getStyleClass().remove("selected");
            sourceNode = null;
        }
    }

    @FXML
    private void motherNatureOnClick(Event event) {
        sourceNode = ((Node)event.getSource());
        sourceNode.setDisable(true);
        sourceNode.getStyleClass().add("selected");
        for (Node island : guiBuilder.getIslands()) {
            island.getStyleClass().add("borderOnHover");
            island.setCursor(Cursor.HAND);
        }
        event.consume();
    }

    @FXML
    private void cloudOnClick(Event event) {
        Node cloud = (Node) event.getSource();
        int position = Integer.parseInt(cloud.getId().substring(cloud.getId().length() - 1));
        this.gui.fireRequest(new RequestEvent("takeStudentsFromCloud", this.gui.getId(), position + 1));
    }

    @FXML
    private void islandOnClick(Event event) {
        if (sourceNode != null) {
            Node islandNode = (Node) event.getSource();
            int island = Integer.parseInt(islandNode.getId().substring(6));
            if (sourceNode.getId().matches("mn[0-9][0-1]?")) { //Regex expression
                //TODO handling when islands are connected
                int mn = Integer.parseInt(sourceNode.getId().substring(2));
                while (model.isIslandLinkedNext(mn)) {
                    mn++;
                }
                while (model.isIslandLinkedPrev(island)) {
                    island--;
                }
                int steps = (island - mn + 12) % 12;
                this.gui.fireRequest(new RequestEvent("moveMotherNature", this.gui.getId(), steps));
            } else if (sourceNode.getId().matches("studentEntrance[0-8]")) { //Regex expression
                int student = Integer.parseInt(sourceNode.getId().substring(sourceNode.getId().length() - 1));
                this.gui.fireRequest(new RequestEvent("moveStudentToIsland", this.gui.getId(), student + 1, island + 1));
            } else if (sourceNode.equals(characterCardButton)) {
                Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
                if (text.getText().contains("2")) {
                    System.out.println("fire extraAction");
                    this.gui.fireRequest(new RequestEvent("extraAction", this.gui.getId(), island + 1));
                }
            }
            sourceNode.getStyleClass().remove("selected");
            sourceNode = null;
        }
    }

    @FXML
    private void diningRoomOnClick(Event event) {
        if (sourceNode != null) {
            int position = Integer.parseInt(sourceNode.getId().substring(sourceNode.getId().length() - 1));
            this.gui.fireRequest(new RequestEvent("moveStudentToDiningRoom", this.gui.getId(), position + 1));
            sourceNode.getStyleClass().remove("selected");
            sourceNode = null;
        }
    }

    @FXML
    private void colorOnClick(Event event) {
        if (sourceNode instanceof Text) {
            Node source = (Node) event.getSource();
            Color color = Color.valueOf(source.getId().substring(2).toUpperCase());
            this.gui.fireRequest(new RequestEvent("extraAction", gui.getId(), color.ordinal()));
        }
    }

    @FXML
    private void endActionOnClick(Event event) {
        this.gui.fireRequest(new RequestEvent("endAction", this.gui.getId()));
    }

    @FXML
    private void playStopMusic(Event event) {
        if (gui.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            gui.mediaPlayer.pause();
            ((ImageView) event.getSource()).setImage(new Image(this.guiBuilder.getVolumeIconPath(true)));
        }
        else {
            gui.mediaPlayer.play();
            ((ImageView) event.getSource()).setImage(new Image(this.guiBuilder.getVolumeIconPath(false)));
        }
    }

    @FXML
    private void exitGame(Event event) {
        Platform.exit();
    }

}
