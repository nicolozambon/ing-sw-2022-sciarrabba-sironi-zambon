package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class Island extends Board<Student>{

    private Tower tower;

    public Island(List<Student> students) {
        super(students);
        tower = null;
    }

    public Tower setTower(Tower tower){
        Tower temp = this.tower;
        this.tower = tower;
        return temp;
    }

    public Tower getTower(){
        return tower;
    }

    private int countByColor(Color color){
        int num = (int) this.getPawns()
                .stream()
                .filter(x -> x.getColor() == color)
                .count();
        return num;
    }

    public Player getInfluencer(List<Player> players){
        Player influencer = null;
        List<Integer> values = new ArrayList<Integer>();
        int highestInfluence = 0;
        int influence;

        if (tower != null) influencer = tower.owner;

        for (Player player : players) {
            influence = 0;
            if (influencer == player) influence++;

            for(Professor prof : player.school.professorsTable.getPawns()){
                influence = influence + countByColor(prof.getColor());
            }

            if (influence > highestInfluence) {
                highestInfluence = influence;
                influencer = player;
            }
            values.add(influence);
        }

        final int streamVar = highestInfluence;

        if( values.stream().filter(x -> x == streamVar).count() > 1){
            influencer = tower.owner;
        }

        return influencer;
    }

    public int getInfluence(Player player){
        int influence = 0;
        if (player == tower.owner) influence++;

        for(Professor prof : player.school.professorsTable.getPawns()){
            influence = influence + countByColor(prof.getColor());
        }
        return influence;
    }

}
