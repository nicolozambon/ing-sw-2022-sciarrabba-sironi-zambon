package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private final String DEFAULT_IP_SETTINGS = "";
    private int port;
    private String ip;
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
        this.ip = DEFAULT_IP_SETTINGS;
    }

    public Server(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public void startServer() throws IOException {
        if (!ip.equals(DEFAULT_IP_SETTINGS)) {
            // SET A SPECIFIC IP
            // 127.0.0.1 is the loopback address (server and client in the same host)
            // to bind on any network address 0.0.0.0
            // to bind to a specific IP address, e.g. 192.168.1.1
            // InetAddress addr = InetAddress.getByName("127.0.0.1");
            // serverSocket = new ServerSocket( port);
            InetAddress address = InetAddress.getByName("127.0.0.1");
            serverSocket = new ServerSocket(port, 0, address);
        } else {
            //open TCP port
            serverSocket = new ServerSocket(port);
        }
        System.out.println("Server socket ready on port: " +  serverSocket.getLocalPort());
        System.out.println("Server socket ready on IP: " + serverSocket.getInetAddress().getHostAddress());

        //wait for connection
        Socket socket = serverSocket.accept();
        System.out.println("Received client connection");
        // open input and output streams to read and write
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        //read from and write to the connection until I receive "quit"
        while (true) {
            String line = in.nextLine();
            if (line.equals("quit")) {
                break;
            } else {
                out.println("UPPER CASE: " + line.toUpperCase());
                out.flush();
            }
        }
        //close streams and socket
        System.out.println("Closing sockets");
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }

}
