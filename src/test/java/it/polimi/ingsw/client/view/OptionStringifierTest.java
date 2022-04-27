package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.view.OptionStringifier;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class OptionStringifierTest {

    @Test
    void stringify() {
        OptionStringifier optionStringifier = new OptionStringifier();
        System.out.println(optionStringifier.stringify());

        Map<String, Integer> callableMethod = new HashMap<>(){{
            this.put("move_student_dining", 4);
            this.put("move_student_island", 4);
            this.put("move_mothernature", 0);
            this.put("character_card", 1);
            this.put("students_cloud", 0);
            this.put("extra_action", 0);
            this.put("end_action", 0);
        }};

        System.out.println(optionStringifier.stringify(callableMethod));
    }
}