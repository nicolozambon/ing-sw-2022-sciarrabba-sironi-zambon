package it.polimi.ingsw.client.view.gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        /*
        stage.setTitle("Hello World!");
        Button btn = new Button();
        Label label = new Label();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                label.setText("Hello World!");
                System.out.println("Hello World!");
            }
        });

        StackPane stackPane = new StackPane(btn, label);
        StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
        stage.setScene(new Scene(stackPane, 300, 250));
        stage.show(); */

        Parent root = FXMLLoader.load(getClass().getResource("/assets/gui/scenes/startMenu.fxml"));

        Scene scene = new Scene(root, 980, 720);

        RadioButton rBtnIP = (RadioButton) scene.lookup("#customIPID");
        TextField textFieldIP = (TextField) scene.lookup("#textFieldIP");
        rBtnIP.setOnAction(actionEvent -> {
            if (rBtnIP.isSelected()) {
                textFieldIP.setStyle("display: block");
            } else {
                textFieldIP.setStyle("display: none");
            }
        });
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();


    }
}