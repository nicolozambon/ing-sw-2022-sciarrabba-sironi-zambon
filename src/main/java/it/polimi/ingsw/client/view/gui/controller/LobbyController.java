package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.events.AnswerEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.util.List;

public class LobbyController implements GUIController {

    private ViewGUI gui;

    @FXML
    private VBox playersVBox;
    @FXML
    private ImageView testImageView;

    @FXML
    private void exitButtonClicked() {
        System.out.println("exit");
        try {
            this.gui.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void optionsHandling(List<String> options) {
        playersVBox.getChildren().clear();
        options.forEach(n -> playersVBox.getChildren().add(new Text(n)));
    }
}
