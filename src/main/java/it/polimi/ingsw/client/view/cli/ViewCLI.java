package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.model.ThinModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewCLI implements AnswerListener, RequestListenableInterface {

    /**
     * Client connection
     */
    private ClientConnection clientConnection;

    /**
     * Nickname
     */
    private String nickname;

    /**
     * Identifier
     */
    private int id;

    /**
     * List of options
     */
    private List<String> options;

    /**
     * Instance of OptionLister
     */
    private final OptionLister optionLister;

    /**
     * Instance of OptionHandler
     */
    private final OptionHandler optionHandler;

    /**
     * Instance of RequestListenable
     */
    private final RequestListenable requestListenable;

    /**
     * Instance of ThinModel
     */
    private ThinModel model = null;

    /**
     * Instance of CLIBuilder
     */
    private final CLIBuilder cliBuilder;

    /**
     * Constructor of ViewCLI
     */
    public ViewCLI() {
        this.optionLister = new OptionLister();
        this.options = new ArrayList<>(List.of("nickname"));
        this.optionHandler = new OptionHandler();
        this.requestListenable = new RequestListenable();
        this.cliBuilder = new CLIBuilder();
    }

    @Override
    public synchronized void onAnswerEvent(AnswerEvent answerEvent) {
        switch(answerEvent.getPropertyName()) {
            case "set_nickname" -> this.nickname = answerEvent.getMessage();
            case "lobby" -> System.out.println(
                    "Player in lobby: " +
                    answerEvent.getOptions().toString().substring(1, answerEvent.getOptions().toString().length() - 1));
            case "set_id" -> {
                this.id = answerEvent.getNum();
                this.optionHandler.setPlayerId(this.id);
            }
            case "options" -> handleOptions(answerEvent);
            case "update" -> updateModel(answerEvent);
            case "wait" -> {
                if (this.model != null) cliBuilder.buildCLI(model, nickname).showGameBoard();
                System.out.print(optionLister.list(answerEvent.getPropertyName()));
                if (answerEvent.getMessage() != null) System.out.print(", it is " + answerEvent.getMessage() + "'s turn");
                System.out.print("\n");
            }
            case "error" -> {
                System.out.println(answerEvent.getMessage());
                handleOptions(new AnswerEvent("error", this.options));
            }
            case "stop" -> {
                if (answerEvent.getMessage() != null) {
                    System.out.println(answerEvent.getMessage());
                } else System.out.println("Server unreachable!");
            }
            case "winner" -> {
                if (this.model != null) cliBuilder.buildCLI(model, nickname).showGameBoard();
                List<String> winners = answerEvent.getOptions();
                if (winners.contains(nickname)) winners.set(winners.indexOf(nickname), "You");
                System.out.println("Winners of this game: " +
                        answerEvent.getOptions().toString().substring(1, answerEvent.getOptions().toString().length() - 1));
            }
            default -> System.out.println("Answer Error!");
        }

    }

    /**
     * Handle options method
     * @param answerEvent AnswerEvent instance
     */
    private void handleOptions(AnswerEvent answerEvent) {
        this.options = answerEvent.getOptions();
        if (this.model != null && !answerEvent.getPropertyName().equals("error")) cliBuilder.buildCLI(this.model, nickname).showGameBoard();
        RequestEvent requestEvent = null;
        try {
            requestEvent = optionHandler.getRequestEvent(this.options);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        if (requestEvent != null) fireRequest(requestEvent);
    }

    /**
     * Update model
     * @param answerEvent AnswerEvent instance
     */
    private void updateModel(AnswerEvent answerEvent) {
        this.model = answerEvent.getModel();
    }

    /**
     * Start CLI method
     */
    public void startCLI() {
        Scanner stdin = new Scanner(System.in);
        System.out.println("Server IP:");
        String ip = stdin.nextLine();
        try {
            this.clientConnection = new ClientConnection(ip);
            this.clientConnection.addAnswerListener(this);
            this.addRequestListener(this.clientConnection);
            new Thread(this.clientConnection).start();
            handleOptions(new AnswerEvent("options", this.options));
        } catch (IOException e) {
            System.out.println("Server not found!");
        }

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
    public void fireRequest(RequestEvent requestEvent) {
        try {
            this.requestListenable.fireRequest(requestEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
