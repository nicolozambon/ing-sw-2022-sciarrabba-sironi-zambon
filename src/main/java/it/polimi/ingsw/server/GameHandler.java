package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import it.polimi.ingsw.model.*;

public class GameHandler implements Runnable {

    private Model model;
    private ModelBuilder modelBuilder;

    public GameHandler(List<String> nicknames) {
        model = modelBuilder.buildModel(nicknames);
    }

    @Override
    public void run() {

    }
}
