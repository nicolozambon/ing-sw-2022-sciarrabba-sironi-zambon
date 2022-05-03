package it.polimi.ingsw.client;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.AnswerListenable;
import it.polimi.ingsw.client.view.ExampleView;

import java.io.*;
import java.net.Socket;

public class Client extends AnswerListenable {

    private final String ip;
    private final int port;
    private Socket socket;
    private boolean active;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private ExampleView exampleView;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public synchronized void read() {
        try {
            AnswerEvent answer = (AnswerEvent) inputStream.readObject();
            this.fireAnswer(answer);
        } catch (Exception e) {
            e.printStackTrace();
            stopClient();
        }
    }

    public void startClient() throws IOException {
        this.socket = new Socket(ip, port);

        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
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
            outputStream.writeObject(request);
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
            stopClient();
        }
    }
}
