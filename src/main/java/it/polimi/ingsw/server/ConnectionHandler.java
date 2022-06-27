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

/**
 * An Object to handle a single client independently, read and send from client's input and output stream, ping functionality
 * to track if client is still connected
 */
public class ConnectionHandler implements Runnable, RequestListenableInterface {

    /**
     * Player's nickname connected to this connection
     */
    private String nickname = null;

    /**
     * Server connected to
     * @see Server
     */
    private final Server server;

    /**
     * Socket between client and server
     */
    private final Socket socket;

    /**
     * Boolean to track the client's status
     */
    private boolean active;

    /**
     * InputStream from the client
     */
    private DataInputStream inputStream;

    /**
     * OutputStream to the client
     */
    private DataOutputStream outputStream;

    /**
     * Boolean to track the ping reply
     */
    private boolean pong;

    /**
     * Concrete RequestListenable
     * @see RequestListenable
     */
    private final RequestListenable requestListenable;

    /**
     * Gson serializer/deserializer to read/send over the network
     */
    private final Gson gson;

    /**
     * ExecutorService to have reusable thread with less overhead
     */
    private final ExecutorService executorService;

    /**
     * Constructor, initialize all attributes, opens streams
     * @param socket the Socket that accepted the connection
     * @param server the Server that accepted the connection
     * @see Server
     */
    protected ConnectionHandler(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        this.requestListenable = new RequestListenable();
        this.gson = new Gson();
        this.executorService = Executors.newCachedThreadPool();
        startConnection();
        this.pong = true;
    }

    /**
     * Run method to be executed by a Thread, begins ping client, starts reading from InputStream
     */
    @Override
    public void run() {
        executorService.submit(this::pingFunction);
        read();
    }

    /**
     * Reads constantly from InputStream, filter and dispatch received RequestEvent
     */
    private void read(){
        while (active) {
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
    }

    /**
     * Sends the AnswerEvent to the client
     * @param answerEvent the AnswerEvent to be sent
     */
    protected synchronized void send(AnswerEvent answerEvent) {
        try {
            outputStream.writeUTF(gson.toJson(answerEvent));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            server.unexpectedDisconnection(this);
            stopConnection();
        }
    }

    /**
     * Opens the DataInputStream and DataOutputStream
     */
    protected void startConnection() {
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

    /**
     * Stops the connection, terminates every thread
     */
    protected void stopConnection() {
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

    /**
     * Returns the nickname of the player connected through this connection
     * @return the nickname of the player connected
     */
    protected String getNickname() {
        return this.nickname;
    }

    /**
     * Pings the client every 5s to check if is still connected
     */
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
