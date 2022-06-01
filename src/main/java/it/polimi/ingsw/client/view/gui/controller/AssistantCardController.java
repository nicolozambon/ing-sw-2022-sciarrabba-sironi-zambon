package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.model.ThinModel;
import it.polimi.ingsw.model.card.AssistantCard;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AssistantCardController implements GUIController {

    private ViewGUI gui;

    private List<Node> assistantCards;
    private int assistantCardSelectedID = -1;

    private Stage myStage;

    private void setAssistantCards() {
        Scene scene = gui.getScenes().get("assistantCardSelector");
        this.assistantCards = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Node card = scene.lookup("#" + getCardFXID(i));
            card.setVisible(false);
            this.assistantCards.add(card);
        }
    }

    public void setAssistantCards(List<AssistantCard> cards) {
        setAssistantCards();
        for (int i = 0; i < assistantCards.size(); i++) {
            assistantCards.get(i).setVisible(false);
            this.assistantCards.get(cards.get(i).getValue() - 1).setVisible(true);
        }
    }

    private String getCardFXID (int id) {
        String string = Integer.toString(id);
        if (string.length() < 2) string = "0" + string;
        return "acard" + string;
    }

    @FXML
    private void chooseAssistantCard(Event event) {
        assistantCardSelectedID = assistantCards.indexOf((Node) event.getSource());
        System.out.println(assistantCardSelectedID);
        if (this.assistantCardSelectedID != -1) {
            assistantCards.get(assistantCardSelectedID).setStyle("-fx-effect: dropshadow(three-pass-box, #ffff00, 2.0, 1.0, 0, 0);");
            for (int i = 0; i < assistantCards.size(); i++) {
                if (i != assistantCardSelectedID) assistantCards.get(i).setStyle("");
            }
        }
    }

    @FXML
    private void playAssistantCardConfirmBtn() throws Exception {
        if (this.assistantCardSelectedID > 0) {
            gui.fireRequest(new RequestEvent("playAssistantCard", gui.getId(), this.assistantCardSelectedID));
            assistantCardSelectedID = -1;
        }
        if (myStage != null) myStage.close();
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
    public void updateModel(ThinModel model) {
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    public void setMyStage(Stage stage) {
        this.myStage = stage;
    }
}
