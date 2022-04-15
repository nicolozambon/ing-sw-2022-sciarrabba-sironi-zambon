package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.ConnectionAction;
import it.polimi.ingsw.exceptions.OutOfBoundsException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final String DEFAULT_IP_SETTINGS = "";
    private int port;
    private String ip;

    /**
     * Identifies a new connection with the nickname.
     */
    private Map<String, InitialClientConnection> players = new HashMap<>();

    /**
     * Number of players for the match being built.
     */
    private int numPlayers = -1;

    //private GameHandler currentGame;

    public Server(int port){
        this.port = port;
        this.ip = DEFAULT_IP_SETTINGS;
    }

    public Server(String ip, int port) {
        this.port = port;
        this.ip = ip;
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

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Received client connection");
                executor.submit(new InitialClientConnection(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        executor.shutdown();
        serverSocket.close();
    }

    protected synchronized void addPlayer(String nickname, InitialClientConnection connection) {
        if(!players.containsKey(nickname)) {
            players.put(nickname, connection);
            players.get(nickname).setState(ConnectionAction.WAITING);
            lobby(nickname);
        } else {
            //TODO gestire errore nickname uguale
        }
    }

    private synchronized void lobby(String nickname) {

        if (players.size() == 1) { //First player, decides for the number of players.
            players.get(nickname).setState(ConnectionAction.SET_NUM_PLAYERS);
        } else if (players.size() == numPlayers) { //Start round, reached num of players.

            boolean check = true;
            for (String name : players.keySet()) {
                if (players.get(name).getState() != ConnectionAction.WAITING) {
                    check = false;
                    System.out.println(name + " causing " + check);
                }
            }

            if (check) {
                System.out.println("Starting new Thread ");
                /*for (String name : players.keySet()) {
                    players.get(name).setState(ConnectionAction.START);
                }*/
                new Thread(new GameHandler(players)).start();

                players = new HashMap<>();
                numPlayers = -1;
            }
        }
        //TODO enrich with messages signaling the current situation of the queue to the connected players.
    }

    protected void setNumPlayers(int num) throws OutOfBoundsException {
        if (num != 2 && num != 3) {
            throw new OutOfBoundsException();
        } else {
            this.numPlayers = num;
        }
    }

}
