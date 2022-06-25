package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.exceptions.WrongSetupException;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class, handling first client connection, lobby functionality, matches creation and unexpected client
 * disconnection
 */
public class Server {

    /**
     * Port number to be used by the Server
     */
    private static final int port = 1337;

    /**
     * Queue of ConnectionHandler, before acceptance
     * @see ConnectionHandler
     */
    private final Queue<ConnectionHandler> queue;

    /**
     * Map of nicknames and ConnectionHandler of that player
     * @see ConnectionHandler
     */
    private final Map<String, ConnectionHandler> players;

    /**
     * List of GameHandler, includes all games hosted by the Server
     * @see GameHandler
     */
    private final List<GameHandler> games;

    /**
     * Number of players of the game
     */
    private int numPlayers = -1;

    /**
     * Boolean flag to set the game to complete rules or simple rules
     */
    private boolean completeRule = true;

    /**
     * Server constructor, initialize all private attributes and create if not exists 'saves' folder
     */
    public Server(){
        this.queue = new ArrayDeque<>();
        this.players = new ConcurrentHashMap<>();
        this.games = new ArrayList<>();
        new File("./saves").mkdirs();
    }

    /**
     * Main method for server
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        try {
            new Server().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts Server on specified port, begin to accept client connections
     * @throws IOException if server can't accept the client
     */
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

        //start a thread to handling the lobby
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

    /**
     * Enqueues each client connection after it received the nickname, check if nickname is valid and then add it to the
     * queue
     * @param connectionHandler ConnectionHandler of the client who has set nickname
     * @see ConnectionHandler
     */
    public void enqueuePlayer(ConnectionHandler connectionHandler) {
        if (isNicknameValid(connectionHandler)) {
            connectionHandler.send(new AnswerEvent("set_nickname", connectionHandler.getNickname()));
            synchronized (queue) {
                this.queue.add(connectionHandler);
                if (players.size() > 0) connectionHandler.send(new AnswerEvent("wait"));
                this.queue.notifyAll();
            }
        }
    }

    /**
     * Handles different client and put it in a lobby, ready to start the game
     */
    private void lobby() {
        ConnectionHandler connectionHandler;
        List<String> first_playerOption = new ArrayList<>(List.of("firstPlayer"));
        ExecutorService gameThreadPool = Executors.newCachedThreadPool();
        while (true) {

            //Check if there is client ready to join a game
            while (queue.isEmpty()) {
                try {
                    synchronized (queue) {
                        this.queue.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Add client to the game
            synchronized (queue) {
                connectionHandler = queue.remove();
            }
            synchronized (players) {
                players.put(connectionHandler.getNickname(), connectionHandler);
            }
            System.out.println("added player to a game");

            //Ask the first client with how many players the game will be
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

            //Send to each client who is in the lobby
            players.values().forEach(c -> c.send(new AnswerEvent("lobby", players.keySet().stream().toList())));

            //Start the game once the lobby is full
            if (players.size() == numPlayers) {
                GameHandler game = new GameHandler(players, completeRule);
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

    /**
     * Checks whether the nickname is valid
     * @param connectionHandler ConnectionHandler to the client which has set a nickname
     * @return true if the nickname is valid, false otherwise
     */
    private boolean isNicknameValid(ConnectionHandler connectionHandler) {
        if (connectionHandler.getNickname() == null) return false;

        if (connectionHandler.getNickname().length() > 12) {
            connectionHandler.send(new AnswerEvent("error", "Nickname too long"));
            return false;
        }
        if (connectionHandler.getNickname().length() < 1) {
            connectionHandler.send(new AnswerEvent("error", "Nickname too short"));
            return false;
        }

        synchronized (players) {
            if (players.containsKey(connectionHandler.getNickname())) {
                connectionHandler.send(new AnswerEvent("error", "Nickname already taken!"));
                return false;
            }
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

    /**
     * Sets game with first player's preferences
     * @param num number of players for the game
     * @param completeRule true if the game has complete rules, false otherwise
     * @throws WrongSetupException if the number of players is not allowed
     */
    public synchronized void firstPlayerSetup(int num, int completeRule) throws WrongSetupException {
        if ((num != 2 && num != 3) || (completeRule != 1 && completeRule != 0)) {
            throw new WrongSetupException();
        } else {
            this.numPlayers = num;
            this.completeRule = completeRule == 1;
            notifyAll();
        }
    }

    /**
     * Closes the connection for client who has finished the match
     * @param connectionHandler ConnectionHandler of the client who has finished the match
     */
    protected void endGame(ConnectionHandler connectionHandler) {

        synchronized (games) {
            GameHandler currentGame = null;
            for (GameHandler game : games) {
                if (game.getNicknames().contains(connectionHandler.getNickname())) {
                    game.removeConnection(connectionHandler);
                    currentGame = game;
                }
            }

            //Removing tha game from games if all players have received and command
            if (currentGame != null && currentGame.getConnections().isEmpty()) games.remove(currentGame);
        }
    }

    /**
     * Closes the connection for client who has unexpectedly disconnected and every other clients in the same lobby or
     * match
     * @param connectionHandler ConnectionHandler of the client who has unexpectedly disconnected
     */
    protected void unexpectedDisconnection(ConnectionHandler connectionHandler) {
        System.out.println("removing connection");
        synchronized (queue) {
            queue.remove(connectionHandler);
        }
        synchronized (players) {
            if (players.containsValue(connectionHandler)) {
                players.values().stream().filter(c -> !c.equals(connectionHandler)).forEach(this::stopConnection);
                players.clear();
            }
        }
        synchronized (games) {
            for (GameHandler game : games) {
                if (game.getNicknames().contains(connectionHandler.getNickname())) {
                    game.getConnections()
                            .stream()
                            .filter(c -> !c.equals(connectionHandler))
                            .forEach(this::stopConnection);
                    games.remove(game);
                }
            }
        }
        synchronized(this) {
            notifyAll();
        }
    }

    /**
     * Stops client's ConnectionHandler
     * @param connectionHandler ConnectionHandler of the client who has to stop
     */
    private void stopConnection(ConnectionHandler connectionHandler) {
        connectionHandler.send(new AnswerEvent("stop", "A client has disconnected, closing the game"));
        connectionHandler.stopConnection();
    }

}
