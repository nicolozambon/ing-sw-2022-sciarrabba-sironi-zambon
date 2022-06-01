package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ThinModelTest {

    @Test
    void testToString() {
        List<String> names = new ArrayList<>();
        names.add("P1");
        names.add("P2");
        Model model = new ModelBuilder().buildModel(names, false, true);
        ThinModel thinModel = new ThinModel(model);
        System.out.println(thinModel);
    }
}