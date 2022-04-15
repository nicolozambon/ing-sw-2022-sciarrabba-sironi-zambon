package it.polimi.ingsw.server;

import java.io.*;

public class ClientConnection {

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private String nickname;

    private boolean active;

    public ClientConnection (String nickname, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.nickname = nickname;
        this.active = true;
    }

    public void game() {
        System.out.println("ClientConnection started for " + nickname);
        while (active) {
            try {
                Object input = inputStream.readObject();
                if (input instanceof String) {
                    String string = (String) input;
                    if(string.equals("quit")) {
                        closeConnection();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                closeConnection();
            }
        }
    }

    public synchronized void closeConnection() {
        System.out.println("Closing connection for " + nickname);
        try {
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.active = false;
    }
}
