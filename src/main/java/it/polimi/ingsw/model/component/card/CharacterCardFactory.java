package it.polimi.ingsw.model.component.card;

public class CharacterCardFactory {

    public void CharacterCardFactory() {}

    public CharacterCard setSubclass(CharacterCard card) {
        CharacterCard final_card;
        switch(card.getCategory()) {
            case "influence":
                final_card = new InfluenceCharacterCard();
            case "mother_nature":
                final_card = new MotherNatureCharacterCard();
            case "movement":
                final_card = new MovementCharacterCard();
        }
        return final_card;
    }

}
