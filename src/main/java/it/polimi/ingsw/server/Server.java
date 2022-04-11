package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final String DEFAULT_IP_SETTINGS = "";
    private int port;
    private String ip;
    private ServerSocket serverSocket;

    private int numOfPlayers;

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
                executor.submit(new GameHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        executor.shutdown();
        serverSocket.close();
    }
}
