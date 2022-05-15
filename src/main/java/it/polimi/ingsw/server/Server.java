package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.exceptions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final String DEFAULT_IP = "";

    private final int port;
    private final String ip;

    private final Queue<Connection> queue;
    private final List<GameHandler> games;
    private final Map<String, Connection> players = new HashMap<>();


    /**
     * Number of players for the match being built.
     */
    private int numPlayers = -1;

    public Server(int port){
        this.port = port;
        this.ip = DEFAULT_IP;
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
                connectionThreadPool.submit(connection);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        connectionThreadPool.shutdown();
        serverSocket.close();
    }

    public void enqueuePlayer(Connection connection) {
        while (!isNicknameUnique(connection)) {
            try {
                synchronized (connection) {
                    connection.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("adding to queue");
        synchronized (queue) {
            this.queue.add(connection);
            this.queue.notifyAll();
        }
    }

    private void lobby() {
        Connection connection;
        List<String> options = new ArrayList<>();
        ExecutorService gameThreadPool = Executors.newCachedThreadPool();
        while (true) {

            while (queue.isEmpty()) {
                try {
                    synchronized (queue) {
                        this.queue.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (queue) {
                connection = queue.remove();
            }

            synchronized(players) {
                players.put(connection.getNickname(), connection);
            }
            System.out.println("added player to a game");

            if (players.size() == 1) {
                numPlayers = -1;
                options.add("first_player");
                connection.send(new AnswerEvent("options", options));
                options.remove("first_player");
                while (numPlayers < 0) {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            connection.send(new AnswerEvent("wait"));

            if (players.size() == numPlayers) {
                GameHandler game = new GameHandler(players);
                gameThreadPool.submit(game);
                games.add(game);
                numPlayers = -1;
                players.clear();
                System.out.println("Start game...");
            }

        }

    }

    private synchronized boolean isNicknameUnique(Connection connection) {

        synchronized (players) {
            if (players.containsKey(connection.getNickname())) {
                connection.send(new AnswerEvent("error", "Nickname already taken!"));
                return false;
            }
        }
        synchronized (games) {
            for (GameHandler game : games) {
                if (game.getNicknames().contains(connection.getNickname())) {
                    connection.send(new AnswerEvent("error", "Nickname already taken!"));
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized void setNumPlayers (int num) throws OutOfBoundsException {
        if (num != 2 && num != 3) {
            throw new OutOfBoundsException();
        } else {
            this.numPlayers = num;
            notifyAll();
        }
    }

    protected void removeConnection(Connection connection) {
        System.out.println("removing connection");
        synchronized (queue) {
            queue.remove(connection);
        }
        synchronized (players) {
            players.values().stream().filter(c -> !c.equals(connection)).forEach(c -> {
                disconnectConnection(c);
                players.remove(c.getNickname());
            });
        }
        synchronized (games) {
            for (GameHandler game : games) {
                if (game.getNicknames().contains(connection.getNickname())) {
                    game.getConnections()
                            .stream()
                            .filter(c -> !c.equals(connection))
                            .forEach(this::disconnectConnection);
                    games.remove(game);
                }
            }
        }
    }

    private void disconnectConnection(Connection connection) {
        connection.send(new AnswerEvent("stop", "A client has disconnected, closing the game"));
        connection.stopConnection();
    }

}
