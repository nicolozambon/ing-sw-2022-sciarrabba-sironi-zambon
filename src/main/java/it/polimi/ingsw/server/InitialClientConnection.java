package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.ConnectionAction;

import java.io.*;
import java.net.Socket;



public class InitialClientConnection implements Runnable {

    private Server server;
    private Socket socket;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private boolean active;
    private ConnectionAction STATE;

    private String nickname;

    public InitialClientConnection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.active = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setState(ConnectionAction newState) {
        this.STATE = newState;
        send(STATE);
    }

    public ConnectionAction getState() {
        return this.STATE;
    }

    public synchronized void read(){
        try {

            switch(STATE) {
                case SET_NICKNAME -> {
                    Object input = inputStream.readObject();
                    if (input instanceof String) {
                        nickname = (String) input;
                        server.addPlayer(nickname, this);
                    }
                    break;
                }

                case SET_NUM_PLAYERS -> {
                    Object input = inputStream.readObject();
                    if (input instanceof Integer) {
                        server.setNumPlayers((Integer) input);
                        setState(ConnectionAction.WAITING);
                    }
                    break;
                }

                case WAITING -> {
                    break;
                }

                case START -> {
                    System.out.println("Starting game for " + nickname);
                    startGame();
                    break;
                }
            }
        } catch (Exception e) {
            active = false;
        }
    }

    private void send(final ConnectionAction state){
        try {
            outputStream.writeObject(state);
            outputStream.flush();
        }catch(Exception e){
            active = false;
        }
    }

    private synchronized void startGame() {
        new ClientConnection(nickname, inputStream, outputStream).game();
        this.closeConnection();
    }

    @Override
    public void run() {
        setState(ConnectionAction.SET_NICKNAME);

        while(active) {
            read();
        }

        this.closeConnection();
    }


    public synchronized void closeConnection() {
        try {
            socket.close();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.active = false;
    }
}
