package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.Comparator;
import java.util.List;

//TODO remain card 2 and card 10
public class MovementHandler extends Handler {

    MovementCharacterCard card;

    protected MovementHandler(List<Player> players, MovementCharacterCard card) {
        super(players);
        this.card = card;
    }

    /**extraAction for Card 10. int value is mapped so that each digit is an option following the schema:
     * most left digit -> entrance pawn position (2)
     * mid left digit -> entrance pawn color (2)
     * mid right digit -> entrance pawn position (1)
     * most right digit -> entrance pawn color (1)
     *
     * @param currentPlayer
     * @param value
     * @param model
     */
    @Override
    protected void extraAction(Player currentPlayer, int value, Model model) {
        if (card.getNumOfStudentsToReturn() > 0) { //Card 12
            model.returnStudentsToBag(Color.values()[value], card.getNumOfStudentsToReturn());
        } else if (card.getPossibleExchange() > 0) { //Card 10
            int num = card.getNumOfStudentsToReturn();
            for (; num > 0 && num/10 > 0; num = num/100) { //TODO check zeros es 0123
                currentPlayer.exchangeStudentsDiningRoomEntrance(Color.values()[num%10 - 1], (num/10)%10 - 1);
            }
        }

    }

    @Override
    protected void professorControl(Player currentPlayer, Color color, Board<Professor> startingProfBoard) {
        Professor professor = getProfessor(color, startingProfBoard);
        Board<Professor> professorBoard = getProfessorBoard(color, startingProfBoard);

        Player playerWithMoreStudent =
                players
                        .stream()
                        .max(Comparator.comparingInt(x -> getPlayersNumStudentsDR(x, color)))
                        .get();

        for (Player player : players) {
            if ( !player.equals(playerWithMoreStudent) &&
                    getPlayersNumStudentsDR(player, color) == getPlayersNumStudentsDR(playerWithMoreStudent, color))
            {
                if (currentPlayer.equals(player) && card.getExtraControl() == 1) { // Card 2
                    playerWithMoreStudent = currentPlayer;
                }
                else {
                    playerWithMoreStudent = null;
                }
            }
        }

        if (playerWithMoreStudent != null) {
            playerWithMoreStudent.getSchool().setProfessor(professor, professorBoard);
        }
    }

}
