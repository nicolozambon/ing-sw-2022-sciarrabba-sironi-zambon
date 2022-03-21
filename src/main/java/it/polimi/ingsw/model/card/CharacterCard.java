package it.polimi.ingsw.model.card;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CharacterCard {

    private CharacterCard[] cards;

    public CharacterCard() {

    }

    public void doEffectByID(int ID) {

    }

    private void getCardsConfiguration() throws FileNotFoundException {
        Gson gson = new Gson();
        CharacterCard[] cards = gson.fromJson(new FileReader("src/main/resources/json/characters_card.json"), CharacterCard[].class);
        this.cards = cards;
    }

}
