package it.polimi.ingsw.controller.handler;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.Professor;
import it.polimi.ingsw.model.component.School;
import it.polimi.ingsw.model.component.card.MovementCharacterCard;

import java.util.List;

public class MovementModifier extends Handler {

    /* Card has this params:
    private int extraControl;
    private int possibleExchange;
    private int numOfStudentsToReturn;
    */

    private final MovementCharacterCard card;

    public MovementModifier(Player player, MovementCharacterCard card) {
        super(player);
        this.card = card;
    }

    @Override
    public void professorControl(List<Player> otherPlayers) {
        School school = super.getActualPlayer().getSchool();
        Color color = null;
        for (Player player : otherPlayers) {
            for (Professor prof : player.getSchool().getProfessorsTable().getPawns()) {
                color = prof.getColor();
                if (this.card.getExtraControl() == 1) {
                    if (school.getDiningRoomByColor(color).getNumPawns() >= player.getSchool().getDiningRoomByColor(color).getNumPawns()) {
                        school.controlProfessor(prof, player.getSchool().getProfessorsTable());
                    }
                } else {
                    if (school.getDiningRoomByColor(color).getNumPawns() > player.getSchool().getDiningRoomByColor(color).getNumPawns()) {
                        school.controlProfessor(prof, player.getSchool().getProfessorsTable());
                    }
                }
            }
        }
    }
}
