package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.json.HandlerDeserializer;
import it.polimi.ingsw.json.HandlerSerializer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SerializationTest {

    @Test
    void serializationTest() throws Exception{
        List<String> nicknames = new ArrayList<>(List.of("player0", "player1", "player2"));
        Model model = new ModelBuilder().buildModel(nicknames, true);
        model.getController();


        model.setWizard(0, Wizard.WIZARD);
        model.setWizard(1, Wizard.PIXIE);
        model.setWizard(2, Wizard.KING);

        model.playAssistantCard(0, 1);
        model.playAssistantCard(0, 2);
        model.playAssistantCard(0, 3);

        model.playCharacterCard(0, 7);


        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Handler.class, new HandlerSerializer());
        gsonBuilder.registerTypeAdapter(Handler.class, new HandlerDeserializer());
        Gson gson = gsonBuilder.create();

        String string = gson.toJson(model);

        Model model1 = gson.fromJson(string, Model.class);

        System.out.println(model.getHandler().getCategory());
        System.out.println(model1.getHandler().getCategory());

    }
}
