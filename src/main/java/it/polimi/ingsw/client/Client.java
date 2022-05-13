package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.AnswerListenable;
import it.polimi.ingsw.client.view.ExampleView;
import it.polimi.ingsw.listenables.AnswerListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;

import java.io.*;
import java.net.Socket;

public class Client implements AnswerListenableInterface {

    private final String ip;
    private final int port;
    private Socket socket;
    private boolean active;

    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private ExampleView exampleView;

    private final AnswerListenable answerListenable;

    private final Gson gson;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
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

    public void startClient() throws IOException {
        this.socket = new Socket(ip, port);

        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.active = true;

        System.out.println("Connection established");
        this.exampleView = new ExampleView(this);
        this.addAnswerListener(this.exampleView);

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
}
