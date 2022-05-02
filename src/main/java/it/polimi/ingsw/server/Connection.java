package it.polimi.ingsw.server;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.Request;

import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {

    private final Server server;
    private final Socket socket;
    private boolean active;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private final VirtualView view;


    private String nickname;

    public Connection(Socket socket, Server server, VirtualView view) {
        this.server = server;
        this.socket = socket;
        this.view = view;
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
            RequestEvent request = (RequestEvent) inputStream.readObject();
            if (request.getPropertyName().equals("nickname")) this.nickname = request.getString();
            view.fireRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            stopConnection();
        }
    }

    public void send(Answer answer) {
        try {
            outputStream.writeObject(answer);
            outputStream.flush();
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
