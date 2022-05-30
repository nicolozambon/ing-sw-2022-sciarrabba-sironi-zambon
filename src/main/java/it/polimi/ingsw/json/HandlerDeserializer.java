package it.polimi.ingsw.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.Handler;
import it.polimi.ingsw.model.InfluenceHandler;
import it.polimi.ingsw.model.MotherNatureHandler;
import it.polimi.ingsw.model.MovementHandler;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

public class HandlerDeserializer implements JsonDeserializer<Handler> {

    private static final Map<String, Class> map = new TreeMap<>();

    static {
        map.put("default", Handler.class);
        map.put("mother_nature", MotherNatureHandler.class);
        map.put("influence", InfluenceHandler.class);
        map.put("movement", MovementHandler.class);
    }

    @Override
    public Handler deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String type1 = jsonElement.getAsJsonObject().get("card").getAsJsonObject().get("category").getAsString();
        Class c = map.get(type1);
        if (c == null) throw new RuntimeException("Unknown class: " + type);
        return jsonDeserializationContext.deserialize(jsonElement, c);
    }
}

