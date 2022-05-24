package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.view.gui.controller.GUIController;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.model.ThinModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewGUI extends Application implements RequestListenableInterface, AnswerListener {

    private ClientConnection clientConnection;

    private RequestListenable requestListenable;

    private Map<String, GUIController> controllerMap;
    private Map<String, Scene> sceneMap;

    private Stage stage;
    private Scene currentScene;
    private GUIController currentController;

    private int id = -1;
    private String nickname;
    private ThinModel model;

    public ViewGUI() {
        sceneMap =new HashMap<>();
        controllerMap = new HashMap<>();
        requestListenable =new RequestListenable();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.stage = stage;
        this.stage.setScene(currentScene);
        this.stage.setTitle("Eriantys");
        this.stage.show();
    }

    public void setup() {
        List<String> fxmlScenes = new ArrayList<>(List.of("startMenuScene", "lobbyScene", "errorScene", "boardScene"));
        FXMLLoader loader;
        GUIController controller;
        try {
            for (String scene : fxmlScenes) {
                loader = new FXMLLoader(getClass().getResource("/assets/gui/scenes/" + scene + ".fxml"));
                sceneMap.put(scene, new Scene(loader.load()));
                controller = loader.getController();
                controller.setGUI(this);
                controllerMap.put(scene, controller);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentScene = sceneMap.get("startMenuScene");
        currentController = controllerMap.get("startMenuScene");
    }

    public void connect(String ip, String nickname) throws Exception {
        clientConnection = new ClientConnection(ip);
        new Thread (clientConnection).start();
        clientConnection.addAnswerListener(this);
        this.addRequestListener(clientConnection);
        this.fireRequest(new RequestEvent("nickname", this.id, nickname));
    }

    public void changeScene(String sceneName) {
        currentController = controllerMap.get(sceneName);
        currentScene = sceneMap.get(sceneName);
        stage.setScene(currentScene);
        stage.show();
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public ThinModel getModel() {
        return model;
    }

    public Stage getStage() {
        return stage;
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
    public synchronized void onAnswerEvent(AnswerEvent answerEvent) {
        Platform.runLater(() -> {
            switch(answerEvent.getPropertyName()) {
                case "set_nickname" -> this.nickname = answerEvent.getMessage();
                case "set_id" -> this.id = answerEvent.getNum();
                case "lobby" -> {
                    if (!currentScene.equals(sceneMap.get("lobbyScene"))) changeScene("lobbyScene");
                    currentController.optionsHandling(answerEvent.getOptions());
                }
                case "options" -> currentController.optionsHandling(answerEvent.getOptions());
                case "update" -> {
                    if (!currentScene.equals(sceneMap.get("boardScene"))) changeScene("boardScene");
                    this.model = new ThinModel(answerEvent.getModel());
                }
                case "error" -> {
                    Stage stage = new Stage();
                    stage.setScene(sceneMap.get("errorScene"));
                    stage.show();
                    controllerMap.get("errorScene").optionsHandling(new ArrayList<>(List.of(answerEvent.getMessage())));
                }
                case "stop" -> {
                    System.out.println(answerEvent.getMessage());
                    this.stop();
                }
                default -> System.out.println("Answer Error!");
            }
        });
    }

    @Override
    public void stop() {
        clientConnection.stopClient();
        this.stage.close();
    }
}