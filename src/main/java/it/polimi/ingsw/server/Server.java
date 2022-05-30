package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.exceptions.OutOfBoundsException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int port = 1337;

    private final Queue<ConnectionHandler> queue;
    private final List<GameHandler> games;
    private final Map<String, ConnectionHandler> players;

    /**
     * Number of players for the match being built.
     */
    private int numPlayers = -1;

    public Server(){
        this.queue = new ArrayDeque<>();
        this.players = new ConcurrentHashMap<>();
        this.games = new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            new Server().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

                ConnectionHandler connectionHandler = new ConnectionHandler(socket, this);
                connectionThreadPool.submit(connectionHandler);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        connectionThreadPool.shutdown();
        serverSocket.close();
    }

    public void enqueuePlayer(ConnectionHandler connectionHandler) {
        if (isNicknameUnique(connectionHandler)) {
            connectionHandler.send(new AnswerEvent("set_nickname", connectionHandler.getNickname()));
            synchronized (queue) {
                this.queue.add(connectionHandler);
                if (players.size() > 0) connectionHandler.send(new AnswerEvent("wait"));
                this.queue.notifyAll();
            }
        }
    }

    private void lobby() {
        ConnectionHandler connectionHandler;
        List<String> first_playerOption = new ArrayList<>(List.of("first_player"));
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
                connectionHandler = queue.remove();
            }

            players.put(connectionHandler.getNickname(), connectionHandler);
            System.out.println("added player to a game");

            if (players.size() == 1) {
                numPlayers = -1;
                connectionHandler.send(new AnswerEvent("options", first_playerOption));
                while (players.size() > 0 && numPlayers < 0) {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                connectionHandler.send(new AnswerEvent("wait"));
            }

            players.values().forEach(c -> c.send(new AnswerEvent("lobby", players.keySet().stream().toList())));

            if (players.size() == numPlayers) {
                GameHandler game = new GameHandler(players);
                gameThreadPool.submit(game);
                synchronized (games) {
                    games.add(game);
                }
                numPlayers = -1;
                synchronized (players) {
                    players.clear();
                }
                System.out.println("Start game...");
            }

        }

    }

    private boolean isNicknameUnique(ConnectionHandler connectionHandler) {
        if (connectionHandler.getNickname() == null) return false;

        if (connectionHandler.getNickname().length() > 12) {
            connectionHandler.send(new AnswerEvent("error", "Nickname too long"));
            return false;
        }
        if (connectionHandler.getNickname().length() < 1) {
            connectionHandler.send(new AnswerEvent("error", "Nickname too short"));
            return false;
        }

        if (players.containsKey(connectionHandler.getNickname())) {
            connectionHandler.send(new AnswerEvent("error", "Nickname already taken!"));
            return false;
        }

        synchronized (games) {
            for (GameHandler game : games) {
                if (game.getNicknames().contains(connectionHandler.getNickname())) {
                    connectionHandler.send(new AnswerEvent("error", "Nickname already taken!"));
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

    protected void removeConnection(ConnectionHandler connectionHandler) {
        System.out.println("removing connection");
        synchronized (queue) {
            queue.remove(connectionHandler);
        }
        synchronized (players) {
            if (players.containsValue(connectionHandler)) {
                players.values().stream().filter(c -> !c.equals(connectionHandler)).forEach(this::disconnectConnection);
                players.clear();
            }
        }
        synchronized (games) {
            for (GameHandler game : games) {
                if (game.getNicknames().contains(connectionHandler.getNickname())) {
                    game.getConnections()
                            .stream()
                            .filter(c -> !c.equals(connectionHandler))
                            .forEach(this::disconnectConnection);
                    games.remove(game);
                }
            }
        }
        synchronized(this) {
            notifyAll();
        }
    }

    private void disconnectConnection(ConnectionHandler connectionHandler) {
        connectionHandler.send(new AnswerEvent("stop", "A client has disconnected, closing the game"));
        connectionHandler.stopConnection();
    }

}
