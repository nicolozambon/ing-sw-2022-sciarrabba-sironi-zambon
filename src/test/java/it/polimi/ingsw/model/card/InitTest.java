package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;

class InitTest {

    /**
     * Method contentTest checks the content of the character JSON file.
     *
     * @throws FileNotFoundException when file is invalid.
     */
    @Test
    @DisplayName("Assistants and characters parsing test")
    void contentTest() throws FileNotFoundException {
        Gson gson = new Gson();
        AssistantCard[] assistants = gson.fromJson(new FileReader("src/main/resources/json/assistant_cards.json"), AssistantCard[].class);
        CharacterCard[] characters = gson.fromJson(new FileReader("src/main/resources/json/character_cards.json"), CharacterCard[].class);
        assertEquals(40, assistants.length);
        assertEquals(12, characters.length);
    }

}
