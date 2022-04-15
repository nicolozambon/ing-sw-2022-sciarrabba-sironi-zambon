package it.polimi.ingsw.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient implements Client {

    private String nickname;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner stdin;

    private boolean active;

    public GameClient(String nickname, Scanner input, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.nickname = nickname;
        stdin = input;
        active = true;
    }

    @Override
    public void startClient() throws IOException {
        System.out.println("GameClient started for " + nickname);
        while (active) {
            try {
                String input = stdin.nextLine();
                if (input.equals("quit")) {
                    outputStream.writeObject("quit");
                    stopClient();
                }

            } catch (IOException e) {
                e.printStackTrace();
                stopClient();
            }
        }
    }

    @Override
    public void stopClient() {
        System.out.println("Closing client...");
        try {
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        active = false;

    }

}
