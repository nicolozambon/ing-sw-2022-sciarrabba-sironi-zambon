package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Round {

    private StudentBag studentbag;
    private MotherNature mothernature;

    private List<Player> playerOrder;

    public Round(List<Player> playerOrder, StudentBag sb, MotherNature mn) {
        this.studentbag = sb;
        this.mothernature = mn;
        this.playerOrder = playerOrder;
    }

    /**
     * Planning phase of the game.
     * Step 1 -> Populate all 2(3) clouds with 3 students.
     * Step 2 -> Choose an assistant card and put it in the discard deck.
     *        -> Modify the order of playerOrder
     * @param clouds All Clouds.
     */
    public void planningPhase(Cloud... clouds) {
        List<AssistantCard> playedCardInRound = new ArrayList<>();
        boolean hasBeenPlayed = false;

        int i = 0;
        int minValue = 100; //FIRST

        //Step 1
        for (Cloud cloud : clouds) {
            studentbag.extractStudentAndMove(cloud);
        }

        //Step 2
        for (Player p : playerOrder) {
            AssistantCard choice;
            int index = 0; //TODO: Fix
            do {
                //TODO: Player makes choice
                hasBeenPlayed = false;
                choice = (AssistantCard) p.playAssistantCard(index);
                for (AssistantCard c : playedCardInRound) {
                    if (choice.equals(c)) {
                        hasBeenPlayed = true;
                    }
                }
            } while (hasBeenPlayed == true);
            playedCardInRound.add(choice);
        }

        //Step 3
        for (Player p : playerOrder) {

        }

    }

    public void actionPhase() {
        Board<Student> dest;
        Student student;
        for (Player p : playerOrder) {
            for (int i = 0; i < 4; i++){
                //player choose island where to move students
                p.school.diningRoom.moveToPawn(student, dest);
            }
        }
    }

}
