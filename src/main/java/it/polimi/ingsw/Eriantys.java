package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.server.Server;

import java.util.Scanner;

public class Eriantys {

    public static void main(String[] args){
        System.out.println("Welcome to Eriantys");
        System.out.println("1 - Server\n2 - Client");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            switch (scanner.nextInt()) {
                case 1:
                    new Thread(new Server(1337)).start();
                    break;
                case 2:
                    new Thread(new Client("127.0.0.1", 1337)).start();
                    break;
            }
        }
    }

}
