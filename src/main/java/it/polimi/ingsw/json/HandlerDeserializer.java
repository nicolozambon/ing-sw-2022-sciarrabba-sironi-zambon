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
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Custom Json deserializer to correctly manage inheritance of Handler and subclasses
 */
public class HandlerDeserializer implements JsonDeserializer<Handler> {

    /**
     * Map Handler and subclasses category attribute to the correct class
     */
    private static final Map<String, Class> map = new TreeMap<>();

    static {
        map.put("default", Handler.class);
        map.put("mother_nature", MotherNatureHandler.class);
        map.put("influence", InfluenceHandler.class);
        map.put("movement", MovementHandler.class);
    }

    /**
     * Custom deserialize method following Gson specification
     * @param jsonElement JsonElement to be deserialized
     * @param type the type of the object to be deserialized
     * @param jsonDeserializationContext the context of JSON deserialize
     * @return Handler deserialized from the json
     * @throws JsonParseException if there is error in parsing the json
     */
    @Override
    public Handler deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String type1 = jsonElement.getAsJsonObject().get("category").getAsString();
        Class c = map.get(type1);
        if (c == null) throw new RuntimeException("Unknown class: " + type);
        if (c != map.get("default")) return jsonDeserializationContext.deserialize(jsonElement, c);
        else {
            return new Handler(new ArrayList<>());
        }
    }
}

