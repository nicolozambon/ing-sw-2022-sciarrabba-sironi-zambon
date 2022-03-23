package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.component.card.AssistantCard;
import it.polimi.ingsw.model.component.card.CharacterCard;

import java.io.FileNotFoundException;
import java.io.FileReader;


public class Init {

    private AssistantCard[] assistants;
    private CharacterCard[] characters;

    public Init() {
        readCardsConfiguration();
    }

    public AssistantCard[] getAssistantsConfiguration() {
        return this.assistants;
    }

    public CharacterCard[] getCharactersConfiguration() {
        return this.characters;
    }

    private void readCardsConfiguration() {
        try {
            Gson gson = new Gson();
            AssistantCard[] assistants = gson.fromJson(new FileReader("src/main/resources/json/assistant_cards.json"), AssistantCard[].class);
            CharacterCard[] characters = gson.fromJson(new FileReader("src/main/resources/json/character_cards.json"), CharacterCard[].class);
            this.assistants = assistants;
            this.characters = characters;
            //TODO should check if arrays are not empty
        } catch (FileNotFoundException exception) {
            //TODO Handle exception --> should quit game
            exception.printStackTrace();
        }
    }

}
