package it.polimi.ingsw;

import it.polimi.ingsw.enums.ActionEnum;
import org.junit.jupiter.api.Test;

public class test {
    @Test
    void prova() {
        String string = "move_student_to_island";
        ActionEnum ae = ActionEnum.valueOf(string.toUpperCase());

        System.out.println(ae);
    }
}
