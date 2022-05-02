package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.client.view.ExampleView;
import it.polimi.ingsw.messages.requests.Request;

import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

public class Client {

    private final String ip;
    private final int port;
    private Socket socket;
    private boolean active;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private final PropertyChangeSupport observable;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.observable = new PropertyChangeSupport(this);
    }

    public synchronized void read() {
        try {
            Answer answer = (Answer) inputStream.readObject();
            this.observable.firePropertyChange(answer.process(this));
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

        ExampleView view = new ExampleView();

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

    private void send(Request request) {
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
            stopClient();
        }
    }
}
