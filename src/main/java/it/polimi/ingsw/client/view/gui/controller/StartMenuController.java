package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.model.ThinModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

import java.util.List;

public class StartMenuController implements GUIController{

    private ViewGUI gui;

    @FXML
    private TextField ipTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private ToggleGroup toggleRadioButton;
    @FXML
    private RadioButton localhostRadioButton;
    @FXML
    private RadioButton customRadioButton;
    @FXML
    private RadioButton onlineRadioButton;

    @FXML
    private StackPane firstPlayerStackPane;

    @FXML
    private CheckBox expertCheckBox;

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void optionsHandling(List<String> options) {
        if (options.get(0).equals("firstPlayer")) {
            gui.getStage().getScene().lookup("#waitPane").setVisible(false);
            firstPlayerStackPane.setVisible(true);
        }
    }

    @Override
    public void onWaitEvent(String name) {
        firstPlayerStackPane.setVisible(false);
        this.gui.getStage().getScene().lookup("#waitPane").setVisible(true);
    }


    @Override
    public void updateModel(ThinModel model) {

    }

    @FXML
    private void toggleSelection() {
        ipTextField.setDisable(!toggleRadioButton.getSelectedToggle().equals(customRadioButton));
    }

    @FXML
    private void numPlayerOnClick(Event event) {
        int expert = 0;
        if (expertCheckBox.isSelected()) expert = 1;
        String sourceId =  ((Node) event.getSource()).getId();
        int num = Integer.parseInt(sourceId.substring(sourceId.length()-1));
        try {
            gui.fireRequest(new RequestEvent("firstPlayer", gui.getId(), num,expert));
            gui.playPopEffect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void connectImageViewOnClick(Event event) {
        if (customRadioButton.isSelected()) gui.connect(ipTextField.getText(), nicknameTextField.getText());
        if (localhostRadioButton.isSelected()) gui.connect("127.0.0.1", nicknameTextField.getText());
        if (onlineRadioButton.isSelected()) gui.connect("10.8.0.1", nicknameTextField.getText());
        gui.playPopEffect();
        event.consume();
    }

}
