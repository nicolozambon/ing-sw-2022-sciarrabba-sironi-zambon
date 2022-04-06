package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardFactoryTest {

    private CharacterCard[] tests;

    public CharacterCardFactoryTest() {
        CharacterCardTest tester = new CharacterCardTest();
        this.tests = tester.getAll();
    }

    @Test
    void permittedCategories() {
        String[] permitted = {"influence", "mother_nature", "movement"};
        String category = null;
        for (CharacterCard card : this.tests) {
            category = card.getCategory();
            assertTrue(Arrays.asList(permitted).contains(category));
        }
    }
}