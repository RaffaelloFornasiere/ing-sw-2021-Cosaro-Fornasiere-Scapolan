package it.polimi.ingsw.utilities;

import com.google.gson.*;
import it.polimi.ingsw.model.LeaderCards.*; //needed for main test
import it.polimi.ingsw.model.Resource; //needed for main test

import java.lang.reflect.Type;
import java.util.ArrayList; //needed for main test
import java.util.HashMap; //needed for main test

public class GsonInheritanceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> klass = null;
        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
        return jsonDeserializationContext.deserialize(jsonObject.get(INSTANCE), klass);
    }

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject retValue = new JsonObject();
        String className = t.getClass().getName();
        retValue.addProperty(CLASSNAME, className);
        JsonElement elem = jsonSerializationContext.serialize(t);
        retValue.add(INSTANCE, elem);
        return retValue;
    }
}
