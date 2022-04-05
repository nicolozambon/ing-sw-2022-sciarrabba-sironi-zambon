package it.polimi.ingsw.model.card;

import com.google.gson.Gson;
import it.polimi.ingsw.model.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    private CharacterCard[] tests;

    public CharacterCardTest() {
        try {
            Gson gson = new Gson();
            this.tests = gson.fromJson(new FileReader("src/main/resources/json/character_cards.json"), CharacterCard[].class);
        } catch (FileNotFoundException exception) {}
    }

    public CharacterCard[] getAll() {
        return this.tests;
    }

    @Test
    void lengthNotNull() {
        assertNotEquals(0, this.tests.length);
    }

    @Test
    void coinsBetweenOneAndThree() {
        int coins = 0;
        for (CharacterCard card : this.tests) {
            coins = card.getCoins();
            assertTrue(coins <= 3 && coins >= 1);
        }
    }

}