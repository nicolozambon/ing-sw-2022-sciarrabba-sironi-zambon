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

public class ViewCLI implements AnswerListener, RequestListenableInterface {

    private ClientConnection clientConnection;

    private String nickname;
    private int id;

    private List<String> options;
    private final OptionLister optionLister;
    private final OptionHandler optionHandler;

    private final RequestListenable requestListenable;

    private ThinModel model = null;

    public ViewCLI() {
        this.optionLister = new OptionLister();
        this.options = new ArrayList<>() {{
            this.add("nickname");
        }};
        this.optionHandler = new OptionHandler();
        this.requestListenable = new RequestListenable();
    }


    @Override
    public synchronized void onAnswerEvent(AnswerEvent answerEvent) {
        switch(answerEvent.getPropertyName()) {
            case "set_id" -> {
                this.id = answerEvent.getNum();
                this.optionHandler.setPlayerId(this.id);
                System.out.println("My id is: " + this.id);
            }

            case "set_nickname" -> {
                this.nickname = answerEvent.getMessage();
                System.out.println("My nickname is: " + this.nickname);
            }

            case "options" -> handleOptions(answerEvent);
            case "update" -> updateModel(answerEvent);
            //case "wait" -> System.out.println(optionLister.list(answerEvent.getPropertyName()));
            case "error" -> {
                System.out.println(answerEvent.getMessage());
                handleOptions(new AnswerEvent("error", this.options));
            }
            case "stop" -> {
                System.out.println(answerEvent.getMessage());
                clientConnection.stopClient();
            }
            default -> System.out.println("Answer Error!");
        }

    }


    private void handleOptions(AnswerEvent answerEvent) {
        this.options = answerEvent.getOptions();
        if (this.model != null && !answerEvent.getPropertyName().equals("error")) System.out.println(this.model);
        RequestEvent requestEvent = null;
        try {
            requestEvent = optionHandler.getRequestEvent(this.options);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (requestEvent != null) fireRequest(requestEvent);
    }

    private void updateModel(AnswerEvent answerEvent) {
        this.model = new ThinModel(answerEvent.getModel());
    }

    //TODO default loopback ip for testing purpose
    public void startCLI() throws IOException {
        /*Scanner stdin = new Scanner(System.in);
        System.out.println("Server IP:");
        String ip = stdin.nextLine();
        this.client = new Client(ip);*/
        this.clientConnection = new ClientConnection();
        this.clientConnection.addAnswerListener(this);
        this.addRequestListener(this.clientConnection);
        new Thread(this.clientConnection).start();
        handleOptions(new AnswerEvent("options", this.options));
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
