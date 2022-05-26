package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializationTest {

    @Test
    void serializationTest() {
        Gson gson = new Gson();
        List<String> names = new ArrayList<>();
        names.add("Pluto");
        names.add("Pippo");
        Model model = new ModelBuilder().buildModel(names);
        String string = gson.toJson(model);

        Tower tower = model.getPlayers().get(0).getSchool().getTowersBoard().getPawns().get(0);
        System.out.println(string);

        Model model1  = gson.fromJson(string, Model.class);

        Tower tower1 = model1.getPlayers().get(0).getSchool().getTowersBoard().getPawns().get(0);

        assertEquals(tower.getColor(), tower1.getColor());

    }
}
