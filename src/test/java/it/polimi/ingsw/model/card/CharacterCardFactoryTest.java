package it.polimi.ingsw.model.card;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardFactoryTest {

    private CharacterCard[] tests;

    public CharacterCardFactoryTest() {
        try {
            Gson gson = new Gson();
            this.tests = gson.fromJson(new FileReader("src/main/resources/config/character_cards.json"), CharacterCard[].class);
        } catch (FileNotFoundException exception) {
            assertTrue(false);
        }
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