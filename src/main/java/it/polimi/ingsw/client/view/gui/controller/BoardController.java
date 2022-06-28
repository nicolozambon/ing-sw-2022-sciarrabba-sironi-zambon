package it.polimi.ingsw.client.view.gui.controller;

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

/**
 * GUI Controller for the Board Scene (main scene)
 */
public class BoardController implements GUIController {

    /**
     * Current GUI
     */
    private ViewGUI gui;

    /**
     * Associated GUIBuilder
     */
    private GUIBuilder guiBuilder = null;

    /**
     * Thin Model representation of the match
     */
    private ThinModel model;

    /**
     * Lists all the possible in-game options
     */
    private final OptionLister optionLister;

    /**
     * Wizard selector Pane
     */
    @FXML private BorderPane wizardBorderPane;

    /**
     * AssistantCard selector Pane
     */
    @FXML private BorderPane assistantBorderPane;

    /**
     * Dining Room button, clickable to move a student into the player's dining room
     */
    @FXML private Button diningRoom;

    /**
     * Node for the Green Dining ROom
     */
    @FXML private Node dgGreen;

    /**
     * Node for the Red Dining ROom
     */
    @FXML private Node dgRed;

    /**
     * Node for the Yellow Dining ROom
     */
    @FXML private Node dgYellow;

    /**
     * Node for the Blue Dining ROom
     */
    @FXML private Node dgBlue;

    /**
     * Node for the Pink Dining ROom
     */
    @FXML private Node dgPink;

    /**
     * Node for the End Action Button (displayed when appropriate, and it's the player's turn)
     */
    @FXML private Node endActionButton;

    /**
     * Node for the CharacterCard selector opener
     */
    @FXML private Node characterCardButton;

    /**
     * Text node. Messages to the user, regarding the possible actions
     */
    @FXML private Text messages;

    /**
     * List of all the nodes for the player's school entrance
     */
    private List<Node> entranceStudents;

    /**
     * Source node (start) of the click action
     */
    private Node sourceNode;

    /**
     * Constructor for the class, creates a new OptionLister object
     */
    public BoardController() {
        optionLister = new OptionLister();
    }

    @Override
    public void optionsHandling(List<String> options) {
        messages.setText(optionLister.list(options).substring(1));
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Character Card");
        disableAll();
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
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Character Card");
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
        guiBuilder.updateGUI(model);
    }

    /**
     * Sets the Wizard Selector Pane visible
     */
    private void chooseWizard() {
        wizardBorderPane.setVisible(true);
    }

    /**
     * Sets the Assistant Card Selector Pane visible
     */
    private void playAssistantCard() {
        assistantBorderPane.setVisible(true);
    }

    /**
     * Enables the pawns in the entrance for a click event
     */
    private void moveStudentToDiningRoom() {
        entranceStudents.forEach(n -> n.setDisable(false));
    }

    /**
     * Enables the pawns in the entrance for a click event
     */
    private void moveStudentToIsland() {
        entranceStudents.forEach(n -> n.setDisable(false));
    }

    /**
     * Enables the Mother Nature pawn for a click event
     */
    private void moveMotherNature() {
        guiBuilder.getMotherNature(model.getMNPosition()).setDisable(false);
    }

    /**
     * Sets the CharacterCard Selector Pane visible
     */
    private void playCharacterCard() {
        this.gui.getScenes().get("boardScene").lookup("#characterCardButton").setDisable(false);
    }

    /**
     * Enables the Clouds for a click event
     */
    private void takeStudentsFromCloud() {
        guiBuilder.getClouds().forEach(n -> n.setDisable(false));
    }

    /**
     * Sets the End Action button visible and clickable
     */
    private void endAction() {
        endActionButton.setDisable(false);
        endActionButton.setVisible(true);
    }

    /**
     * Sets the Text under the Character Card button after selecting the Character Card 2
     */
    private void card2() {
        //Island
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 2 Effect");
        characterCardButton.setDisable(false);
    }

    /**
     * Sets the Text under the Character Card button after selecting the Character Card 6
     */
    private void card6() {
        //Color
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 6 Effect");
        characterCardButton.setDisable(false);
    }

    /**
     * Sets the Text under the Character Card button after selecting the Character Card 7
     */
    private void card7() {
        //Change student
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 7 Effect");
        characterCardButton.setDisable(false);
    }

    /**
     * Sets the Text under the Character Card button after selecting the Character Card 8
     */
    private void card8() {
        //Color
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        text.setText("Use Card 8 Effect");
        characterCardButton.setDisable(false);
    }

    /**
     * Disables all pawns, making them unclickable
     */
    private void disableAll() {

        //Disable mother nature
        guiBuilder.getMotherNature(model.getMNPosition()).getStyleClass().remove("selected");
        guiBuilder.getMotherNature(model.getMNPosition()).setDisable(true);

        //Hide other pane
        wizardBorderPane.setVisible(false);
        assistantBorderPane.setVisible(false);

        //Disable button not needed
        endActionButton.setVisible(false);
        characterCardButton.setDisable(true);
        characterCardButton.getStyleClass().remove("selected");

        //Disable 'full' dining room, entrance student, islands, clouds
        diningRoom.setDisable(true);
        entranceStudents.forEach(n -> { n.setDisable(true);
                                        n.getStyleClass().remove("selected");});

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

    /**
     * Save the user's choice regarding the wizard, getting the selected one from the source of the click event
     * @param event Click Event
     */
    @FXML
    private void chooseWizard(Event event) {
        String id = ((Node) event.getSource()).getId();
        id = id.substring(0, id.length() - 5);
        this.gui.fireRequest(new RequestEvent("chooseWizard", this.gui.getId(), Wizard.valueOf(id.toUpperCase()).ordinal()));
        gui.playPopEffect();
        //System.out.println(Wizard.valueOf(id.toUpperCase()));
    }

    /**
     * Save the user's choice regarding the Assistant Card, getting the selected one from the source of the click event
     * @param event Click Event
     */
    @FXML
    private void chooseAssistantCard(Event event) {
        String id = ((Node) event.getSource()).getId();
        id = id.substring(id.length() - 2);
        //System.out.println(id);
        assistantBorderPane.setVisible(false); //remove from here
        gui.fireRequest(new RequestEvent("playAssistantCard", gui.getId(), Integer.parseInt(id)));
        gui.playPopEffect();
    }

    /**
     * Save the user's choice regarding the Character Card, getting the selected one from the source of the click event
     * @param event Click Event
     */
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

    /**
     * Opens the Character Card Selector Pane
     * @param event
     */
    @FXML
    private void openCharacterCardSelector(Event event) {
        Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
        if (text.getText().equals("Character Card")) {
            gui.getStage().getScene().lookup("#characterBorderPane").setVisible(true);
            gui.playPopEffect();
        } else if (text.getText().startsWith("Use")){
            if (characterCardButton.getStyleClass().contains("selected")) {
                disableAll();
                characterCardButton.getStyleClass().remove("selected");
                optionsHandling(gui.getOptions());
            } else {
                disableAll();
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
                    case 7 -> entranceStudents.forEach(n -> n.setDisable(false));
                    default -> {
                        diningRoom.setDisable(true);
                        dgGreen.setDisable(false);
                        dgPink.setDisable(false);
                        dgBlue.setDisable(false);
                        dgYellow.setDisable(false);
                        dgRed.setDisable(false);
                    }
                }
            }

        }
    }

    /**
     * Closes the Character Card Selector Pane, setting the visibility to false
     */
    @FXML
    private void closeCharacterCardSelector () {
        gui.getStage().getScene().lookup("#characterBorderPane").setVisible(false);
        gui.playPopEffect();
    }

    /**
     * Specifies the behavior of the UI after a click on a student on the entrance. Sets enabled the dining room and the islands
     * as destinations of the pawn
     * @param event Click event on the student's pawn
     */
    @FXML
    private void entranceStudentOnClick(Event event) {
        if (sourceNode == null) {
            entranceStudents.forEach(n -> n.setDisable(true));
            sourceNode = ((Node) event.getSource());
            sourceNode.getStyleClass().add("selected");
            sourceNode.setDisable(false);
            characterCardButton.setDisable(true);
            if (characterCardButton.getStyleClass().contains("selected")) {
                diningRoom.setDisable(true);
                dgGreen.setDisable(false);
                dgPink.setDisable(false);
                dgBlue.setDisable(false);
                dgYellow.setDisable(false);
                dgRed.setDisable(false);
            } else {
                for (Node island : guiBuilder.getIslands()) {
                    island.getStyleClass().add("borderOnHover");
                    island.setCursor(Cursor.HAND);
                }
                diningRoom.setDisable(false);
            }
        } else if (sourceNode.equals(event.getSource())) {
            entranceStudents.forEach(n -> n.setDisable(false));
            characterCardButton.setDisable(false);
            sourceNode.getStyleClass().remove("selected");
            if (characterCardButton.getStyleClass().contains("selected")) {
                dgGreen.setDisable(true);
                dgPink.setDisable(true);
                dgBlue.setDisable(true);
                dgYellow.setDisable(true);
                dgRed.setDisable(true);
            } else {
                for (Node island : guiBuilder.getIslands()) {
                    island.getStyleClass().clear();
                    island.setCursor(Cursor.DEFAULT);
                }
            }
            diningRoom.setDisable(true);
            sourceNode = null;
        }
    }

    /**
     * Specifies the behavior of the UI after a click on Mother Nature. Sets the islands as destination, adding
     * the borderOnHover CSS Style to them
     * @param event Click Event
     */
    @FXML
    private void motherNatureOnClick(Event event) {
        sourceNode = ((Node)event.getSource());
        sourceNode.setDisable(true);
        sourceNode.getStyleClass().add("selected");
        characterCardButton.setDisable(true);
        for (Node island : guiBuilder.getIslands()) {
            island.getStyleClass().add("borderOnHover");
            island.setCursor(Cursor.HAND);
        }
        event.consume();
    }

    /**
     * Specifies the behavior of the UI after a selection of a cloud, getting the choice from the user based on the
     * FX:ID of the source node of the event
     * @param event Click event
     */
    @FXML
    private void cloudOnClick(Event event) {
        Node cloud = (Node) event.getSource();
        int position = Integer.parseInt(cloud.getId().substring(cloud.getId().length() - 1));
        this.gui.fireRequest(new RequestEvent("takeStudentsFromCloud", this.gui.getId(), position + 1));
    }

    /**
     * Specifies the behavior of the UI after a click on an island, registering the choice from the user based on
     * the FX:ID of the source node of the event.
     * If the source of the click event is a MotherNature pawn, then the method calculates if the number of steps
     * are possible, considering if the islands are linked together.
     * If the source of the click event is a student, a request to move the student is fired to the server.
     * Graphical Style of the pawns involved in the event are changed accordingly
     * @param event Click Event
     */
    @FXML
    private void islandOnClick(Event event) {
        Node islandNode = (Node) event.getSource();
        int island = Integer.parseInt(islandNode.getId().substring(6));
        if (sourceNode != null) {
            if (sourceNode.getId().matches("mn[0-9][0-1]?")) { //Regex expression
                //Calculate the correct number of steps if islands are united
                int mn = Integer.parseInt(sourceNode.getId().substring(2));
                int steps = (island - mn + 12) % 12;
                for (int i = 0; i < (island - mn + 12) % 12; i++) {
                    if (model.isIslandLinkedNext((mn + i) % 12)) {
                        steps--;
                    }
                }
                this.gui.fireRequest(new RequestEvent("moveMotherNature", this.gui.getId(), steps));
            } else if (sourceNode.getId().matches("studentEntrance[0-8]")) { //Regex expression
                int student = Integer.parseInt(sourceNode.getId().substring(sourceNode.getId().length() - 1));
                this.gui.fireRequest(new RequestEvent("moveStudentToIsland", this.gui.getId(), student + 1, island + 1));
            }
            sourceNode.getStyleClass().remove("selected");
            sourceNode = null;
        } else if (characterCardButton.getStyleClass().contains("selected")) { //case card 2 effect
            Text text = (Text) this.gui.getScenes().get("boardScene").lookup("#characterCardText");
            if (text.getText().contains("2")) {
                this.gui.fireRequest(new RequestEvent("extraAction", this.gui.getId(), island + 1));
                characterCardButton.getStyleClass().remove("selected");
            }
        }
    }

    /**
     * If the Source of the click event is valid, then a request is fired to move the student to the dining room to the server
     * @param event Click event
     */
    @FXML
    private void diningRoomOnClick(Event event) {
        if (sourceNode != null) {
            int position = Integer.parseInt(sourceNode.getId().substring(sourceNode.getId().length() - 1));
            this.gui.fireRequest(new RequestEvent("moveStudentToDiningRoom", this.gui.getId(), position + 1));
            sourceNode.getStyleClass().remove("selected");
            sourceNode = null;
        }
    }

    /**
     * Helper method for Character Card 6, 7, 8, that require the choice of a color for the action.
     * if (sourceNode == null) branch handles cards 6, 8.
     * else branch handles card 7, which requires a student and a color
     * @param event Click Event
     */
    @FXML
    private void colorOnClick(Event event) {
        if (sourceNode == null) {
            Node source = (Node) event.getSource();
            Color color = Color.valueOf(source.getId().substring(2).toUpperCase());
            this.gui.fireRequest(new RequestEvent("extraAction", gui.getId(), color.ordinal()));
        } else if (sourceNode.getId().matches("studentEntrance[0-8]")) { //Regex expression
            Node source = (Node) event.getSource();
            Color color = Color.valueOf(source.getId().substring(2).toUpperCase());
            int student = Integer.parseInt(sourceNode.getId().substring(sourceNode.getId().length() - 1));
            this.gui.fireRequest(new RequestEvent("extraAction", this.gui.getId(), student, color.ordinal()));
            sourceNode.getStyleClass().remove("selected");
            sourceNode = null;
        }
        characterCardButton.getStyleClass().remove("selected");
    }

    /**
     * When the End Action button is pressed, a new request is fired ("endAction")
     * @param event
     */
    @FXML
    private void endActionOnClick(Event event) {
        this.gui.fireRequest(new RequestEvent("endAction", this.gui.getId()));
    }

    /**
     * Plays and pauses the soundtrack in-game music when the button is pressed, and the icon is changed accordingly
     * @param event
     */
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

    /**
     * Quits the application
     */
    @FXML
    private void exitGame(Event event) {
        Platform.exit();
    }

}
