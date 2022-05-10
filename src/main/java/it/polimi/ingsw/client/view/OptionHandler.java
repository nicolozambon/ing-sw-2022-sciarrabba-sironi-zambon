package it.polimi.ingsw.client.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.events.RequestEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//TODO Class for handling input by the user
public class OptionHandler {

    private Map<String, String> optionSelected;
    private final OptionLister optionLister;
    private final Scanner stdin;
    private int playerId;

    public OptionHandler(int playerId) {
        this.playerId = playerId;
        this.optionLister = new OptionLister();
        this.stdin = new Scanner(System.in);

        Gson gson = new Gson();
        String path = "src/main/resources/config/options_selected.json";
        try {
            this.optionSelected = gson.fromJson(new FileReader(path), new TypeToken<Map<String, String>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public OptionHandler() {
        this.playerId = -1;
        this.optionLister = new OptionLister();
        this.stdin = new Scanner(System.in);

        Gson gson = new Gson();
        String path = "src/main/resources/config/options_selected.json";
        try {
            this.optionSelected = gson.fromJson(new FileReader(path), new TypeToken<Map<String, String>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public RequestEvent getRequestEvent(List<String> options) {
        if (options.size() > 0) {
            int choice;
            if (options.size() > 1) {
                System.out.println(optionLister.list(options));
                choice = stdin.nextInt();
                stdin.nextLine();
            } else {
                choice = 1;
            }
            if (choice <= options.size() && choice > 0) {
                choice = choice - 1;
                switch(options.get(choice)) {
                    case "moveStudentToIsland" -> {
                        return twoInputHandler(options.get(choice));
                    }
                    case "endAction" -> {
                        return noInputHandler(options.get(choice));
                    }
                    case "nickname" -> {
                        return setNickname(options.get(choice));
                    }
                    case "first_player" -> {
                        return setNumOfPlayers(options.get(choice));
                    }

                    case "extraAction" -> {
                        return extraActionInputHandler(options.get(choice));
                    }
                    default -> {
                        return oneInputHandler(options.get(choice));
                    }
                }
            }

        }
        return null;
    }

    //TODO Improve control on user input
    private RequestEvent twoInputHandler(String option)  {
        System.out.println(this.optionSelected.get(option));
        String[] input = stdin.nextLine().split(",");
        int student = Integer.parseInt(input[0]);
        int island = Integer.parseInt(input[1]);
        System.out.println(student + " " + island);
        return new RequestEvent(option, this.playerId, student, island);
    }

    private RequestEvent oneInputHandler(String option) {
        System.out.println(this.optionSelected.get(option));
        int choice = stdin.nextInt();
        stdin.nextLine();
        return new RequestEvent(option, this.playerId, choice);
    }

    private RequestEvent noInputHandler(String option) {
        return new RequestEvent(option, this.playerId);
    }

    private RequestEvent setNickname(String option) {
        System.out.println(this.optionSelected.get(option));
        String nickname = stdin.nextLine();
        return new RequestEvent("nickname", this.playerId, nickname);
    }

    private RequestEvent extraActionInputHandler(String option) {
        System.out.println(this.optionSelected.get(option)); //TODO show correct messages based on character card
        String[] input = stdin.nextLine().split(",");
        int value0 = Integer.parseInt(input[0]);
        int value1 = Integer.parseInt(input[1]);
        int value2 = Integer.parseInt(input[2]);
        int value3 = Integer.parseInt(input[3]);
        return new RequestEvent(option, this.playerId, value0, value1, value2, value3);
    }

    private RequestEvent setNumOfPlayers(String option) {
        System.out.println(this.optionSelected.get(option));
        int num = stdin.nextInt();
        stdin.nextLine();
        return new RequestEvent("first_player", this.playerId, num);
    }

}
