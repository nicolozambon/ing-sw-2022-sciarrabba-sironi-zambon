package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.events.RequestEvent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

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
                        default -> {
                            if (options.get(choice).matches("card[2,6,7,8]")) {
                                return extraActionInputHandler(options.get(choice));
                            }
                            return oneInputHandler(options.get(choice));
                        }
                    }
                }
            } while (choice > options.size() || choice < 1);

        }
        return null;
    }

    private RequestEvent twoInputHandler(String option)  {
        String[] input;
        int student = 0;
        int island = 0;
        boolean error = true;
        while (error) {
            System.out.println(this.optionSelected.get(option));
            try {
                input = stdin.nextLine().split(",");
                if (input.length == 2) {
                    student = Integer.parseInt(input[0]);
                    island = Integer.parseInt(input[1]);
                    error = false;
                } else {
                    System.out.println("Wrong numbers of arguments!");
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

    private RequestEvent noInputHandler(String option) {
        return new RequestEvent(option, this.playerId);
    }

    private RequestEvent setNickname(String option) {
        System.out.println(this.optionSelected.get(option));
        String nickname = stdin.nextLine();
        return new RequestEvent("nickname", this.playerId, nickname);
    }

    private RequestEvent extraActionInputHandler(String option) {
        try {
            Method method = OptionHandler.class.getDeclaredMethod(option, String.class);
            method.setAccessible(true);
            return (RequestEvent) method.invoke(this, option);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private RequestEvent card2(String option) {
        return oneInputHandler(option);
    }

    //TODO implements this card
    private RequestEvent card6(String option) {
        String[] input;
        int[] values = null;
        boolean error = true;
        while (error) {
            System.out.println(this.optionSelected.get(option));
            try {
                input = stdin.nextLine().split(",");
                if (input.length == 2) {
                    values = new int[2];
                    values[0] = Integer.parseInt(input[0]) - 1;
                    values[1] = Color.valueOf(input[1].toUpperCase()).ordinal();
                    error = false;
                } else if (input.length == 4) {
                    values = new int[4];
                    values[0] = Integer.parseInt(input[0]) - 1;
                    values[1] = Color.valueOf(input[1].toUpperCase()).ordinal();
                    values[2] = Integer.parseInt(input[2]) - 1;
                    values[3] = Color.valueOf(input[3].toUpperCase()).ordinal();
                    if (values[0] < values[2]) values[2]--;
                    error = false;
                } else {
                    System.out.println("Wrong number of arguments");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not numbers retry!");
            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid color retry!");
            }
        }
        return new RequestEvent("extraAction", this.playerId, values);
    }

    private RequestEvent card7(String option) {
        String[] input;
        int[] values = null;
        boolean error = true;
        while (error) {
            System.out.println(this.optionSelected.get(option));
            try {
                input = stdin.nextLine().split(",");
                if (input.length == 2) {
                    values = new int[2];
                    values[0] = Integer.parseInt(input[0]) - 1;
                    values[1] = Color.valueOf(input[1].toUpperCase()).ordinal();
                    error = false;
                } else {
                    System.out.println("Wrong number of arguments");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a number retry!");
            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid color retry!");
            }
        }
        return new RequestEvent("extraAction", this.playerId, values);
    }

    //TODO implements this card
    private RequestEvent card8(String option) {
        String[] input;
        int[] values = null;
        boolean error = true;
        while (error) {
            System.out.println(this.optionSelected.get(option));
            try {
                input = stdin.nextLine().split(",");
                if (input.length == 2) {
                    values = new int[2];
                    values[0] = Integer.parseInt(input[0]) - 1;
                    values[1] = Color.valueOf(input[1].toUpperCase()).ordinal();
                    error = false;
                } else if (input.length == 4) {
                    values = new int[4];
                    values[0] = Integer.parseInt(input[0]) - 1;
                    values[1] = Color.valueOf(input[1].toUpperCase()).ordinal();
                    values[2] = Integer.parseInt(input[2]) - 1;
                    values[3] = Color.valueOf(input[3].toUpperCase()).ordinal();
                    if (values[0] < values[2]) values[2]--;
                    error = false;
                } else {
                    System.out.println("Wrong number of arguments");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not numbers retry!");
            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid color retry!");
            }
        }
        return new RequestEvent("extraAction", this.playerId, values);
    }

}
