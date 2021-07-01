package it.polimi.ingsw.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.FaithTrack.AbstractCell;
import it.polimi.ingsw.model.LeaderCards.*;
import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GsonInheritanceAdapterTest {

    @Test
    public void testSerialize() {
        int vp = 3;

        HashMap<Resource, Integer> resourcesRequirement = new HashMap<>();
        resourcesRequirement.put(Resource.SHIELD, 5);
        ResourcesRequirement r = new ResourcesRequirement(resourcesRequirement);
        ArrayList<Requirement> requirement = new ArrayList<>();
        requirement.add(r);

        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.COIN, 2);
        DepositLeaderPower p = new DepositLeaderPower(maxResources);
        ArrayList<LeaderPower> power = new ArrayList<>();
        power.add(p);

        LeaderCard lc = new LeaderCard("Test", vp, requirement, power);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        String JSONLeaderCard = gson.toJson(lc);
        String expected = "{\"cardID\":\"Test\",\"victoryPoints\":3,\"activationRequirements\":[{\"CLASSNAME\":" +
                "\"it.polimi.ingsw.model.LeaderCards.ResourcesRequirement\",\"INSTANCE\":{\"resources\":{\"SHIELD\":5}}}]," +
                "\"powers\":[{\"KEY_CLASSNAME\":\"it.polimi.ingsw.model.LeaderCards.DepositLeaderPower\"," +
                "\"KEY_INSTANCE\":{\"maxResources\":{\"COIN\":2},\"currentResources\":{\"COIN\":0},\"observers\":[]}," +
                "\"VALUE_CLASSNAME\":\"java.lang.Boolean\",\"VALUE_INSTANCE\":false}],\"observers\":[]}";

        assertEquals(expected, JSONLeaderCard);
    }

    @Test
    public void testDeserialize() {
        int vp = 3;

        HashMap<Resource, Integer> resourcesRequirement = new HashMap<>();
        resourcesRequirement.put(Resource.SHIELD, 5);
        ResourcesRequirement r = new ResourcesRequirement(resourcesRequirement);
        ArrayList<Requirement> requirement = new ArrayList<>();
        requirement.add(r);

        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.COIN, 2);
        DepositLeaderPower p = new DepositLeaderPower(maxResources);
        ArrayList<LeaderPower> power = new ArrayList<>();
        power.add(p);

        LeaderCard lc = new LeaderCard("Test", vp, requirement, power);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        String JSONLeaderCard = gson.toJson(lc);

        LeaderCard lc2 = gson.fromJson(JSONLeaderCard, LeaderCard.class);

        assertEquals(JSONLeaderCard, gson.toJson(lc2));
    }

    public static void main(String[] args) throws IOException {

//        AbstractCell last= new PopeCell(24,20, new PopeFavorCard(4), 6);
//        AbstractCell cell= new LastCell(last);
//        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
//        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
//        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
//        Gson gson = builder.create();
//        String json = gson.toJson(cell);
//        System.out.println(json);


        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        try {
            String cellsEffectJSON = Files.readString(Paths.get("src/main/resources/CompleteFaithTrack.json"));
            cellsEffectJSON = cellsEffectJSON.substring(1, cellsEffectJSON.length() - 1);
            String[] cells = cellsEffectJSON.split("(,)(?=\\{)");

            int n = 0;
            for (String s : cells) {
                AbstractCell cell = gson.fromJson(s, AbstractCell.class);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
