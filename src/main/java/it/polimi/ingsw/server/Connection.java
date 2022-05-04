package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.RequestListener;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable, RequestListenableInterface {


    private final Server server;
    private final Socket socket;
    private boolean active;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String nickname = null;

    private final RequestListenable requestListenable;

    public Connection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        this.requestListenable = new RequestListenable();
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

    @Override
    public void addRequestListener(RequestListener requestListener) {
        this.requestListenable.addRequestListener(requestListener);
    }

    @Override
    public void removeRequestListener(RequestListener requestListener) {
        this.requestListenable.removeRequestListener(requestListener);
    }

    @Override
    public void fireRequest(RequestEvent requestEvent) throws Exception {
        this.requestListenable.fireRequest(requestEvent);
    }
}
