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

    public static void main(String[] args) {
        int vp = 3;

        HashMap<Resource, Integer> resourcesRequirement = new HashMap<>();
        resourcesRequirement.put(Resource.SHIELD, 5);
        ResourcesRequirement r = new ResourcesRequirement(resourcesRequirement);
        ArrayList<Requirement> requirement = new ArrayList<>();
        requirement.add(r);

        HashMap<Resource, Integer> maxResources= new HashMap<>();
        maxResources.put(Resource.COIN, 2);
        DepositLeaderPower p = new DepositLeaderPower(maxResources);
        ArrayList<LeaderPower> power = new ArrayList<>();
        power.add(p);

        LeaderCard lc = new LeaderCard(vp, requirement, power);

        System.out.println(((DepositLeaderPower)lc.getPower().get(0)).getMaxResources());

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        String JSONLeaderCard = gson.toJson(lc);
        System.out.println(JSONLeaderCard);
        System.out.println(gson.toJson(power));
        System.out.println(gson.toJson(lc.getPower()));
        System.out.println(gson.toJson(requirement));
        System.out.println(gson.toJson(lc.getActivationRequirement()));
        System.out.println(gson.toJson(lc));

        LeaderCard lc2 = gson.fromJson(JSONLeaderCard, LeaderCard.class);

        System.out.println(((DepositLeaderPower)lc2.getPower().get(0)).getMaxResources());

    }
}
