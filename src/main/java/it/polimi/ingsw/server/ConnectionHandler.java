package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.WrongSetupException;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.RequestListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class ConnectionHandler implements Runnable, RequestListenableInterface {

    private String nickname = null;

    private final Server server;
    private final Socket socket;
    private boolean active;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean pong;

    private final RequestListenable requestListenable;
    private final Gson gson;
    private final ExecutorService executorService;

    public ConnectionHandler(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        this.requestListenable = new RequestListenable();
        this.gson = new Gson();
        this.executorService = Executors.newCachedThreadPool();
        startConnection();
        this.pong = true;
    }

    @Override
    public void run() {
        executorService.submit(this::pingFunction);
        while (active) {
            read();
        }
    }

    private void read(){
        try {
            RequestEvent request = this.gson.fromJson(inputStream.readUTF(), RequestEvent.class);
            switch (request.getPropertyName()) {
                case "nickname" -> {
                    this.nickname = request.getString();
                    server.enqueuePlayer(this);
                }
                case "firstPlayer" -> this.server.firstPlayerSetup(request.getValues()[0], request.getValues()[1]);
                case "end" -> {
                    this.server.endGame(this);
                    stopConnection();
                }
                case "ping" -> pong = true;
                default -> executorService.submit(() -> {
                    try {
                        fireRequest(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                        server.unexpectedDisconnection(this);
                        stopConnection();
                    }
                });
            }
            sleep(50);
        } catch (WrongSetupException e) {
            send(new AnswerEvent("error", "Number of players not possible or complete rules wrong choice!"));
        } catch (Exception e) {
            e.printStackTrace();
            server.unexpectedDisconnection(this);
            stopConnection();
        }
    }

    public synchronized void send(AnswerEvent answer) {
        try {
            outputStream.writeUTF(gson.toJson(answer));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            server.unexpectedDisconnection(this);
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
            server.unexpectedDisconnection(this);
            stopConnection();
        }
    }

    public void stopConnection() {
        try {
            executorService.shutdownNow();
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

    private void pingFunction() {
        while (pong) {
            send(new AnswerEvent("ping"));
            pong = false;
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        server.unexpectedDisconnection(this);
        stopConnection();
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
