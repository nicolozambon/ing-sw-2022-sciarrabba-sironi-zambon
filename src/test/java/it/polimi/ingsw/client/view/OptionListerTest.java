package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.view.cli.OptionLister;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class OptionListerTest {

    @Test
    void stringify() {
        OptionLister optionLister = new OptionLister();
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
        
        System.out.println(optionLister.list(options));
    }
}