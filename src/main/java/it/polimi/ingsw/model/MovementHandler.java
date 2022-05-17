package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

//TODO remain card 2 and card 10
public class MovementHandler extends Handler {

    private final MovementCharacterCard card;

    protected MovementHandler(List<Player> players, MovementCharacterCard card) {
        super(players);
        this.card = card;
    }

    /**
     * Card 10: exchange 1 or 2 students from the dining room and viceversa; takes a couple of Position-Color values to exchange.
     * @param currentPlayer
     * @param model
     * @param values
     */
    @Override
    protected void extraAction(Player currentPlayer, Model model, int ... values) {
        if (card.getNumOfStudentsToReturn() > 0) { //Card 12
            model.returnStudentsToBag(Color.values()[values[0]], card.getNumOfStudentsToReturn());
        } else if (card.getPossibleExchange() > 0) { //Card 7
            for (int i = 0; i < values.length; i += 2) {
                int finalI = i;
                System.out.println(values[i] + " " + Arrays.stream(Color.values()).filter(c -> c.ordinal() == values[finalI +1]).findFirst().get());
                model.exchangeStudentsDiningRoomEntrance(currentPlayer.getId(), values[i], Arrays.stream(Color.values()).filter(c -> c.ordinal() == values[finalI +1]).findFirst().get());

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

    @Override
    public int getCardId() {
        return this.card.getId();
    }
}
