package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ThinModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewCLI implements AnswerListener, RequestListenableInterface {

    private Client client;

    private String nickname;
    private int id;

    private List<String> options;
    private final OptionLister optionLister;
    private final OptionHandler optionHandler;

    private final RequestListenable requestListenable;

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

            case "options" -> setOptions(answerEvent);
            case "update" -> updateModel(answerEvent);
            case "wait" -> System.out.println(optionLister.list(answerEvent.getPropertyName()));
            case "error" -> {
                System.out.println(answerEvent.getMessage());
                client.send(optionHandler.getRequestEvent(this.options));
            }
            case "stop" -> {
                System.out.println(answerEvent.getMessage());
                client.stopClient();
            }
            default -> System.out.println("Answer Error!");
        }
    }


    private void setOptions(AnswerEvent answerEvent) {
        this.options = answerEvent.getOptions();
        fireRequest(optionHandler.getRequestEvent(this.options));
    }

    private void updateModel(AnswerEvent answerEvent) {
        Model temp = answerEvent.getModel();
        ThinModel model = new ThinModel(temp);
        System.out.println(model);
    }

    public void startCLI() throws IOException {
        /*Scanner stdin = new Scanner(System.in);
        System.out.println("Server IP:");
        String ip = stdin.nextLine();
        this.client = new Client(ip);*/
        this.client = new Client();
        this.client.addAnswerListener(this);
        this.addRequestListener(this.client);
        new Thread(this.client).start();
        setOptions(new AnswerEvent("options", this.options));
    }

    public static void main(String[] args) throws IOException{
        ViewCLI view = new ViewCLI();
        view.startCLI();
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
