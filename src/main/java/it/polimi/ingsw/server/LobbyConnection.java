package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.exceptions.OutOfBoundsException;

import java.io.*;
import java.net.Socket;

public class LobbyConnection implements Runnable, Connection {

    private Server server;
    private Socket socket;
    private boolean active;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String nickname;

    public LobbyConnection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        startConnection();
    }

    @Override
    public void run() {

        while (active) {
            read();
        }
    }

    public synchronized void read(){
        try {
            Message input = (Message) inputStream.readObject();
            switch (input.getName()) {
                case "NICKNAME" -> {
                    this.nickname = (String)input.getValue();
                    server.enqueuePlayer(this);
                }
                case "NUM_PLAYERS" -> {
                    try {
                        server.setNumPlayers((int)input.getValue());
                    } catch (OutOfBoundsException e) {
                        send(new Message("ERROR", "Number of players not valid, retry"));
                        send(new Message("NUM_PLAYERS", "You are the first player, choose (2-3):"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            stopConnection();
        }
    }

    public void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startConnection() {
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.active = true;
        } catch (IOException e) {
            e.printStackTrace();
            stopConnection();
        }
    }

    @Override
    public void stopConnection() {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.active = false;
        }
    }

    public String getNickname() {
        return nickname;
    }

}
