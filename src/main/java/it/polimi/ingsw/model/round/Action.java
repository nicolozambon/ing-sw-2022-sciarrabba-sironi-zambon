package it.polimi.ingsw.model.round;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.manager.Manager;
import it.polimi.ingsw.model.enums.*;

public class Action {

    private Player player;
    private Manager manager;

    public Action(Player player){
        this.player = player;
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


}
