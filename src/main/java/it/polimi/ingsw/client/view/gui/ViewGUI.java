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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewGUI Main Class, start, setup and Stage Handling
 */
public class ViewGUI extends Application implements RequestListenableInterface, AnswerListener {

    /**
     * ClientConnection associated to the client
     */
    private ClientConnection clientConnection  = null;

    /**
     * Listenable for request sending and receiving
     */
    private final RequestListenable requestListenable;

    /**
     * Maps the GUI's Controller for each scene
     */
    private final Map<String, GUIController> controllerMap;

    /**
     * Maps the GUI's scenes to their name
     */
    private final Map<String, Scene> sceneMap;

    /**
     * Stage of the GUI
     */
    private Stage stage;

    /**
     * Stage for error alerts
     */
    private Stage errorStage;

    /**
     * currentScene loaded and displayed in stage
     */
    private Scene currentScene;

    /**
     * Current controller used
     */
    private GUIController currentController;

    /**
     * ID of the GUI (View)
     */
    private int id = -1;

    /**
     * Player's Nickname
     */
    private String nickname;

    /**
     * ThinModel instance, representation of the model for the view
     */
    private ThinModel model;

    /**
     * Available options
     */
    private List<String> options;

    /**
     * Media for the in-game music
     */
    public Media soundtrack;

    /**
     * Media for the click sound effect
     */
    public Media clickEffect;

    /**
     * MediaPlayer for the in-game music
     */
    public MediaPlayer mediaPlayer;

    /**
     * MediaPlayer for the click sound effect
     */
    public MediaPlayer mediaPlayerEffect;

    /**
     * ViewGUI Constructor
     */
    public ViewGUI() {
        sceneMap = new HashMap<>();
        controllerMap = new HashMap<>();
        requestListenable =new RequestListenable();
    }

    /**
     * Method that launches the GUI and loads the font as a resource
     */
    public void startGUI() {
        Font.loadFonts(getClass().getResource("/assets/gui/fonts/Avenir_Next_Condensed.ttc").toExternalForm(), 16);
        launch();
    }

    /**
     * Start and initialize the main stage and error stage
     * @param stage main stage
     * @throws IOException if there is an error in loading the icon's images
     */
    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.errorStage = new Stage();
        this.errorStage.setScene(sceneMap.get("errorScene"));
        this.errorStage.setResizable(false);
        this.errorStage.setTitle("Error");
        this.errorStage.getIcons().add(new Image("/assets/gui/images/utils/errorIcon.png"));

        this.stage = stage;
        this.stage.setScene(currentScene);
        this.stage.setTitle("Eriantys");
        this.stage.setResizable(false);
        this.stage.getIcons().add(new Image("/assets/gui/images/utils/desktop_icon.png"));
        this.stage.show();
        this.stage.setAlwaysOnTop(true);
        this.stage.setAlwaysOnTop(false);
    }

    /**
     * Setup of the scenes, mapped and loaded the corresponding .fxml. Initializes the MediaPlayers and sets currentScene and
     * currentController for the startMenu scene
     */
    public void setup() {
        List<String> fxmlScenes = new ArrayList<>(List.of("startMenuScene", "lobbyScene", "errorScene", "boardScene", "winnerScene"));
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

        initMusicPlayers();

        currentScene = sceneMap.get("startMenuScene");
        currentController = controllerMap.get("startMenuScene");
    }

    /**
     * @return all available scenes
     */
    public HashMap<String, Scene> getScenes() {
        return new HashMap<>(this.sceneMap);
    }

    /**
     * Connects the GUI to the Server, initializing clientConnection
     * @param ip server's IP Address
     * @param nickname nickname chosen by the player
     */
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

    /**
     * Change the currentScene in the stage
     * @param sceneName the scene name
     */
    public void changeScene(String sceneName) {
        currentController = controllerMap.get(sceneName);
        currentScene = sceneMap.get(sceneName);
        stage.setScene(currentScene);
        stage.show();
        if (sceneName.equals("boardScene")) {
            music();
        }
    }

    /**
     * Setup of the MediaPlayers and Media loading
     */
    private void initMusicPlayers() {
        this.soundtrack = new Media(getClass().getResource("/assets/gui/music/track.mp3").toExternalForm());
        this.mediaPlayer = new MediaPlayer(this.soundtrack);
        this.clickEffect = new Media(getClass().getResource("/assets/gui/music/pop.mp3").toExternalForm());
        this.mediaPlayerEffect = new MediaPlayer(this.clickEffect);
    }

    /**
     * Start the in-game soundtrack, and set it to repeat
     */
    private void music() {
        mediaPlayer.setVolume(40);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * Play the click sound effect
     */
    public void playPopEffect() {
        mediaPlayerEffect.setVolume(50);
        mediaPlayerEffect.seek(mediaPlayerEffect.getStartTime());
        mediaPlayerEffect.play();
    }

    /**
     * @return the nickname associated with this GUI
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return this GUI's ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return ThinModel representation of the model that this GUI holds
     */
    public ThinModel getModel() {
        return model;
    }

    /**
     * @return the currently displayed stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return the Error Stage
     */
    public Stage getErrorStage() {
        return errorStage;
    }

    /**
     * Adds the specified requestListener object
    * @param requestListener to add
     * @see RequestListenable
     */
    @Override
    public void addRequestListener(RequestListener requestListener) {
        this.requestListenable.addRequestListener(requestListener);
    }

    /**
     * Removes the specified requestListener from the requestListenable list
     * @param requestListener to remove
     */
    @Override
    public void removeRequestListener(RequestListener requestListener) {
        this.requestListenable.removeRequestListener(requestListener);
    }

    /**
     * Fires a new request Event incoming from the GUI's choices to the server
     * @param requestEvent the event requested
     */
    @Override
    public synchronized void fireRequest(RequestEvent requestEvent) {
        try {
            this.requestListenable.fireRequest(requestEvent);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Provides all actions to be taken upon receiving an answerEvent, given its type
     * @param answerEvent received event
     */
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
                        stage.close();
                        changeScene("boardScene");
                        stage.setResizable(false);
                        stage.show();
                        stage.setAlwaysOnTop(true);
                        stage.setAlwaysOnTop(false);
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
                    mediaPlayer.pause();
                    stage.close();

                    changeScene("errorScene");
                    Text text = (Text) currentScene.lookup("#errorMessage");
                    StackPane stackPane = (StackPane) currentScene.lookup("#stackPane");
                    Button btn = (Button) currentScene.lookup("#okErrorBtn");
                    btn.setOnAction(event -> exitGame());

                    if (answerEvent.getMessage() != null) {
                        text.setText(answerEvent.getMessage());
                    } else text.setText("Server unreachable!");
                    stackPane.setPrefHeight(text.getBoundsInLocal().getHeight()*2);

                    stage.show();
                    stage.setAlwaysOnTop(true);
                    stage.setAlwaysOnTop(false);
                }
                case "winner" -> {
                    if (this.model != null) currentController.updateModel(this.model);
                    this.mediaPlayer.pause();

                    //Disable boardScene
                    currentScene.lookup("#mainPane").setOpacity(0.5);
                    currentScene.lookup("#mainPane").setDisable(true);

                    //Set winnerScene
                    controllerMap.get("winnerScene").optionsHandling(answerEvent.getOptions());
                    Stage winnerStage = new Stage();
                    winnerStage.setScene(this.getScenes().get("winnerScene"));
                    winnerStage.show();
                    winnerStage.setAlwaysOnTop(true);
                    winnerStage.setAlwaysOnTop(false);
                }
                default -> System.out.println("Answer Error!");
            }
        });
    }

    /**
     * @return all available options that can be taken
     */
    public List<String> getOptions() {
        return new ArrayList<>(options);
    }

    /**
     * Stops the GUI if the clientConnection is valid
     */
    @Override
    public void stop() {
        if (clientConnection != null) clientConnection.stopClient();
    }

    /**
     * Closes the application entirely
     */
    private void exitGame() {
        Platform.exit();
    }
}