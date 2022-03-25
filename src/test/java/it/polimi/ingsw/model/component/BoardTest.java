package it.polimi.ingsw.model.component;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;


public class BoardTest<T> {

    private T pawn;
    private Board<T> dest;
    @Before
    public void initialization(T pawn){
        this.pawn = pawn;
        dest = new Board<>();
    }

    @Test
    @DisplayName("Correctly moved pawns")
    public void checkPawnMovement(Board<T> src) {
        dest.moveInPawn(pawn, src);
        assertTrue(dest.getPawns().contains(pawn), "Destination get pawn");
        assertFalse(src.getPawns().contains(pawn), "Source removed pawn");
    }
}
