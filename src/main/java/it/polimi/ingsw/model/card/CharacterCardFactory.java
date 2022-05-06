package it.polimi.ingsw.model.card;

public class CharacterCardFactory {

    public CharacterCard setSubclass(CharacterCard card) {
        switch (card.getCategory()) {
            case "influence" -> {
                return new InfluenceCharacterCard(card);
            }
            case "mother_nature" -> {
                return new MotherNatureCharacterCard(card);
            }
            case "movement" -> {
                return new MovementCharacterCard(card);
            }
            default -> {
                return null;
            }
        }
    }

}
