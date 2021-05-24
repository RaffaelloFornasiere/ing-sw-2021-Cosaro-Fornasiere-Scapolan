package it.polimi.ingsw.utilities;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("")
public class Config {
    private static transient Config instance = null;

    private int maxPlayers;
    private int devCardNumber;
    private int devCardsToWin;
    private int leaderCardNumber;
    private int leaderCardPerPlayerToChooseFrom;
    private int leaderCardPerPlayerToChoose;
    private int numberOfCardSlots;
    private int faithPointPerDiscardedResource;
    private int resourcesPerVictoryPoint;
    private ArrayList<Integer> depotCapacities;

    private int marketRows;
    private int marketColumns;
    private HashMap<Marble, Integer> marbles;

    private ArrayList<ProductionPower> personalPowers;
    private ArrayList<Integer> faithPointHandicap;
    private ArrayList<Integer> resourcesHandicap;

    private final int maxPlayersDefault = 4;
    private final int devCardNumberDefault = 48;
    private final int devCardsToWinDefault = 7;
    private final int leaderCardNumberDefault = 16;
    private final int leaderCardPerPlayerToChooseFromDefault = 4;
    private final int leaderCardPerPlayerToChooseDefault = 2;
    private final int numberOfCardSlotsDefault = 3;
    private final int faithPointPerDiscardedResourceDefault = 1;
    private final int resourcesPerVictoryPointDefault = 5;
    private final ArrayList<Integer> depotCapacitiesDefault = new ArrayList<>(){{
        add(1);
        add(2);
        add(3);
    }};

    private final int marketRowsDefault = 4;
    private final int marketColumnsDefault = 3;
    private final HashMap<Marble, Integer> marblesDefault = new HashMap<>(){{
        put(Marble.GRAY, 2);
        put(Marble.YELLOW, 2);
        put(Marble.PURPLE, 2);
        put(Marble.BLUE, 2);
        put(Marble.WHITE, 4);
        put(Marble.RED, 1);
    }};

    private final ArrayList<ProductionPower> personalPowersDefault
            = new ArrayList<>(){{
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
    }};

    private final ArrayList<Integer> faithPointHandicapDefault = new ArrayList<>(){{
        add(0);
        add(0);
        add(1);
        add(1);
    }};

    private final ArrayList<Integer> resourcesHandicapDefault = new ArrayList<>(){{
        add(0);
        add(1);
        add(1);
        add(2);
    }};

    private Config(boolean Default){
        if(Default){
            this.loadDefault();
            instance = this;
            return;
        }

        Gson gson = new Gson();
        String path = "src\\main\\resources\\Config.json";
        try {
            String configJSON = Files.readString(Paths.get(path));
            Config config = gson.fromJson(configJSON, Config.class);

            if(config.devCardsToWin<config.devCardNumber){
                System.err.println("Development card needed to win more than total development card. Loading default option for it");
                config.devCardsToWin = config.devCardsToWinDefault;
            }

            if(config.leaderCardPerPlayerToChooseFrom > config.leaderCardNumber/config.maxPlayers || config.leaderCardPerPlayerToChoose > config.leaderCardPerPlayerToChooseFrom){
                if(config.leaderCardPerPlayerToChooseFrom > config.leaderCardNumber/config.maxPlayers)
                    System.err.print("Not enough leader cards to choose from for each player");
                else
                    System.err.print("Leader card to choose more than leader cards to choose from");
                System.err.println(". Loading default option for the leader cards numbers and max players");
                config.leaderCardNumber = config.leaderCardNumberDefault;
                config.leaderCardPerPlayerToChooseFrom = config.leaderCardPerPlayerToChooseFromDefault;
                config.leaderCardPerPlayerToChoose = config.leaderCardPerPlayerToChooseDefault;
                config.maxPlayers = config.maxPlayersDefault;
            }

            if(config.marbles.size()!=(config.marketRows*config.marketColumns)+1){
                System.err.println("Number of marbles different from (rows*column)+1. Loading default option for the market");
                config.leaderCardPerPlayerToChoose = config.leaderCardPerPlayerToChooseDefault;
                config.marketRows = config.marketRowsDefault;
                config.marketColumns = config.marketColumnsDefault;
            }

            if(config.personalPowers.size()!=config.maxPlayers){
                System.err.println("Number of personal powers different from number of max players. Loading default option for the personal powers and max players");
                config.personalPowers = config.personalPowersDefault;
                config.maxPlayers = config.maxPlayersDefault;
            }

            if(config.faithPointHandicap.size()!=config.maxPlayers){
                System.err.println("Number of faith point handicap different from number of max players. Loading default option for the faith point handicap and max players");
                config.faithPointHandicap = config.faithPointHandicapDefault;
                config.maxPlayers = config.maxPlayersDefault;
            }

            if(config.resourcesHandicap.size()!=config.maxPlayers){
                System.err.println("Number of resources handicap different from number of max players. Loading default option for the resources handicaps and max players");
                config.resourcesHandicap = config.resourcesHandicapDefault;
                config.maxPlayers = config.maxPlayersDefault;
            }

            instance = config;
        } catch (Exception e) {
            System.err.println("Unable to load config from file. Loading default configuration");
            this.loadDefault();
            instance = this;
        }
    }

    public static Config getInstance(){
        if(instance == null) new Config(false);
        return instance;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getDevCardNumber() {
        return devCardNumber;
    }

    public int getDevCardsToWin() {
        return devCardsToWin;
    }

    public int getLeaderCardNumber() {
        return leaderCardNumber;
    }

    public int getLeaderCardPerPlayerToChooseFrom() {
        return leaderCardPerPlayerToChooseFrom;
    }

    public int getLeaderCardPerPlayerToChoose() {
        return leaderCardPerPlayerToChoose;
    }

    public int getNumberOfCardSlots() {
        return numberOfCardSlots;
    }

    public int getFaithPointPerDiscardedResource() {
        return faithPointPerDiscardedResource;
    }

    public int getResourcesPerVictoryPoint() {
        return resourcesPerVictoryPoint;
    }

    public ArrayList<Integer> getDepotCapacities() {
        return depotCapacities;
    }

    public int getMarketRows() {
        return marketRows;
    }

    public int getMarketColumns() {
        return marketColumns;
    }

    @SuppressWarnings("unchecked")
    public HashMap<Marble, Integer> getMarbles() {
        return (HashMap<Marble, Integer>) marbles.clone();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ProductionPower> getPersonalPowers() {
        return (ArrayList<ProductionPower>) personalPowers.clone();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Integer> getFaithPointHandicap() {
        return (ArrayList<Integer>) faithPointHandicap.clone();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Integer> getResourcesHandicap() {
        return (ArrayList<Integer>) resourcesHandicap.clone();
    }

    private void loadDefault(){
        maxPlayers = maxPlayersDefault;
        devCardNumber = devCardNumberDefault;
        devCardsToWin = devCardsToWinDefault;
        leaderCardNumber = leaderCardNumberDefault;
        leaderCardPerPlayerToChooseFrom = leaderCardPerPlayerToChooseFromDefault;
        leaderCardPerPlayerToChoose = leaderCardPerPlayerToChooseDefault;
        numberOfCardSlots = numberOfCardSlotsDefault;
        faithPointPerDiscardedResource = faithPointPerDiscardedResourceDefault;
        resourcesPerVictoryPoint = resourcesPerVictoryPointDefault;
        depotCapacities = depotCapacitiesDefault;

        marketRows = marketRowsDefault;
        marketColumns = marketColumnsDefault;
        marbles = marblesDefault;

        personalPowers = personalPowersDefault;
        faithPointHandicap = faithPointHandicapDefault;
        resourcesHandicap =  resourcesHandicapDefault;
    }

    /*public static void main(String[] args) {
        Config config = new Config();
        config.loadDefault();
        //Gson gson = new GsonBuilder().create();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(config);

        String path = "src\\main\\resources\\Config.json";
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            fw.write(s);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(s);

        Config c = gson.fromJson(s, Config.class);

        assert gson.toJson(c).equals(s);
    }*/
}
