package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.ViewCLI;

import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            if (args[0].equals("-cli")) {
                ViewCLI view = new ViewCLI();
                view.startCLI();
            }
            if (args[0].equals("-gui")) {
                System.out.println("Feature not implemented yet!");
            }
        }
    }

}
