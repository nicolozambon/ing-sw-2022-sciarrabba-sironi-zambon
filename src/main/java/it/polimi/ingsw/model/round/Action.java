package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.manager.Manager;
import it.polimi.ingsw.model.enums.*;

public class Action {

    private Player player;
    private AssistantCard assistantCard;
    private Manager manager;

    public Action(Player player, AssistantCard assistantCard){
        this.player = player;
        this.assistantCard = assistantCard;
        manager = new Manager();
    }

    public void playCharacterCard(Manager manager) {
        this.manager = manager;
    }

    public void moveStudentDiningRoom(Student student){
        switch (student.getColor()) {
            case RED -> player.school.entrance.moveToPawn(student, player.school.RedDiningTable);
            case PINK -> player.school.entrance.moveToPawn(student, player.school.PinkDiningTable);
            case BLUE -> player.school.entrance.moveToPawn(student, player.school.BlueDiningTable);
            case YELLOW -> player.school.entrance.moveToPawn(student, player.school.YellowDiningTable);
            case GREEN -> player.school.entrance.moveToPawn(student, player.school.GreenDiningTable);
        }
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
