package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;

public class StartMenuController implements GUIController{

    private ViewGUI gui;

    @FXML
    private Button connectButton;
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
    private void toggleSelection() {
        RadioButton rbtn = (RadioButton) toggleRadioButton.getSelectedToggle();
        if (rbtn.equals(customRadioButton)) ipTextField.setDisable(false);
        else ipTextField.setDisable(true);
    }

    @FXML
    private void setConnectButton() throws IOException {
        if (customRadioButton.isSelected()) gui.connect(ipTextField.getText(), nicknameTextField.getText());
        if (localhostRadioButton.isSelected()) gui.connect("127.0.0.1",nicknameTextField.getText());
        if (onlineRadioButton.isSelected()) gui.connect("127.0.0.1",nicknameTextField.getText());
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }
}
