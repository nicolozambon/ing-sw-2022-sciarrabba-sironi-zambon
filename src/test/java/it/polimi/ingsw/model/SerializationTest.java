package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationTest {

    @Test
    void serializationTest() {
        Gson gson = new Gson();
        List<String> names = new ArrayList<>();
        names.add("Pluto");
        names.add("Pippo");
        Model model = new ModelBuilder().buildModel(names);
        String string = gson.toJson(model);
        VirtualView vv = new VirtualView();
        model.addAnswerListener(vv);

        Tower tower = model.getPlayers().get(0).getSchool().getTowersBoard().getPawns().get(0);
        System.out.println(string);

        Model model1  = gson.fromJson(string, Model.class);

        Tower tower1 = model1.getPlayers().get(0).getSchool().getTowersBoard().getPawns().get(0);

        assertEquals(tower.getColor(), tower1.getColor());

    }
}
