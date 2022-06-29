package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.AnswerListenable;
import it.polimi.ingsw.listenables.AnswerListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * Client Connection Class, handles the connection to the server
 */
public class ClientConnection implements AnswerListenableInterface, RequestListener, Runnable {

    /**
     * IP of the Server
     */
    private final String ip;

    /**
     * Port of the server
     */
    private static final int port = 1337;

    /**
     * Socket of the connection
     */
    private final Socket socket;

    /**
     * Is the connection active?
     */
    private boolean active;

    /**
     * Output stream of the client
     */
    private final DataOutputStream outputStream;

    /**
     * Input stream of the client
     */
    private final DataInputStream inputStream;

    /**
     * Ping helper boolean variable
     */
    private boolean ping;

    /**
     * Answer Listenable of the connection
     */
    private final AnswerListenable answerListenable;

    /**
     * GSON Instance
     */
    private final Gson gson;

    /**
     * Executor Service for running tasks in asynchronous mode
     */
    private final ExecutorService executorService;

    /**
     * Constructor for the connection with a custom IP
     * @param ip IP of the server
     * @throws IOException if a problem occurs with the Input/Output streams
     */
    public ClientConnection(String ip) throws IOException {
        this.ip = ip;
        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
        this.socket = new Socket(ip, port);

        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.active = true;

        this.executorService = Executors.newCachedThreadPool();
        this.ping = true;
        System.out.println("Connection established");
    }

    /**
     * Constructor for a localhost instance
     * @throws IOException
     */
    public ClientConnection() throws IOException {
        this.ip = "";
        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
        this.socket = new Socket(ip, port);

        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.active = true;

        this.executorService = Executors.newCachedThreadPool();
        this.ping = true;
        System.out.println("Connection established");
    }

    /**
     * Starts a thread for the ping function and waits for an answer
     */
    @Override
    public void run() {
        executorService.submit(this::pingFunction);
        read();
        stopClient();
    }

    /**
     * Helper method for reading an answer to a ping from the server
     */
    private void read() {
        while (active) {
            try {
                AnswerEvent answer = gson.fromJson(inputStream.readUTF(), AnswerEvent.class);
                switch (answer.getPropertyName()) {
                    case "ping" -> {
                        send(new RequestEvent("ping", 0));
                        ping = true;
                        synchronized (this) {
                            notifyAll();
                        }
                    }
                    case "stop" -> {
                        this.fireAnswer(answer);
                        send(new RequestEvent("end", 0, "disconnect"));
                        stopClient();
                    }
                    case "winner" -> {
                        this.fireAnswer(answer);
                        send(new RequestEvent("end", 0));
                        stopClient();
                    }
                    default -> executorService.submit(() -> this.fireAnswer(answer));
                }
                sleep(50);
            } catch (IOException e) {
                fireAnswer(new AnswerEvent("stop"));
                stopClient();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a Request Event after serializing it
     * @param requestEvent Request Event to send
     */
    private synchronized void send(RequestEvent requestEvent) {
        try {
            outputStream.writeUTF(gson.toJson(requestEvent));
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
            stopClient();
        }
    }

    /**
     * Stops the client and closes all sockets and streams
     */
    public void stopClient() {
        try {
            executorService.shutdownNow();
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            active = false;
        }
    }

    /**
     * Ping Function to verify that the connection to the server is active
     */
    private void pingFunction() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (ping) {
            try {
                ping = false;
                sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        fireAnswer(new AnswerEvent("stop"));
        //stopClient();
    }

    @Override
    public void addAnswerListener(AnswerListener answerListener) {
        this.answerListenable.addAnswerListener(answerListener);
    }

    @Override
    public void removeAnswerListener(AnswerListener answerListener) {
        this.answerListenable.removeAnswerListener(answerListener);
    }

    @Override
    public void fireAnswer(AnswerEvent answerEvent) {
        this.answerListenable.fireAnswer(answerEvent);
    }

    @Override
    public void onRequestEvent(RequestEvent requestEvent) {
        send(requestEvent);
    }
}
