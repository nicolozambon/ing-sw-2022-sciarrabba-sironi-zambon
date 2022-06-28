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

/**
 * GUI Controller for the Start Menu Scene
 */
public class StartMenuController implements GUIController{
    /**
     * Current GUI
     */
    private ViewGUI gui;

    /**
     * Text Field to input a custom IP Address of the server
     */
    @FXML
    private TextField ipTextField;

    /**
     * Text Field to input the user's nickname
     */
    @FXML
    private TextField nicknameTextField;

    /**
     * Toggle Group of options for the server's IP Address
     */
    @FXML
    private ToggleGroup toggleRadioButton;

    /**
     * Localhost IP Address Option button
     */
    @FXML
    private RadioButton localhostRadioButton;

    /**
     * Custom IP Address Option button (TextField)
     */
    @FXML
    private RadioButton customRadioButton;

    /**
     * Online Server IP Address Option button - Preset for the Online Server of the Game
     */
    @FXML
    private RadioButton onlineRadioButton;

    /**
     * Pane to show only if the current user is the first player in lobby.
     * Contains options with regard to the match's settings - number of players and Expert/Simplified game rules
     */
    @FXML
    private StackPane firstPlayerStackPane;

    /**
     * Expert Rules Checkbox: if flagged, the match will be played with the expert rules
     */
    @FXML
    private CheckBox expertCheckBox;

    /**
     * Setter for this.gui
     * @param gui current ViewGUI
     */
    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    /**
     * Option Handling method for the Start Menu Scene
     * @param options available options to the controller
     */
    @Override
    public void optionsHandling(List<String> options) {
        if (options.get(0).equals("firstPlayer")) {
            gui.getStage().getScene().lookup("#waitPane").setVisible(false);
            firstPlayerStackPane.setVisible(true);
        }
    }

    /**
     * Actions to take if a wait event is received
     * @param name
     */
    @Override
    public void onWaitEvent(String name) {
        firstPlayerStackPane.setVisible(false);
        this.gui.getStage().getScene().lookup("#waitPane").setVisible(true);
    }

    /**
     * Not implemented in this class
     */
    @Override
    public void updateModel(ThinModel model) {

    }

    /**
     * Disable the text field of the custom IP Address if the customRadioButton is not selected
     */
    @FXML
    private void toggleSelection() {
        ipTextField.setDisable(!toggleRadioButton.getSelectedToggle().equals(customRadioButton));
    }

    /**
     * Sets the match's options when the user clicks the icon of 2 or 3 players accordingly, also setting the expert mode on or off
     * @param event raised by the user's action
     */
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

    /**
     * Connect custom ImageView Button at startup
     * @param event raised by the user's action
     */
    @FXML
    private void connectImageViewOnClick(Event event) {
        if (customRadioButton.isSelected()) gui.connect(ipTextField.getText(), nicknameTextField.getText());
        if (localhostRadioButton.isSelected()) gui.connect("127.0.0.1", nicknameTextField.getText());
        if (onlineRadioButton.isSelected()) gui.connect("10.8.0.1", nicknameTextField.getText());
        gui.playPopEffect();
        event.consume();
    }

}
