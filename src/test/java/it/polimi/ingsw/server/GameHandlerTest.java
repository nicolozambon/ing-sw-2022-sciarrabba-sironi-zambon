package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ModelSerializable;
import it.polimi.ingsw.server.GameHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameHandlerTest {
    @Test
    void gsonTest() {
        Gson gson = new Gson();
        List<String> names = new ArrayList<>();
        names.add("Pluto");
        names.add("Pippo");
        Model model = new ModelBuilder().buildModel(names);

        ModelSerializable ms1 = new ModelSerializable(model);
        //System.out.println(ms1);

        String s = gson.toJson(ms1);

        ModelSerializable ms2 = gson.fromJson(s, ModelSerializable.class);


        assertNotEquals(ms1, ms2);
        assertEquals(ms1.toString(), ms2.toString());

    }

}
