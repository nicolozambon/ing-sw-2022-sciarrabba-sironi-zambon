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

        List<Player> players =
                this.players
                        .stream()
                        .sorted(Comparator.comparingInt(x -> getNumStudentsDR(x, color)))
                        .toList();

        int size = players.size() - 1;

        if (getNumStudentsDR(players.get(size), color) > getNumStudentsDR(players.get(size - 1), color)) {
            players.get(size).getSchool().setProfessor(professor, professorBoard);
        } else if (card.getExtraControl() == 1 &&
                getNumStudentsDR(players.get(size), color) == getNumStudentsDR(currentPlayer, color)) {
            currentPlayer.getSchool().setProfessor(professor, professorBoard);
        }
    }

}
