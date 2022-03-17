package it.polimi.ingsw.model;

import java.util.List;

public class Island extends Board<Student>{

    Tower tower;

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

    //TODO fix not working properly
    public Player getInfluencer(List<Player> players){
        Player influencer = null;
        int highestInfluence = 0;

        if (tower != null) influencer = tower.owner;

        for (Player player : players) {
            int influence = 0;
            for(Professor prof : player.school.professorsTable.getPawns()){
                influence = influence + countByColor(prof.getColor());
            }

            if (influencer == player) influence++;

            //Here!
            if (influence > highestInfluence) highestInfluence = influence;

        }

        return influencer;
    }

}
