package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.events.RequestEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OptionHandler {

    /**
     * Map of selected options
     */
    private final Map<String, String> optionSelected;

    /**
     * Lister of option
     */
    private final OptionLister optionLister;

    /**
     * Standard input
     */
    private final Scanner stdin;

    /**
     * Identifier of the player
     */
    private int playerId;

    /**
     * OS identifier
     */
    private final boolean isWindows;

    /**
     * Constructor of OptionHandler
     */
    public OptionHandler() {
        this.playerId = -1;
        this.optionLister = new OptionLister();
        this.stdin = new Scanner(System.in);
        Gson gson = new Gson();
        InputStream inputStream = getClass().getResourceAsStream("/assets/cli/json/options_selected.json");
        this.optionSelected = gson.fromJson(new InputStreamReader(inputStream), new TypeToken<Map<String, String>>(){}.getType());
        isWindows = System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Setter of playerId
     * @param playerId playerId
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Getter of request event
     * @param options list of options
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    public RequestEvent getRequestEvent(List<String> options) throws InterruptedException {
        if (options.size() > 0) {
            int choice = 1;
            do {
                if (options.size() > 1) {
                    choice = inputNumber(optionLister.list(options));
                }
                if (choice <= options.size() && choice > 0) {
                    choice = choice - 1;
                    switch (options.get(choice)) {
                        case "moveStudentToIsland", "firstPlayer" -> {
                            return twoInputHandler(options.get(choice));
                        }
                        case "endAction" -> {
                            return noInputHandler(options.get(choice));
                        }
                        case "nickname" -> {
                            return setNickname(options.get(choice));
                        }
                        case "chooseWizard" -> {
                            return chooseWizard(options.get(choice));
                        }
                        default -> {
                            if (options.get(choice).matches("card[2,6,7,8]")) {
                                return extraActionInputHandler(options.get(choice));
                            }
                            return oneInputHandler(options.get(choice));
                        }
                    }
                } else {
                    System.out.println("Not a possible choice, retry!");
                }
            } while (choice > options.size() || choice < 1);
        }
        return null;
    }

    /**
     * Choose wizard
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent chooseWizard(String option) throws InterruptedException {
        int choice = -1;
        boolean error = true;
        while (error) {
            System.out.println(this.optionSelected.get(option));
            try {
                while (!isWindows && System.in.available() == 0) {
                    Thread.sleep(100);
                }
                choice = Wizard.valueOf(stdin.nextLine().toUpperCase()).ordinal();
                error = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid Wizard retry!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new RequestEvent("chooseWizard", this.playerId, choice);
    }

    /**
     * Two input handler
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent twoInputHandler(String option) throws InterruptedException {
        String[] input;
        int num1 = 0;
        int num2 = 0;
        boolean error = true;
        while (error) {
            System.out.println(this.optionSelected.get(option));
            try {
                while (!isWindows && System.in.available() == 0) {
                    Thread.sleep(100);
                }
                input = stdin.nextLine().split(",");
                if (input.length == 2) {
                    num1 = Integer.parseInt(input[0]);
                    num2 = Integer.parseInt(input[1]);
                    error = false;
                } else {
                    System.out.println("Wrong numbers of arguments!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not numbers retry!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(student + " " + island);
        return new RequestEvent(option, this.playerId, num1, num2);
    }

    /**
     * One input handler
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent oneInputHandler(String option) throws InterruptedException {
        int choice = inputNumber(this.optionSelected.get(option));
        return new RequestEvent(option, this.playerId, choice);
    }

    /**
     * Input number
     * @param message string of message
     * @return integer
     * @throws InterruptedException raised on interruption
     */
    private int inputNumber(String message) throws InterruptedException {
        boolean error = true;
        int choice = 0;
        while (error) {
            try {
                System.out.println(message);
                while (!isWindows && System.in.available() == 0) {
                    Thread.sleep(100);
                }
                choice = stdin.nextInt();
                stdin.nextLine();
                error = false;
            } catch (InputMismatchException e) {
                System.out.println("Not a number retry!");
                stdin.nextLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return choice;
    }

    /**
     * No input handler
     * @param option choice
     * @return RequestEvent instance
     */
    private RequestEvent noInputHandler(String option) {
        System.out.println(this.optionSelected.get(option));
        return new RequestEvent(option, this.playerId);
    }

    /**
     * Setter for the nickname
     * @param option choice
     * @return RequestEvent instance
     */
    private RequestEvent setNickname(String option) {
        System.out.println(this.optionSelected.get(option));
        String nickname = stdin.nextLine();
        return new RequestEvent("nickname", this.playerId, nickname);
    }

    /**
     * Extra-action input handler
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent extraActionInputHandler(String option) throws InterruptedException {
        try {
            Method method = OptionHandler.class.getDeclaredMethod(option, String.class);
            method.setAccessible(true);
            return (RequestEvent) method.invoke(this, option);
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
            if (e.getCause() instanceof InterruptedException) throw new InterruptedException();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Input color
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent inputColor(String option) throws InterruptedException {
        boolean error = true;
        int choice = -1;
        while (error) {
            try {
                System.out.println(this.optionSelected.get(option));
                while (!isWindows && System.in.available() == 0) {
                    Thread.sleep(100);
                }
                String s = stdin.nextLine();
                choice = Color.valueOf(s.toUpperCase()).ordinal();
                error = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid color, retry!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new RequestEvent(option, this.playerId, choice);
    }

    /**
     * Card 2
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent card2(String option) throws InterruptedException{
        RequestEvent requestEvent = oneInputHandler(option);
        return new RequestEvent("extraAction", this.playerId, requestEvent.getValues());
    }

    /**
     * Card 6
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent card6(String option) throws InterruptedException{
        RequestEvent requestEvent = inputColor(option);
        return new RequestEvent("extraAction", this.playerId, requestEvent.getValues());
    }

    /**
     * Card 7
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent card7(String option) throws InterruptedException{
        String[] input;
        int[] values = null;
        boolean error = true;
        while (error) {
            System.out.println(this.optionSelected.get(option));
            try {
                while (!isWindows && System.in.available() == 0) {
                    Thread.sleep(100);
                }
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new RequestEvent("extraAction", this.playerId, values);
    }

    /**
     * Card 8
     * @param option choice
     * @return RequestEvent instance
     * @throws InterruptedException raised on interruption
     */
    private RequestEvent card8(String option) throws InterruptedException{
        RequestEvent requestEvent = inputColor(option);
        return new RequestEvent("extraAction", this.playerId, requestEvent.getValues());
    }

}
