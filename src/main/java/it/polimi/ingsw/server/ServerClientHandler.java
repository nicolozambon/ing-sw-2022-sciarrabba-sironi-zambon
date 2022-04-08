package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Repeatable;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientHandler implements Runnable {

    private Socket socket;

    public ServerClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while (true) {
                String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                } else {
                    out.println("UPPER CASE: " + line.toUpperCase());
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
