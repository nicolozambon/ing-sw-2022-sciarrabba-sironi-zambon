package it.polimi.ingsw.client.view.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
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
        stage.show();
    }
}