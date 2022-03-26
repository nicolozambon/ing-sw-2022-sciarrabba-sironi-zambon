package it.polimi.ingsw.model;

import it.polimi.ingsw.model.component.Coin;
import it.polimi.ingsw.model.component.Student;
import it.polimi.ingsw.model.component.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.component.card.*;

import java.util.List;
import java.util.ArrayList;

public class GameTest {
    Game testGame;
    ArrayList<Player> playerOrder;
    Player player1;
    Player player2;

    @BeforeEach
    void initialization() {
        testGame = new Game();
        List<Student> students1 = new ArrayList<Student>();
        List<Student> students2 = new ArrayList<Student>();
        //List<Tower> towers1 = new ArrayList<Tower>(BLACK);
       // List<Tower> towers2 = new ArrayList<Tower>(WHITE);
        Deck assistantCardDeck1 = new Deck(new ArrayList<Card>());
        Deck discardPileDeck1 = new Deck(new ArrayList<Card>());
        Deck assistantCardDeck2 = new Deck(new ArrayList<Card>());
        Deck discardPileDeck2 = new Deck(new ArrayList<Card>());
        Coin coin1 = new Coin();
        Coin coin2 = new Coin();

        //player1 = new Player("Player1", students1, towers1, 0, assistantCardDeck1, discardPileDeck1, coin1);
       // player2 = new Player("Player2", students2, towers2, 1, assistantCardDeck2, discardPileDeck2, coin2);

        playerOrder = testGame.getPlayerOrder();
    }

    @Test
    @DisplayName("Correctly set playerOrder")
    void checkPlayerOrderTest() {
        assertEquals(player1, playerOrder.get(0));
        assertEquals(player2, playerOrder.get(1));
    }

}
