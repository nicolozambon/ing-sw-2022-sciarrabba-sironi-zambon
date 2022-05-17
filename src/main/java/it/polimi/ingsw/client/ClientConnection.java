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

public class ClientConnection implements AnswerListenableInterface, RequestListener, Runnable {

    private final String ip;
    private static final int port = 1337;
    private final Socket socket;
    private boolean active;

    private final DataOutputStream outputStream;
    private final DataInputStream inputStream;

    private final AnswerListenable answerListenable;

    private final Gson gson;

    private ExecutorService executorService;

    public ClientConnection(String ip) throws IOException {
        this.ip = ip;
        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
        this.socket = new Socket(ip, port);

        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.active = true;

        this.executorService = Executors.newCachedThreadPool();
        System.out.println("Connection established");
    }

    public ClientConnection() throws IOException {
        this.ip = "";
        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
        this.socket = new Socket(ip, port);

        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.active = true;

        this.executorService = Executors.newCachedThreadPool();
        System.out.println("Connection established");
    }

    public synchronized void read() {
        try {
            AnswerEvent answer = gson.fromJson(inputStream.readUTF(), AnswerEvent.class);
            executorService.submit(() -> this.fireAnswer(answer));
            if (answer.getPropertyName().equals("stop")) stopClient();
        } catch (Exception e) {
            e.printStackTrace();
            stopClient();
        }
    }

    @Override
    public void run() {
        while (active) {
            read();
        }
        stopClient();
    }

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

    public void send(RequestEvent request) {
        try {
            outputStream.writeUTF(gson.toJson(request));
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
            stopClient();
        }
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
