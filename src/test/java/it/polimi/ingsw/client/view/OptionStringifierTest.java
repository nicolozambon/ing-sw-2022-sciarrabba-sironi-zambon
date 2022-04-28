package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.view.OptionStringifier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OptionStringifierTest {

    @Test
    void stringify() {
        OptionStringifier optionStringifier = new OptionStringifier();
        //System.out.println(optionStringifier.stringify());

        Map<Integer, String> options = new HashMap<>(){{
            this.put(1, "move_student_dining");
            this.put(2, "move_student_island");
            this.put(3, "move_mothernature");
            this.put(4, "character_card");
            this.put(5, "students_cloud");
            this.put(6, "extra_action");
            this.put(7, "end_action");
        }};

        System.out.println(optionStringifier.stringify(options));
    }
}