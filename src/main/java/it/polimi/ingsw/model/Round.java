package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Round (ArrayList<Player> playerOrder, StudentBag sb, MotherNature mn) {
    private StudentBag studentbag;
    private MotherNature mothernature;
    public Round () {
        this.studentbag = sb;
        this.mothernature = mn;
    }

    public void planningPhase () {
        //Step 1
        putStudentsOnClouds(cloud1, cloud2, cloud3);

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

    /**
     * Refills the cloud tiles with new students from the Student's Bag
     * @param cloud1
     * @param cloud2
     * @param cloud3
     */
    public void putStudentsOnClouds(Cloud cloud1, Cloud cloud2, Cloud cloud3) {
        for (int i = 0; i < 3; i++) {
            studentbag.extractStudentAndMove(cloud1);
            studentbag.extractStudentAndMove(cloud2);
            studentbag.extractStudentAndMove(cloud3);
        }
    }



}
