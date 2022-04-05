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

    /**
     *
     * @param currentPlayer
     * @param values values[0] = color 1, values[1] = position 1, values[2] = color 2, values[3] = position 2
     * @param model
     */
    @Override
    protected void extraAction(Player currentPlayer, Model model, int ... values) {
        if (card.getNumOfStudentsToReturn() > 0) { //Card 12
            model.returnStudentsToBag(Color.values()[values[0]], card.getNumOfStudentsToReturn());
        } else if (card.getPossibleExchange() > 0) { //Card 10
            for (int i = 0; i < values.length; i += 2) {
                currentPlayer.exchangeStudentsDiningRoomEntrance(Color.values()[values[i]], values[i+1]);
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
