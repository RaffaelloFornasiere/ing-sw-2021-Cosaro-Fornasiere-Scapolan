package it.polimi.ingsw.utilities;

import com.google.gson.*;
import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.FaithTrack.AbstractCell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

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

    public ArrayList<AbstractCell> buildArrayFromJson(String path) throws IOException {
        GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        gsonBuilder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson= gsonBuilder.create();
        ArrayList<AbstractCell> array= new ArrayList<>();
        String string= new BufferedReader( new FileReader(path)).readLine();
        string= string.substring(1,string.length()-1);
        String cells[]= string.split("(,)(?=\\{)");

        for( int i=0 ; i<cells.length;i ++){
            AbstractCell cell=gson.fromJson(cells[i],AbstractCell.class);
            array.add(cell);
        }

        return array;
    }











}
