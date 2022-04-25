package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.exceptions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private final String DEFAULT_IP_SETTINGS = "";
    private int port;
    private String ip;

    private Queue<LobbyConnection> queue;

    /**
     * Number of players for the match being built.
     */
    private int numPlayers = -1;


    public Server(int port){
        this.port = port;
        this.ip = DEFAULT_IP_SETTINGS;
        this.queue = new ArrayDeque<>();
    }

    public Server(String ip, int port) {
        this.port = port;
        this.ip = ip;
        this.queue = new ArrayDeque<>();
    }

    public void startServer() throws IOException {

        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Server socket ready on port: " +  serverSocket.getLocalPort());
        System.out.println("Server socket ready on IP: " + serverSocket.getInetAddress().getHostAddress());

        executor.submit(this::lobby);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Received client connection");
                executor.submit(new LobbyConnection(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        executor.shutdown();
        serverSocket.close();
    }

    protected void enqueuePlayer(LobbyConnection connection) {
        synchronized (queue) {
            this.queue.add(connection);
            this.queue.notifyAll();
        }
    }

    private void lobby() {
        LobbyConnection connection;
        Map<String, Connection> players = new HashMap<>();
        while (true) {

            while (queue.isEmpty()) {
                try {
                    synchronized (queue) {
                        this.queue.wait();
                    }
                } catch (InterruptedException e) {

                }
            }
            synchronized (queue) {
                connection = queue.remove();
            }

            System.out.println("added player to a game");
            if (numPlayers < 0) {
                connection.send(new Message("NUM_PLAYERS", null));
                while (numPlayers < 0) {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {

                    }
                }
            }

            if (!players.containsKey(connection.getNickname())) {
                players.put(connection.getNickname(), connection);
            } else {
                connection.send(new Message("ERROR", "Nickname already taken, retry"));
                connection.send(new Message("NICKNAME", null));
            }


            if (players.size() == numPlayers) {
                new Thread(new GameHandler(players));
                numPlayers = -1;
                players = new HashMap<>();
            }

        }

    }

    protected synchronized void setNumPlayers (int num) throws OutOfBoundsException {
        if (num != 2 && num != 3) {
            throw new OutOfBoundsException();
        } else {
            this.numPlayers = num;
            notifyAll();
        }
    }

}
