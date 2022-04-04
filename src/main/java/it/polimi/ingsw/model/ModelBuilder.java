package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterCardFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;


public class ModelBuilder {

    private AssistantCard[] assistants;
    private CharacterCard[] characters;

    private ArrayList<CharacterCard> selectedCharacterCards;

    public ModelBuilder() {
        readCardsConfiguration();
        chooseCharacterCards();
    }

    protected AssistantCard[] getAssistantsConfiguration() {
        return this.assistants;
    }

    protected CharacterCard[] getCharactersConfiguration() {
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

    private void chooseCharacterCards() {
        CharacterCardFactory factory = new CharacterCardFactory();
        Random generator = new Random();
        int index = 0;
        for (int i = 0; i < 3; i++) {
            index = generator.nextInt(this.characters.length);
            this.selectedCharacterCards.add(factory.setSubclass(this.characters[index]));
        }
    }
}
