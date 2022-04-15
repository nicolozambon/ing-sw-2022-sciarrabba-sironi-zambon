package it.polimi.ingsw.client;

import it.polimi.ingsw.enums.ConnectionAction;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class InitialClient implements Client {

    private String ip;
    private int port;
    private Socket socket;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner stdin;

    private boolean active;

    private String nickname;

    public InitialClient(String ip, int port){
        this.ip = ip;
        this.port = port;
        stdin = new Scanner(System.in);
    }


    public synchronized void read() {
        try {
            Object input = inputStream.readObject();
            if (input instanceof ConnectionAction) {
                switch ((ConnectionAction) input) {
                    case SET_NICKNAME -> {
                        writeNickname();
                        break;
                    }
                    case SET_NUM_PLAYERS -> {
                        writeNumPlayer();
                        break;
                    }
                    case WAITING -> {
                        System.out.println("Waiting other players...");
                        break;
                    }
                    case START -> {
                        System.out.println("Starting game...");
                        try {
                            outputStream.writeObject(nickname);
                            outputStream.flush();
                        } catch(Exception e) {
                            e.printStackTrace();
                            active = false;
                        }
                        startGame();
                        break;
                    }
                }
            } else if (input instanceof String) {
                System.out.println((String) input);
            }
        } catch (Exception e){
            e.printStackTrace();
            active = false;
        }
    }

    public synchronized void writeNickname() {
        System.out.println("Set your Nickname:");
        try {

            nickname = stdin.nextLine();
            outputStream.writeObject(nickname);
            outputStream.flush();

        }catch(Exception e){
            e.printStackTrace();
            active = false;
        }
    }

    public synchronized void writeNumPlayer() {
        System.out.println("You are the first player, choose number of players (2-3):");
        try {
            int input = stdin.nextInt();
            outputStream.writeObject(input);
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
            active = false;
        }
    }

    private synchronized void startGame() {
        try {
            new GameClient(nickname, stdin, inputStream, outputStream).startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopClient();
    }

    @Override
    public void startClient() throws IOException {
        socket = new Socket(ip, port);
        active = true;
        System.out.println("Connection established");

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        while (active) {
            read();
        }

        stopClient();
    }

    @Override
    public void stopClient() {
        try {
            stdin.close();
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            active = false;
        }


    }

}
