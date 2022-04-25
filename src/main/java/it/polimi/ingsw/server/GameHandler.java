package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;

import java.io.*;
import java.util.*;

public class GameHandler implements Runnable {

    private Model model;
    private ModelBuilder modelBuilder;
    Map<String, Connection> playersConnection;

    public GameHandler(Map<String, Connection> playersConnection) {

        this.playersConnection = new HashMap<>(playersConnection);

        this.modelBuilder = new ModelBuilder();

        this.model = modelBuilder.buildModel(this.playersConnection.keySet().stream().toList());
        System.out.println("GameHandler");
    }

    @Override
    public void run() {
        System.out.println("GameHandler");
        try {
            System.out.println("Hello");
            ByteArrayOutputStream os = sendModelSerialized();
            receiveModelSerialized(os);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        for (String name : playersConnection.keySet()) {

        }
    }

    public ByteArrayOutputStream sendModelSerialized (/*ArrayList<Connection> connections*/) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this.model);
        System.out.println("Sent model.");
        /*for (Connection connection : connections) {

        } */

        return byteArrayOutputStream;
    }

    public void receiveModelSerialized(ByteArrayOutputStream os) throws IOException, ClassNotFoundException {
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray() ) ;
        ObjectInputStream in = new ObjectInputStream(is);
        Model newModel = (Model)in.readObject();
        System.out.println("Read model.");
        newModel.addStudentsToClouds();
    }
}
