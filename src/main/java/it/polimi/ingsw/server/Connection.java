package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.listeners.Listenable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

public class Connection implements Runnable, Listenable {

    private Server server;
    private Socket socket;
    private boolean active;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String nickname;

    private PropertyChangeSupport observable;

    public Connection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        startConnection();
        this.observable = new PropertyChangeSupport(this);
    }

    @Override
    public void run() {
        while (active) {
            read();
        }
    }

    public synchronized void read(){
        try {
            Message input = (Message) inputStream.readObject();
            this.observable.firePropertyChange(input.getName(), null, input.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            stopConnection();
        }
    }

    public void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            stopConnection();
        }
    }

    public void startConnection() {
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.active = true;
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void addListener(PropertyChangeListener listener) {

    }

    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {

    }

    @Override
    public void removeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {

    }
}
