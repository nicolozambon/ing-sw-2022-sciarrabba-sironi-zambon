package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.util.Scanner;

public class Eriantys {

    public static void main(String[] args) {
        System.out.println("Welcome to Eriantys");
        System.out.println("1 - Server\n2 - Client");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                try {
                    Server server= new Server();
                    server.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Client client = new Client();
                    client.main(args);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

}
