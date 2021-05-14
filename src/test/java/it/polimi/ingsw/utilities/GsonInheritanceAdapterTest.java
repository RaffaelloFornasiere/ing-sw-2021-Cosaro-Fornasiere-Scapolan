package it.polimi.ingsw.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.FaithTrack.AbstractCell;
import it.polimi.ingsw.model.FaithTrack.PopeCell;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.LeaderCards.*;
import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GsonInheritanceAdapterTest {

    @Test
    public void testSerialize(){
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

        System.out.println(((DepositLeaderPower)lc.getLeaderPowers().get(0)).getMaxResources());

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        String JSONLeaderCard = gson.toJson(lc);
        String expected = "{\"victoryPoints\":3,\"activationRequirements\":" +
                "[{\"CLASSNAME\":\"it.polimi.ingsw.model.LeaderCards.ResourcesRequirement\"," +
                "\"INSTANCE\":{\"resources\":{\"SHIELD\":5}}}]," +
                "\"powers\":[{\"power\":{\"CLASSNAME\":\"it.polimi.ingsw.model.LeaderCards.DepositLeaderPower\"," +
                "\"INSTANCE\":{\"maxResources\":{\"COIN\":2},\"currentResources\":{\"COIN\":0}}},\"selected\":false}]}";

        assertEquals(expected, JSONLeaderCard);
        LeaderCard lc2 = gson.fromJson(JSONLeaderCard, LeaderCard.class);


    }

    @Test
    public void testDeserialize(){
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

        System.out.println(((DepositLeaderPower)lc.getLeaderPowers().get(0)).getMaxResources());

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        String JSONLeaderCard = gson.toJson(lc);

        LeaderCard lc2 = gson.fromJson(JSONLeaderCard, LeaderCard.class);

        assertEquals(JSONLeaderCard, gson.toJson(lc2));
    }
    @Test
    public void testBuildArrayFromJson() {
        PopeCell cellEffect1= new PopeCell(8,2, new PopeFavorCard(2),4);
        PopeCell cellEffect2= new PopeCell(16,9, new PopeFavorCard(3),5);
        PopeCell cellEffect3= new PopeCell(24,20, new PopeFavorCard(4),6);
        ArrayList<AbstractCell> a= new ArrayList();
        a.add(cellEffect1);
        a.add(cellEffect2);
        a.add(cellEffect3);
        GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        gsonBuilder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson= gsonBuilder.create();
        assertEquals("{\"CLASSNAME\":\"it.polimi.ingsw.model.FaithTrack.PopeCell\",\"INSTANCE\":" +
                "{\"vaticanReportSection\":4,\"card\":{\"victoryPoints\":2},\"cell\":{\"CLASSNAME\":\"" +
                "it.polimi.ingsw.model.FaithTrack.Cell\",\"INSTANCE\":{\"index\":8,\"victoryPoints\":2}}," +
                "\"effectDone\":false,\"effect\":{\"CLASSNAME\":\"it.polimi.ingsw.controller.EffectOfPopeCell\"," +
                "\"INSTANCE\":{}}}}", gson.toJson(cellEffect1, AbstractCell.class));

        String fileName = "src/main/resources/FaithTrack.json";
        Path path = Paths.get(fileName);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            StringBuilder string = new StringBuilder("[");
            string.append(gson.toJson(cellEffect1, AbstractCell.class))
                    .append(",")
                    .append(gson.toJson(cellEffect2, AbstractCell.class))
                    .append(",")
                    .append(gson.toJson(cellEffect3, AbstractCell.class))
                    .append("]");
            writer.write(string.toString());



        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<AbstractCell> array=new ArrayList<>();
        try {
            array= new GsonInheritanceAdapter<>().buildArrayFromJson("src/main/resources/FaithTrack.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson3= gsonBuilder.create();

        assertEquals("{\"CLASSNAME\":\"it.polimi.ingsw.model.FaithTrack.PopeCell\",\"INSTANCE\":" +
                "{\"vaticanReportSection\":4,\"card\":{\"victoryPoints\":2},\"cell\":{\"CLASSNAME\":\"" +
                "it.polimi.ingsw.model.FaithTrack.Cell\",\"INSTANCE\":{\"index\":8,\"victoryPoints\":2}}," +
                "\"effectDone\":false,\"effect\":{\"CLASSNAME\":\"it.polimi.ingsw.controller.EffectOfPopeCell\"," +
                "\"INSTANCE\":{}}}}", gson3.toJson(cellEffect1, AbstractCell.class));
        assertEquals("it.polimi.ingsw.model.FaithTrack.PopeCell",array.get(0).getClass().getName());
        assertEquals(8, array.get(0).getIndex());
        assertEquals(2, array.get(0).getVictoryPoints());

    }
}