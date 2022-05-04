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
            this.add("playAssistantCard");
            this.add("moveStudentToDiningRoom");
            this.add("moveStudentToIsland");
            this.add("moveMotherNature");
            this.add("playCharacterCard");
            this.add("takeStudentsFromCloud");
            this.add("extraAction");
            this.add("endAction");
        }};
        
        System.out.println(optionStringifier.stringify(options));
    }
}