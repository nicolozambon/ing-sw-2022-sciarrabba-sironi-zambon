package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.OutOfBoundsException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;

public class Server {

    private final String DEFAULT_IP_SETTINGS = "";
    private int port;
    private String ip;

    /**
     * Identifies a new connection with the nickname.
     */
    private HashMap<Connection, String> players = new HashMap<>();

    /**
     * Contains all active connections to the server.
     */
    private List<Connection> connections = new ArrayList<>();

    /**
     * Number of players for the match being built.
     */
    private int numPlayers;

    private GameHandler currentGame;

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
                executor.submit(new Connection(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        executor.shutdown();
        serverSocket.close();
    }

    protected void addPlayer(Connection c, String nickname) throws OutOfBoundsException {
        this.connections.add(c);
        this.lobby(c, nickname);
    }

    protected void lobby (Connection c, String nickname) throws OutOfBoundsException {
        this.players.put(c, nickname);
        if (players.size() == 1) { //First player, decides for the number of players.
            //TODO request number of players to the connection of the first player.
            setNumPlayers(2);
        } else if (players.size() == numPlayers) { //Start round, reached num of players.
            //TODO get an arraylist with nicknames and send it to gamehandler.
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
