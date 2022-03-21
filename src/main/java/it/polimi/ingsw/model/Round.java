package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.AssistantCard;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private StudentBag studentbag;
    private MotherNature mothernature;

    private List<Player> playerOrder;

    public Round (List<Player> playerOrder, StudentBag sb, MotherNature mn) {
        this.studentbag = sb;
        this.mothernature = mn;
        this.playerOrder = playerOrder;
    }

    /**
     * Planning phase of the game.
     * Step 1 -> Populate all 2(3) clouds with 3 students.
     * Step 2 -> Choose an assistant card and put it in the discard deck.
     * @param clouds All Clouds.
     */
    public void planningPhase (Cloud ... clouds) {
        ArrayList<AssistantCard> playedCardInRound;
        //Step 1
        for(Cloud cloud : clouds) {
            studentbag.extractStudentAndMove(cloud);
        }

        //Step 2
        for (Player p : playerOrder) {
            //choice = askuser; //TODO: Implement a way to get the choice of the user.
            playedCardInRound.add(choice);
            if (choice != ) {
                    p.playAssistantCard(choice)
            };
        }
    }

    public void actionPhase () {
        for (Player p : playerOrder) {

        }
    }

}
