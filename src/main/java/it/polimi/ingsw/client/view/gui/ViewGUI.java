package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.view.gui.controllers.GUIController;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class ViewGUI extends Application implements RequestListenableInterface, AnswerListener {

    ClientConnection clientConnection;

    private RequestListenable requestListenable;
    private Map<String, GUIController> guiControllerMap;

    private int id;
    private String nickname;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/gui/scenes/startMenu.fxml"));
        Scene scene = new Scene(loader.load());
        GUIController controller = loader.getController();
        controller.setGUI(this);
        stage.setScene(scene);
        stage.setTitle("FXML Welcome");
        stage.show();
    }

    public void setup() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/gui/scenes/startMenu.fxml"));
        GUIController controller = loader.getController();
        System.out.println(controller);
        //controller.setGUI(this);
    }

    public void connect(String ip, String nickname) throws IOException {
        clientConnection = new ClientConnection(ip);
        new Thread (clientConnection).start();
        clientConnection.addAnswerListener(this);
        clientConnection.send(new RequestEvent("nickname", this.id, nickname));
    }

    @Override
    public void addRequestListener(RequestListener requestListener) {
        this.requestListenable.addRequestListener(requestListener);
    }

    @Override
    public void removeRequestListener(RequestListener requestListener) {
        this.requestListenable.removeRequestListener(requestListener);
    }

    @Override
    public void fireRequest(RequestEvent requestEvent) throws Exception {
        this.requestListenable.fireRequest(requestEvent);
    }

    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {

    }
}