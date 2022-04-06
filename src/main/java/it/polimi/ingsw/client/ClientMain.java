package it.polimi.ingsw.client;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args){
        Client client = new Client("127.0.0.1", 1337);
        try {
            client.startClient();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

}
