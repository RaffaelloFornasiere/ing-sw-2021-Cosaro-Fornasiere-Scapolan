package it.polimi.ingsw.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GsonPairAdapterTest {

    @Test
    public void serialize() {
        Pair<String, Integer> testPair = new Pair<>("Hi!", 42);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = gsonBuilder.create();

        String pairJSON = gson.toJson(testPair);
        assertEquals("{\"KEY_CLASSNAME\":\"java.lang.String\",\"KEY_INSTANCE\":\"Hi!\"," +
                "\"VALUE_CLASSNAME\":\"java.lang.Integer\",\"VALUE_INSTANCE\":42}", pairJSON);
    }

    @Test
    public void deserialize() {
        String pairJSON = "{\"KEY_CLASSNAME\":\"java.lang.String\",\"KEY_INSTANCE\":\"Hi!\"," +
                "\"VALUE_CLASSNAME\":\"java.lang.Integer\",\"VALUE_INSTANCE\":42}";

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = gsonBuilder.create();

        Pair<String, Integer> testPair = gson.fromJson(pairJSON, Pair.class);

        assertEquals("Hi!", testPair.getKey());
        assertEquals(42, (int) testPair.getValue());
    }
}