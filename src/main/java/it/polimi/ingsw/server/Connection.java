package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Connection implements Runnable {
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private boolean active;
    private final Server server;
    private String nickname;

    public Connection (Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.active = true;
    }

    public void send (String message) { //TODO: modify to use serialized objects
        output.println(message);
        output.flush();
    }

    public void asyncSend (final String message) {
        new Thread (new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    public synchronized void closeConnection() {
        send("The connection is being closed server-side.");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.active = false;
    }

    private void close() {
        this.closeConnection();
    }

    @Override
    public void run() {
        try {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream());
            asyncSend("Welcome, player! Choose a nickname...");
            nickname = input.nextLine();
            this.server.addPlayer(this, nickname);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public boolean isActive() {
        return this.active;
    }
}
