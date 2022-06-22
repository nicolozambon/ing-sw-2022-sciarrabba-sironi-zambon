package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.ViewCLI;
import it.polimi.ingsw.client.view.gui.ViewGUI;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        new Client().startClient();
    }

    public void startClient() {
        boolean check = true;
        while (check) {
            try {
                System.out.println("Choose client interface:");
                System.out.println("1 - CLI\n2 - GUI");
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> {
                        check = false;
                        new ViewCLI().startCLI();
                    }
                    case 2 -> {
                        check = false;
                        new ViewGUI().startGUI();
                    }
                    default -> System.out.println("Not possible choice, retry!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input it is not a number, retry!");
            }
        }
    }
}
