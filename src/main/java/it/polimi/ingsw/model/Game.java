package it.polimi.ingsw.model;

import it.polimi.ingsw.model.component.Cloud;
import it.polimi.ingsw.model.component.StudentBag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {

    Map<Integer, Integer> numOfStudent = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        this.put(4, 3);
    }};

    private ArrayList<Player> players;

    private Cloud Cloud1;
    private Cloud Cloud2;
    private Cloud Cloud3;

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
