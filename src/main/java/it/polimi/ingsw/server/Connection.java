package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.Request;

import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

public class Connection extends RequestListenable implements Runnable {

    private final Server server;
    private final Socket socket;
    private boolean active;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String nickname = null;

    public Connection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        startConnection();
    }

    @Override
    public void run() {

        while (active) {
            read();
        }
        stopConnection();
    }

    public synchronized void read(){
        try {
            RequestEvent request = (RequestEvent) inputStream.readObject();
            switch (request.getPropertyName()) {
                case "nickname" -> {
                    this.nickname = request.getString();
                    synchronized (server) {
                        server.notifyAll();
                    }
                }
                case "first_player" -> {
                    this.server.setNumPlayers(request.getValues()[0]);
                }
                default -> fireRequest(request);
            }
        } catch (OutOfBoundsException e) {
            send(new AnswerEvent("error", "Number of players not possible!"));
            send(new AnswerEvent("first_player", null));
        } catch (Exception e) {
            e.printStackTrace();
            stopConnection();
        }
    }

    public void send(AnswerEvent answer) {
        try {
            outputStream.writeObject(answer);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
            stopConnection();
        }
    }

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
        return this.nickname;
    }

}
