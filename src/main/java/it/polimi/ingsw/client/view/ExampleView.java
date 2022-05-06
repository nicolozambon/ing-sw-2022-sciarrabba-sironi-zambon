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

    private Scanner stdin;
    private final Client client;

    private ModelSerializable model;
    private String nickname;
    private int id;

    private List<String> options;
    private final OptionStringifier optionStringifier;

    public ExampleView(Client client) {
        this.stdin = new Scanner(System.in);
        this.client = client;
        this.optionStringifier = new OptionStringifier();
    }


    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {
        switch(answerEvent.getPropertyName()) {
            case "nickname" ->  setNickname(answerEvent);
            case "first_player" -> setNumOfPlayers(answerEvent);
            case "set_id" -> {
                this.id = (int) answerEvent.getValue();
                System.out.println("my id: " + this.id);
            }
            case "wait" -> System.out.println(optionStringifier.stringify(answerEvent.getPropertyName()));
            case "error" -> {
                //TODO Not handle setup error
                System.out.println((String) answerEvent.getValue());
                optionsHandler();
            }
            case "options" -> setOptions(answerEvent);
            case "update" -> updateModel(answerEvent);
            default -> System.out.println("Answer Error!");
        }
    }

    private void setNickname(AnswerEvent answerEvent) {
        System.out.println(optionStringifier.stringify(answerEvent.getPropertyName()));
        this.nickname = stdin.nextLine();
        client.send(new RequestEvent("nickname", 0, nickname));
    }

    private void setNumOfPlayers(AnswerEvent answerEvent) {
        System.out.println(optionStringifier.stringify(answerEvent.getPropertyName()));
        int num = stdin.nextInt();
        stdin.nextLine();
        client.send(new RequestEvent("first_player", 0, num));
    }

    private void setOptions(AnswerEvent answerEvent) {
        this.options = (List<String>) answerEvent.getValue();
        System.out.println(this.model);
        optionsHandler();
    }

    private void updateModel(AnswerEvent answerEvent) {
        Gson gson = new Gson();
        Model temp = gson.fromJson((String)answerEvent.getValue(), Model.class);
        this.model = new ModelSerializable(temp);
    }

    private void defaultInputHandler(int option) {
        System.out.println("Make your choice:");
        int choice = stdin.nextInt();
        stdin.nextLine();
        client.send(new RequestEvent(this.options.get(option), this.id, choice));
    }

    private void twoInputHandler(int option) {
        System.out.println("Choose student and island es. 1,2 : ");
        String[] input = stdin.nextLine().split(",");
        int student = Integer.parseInt(input[0]);
        int island = Integer.parseInt(input[1]);
        System.out.println(student + " " + island);
        client.send(new RequestEvent(this.options.get(option), this.id, student, island));
    }

    private void noInputHandler(int option) {
        client.send(new RequestEvent(this.options.get(option), this.id));
    }

    private void optionsHandler() {
        if (this.options.size() > 0) {
            System.out.println(optionStringifier.stringify(options));
            int choice = stdin.nextInt();
            stdin.nextLine();
            if (choice <= options.size() && choice > 0) {
                choice = choice - 1;
                switch(this.options.get(choice)) {
                    case "moveStudentToIsland" -> twoInputHandler(choice);
                    case "endAction" -> noInputHandler(choice);
                    default -> defaultInputHandler(choice);
                }
            }
        }
    }

}
