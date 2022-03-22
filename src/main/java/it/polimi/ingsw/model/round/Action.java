package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.manager.Handler;

public class Action {

    private Player player;
    private AssistantCard assistantCard;
    private Handler handler;

    public Action(Player player, AssistantCard assistantCard){
        this.player = player;
        this.assistantCard = assistantCard;
        handler = new Handler();
    }

    public void playCharacterCard(Handler manager) {
        this.manager = manager;
    }



    public void moveStudentIsland(Student student, Island island){
        player.school.entrance.moveToPawn(student, island);
    }

    public void moveMotherNature(int steps){

    }

    public void putTower(Player p, Island island){
        island.setTower(p.school.towersBoard.pawns.remove(0));
    }


}
