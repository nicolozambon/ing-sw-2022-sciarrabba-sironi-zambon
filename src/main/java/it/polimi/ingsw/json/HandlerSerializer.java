package it.polimi.ingsw.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.model.Handler;
import it.polimi.ingsw.model.InfluenceHandler;
import it.polimi.ingsw.model.MotherNatureHandler;
import it.polimi.ingsw.model.MovementHandler;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * Custom Json serializer to correctly manage inheritance of Handler and subclasses
 */
public class HandlerSerializer implements JsonSerializer<Handler> {

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
     *
     * @param handler Handler to be serialized
     * @param type the type of the object to be serialized
     * @param jsonSerializationContext the context of JSON serializer
     * @return
     */
    @Override
    public JsonElement serialize(Handler handler, Type type, JsonSerializationContext jsonSerializationContext) {
        if (handler == null)
            return null;
        else {
                Class c = map.get(handler.getCategory());
                if (c == null) throw new RuntimeException("Unknown class: " + handler.getCategory());
                if (c != map.get("default")) return jsonSerializationContext.serialize(handler, c);
                else {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("category", handler.getCategory());
                    return jsonObject;
                }
        }
    }
}
