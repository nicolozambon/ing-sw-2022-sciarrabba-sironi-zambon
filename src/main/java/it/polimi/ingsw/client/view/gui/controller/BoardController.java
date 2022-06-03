package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.model.ThinModel;
import it.polimi.ingsw.model.card.AssistantCard;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardController implements GUIController {

    private ViewGUI gui;
    private List<Node> assistantCards;
    private AssetsMapper assetsMapper = null;
    @FXML
    private BorderPane wizardBorderPane;
    @FXML
    private BorderPane assistantBorderPane;

    @FXML
    private BorderPane mainPane;

    @FXML
    private HBox diningRoom;
    @FXML
    private Node dgGreen;
    @FXML
    private Node dgRed;
    @FXML
    private Node dgYellow;
    @FXML
    private Node dgBlue;
    @FXML
    private Node dgPink;

    @FXML
    private Node endActionButton;

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
        if (this.assetsMapper == null) {
            assetsMapper = new AssetsMapper(
                    model,
                    this.gui.getStage().getScene(),
                    model.getNicknames().size(),
                    this.gui.getId()
            );
        }
        disableAll();
        this.setNicknamesOnSchools(model);
        this.setupGameBoard(model);

        //TODO
    }

    private void setNicknamesOnSchools(ThinModel model) {
        int i = 0;
        for (String nickname : model.getNicknames()) {
            Text value = (Text) this.gui.getStage().getScene().lookup("#nicknamePlayer" + String.valueOf(i));
            value.setText(nickname);
            i++;
        }
    }

    private void setupGameBoard(ThinModel model) {
        this.setupSchools(model);
        this.setupIslands(model);
    }

    private void setupSchools(ThinModel model) {
        for (int i=0; i<model.getNicknames().size(); i++) {

            int maxTowers = 8;
            int maxEntrance = 7;
            if (model.getNicknames().size() == 3) {
                maxTowers = 6;
                maxEntrance = 9;
            }

            // Add towers
            for (int j=0; j<maxTowers; j++) {
                ImageView tower = this.assetsMapper.getTowerOnSchool(i, model.getTowerColorByPlayer(i), j);
                if (j < model.getNumTowerByPlayer(i)) {
                    tower.setVisible(true);
                } else {
                    tower.setVisible(false);
                }
            }

            // Add professors
            for (Color professorColor : Color.values()) {
                ImageView professor = this.assetsMapper.getProfessorOnSchool(i, professorColor);
                if (model.getProfessorsByPlayer(i).contains(professorColor)) {
                    professor.setVisible(true);
                } else {
                    professor.setVisible(false);
                }
            }

            // Add dining room
            for (Color color : Color.values()) {
                for (int j = 0; j < 10; j++) {
                    ImageView student = this.assetsMapper.getStudentInDiningRoom(i, j, color);
                    if (j < model.getDiningRoomByPlayer(i).get(color)) {
                        student.setVisible(true);
                    } else {
                        student.setVisible(false);
                    }
                }
            }

            // Add entrance
            for (int j = 0; j < maxEntrance; j++) {
                ImageView student = this.assetsMapper.getStudentInEntrance(i, j);
                if (j<model.getEntranceByPlayer(i).size()) {
                    String path = this.assetsMapper.getStudentPath(model.getEntranceByPlayer(i).get(j));
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
            ImageView motherNature = this.assetsMapper.getMotherNature(i);
            motherNature.setVisible(model.getMNPosition() == i);

            // Show students
            for (Color color : Color.values()) {
                Integer numberOfStudents = model.getStudentOnIsland(i).get(color);
                Text label = this.assetsMapper.getStudentLabel(i, color);
                ImageView pawn = this.assetsMapper.getStudentPawn(i, color);
                if (numberOfStudents > 0) {
                    // Set label
                    label.setText("x" + numberOfStudents.toString());
                    // Show pawn
                    pawn.setVisible(true);
                } else {
                    label.setText(null);
                    pawn.setVisible(false);
                }
            }

            // Show bridge
            ImageView bridge = this.assetsMapper.getBridgeOnIsland(i);
            if (model.isIslandLinkedNext(i)) {
                bridge.setVisible(true);
            } else {
                bridge.setVisible(false);
            }

            // Show tower
            TowerColor towerColor = model.getTowerColorOnIsland(i);
            ImageView tower = this.assetsMapper.getTowerOnIsland(i);
            if (towerColor != null) {
                Image towerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(this.assetsMapper.getTowerPath(towerColor))));
                tower.setImage(towerImage);
                tower.setVisible(true);
            } else {
                tower.setVisible(false);
            }

            // TODO add elements here

        }
    }

    private void setupAssistantCards() {
        Scene scene = gui.getScenes().get("boardScene");
        this.assistantCards = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Node card = scene.lookup("#" + this.assetsMapper.getCardFXID(i));
            this.assistantCards.add(card);
        }
    }

    public void setAssistantCards(List<AssistantCard> cards) {
        if (assistantCards == null) setupAssistantCards();
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
        assistantBorderPane.setVisible(true);
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
        setAssistantCards(this.gui.getModel().getAssistantCardsByPlayer(gui.getId()));
    }

    private void moveStudentToDiningRoom() {
        diningRoom.setDisable(false);
        diningRoom.getChildren().forEach(n -> n.setDisable(true));
        assetsMapper.getStudentsInEntrance().values().forEach(n -> n.setDisable(false));
    }

    private void moveStudentToIsland() {

    }

    private void moveMotherNature() {

    }

    private void playCharacterCard() {

    }
    private void takeStudentsFromCloud() {

    }

    private void endAction() {
        this.gui.getScenes().get("boardScene").lookup("#endActionButton").setDisable(false);
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
        this.gui.getScenes().get("boardScene").lookup("#quitButton").setDisable(false);
        this.gui.getScenes().get("boardScene").lookup("#musicButton").setDisable(false);
        this.gui.getScenes().get("boardScene").lookup("#endActionButton").setDisable(true);
        wizardBorderPane.setVisible(false);
        assistantBorderPane.setVisible(false);
        diningRoom.setDisable(true);
        diningRoom.getChildren().forEach(n -> n.setDisable(false));
        assetsMapper.getStudentsInEntrance().values().forEach(n -> n.setDisable(true));
        for (int i = 0; i < 12; i++) {
            String string = "#island" + i;
            this.gui.getScenes().get("boardScene").lookup(string).setDisable(true);
        }
        this.gui.getScenes().get("boardScene").lookup("#characterCardButton").setDisable(true);
    }

    @FXML
    private void playStopMusic(Event event) {
        if (((ToggleButton) event.getSource()).isSelected()) {
            gui.mediaPlayer.pause();
            this.assetsMapper.getImageViewFromFXID("#volumeIcon").setImage(new Image(this.assetsMapper.getVolumeIconPath(true)));
        }
        else {
            gui.mediaPlayer.play();
            this.assetsMapper.getImageViewFromFXID("#volumeIcon").setImage(new Image(this.assetsMapper.getVolumeIconPath(false)));
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

    /**
     * Generates FX:ID of CharacterCard in the selector pane.
     * @param ID 1, 2, 3 of the card in game
     * @return FX:ID generated.
     */
    private String getCharacterCardSelectorID (int ID, boolean coin, boolean cost, boolean text) {
        String id = "";
        if (coin) id = "coin";
        if (cost) id = "cost";
        if (text) id = "text";
        id = id + "CharacterCard" + ID;

        return id;
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
