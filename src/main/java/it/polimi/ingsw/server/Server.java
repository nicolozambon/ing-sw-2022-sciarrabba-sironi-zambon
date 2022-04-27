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

    private Queue<Connection> queue;

    private List<GameHandler> games;

    /**
     * Number of players for the match being built.
     */
    private int numPlayers = -1;


    public Server(int port){
        this.port = port;
        this.ip = DEFAULT_IP_SETTINGS;
        this.queue = new ArrayDeque<>();
        this.games = new ArrayList<>();
    }

    public Server(String ip, int port) {
        this.port = port;
        this.ip = ip;
        this.queue = new ArrayDeque<>();
        this.games = new ArrayList<>();
    }

    public void startServer() throws IOException {

        ExecutorService connectionThreadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Server socket ready on port: " +  serverSocket.getLocalPort());
        System.out.println("Server socket ready on IP: " + serverSocket.getInetAddress().getHostAddress());

        connectionThreadPool.submit(this::lobby);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Received client connection");
                Connection connection = new Connection(socket, this);
                connection.send(new Message("NICKNAME", "Choose a Nickname:"));
                connectionThreadPool.submit(connection);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        connectionThreadPool.shutdown();
        serverSocket.close();
    }

    protected void enqueuePlayer(Connection connection) {
        synchronized (queue) {
            this.queue.add(connection);
            this.queue.notifyAll();
        }
    }

    private void lobby() {
        Connection connection;
        Map<String, Connection> players = new HashMap<>();
        ExecutorService gameThreadPool = Executors.newCachedThreadPool();
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

            if (isNicknameUnique(connection, players)) {
                players.put(connection.getNickname(), connection);
                System.out.println("added player to a game");
                if (numPlayers < 0) {
                    connection.send(new Message("NUM_PLAYERS", "You are the first player, choose (2-3):"));
                    while (numPlayers < 0) {
                        try {
                            synchronized (this) {
                                wait();
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                }
            } else {
                connection.send(new Message("ERROR", "Nickname already taken, retry"));
                connection.send(new Message("NICKNAME", "Choose a Nickname:"));
            }


            if (players.size() == numPlayers) {
                GameHandler game = new GameHandler(players);
                gameThreadPool.submit(game);
                games.add(game);
                numPlayers = -1;
                players = new HashMap<>();
            }

        }

    }

    private synchronized boolean isNicknameUnique(Connection connection, Map<String, Connection> players) {
        if (players.containsKey(connection.getNickname())) return false;
        for (GameHandler game : games) {
            if (game.getPlayersNicknames().contains(connection.getNickname())) return false;
        }
        return true;
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
