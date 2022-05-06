package it.polimi.ingsw.client.view;

import com.google.gson.Gson;
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

    private String nickname;
    private int id;

    private List<String> options;
    private final OptionStringifier optionStringifier;

    public ExampleView(Client client) {
        this.stdin = new Scanner(System.in);
        this.client = client;
        this.optionStringifier = new OptionStringifier();
        this.options = new ArrayList<>();
    }


    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {
        switch(answerEvent.getPropertyName()) {
            case "set_id" -> this.id = (int) answerEvent.getValue();
            case "options" -> setOptions(answerEvent);
            case "update" -> updateModel(answerEvent);
            case "wait" -> System.out.println(optionStringifier.stringify(answerEvent.getPropertyName()));
            case "error" -> {
                System.out.println((String) answerEvent.getValue());
                optionsHandler();
            }
            default -> System.out.println("Answer Error!");
        }
    }

    private void setNickname(int option) {
        System.out.println(optionStringifier.stringify(options.get(option)));
        this.nickname = stdin.nextLine();
        client.send(new RequestEvent("nickname", 0, nickname));
    }

    private void setNumOfPlayers(int option) {
        System.out.println(optionStringifier.stringify(options.get(option)));
        int num = stdin.nextInt();
        stdin.nextLine();
        client.send(new RequestEvent("first_player", 0, num));
    }

    private void setOptions(AnswerEvent answerEvent) {
        this.options = (List<String>) answerEvent.getValue();
        optionsHandler();
    }

    private void updateModel(AnswerEvent answerEvent) {
        Gson gson = new Gson();
        Model temp = gson.fromJson((String)answerEvent.getValue(), Model.class);
        ModelSerializable model = new ModelSerializable(temp);
        System.out.println(model);
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
            int choice;
            if (options.size() > 1) {
                System.out.println(optionStringifier.stringify(options));
                choice = stdin.nextInt();
                stdin.nextLine();
            } else {
                choice = 1;
            }
            if (choice <= options.size() && choice > 0) {
                choice = choice - 1;
                switch(this.options.get(choice)) {
                    case "moveStudentToIsland" -> twoInputHandler(choice);
                    case "endAction" -> noInputHandler(choice);
                    case "nickname" -> setNickname(choice);
                    case "first_player" -> setNumOfPlayers(choice);
                    default -> defaultInputHandler(choice);
                }
            }

        }
    }

}
