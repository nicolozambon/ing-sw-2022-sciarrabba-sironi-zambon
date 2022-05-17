package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.RequestListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable, RequestListenableInterface {


    private final Server server;
    private final Socket socket;
    private boolean active;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private String nickname = null;

    private final RequestListenable requestListenable;

    private final Gson gson;

    public ConnectionHandler(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        this.requestListenable = new RequestListenable();
        this.gson = new Gson();
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
            RequestEvent request = this.gson.fromJson(inputStream.readUTF(), RequestEvent.class);
            switch (request.getPropertyName()) {
                case "nickname" -> {
                    this.nickname = request.getString();
                    new Thread (() -> server.enqueuePlayer(this)).start();
                    notifyAll();
                }
                case "first_player" -> this.server.setNumPlayers(request.getValues()[0]);
                default -> fireRequest(request);
            }
        } catch (OutOfBoundsException e) {
            send(new AnswerEvent("error", "Number of players not possible!"));
        } catch (Exception e) {
            e.printStackTrace();
            server.removeConnection(this);
            stopConnection();
        }
    }

    public void send(AnswerEvent answer) {
        try {
            outputStream.writeUTF(gson.toJson(answer));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            server.removeConnection(this);
            stopConnection();
        }
    }

    public void startConnection() {
        try {
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.active = true;
        } catch (IOException e) {
            e.printStackTrace();
            server.removeConnection(this);
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

    public boolean isActive() {
        return active;
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
