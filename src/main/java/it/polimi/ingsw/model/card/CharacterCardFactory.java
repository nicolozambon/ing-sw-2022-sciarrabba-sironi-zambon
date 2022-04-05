package it.polimi.ingsw.model.card;

public class CharacterCardFactory {

    public void CharacterCardFactory() {}

    public CharacterCard setSubclass(CharacterCard card) {
        CharacterCard final_card = null;
        switch(card.getCategory()) {
            case "influence":
                final_card = new InfluenceCharacterCard(card);
            case "mother_nature":
                final_card = new MotherNatureCharacterCard(card);
            case "movement":
                final_card = new MovementCharacterCard(card);
        }
        return final_card;
    }

}