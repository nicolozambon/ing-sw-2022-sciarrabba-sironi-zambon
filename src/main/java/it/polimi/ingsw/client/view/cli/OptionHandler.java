package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.events.RequestEvent;

import java.io.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//TODO Class for handling input by the user
public class OptionHandler {

    private final Map<String, String> optionSelected;
    private final OptionLister optionLister;
    private final Scanner stdin;
    private int playerId;

    public OptionHandler() {
        this.playerId = -1;
        this.optionLister = new OptionLister();
        this.stdin = new Scanner(System.in);
        Gson gson = new Gson();
        InputStream inputStream = getClass().getResourceAsStream("/config/options_selected.json");
        this.optionSelected = gson.fromJson(new InputStreamReader(inputStream), new TypeToken<Map<String, String>>(){}.getType());
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public RequestEvent getRequestEvent(List<String> options) {
        if (options.size() > 0) {
            int choice = 1;
            do {
                if (options.size() > 1) {
                    choice = inputNumber(optionLister.list(options));
                }
                if (choice <= options.size() && choice > 0) {
                    choice = choice - 1;
                    switch (options.get(choice)) {
                        case "moveStudentToIsland" -> {
                            return twoInputHandler(options.get(choice));
                        }
                        case "endAction" -> {
                            return noInputHandler(options.get(choice));
                        }
                        case "nickname" -> {
                            return setNickname(options.get(choice));
                        }
                        case "extraAction" -> {
                            return extraActionInputHandler(options.get(choice));
                        }
                        default -> {
                            return oneInputHandler(options.get(choice));
                        }
                    }
                }
            } while (choice > options.size() || choice < 1);

        }
        return null;
    }

    //TODO Improve control on user input
    private RequestEvent twoInputHandler(String option)  {
        System.out.println(this.optionSelected.get(option));
        String[] input;
        int student = 0;
        int island = 0;
        boolean error = true;
        while (error) {
            try {
                input = stdin.nextLine().split(",");
                if (input.length == 2) {
                    student = Integer.parseInt(input[0]);
                    island = Integer.parseInt(input[1]);
                    error = false;
                } else {
                    System.out.println("Too many arguments");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not numbers retry!");
            }
        }
        //System.out.println(student + " " + island);
        return new RequestEvent(option, this.playerId, student, island);
    }

    private RequestEvent oneInputHandler(String option) {
        int choice = inputNumber(this.optionSelected.get(option));
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

    private int inputNumber(String message) {
        boolean error = true;
        int choice = 0;
        while (error) {
            try {
                System.out.println(message);
                choice = stdin.nextInt();
                error = false;
            } catch (InputMismatchException e) {
                System.out.println("Not a number retry!");
            } finally {
                stdin.nextLine();
            }
        }
        return choice;
    }

}
