package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ThinModel;

import java.util.ArrayList;
import java.util.List;

public class ExampleView implements AnswerListener {

    private final Client client;

    private String nickname;
    private int id;

    private List<String> options;
    private final OptionLister optionLister;
    private final OptionHandler optionHandler;

    public ExampleView(Client client) {
        this.client = client;
        this.optionLister = new OptionLister();
        this.options = new ArrayList<>();
        this.optionHandler = new OptionHandler();
    }


    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {
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
            //TODO not setting nickname in example view
            case "options" -> setOptions(answerEvent);
            case "update" -> updateModel(answerEvent);
            case "wait" -> System.out.println(optionLister.list(answerEvent.getPropertyName()));
            case "error" -> {
                System.out.println(answerEvent.getMessage());
                client.send(optionHandler.getRequestEvent(this.options));
            }
            default -> System.out.println("Answer Error!");
        }
    }


    private void setOptions(AnswerEvent answerEvent) {
        this.options = answerEvent.getOptions();
        client.send(optionHandler.getRequestEvent(this.options));
    }

    private void updateModel(AnswerEvent answerEvent) {
        Model temp = answerEvent.getModel();
        ThinModel model = new ThinModel(temp);
        System.out.println(model);
    }


}
