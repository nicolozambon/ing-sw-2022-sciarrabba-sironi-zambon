package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.model.ThinModel;
import it.polimi.ingsw.model.card.AssistantCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssistantCardController implements GUIController {
    private ViewGUI gui;
    private HashMap<String, ImageView> assistantCards;
    private int assistantCardSelectedID = -1;

    private void defineAssistantCardsMap (ThinModel model) {
        for (int i = 0; i < 10; i++) {
            String key = getCardFXID(i);
            ImageView value = (ImageView) this.gui.getStage().getScene().lookup("#" + key);
            this.assistantCards.put(key, value);
            value.setVisible(false);
        }
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
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
        this.defineAssistantCardsMap(model);
        this.showAvailableCards(model);
    }

    private void showAvailableCards(ThinModel model) {
        List<AssistantCard> assistantCardsModel = model.getAssistantCardsByPlayer(gui.getId());
        for (AssistantCard card : assistantCardsModel) {
            this.assistantCards.get(card.getValue()).setVisible(true);
        }
    }

    private String getCardFXID (int ID) {
        return "acard" + ID;
    }

    @FXML
    private void chooseAssistantCard (MouseEvent event) throws Exception {
        ImageView eventSource = (ImageView) event.getSource();
        String id = eventSource.getId();
        System.out.println("eventSource: " + id);
        eventSource.setStyle("-fx-effect: dropshadow(three-pass-box, #ffff00, 2.0, 1.0, 0, 0);");
        if (this.assistantCardSelectedID != -1) {
            ImageView imageView = (ImageView) gui.getStage().getScene().lookup(getCardFXID(this.assistantCardSelectedID));
            imageView.setEffect(null);
        }
        this.assistantCardSelectedID = Integer.parseInt(String.valueOf(id.charAt(id.length() - 1)));
    }

    @FXML
    private void playAssistantCardConfirmBtn() throws Exception {
        if (this.assistantCardSelectedID > 0) {
            gui.fireRequest(new RequestEvent("playAssistantCard", gui.getId(), this.assistantCardSelectedID));
            gui.getStage().close();
        }
        assistantCardSelectedID = -1;
    }
}
