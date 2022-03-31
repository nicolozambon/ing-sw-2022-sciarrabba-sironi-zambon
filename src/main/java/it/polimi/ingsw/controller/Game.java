package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.Cloud;
import it.polimi.ingsw.model.component.StudentBag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {

    private Map<Integer, Integer> numOfStudent = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        this.put(4, 3);
    }};

    private ArrayList<Player> players;

    private ArrayList<Cloud> Clouds;

    public Game() {
        StudentBag studentbag = new StudentBag();
    }

    public ArrayList<Player> getPlayerOrder() {
        return this.players;
    }

    void addPlayer(Player player) {
        this.players.add(player);
    }

    private Player isThereWinner(){
        return null;
    }
}