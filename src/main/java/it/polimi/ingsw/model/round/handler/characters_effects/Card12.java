package it.polimi.ingsw.model.round.handler.characters_effects;

import it.polimi.ingsw.model.round.handler.Handler;
import it.polimi.ingsw.model.*;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import java.util.ArrayList;

import static it.polimi.ingsw.model.enums.Color.*;

public class Card12 extends Handler {
    public Card12() {

    }

    public void moveThreeStudentsOfTypeToStudentBag (Board<Student> studentBag, ArrayList<Player> players) {
        //TODO: get choice from player.

        for (Player p : players) {
            for (int i = 0; i < 3; i++) {
                switch (choice) {
                    case RED:
                        if (!(p.school.RedDiningTable.isEmpty())) {
                            p.school.RedDiningTable.moveToPawn(p.school.RedDiningTable.get(p.school.RedDiningTable.size()-1), studentBag);
                        }
                    case GREEN:
                        if (!(p.school.GreenDiningTable.isEmpty())) {
                            p.school.GreenDiningTable.moveToPawn(p.school.GreenDiningTable.get(p.school.GreenDiningTable.size()-1), studentBag);
                        }
                    case YELLOW:
                        if (!(p.school.YellowDiningTable.isEmpty())) {
                            p.school.YellowDiningTable.moveToPawn(p.school.YellowDiningTable.get(p.school.YellowDiningTable.size()-1), studentBag);
                        }
                    case BLUE:
                        if (!(p.school.BlueDiningTable.isEmpty())) {
                            p.school.BlueDiningTable.moveToPawn(p.school.BlueDiningTable.get(p.school.BlueDiningTable.size()-1), studentBag);
                        }
                    case PINK:
                        if (!(p.school.PinkDiningTable.isEmpty())) {
                            p.school.PinkDiningTable.moveToPawn(p.school.PinkDiningTable.get(p.school.PinkDiningTable.size()-1), studentBag);
                        }
                }
            }
        }

    }
}
