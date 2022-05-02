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

        List<String> options = new ArrayList<>(){{
            this.add("move_student_dining");
            this.add("move_student_island");
            this.add("move_mothernature");
            this.add("character_card");
            this.add("students_cloud");
            this.add("extra_action");
            this.add("end_action");
        }};

        System.out.println(optionStringifier.stringify(options));
    }
}