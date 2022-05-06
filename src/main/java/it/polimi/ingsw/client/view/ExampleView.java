package it.polimi.ingsw.client.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExampleView implements AnswerListener {

    private final Scanner stdin;
    private final Client client;
    private final Gson gson;

    private String nickname;
    private int id;

    private List<String> options;
    private final OptionLister optionLister;
    private OptionHandler optionHandler;

    public ExampleView(Client client) {
        this.stdin = new Scanner(System.in);
        this.client = client;
        this.optionLister = new OptionLister();
        this.options = new ArrayList<>();
        this.gson = new Gson();
        this.optionHandler = new OptionHandler(-1);
    }


    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {
        switch(answerEvent.getPropertyName()) {
            case "set_id" -> {
                this.id = (int) answerEvent.getValue();
                this.optionHandler = new OptionHandler(this.id);
            }
            //TODO not setting nickname in example view
            case "options" -> setOptions(answerEvent);
            case "update" -> updateModel(answerEvent);
            case "wait" -> System.out.println(optionLister.list(answerEvent.getPropertyName()));
            case "error" -> {
                System.out.println((String) answerEvent.getValue());
                client.send(optionHandler.getRequestEvent(this.options));
            }
            default -> System.out.println("Answer Error!");
        }
    }


    private void setOptions(AnswerEvent answerEvent) {
        this.options = gson.fromJson((String) answerEvent.getValue(), new TypeToken<List<String>>(){}.getType());
        client.send(optionHandler.getRequestEvent(this.options));
    }

    private void updateModel(AnswerEvent answerEvent) {
        Model temp = gson.fromJson((String)answerEvent.getValue(), Model.class);
        ModelSerializable model = new ModelSerializable(temp);
        System.out.println(model);
    }


}
