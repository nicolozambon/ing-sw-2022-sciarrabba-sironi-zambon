package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.client.view.ExampleView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

public class Client implements PropertyChangeListener {

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
            Message message = (Message) inputStream.readObject();
            this.observable.firePropertyChange(message.getName(), null, message.getValue());
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
        this.observable.addPropertyChangeListener(view);
        view.addListener(this);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Message message = new Message(evt.getPropertyName(), evt.getNewValue());
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
            stopClient();
        }
    }
}
