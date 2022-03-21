package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Round (List<Player> playerOrder, StudentBag sb, MotherNature mn) {

    private StudentBag studentbag;
    private MotherNature mothernature;

    public Round () {
        this.studentbag = sb;
        this.mothernature = mn;
    }

    public void planningPhase (Cloud ... clouds) {
        //Step 1
        for(Cloud cloud : clouds) {
            studentbag.extractStudentAndMove(cloud);
        }

        //Step 2
        for (Player : playerOrder) {
            //TODO: choose an assistant card and put it in the discard deck.
            //TODO: check that the choice can be made (one cannot choose to play the same assistant card already played by another player in the same round.
                    // In the rare case you only have a matching card, you must play it. However, if you do so, you will play after the player who played the card first.
        }
    }

    public void actionPhase () {
        for (Player : playerOrder) {
            for (int i = 0; i < 3; i++) {//TODO: game with 3 players?

            }
        }
    }

}
