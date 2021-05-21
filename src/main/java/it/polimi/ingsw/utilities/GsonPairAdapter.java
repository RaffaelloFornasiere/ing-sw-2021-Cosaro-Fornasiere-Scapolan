package it.polimi.ingsw.utilities;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonPairAdapter implements JsonSerializer<Pair>, JsonDeserializer<Pair> {
    private static final String KEY_CLASSNAME = "KEY_CLASSNAME";
    private static final String KEY_INSTANCE = "KEY_INSTANCE";
    private static final String VALUE_CLASSNAME = "VALUE_CLASSNAME";
    private static final String VALUE_INSTANCE = "VALUE_INSTANCE";

    @Override
    public JsonElement serialize(Pair kvPair, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject pair = new JsonObject();
        pair.addProperty(KEY_CLASSNAME, kvPair.getKey().getClass().getName());
        pair.add(KEY_INSTANCE, jsonSerializationContext.serialize(kvPair.getKey()));
        pair.addProperty(VALUE_CLASSNAME, kvPair.getValue().getClass().getName());
        pair.add(VALUE_INSTANCE, jsonSerializationContext.serialize(kvPair.getValue()));
        return pair;
    }

    @Override
    public Pair deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(KEY_CLASSNAME);
        String className = prim.getAsString();

        Class<?> keyKlass = null;
        try {
            keyKlass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }

        prim = (JsonPrimitive) jsonObject.get(VALUE_CLASSNAME);
        className = prim.getAsString();

        Class<?> valueKlass = null;
        try {
            valueKlass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }

        return new Pair<>(jsonDeserializationContext.deserialize(jsonObject.get(KEY_INSTANCE), keyKlass), jsonDeserializationContext.deserialize(jsonObject.get(VALUE_INSTANCE), valueKlass) );
    }
}
