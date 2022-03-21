package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;

import java.util.ArrayList;
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
     *
     * @param clouds All Clouds.
     */
    public void planningPhase(Cloud... clouds) {
        ArrayList<AssistantCard> playedCardInRound = new ArrayList<AssistantCard>();
        boolean hasBeenPlayed = false;
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
    }

    public void actionPhase() {
        for (Player p : playerOrder) {

        }
    }

}
