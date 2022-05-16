package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.AnswerListenable;
import it.polimi.ingsw.listenables.AnswerListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class Client implements AnswerListenableInterface, RequestListener, Runnable {

    private final String ip;
    private static final int port = 1337;
    private Socket socket;
    private boolean active;

    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private final AnswerListenable answerListenable;

    private final Gson gson;

    public Client(String ip) throws IOException {
        this.ip = ip;
        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
        this.socket = new Socket(ip, port);

        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.active = true;

        System.out.println("Connection established");
    }

    public Client() throws IOException {
        this.ip = "";
        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
        this.socket = new Socket(ip, port);

        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.active = true;

        System.out.println("Connection established");
    }

    public synchronized void read() {
        try {
            AnswerEvent answer = gson.fromJson(inputStream.readUTF(), AnswerEvent.class);
            new Thread(() -> this.fireAnswer(answer)).start();
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
