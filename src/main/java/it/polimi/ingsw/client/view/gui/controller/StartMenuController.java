package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.events.RequestEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

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
    private Pane firstPlayerPane;

    @FXML
    private void toggleSelection() {
        RadioButton rbtn = (RadioButton) toggleRadioButton.getSelectedToggle();
        ipTextField.setDisable(!rbtn.equals(customRadioButton));
    }

    @FXML
    private void connectButtonOnClick() throws Exception {
        if (customRadioButton.isSelected()) gui.connect(ipTextField.getText(), nicknameTextField.getText());
        if (localhostRadioButton.isSelected()) gui.connect("127.0.0.1", nicknameTextField.getText());
        if (onlineRadioButton.isSelected()) gui.connect("127.0.0.1", nicknameTextField.getText());
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void optionsHandling(List<String> options) {
        if (options.get(0).equals("first_player")) {
            firstPlayerPane.setVisible(true);
        }
    }

    @FXML
    private void twoPlayersButtonOnClick() {
        try {
            gui.fireRequest(new RequestEvent("first_player", gui.getId(), 2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void threePlayersButtonOnClick() {
        try {
            gui.fireRequest(new RequestEvent("first_player", gui.getId(),3));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
