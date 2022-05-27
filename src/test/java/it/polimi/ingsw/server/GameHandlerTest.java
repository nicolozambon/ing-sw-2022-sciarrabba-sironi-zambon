package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ThinModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameHandlerTest {
    @Test
    void gsonTest() {
        Gson gson = new Gson();
        List<String> names = new ArrayList<>();
        names.add("Pluto");
        names.add("Pippo");
        Model model = new ModelBuilder().buildModel(names, false);

        ThinModel ms1 = new ThinModel(model);
        //System.out.println(ms1);

        String s = gson.toJson(ms1);

        ThinModel ms2 = gson.fromJson(s, ThinModel.class);


        assertNotEquals(ms1, ms2);
        assertEquals(ms1.toString(), ms2.toString());

    }

}
