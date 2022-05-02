package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.messages.answers.ErrorAnswer;
import it.polimi.ingsw.messages.answers.OptionAnswer;
import it.polimi.ingsw.exceptions.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class Server implements RequestListener {

    private static final String DEFAULT_IP = "";

    private final int port;
    private final String ip;

    private final Queue<Connection> queue;

    private final List<GameHandler> games;

    private final Set<Connection> unqueuedConnection;

    private final VirtualView virtualView;

    /**
     * Number of players for the match being built.
     */
    private int numPlayers = -1;


    public Server(int port){
        this.port = port;
        this.ip = DEFAULT_IP;
        this.queue = new ArrayDeque<>();
        this.games = new ArrayList<>();
        this.unqueuedConnection = new HashSet<>();
        this.virtualView = new VirtualView();
    }

    public Server(String ip, int port) {
        this.port = port;
        this.ip = ip;
        this.queue = new ArrayDeque<>();
        this.games = new ArrayList<>();
        this.unqueuedConnection = new HashSet<>();
        this.virtualView = new VirtualView();
    }

    public void startServer() throws IOException {

        ExecutorService connectionThreadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket();

        this.virtualView.addRequestListener(this);

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

                Connection connection = new Connection(socket, this, this.virtualView);
                unqueuedConnection.add(connection);

                connectionThreadPool.submit(connection);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        connectionThreadPool.shutdown();
        serverSocket.close();
    }

    public void enqueuePlayer(String nickname) {
        synchronized (queue) {
            this.queue.add(unqueuedConnection.stream().filter(x -> x.getNickname().equals(nickname)).findAny().get());
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
                    connection.send(new OptionAnswer("first_player"));
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
            } else {
                connection.send(new ErrorAnswer("Nickname already taken!"));
                connection.send(new OptionAnswer("nickname"));
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

    public synchronized void setNumPlayers (int num) throws OutOfBoundsException {
        if (num != 2 && num != 3) {
            throw new OutOfBoundsException();
        } else {
            this.numPlayers = num;
            notifyAll();
        }
    }


    @Override
    public void requestPerformed(RequestEvent requestEvent) {

        switch(requestEvent.getPropertyName()) {

            case "nickname" -> {
                enqueuePlayer(requestEvent.getString());
            }

            case "first_player" -> {
                try {
                    setNumPlayers(requestEvent.getValues()[0]);
                } catch (OutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            default -> {
                //TODO handling error
            }

        }
    }
}
