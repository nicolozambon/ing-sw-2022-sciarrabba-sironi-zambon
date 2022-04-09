package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameHandler implements Runnable {

    private Socket socket;

    public GameHandler(Socket socket) {
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
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
