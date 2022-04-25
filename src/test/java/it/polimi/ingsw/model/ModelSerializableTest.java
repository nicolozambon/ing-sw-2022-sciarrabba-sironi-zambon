package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelSerializableTest {

    @Test
    void testToString() {
        List<String> names = new ArrayList<>();
        names.add("P1");
        names.add("P2");
        Model model = new ModelBuilder().buildModel(names);
        ModelSerializable modelSerializable = new ModelSerializable(model);
        System.out.println(modelSerializable);
    }
}