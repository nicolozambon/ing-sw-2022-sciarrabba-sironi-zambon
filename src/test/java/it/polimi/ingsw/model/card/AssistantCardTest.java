package it.polimi.ingsw.model.card;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {
    private AssistantCard[] tests;

    public AssistantCardTest() {
        try {
            Gson gson = new Gson();
            this.tests = gson.fromJson(new FileReader("src/main/resources/config/assistant_cards.config"), AssistantCard[].class);
        } catch (FileNotFoundException exception) {}
    }

    @Test
    void lengthNotNull() {
        assertNotEquals(0, this.tests.length);
    }

    @Test
    void valueBetweenOneAndTen() {
        int value = 0;
        for (AssistantCard card : this.tests) {
            value = card.getValue();
            assertTrue(value <= 10 && value >= 1);
        }
    }

    @Test
    void stepsBetweenOneAndFive() {
        int steps = 0;
        for (AssistantCard card : this.tests) {
            steps = card.getSteps();
            assertTrue(steps <= 5 && steps >= 1);
        }
    }
}