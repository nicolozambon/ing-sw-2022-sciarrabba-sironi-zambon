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
import javafx.scene.text.Text;

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

    @Override
    public void optionsHandling(List<String> options) {
        wizardBorderPane.setVisible(false);
        assistantBorderPane.setVisible(false);
        if (options.size() == 1) {
            switch (options.get(0)) {
                case "chooseWizard" -> wizardBorderPane.setVisible(true);
                case "playAssistantCard" -> setAssistantCards(this.gui.getModel().getAssistantCardsByPlayer(gui.getId()));
            }
        }
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

            // Add towers
            for (int j=0; j<model.getNumTowerByPlayer(i); j++) {
                ImageView tower = this.assetsMapper.getTowerOnSchool(i, model.getTowerColorByPlayer(i), j);
                tower.setVisible(true);
            }

            // Add professors
            for (Color professorColor : model.getProfessorsByPlayer(i)) {
                ImageView professor = this.assetsMapper.getProfessorOnSchool(i, professorColor);
                professor.setVisible(true);
            }

            // Add dining room
            for (Color color : Color.values()) {
                for (int j = 0; j<model.getDiningRoomByPlayer(i).get(color); j++) {
                    ImageView student = this.assetsMapper.getStudentInDiningRoom(i, j, color);
                    student.setVisible(true);
                }
            }

            // Add entrance
            for (int j = 0; j<model.getEntranceByPlayer(i).size(); j++) {
                ImageView student = this.assetsMapper.getStudentInEntrance(i, j);
                String path = this.assetsMapper.getStudentPath(model.getEntranceByPlayer(i).get(j));
                Image studentPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                student.setImage(studentPawn);
                student.setVisible(true);
            }
        }
    }

    private void setupIslands(ThinModel model) {
        for (int i=0; i<12; i++) {

            // Show mother nature
            if (model.getMNPosition() == i) {
                ImageView motherNature = this.assetsMapper.getMotherNature(i);
                motherNature.setVisible(true);
            }

            // Show students
            for (Color color : Color.values()) {
                Integer numberOfStudents = model.getStudentOnIsland(i).get(color);
                if (numberOfStudents > 0) {
                    // Set label
                    Text label = this.assetsMapper.getStudentLabel(i, color);
                    label.setText("x" + numberOfStudents.toString());

                    // Show pawn
                    ImageView pawn = this.assetsMapper.getStudentPawn(i, color);
                    pawn.setVisible(true);
                }
            }

            // Show bridge
            if (model.isIslandLinkedNext(i)) {
                ImageView bridge = this.assetsMapper.getBridgeOnIsland(i);
                bridge.setVisible(true);
            }

            // Show tower
            TowerColor towerColor = model.getTowerColorOnIsland(i);
            if (towerColor != null) {
                ImageView tower = this.assetsMapper.getTowerOnIsland(i);
                Image towerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(this.assetsMapper.getTowerPath(towerColor))));
                tower.setImage(towerImage);
                tower.setVisible(true);
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
}
