package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.ViewCLI;
import it.polimi.ingsw.client.view.gui.ViewGUI;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            if (args[0].equals("-cli")) {
                ViewCLI view = new ViewCLI();
                view.startCLI();
            }
            if (args[0].equals("-gui")) {
                ViewGUI viewGUI = new ViewGUI();
                viewGUI.main(args);
            }
        } else {
            System.out.println("Choose client interface:");
            System.out.println("1 - CLI\n2 - GUI");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> new ViewCLI().startCLI();
                case 2 -> new ViewGUI().main(args);
            }
        }
    }

}
