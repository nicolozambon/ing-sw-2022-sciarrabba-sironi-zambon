package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.model.card.MovementCharacterCard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MovementHandler extends Handler {

    private final MovementCharacterCard card;

    protected MovementHandler(List<Player> players, MovementCharacterCard card) {
        super(players, card.getCategory());
        this.card = card;
    }

    /**
     * Card 10: exchange 1 or 2 students from the dining room and vice versa; takes a couple of Position-Color values to exchange.
     * @param currentPlayer Current player, who has invoked extraAction
     * @param model the full model of the games
     * @param values the list of values useful for the function
     */
    @Override
    protected void extraAction(Player currentPlayer, Model model, int ... values) throws InvalidActionException {
        if (card.getNumOfStudentsToReturn() > 0) { //Card 8
            Color color = Arrays.stream(Color.values()).filter(c -> c.ordinal() == values[0]).findFirst().get();
            model.returnStudentsToBag(color, card.getNumOfStudentsToReturn());
        } else if (card.getPossibleExchange() > 0) { //Card 7
            if (values.length == 2) {
                //System.out.println(values[0] + " " + Arrays.stream(Color.values()).filter(c -> c.ordinal() == values[1]).findFirst().get());
                Color color = Arrays.stream(Color.values()).filter(c -> c.ordinal() == values[1]).findFirst().get();
                model.exchangeStudentsDiningRoomEntrance(currentPlayer.getId(), values[0], color);
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

        int last = players.size() - 1;

        if (getNumStudentsDR(players.get(last), color) > getNumStudentsDR(players.get(last - 1), color)) {
            players.get(last).getSchool().setProfessor(professor, professorBoard);
        } else if (card.getExtraControl() == 1 &&
                getNumStudentsDR(players.get(last), color) == getNumStudentsDR(currentPlayer, color)) {
            currentPlayer.getSchool().setProfessor(professor, professorBoard);
        }
    }

    @Override
    public int getCardId() {
        return this.card.getId();
    }

    @Override
    public String getCategory() {
        return super.getCategory();
    }
}
