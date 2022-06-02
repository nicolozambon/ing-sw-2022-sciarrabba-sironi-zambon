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
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewGUI extends Application implements RequestListenableInterface, AnswerListener {

    private ClientConnection clientConnection  = null;

    private final RequestListenable requestListenable;

    private final Map<String, GUIController> controllerMap;
    private final Map<String, Scene> sceneMap;

    private Stage stage;
    private Stage errorStage;
    private Scene currentScene;
    private GUIController currentController;

    private int id = -1;
    private String nickname;
    private ThinModel model;
    private List<String> options;

    public Media soundtrack;
    public Media clickEffect;
    public MediaPlayer mediaPlayer;

    public MediaPlayer mediaPlayerEffect;

    public ViewGUI() {
        sceneMap = new HashMap<>();
        controllerMap = new HashMap<>();
        requestListenable =new RequestListenable();
    }

    public void startGUI() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.errorStage = new Stage();
        this.errorStage.setScene(sceneMap.get("errorScene"));
        this.errorStage.setResizable(false);
        this.errorStage.setTitle("Error");

        this.stage = stage;
        this.stage.setScene(currentScene);
        this.stage.setTitle("Eriantys");
        this.stage.setResizable(false);
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

    public HashMap<String, Scene> getScenes() {
        return new HashMap<>(this.sceneMap);
    }

    public Map<String, GUIController> getControllers() {
        return new HashMap<>(controllerMap);
    }

    public void connect(String ip, String nickname) {
        try {
            if (clientConnection == null ) {
                clientConnection = new ClientConnection(ip);
                new Thread (clientConnection).start();
                clientConnection.addAnswerListener(this);
                this.addRequestListener(clientConnection);
            }
            this.fireRequest(new RequestEvent("nickname", this.id, nickname));
        } catch (Exception e) {
            errorStage.close();
            Text text = (Text) errorStage.getScene().lookup("#errorMessage");
            StackPane stackPane = (StackPane) errorStage.getScene().lookup("#stackPane");
            text.setText("Server not found!");
            stackPane.setPrefHeight(text.getBoundsInLocal().getHeight()*2);
            errorStage.show();
        }

    }

    public void changeScene(String sceneName) {
        currentController = controllerMap.get(sceneName);
        currentScene = sceneMap.get(sceneName);
        stage.setScene(currentScene);
        stage.show();
        if (sceneName.equals("boardScene")) {
            music();
        }
    }

    protected void music() {
        //Media media = new Media(getClass().getResource("/assets/gui/music/track.mp3").toExternalForm());
        //MediaPlayer mediaPlayer = new MediaPlayer(media);
        this.soundtrack = new Media(getClass().getResource("/assets/gui/music/track.mp3").toExternalForm());
        this.mediaPlayer = new MediaPlayer(this.soundtrack);
        this.clickEffect = new Media(getClass().getResource("/assets/gui/music/pop.mp3").toExternalForm());
        this.mediaPlayerEffect = new MediaPlayer(this.clickEffect);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void playPopEffect() {
        mediaPlayerEffect.seek(mediaPlayerEffect.getStartTime());
        mediaPlayerEffect.play();
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

    public Stage getErrorStage() {
        return errorStage;
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
    public synchronized void fireRequest(RequestEvent requestEvent) {
        try {
            this.requestListenable.fireRequest(requestEvent);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
                case "options" -> {
                    this.options = answerEvent.getOptions();
                    currentController.optionsHandling(answerEvent.getOptions());
                }
                case "update" -> {
                    if (!currentScene.equals(sceneMap.get("boardScene"))) {
                        changeScene("boardScene");
                        stage.setResizable(false);
                    }
                    this.model = answerEvent.getModel();
                    currentController.updateModel(this.model);
                }
                case "wait" -> {
                    //if (this.model != null) System.out.println(this.model);
                    this.options = null;
                    currentController.onWaitEvent(answerEvent.getMessage());
                }
                case "error" -> {
                    errorStage.close();
                    Text text = (Text) errorStage.getScene().lookup("#errorMessage");
                    StackPane stackPane = (StackPane) errorStage.getScene().lookup("#stackPane");
                    text.setText(answerEvent.getMessage());
                    stackPane.setPrefHeight(text.getBoundsInLocal().getHeight()*2);
                    errorStage.show();
                    if (options != null) currentController.optionsHandling(this.options);
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
        if (clientConnection != null) clientConnection.stopClient();
        this.stage.close();
        this.errorStage.close();
    }
}