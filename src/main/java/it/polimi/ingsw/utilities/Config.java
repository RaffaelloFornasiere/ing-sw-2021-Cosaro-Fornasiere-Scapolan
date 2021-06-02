package it.polimi.ingsw.utilities;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.AbstractCell;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("FieldCanBeLocal")
public class Config {
    private static transient Config instance = null;

    private transient ArrayList<AbstractCell> faithTrack;

    private int maxPlayers;
    private int devCardNumber;
    private int devCardsToWin;
    private transient ArrayList<DevCard> devCards;
    private int leaderCardNumber;
    private int leaderCardPerPlayerToChooseFrom;
    private int leaderCardPerPlayerToChoose;
    private transient boolean leaderCardDefault;
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

    private transient static final int maxPlayersDefault = 4;
    private transient static final int devCardNumberDefault = 48;
    private transient static final int devCardsToWinDefault = 7;
    private transient static final int leaderCardNumberDefault = 16;
    private transient static final int leaderCardPerPlayerToChooseFromDefault = 4;
    private transient static final int leaderCardPerPlayerToChooseDefault = 2;
    private transient static final boolean leaderCardDefaultDefault = true;
    private transient static final int numberOfCardSlotsDefault = 3;
    private transient static final int faithPointPerDiscardedResourceDefault = 1;
    private transient static final int resourcesPerVictoryPointDefault = 5;
    private transient static final ArrayList<Integer> depotCapacitiesDefault = new ArrayList<>(){{
        add(1);
        add(2);
        add(3);
    }};

    private transient static final int marketRowsDefault = 4;
    private transient static final int marketColumnsDefault = 3;
    private transient static final HashMap<Marble, Integer> marblesDefault = new HashMap<>(){{
        put(Marble.GRAY, 2);
        put(Marble.YELLOW, 2);
        put(Marble.PURPLE, 2);
        put(Marble.BLUE, 2);
        put(Marble.WHITE, 4);
        put(Marble.RED, 1);
    }};

    private transient static final ArrayList<ProductionPower> personalPowersDefault
            = new ArrayList<>(){{
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
        add(new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1 ,0));
    }};

    private transient static final ArrayList<Integer> faithPointHandicapDefault = new ArrayList<>(){{
        add(0);
        add(0);
        add(1);
        add(1);
    }};

    private transient static final ArrayList<Integer> resourcesHandicapDefault = new ArrayList<>(){{
        add(0);
        add(1);
        add(1);
        add(2);
    }};

    private Config(){
        loadDefault();
    }

    private static Config loadFromJSON(){
        Gson gson = new Gson();
        try {
            String configJSON = Files.readString(Paths.get("src\\main\\resources\\Config.json"));
            Config config = gson.fromJson(configJSON, Config.class);

            config.faithTrack = loadFaithTrack(config);

            config.devCards = loadDevCards(config);

            if(config.devCardsToWin>config.devCardNumber){
                System.err.println("Development card needed to win more than total development card. Loading default option for it");
                config.devCardsToWin = devCardsToWinDefault;
            }

            checkLeaderCards(config);

            if(config.leaderCardPerPlayerToChooseFrom > config.leaderCardNumber/config.maxPlayers || config.leaderCardPerPlayerToChoose > config.leaderCardPerPlayerToChooseFrom){
                if(config.leaderCardPerPlayerToChooseFrom > config.leaderCardNumber/config.maxPlayers)
                    System.err.print("Not enough leader cards to choose from for each player");
                else
                    System.err.print("Leader card to choose more than leader cards to choose from");
                System.err.println(". Loading default option for the leader cards numbers and max players");
                config.leaderCardNumber = leaderCardNumberDefault;
                config.leaderCardPerPlayerToChooseFrom = leaderCardPerPlayerToChooseFromDefault;
                config.leaderCardPerPlayerToChoose = leaderCardPerPlayerToChooseDefault;
                config.maxPlayers = maxPlayersDefault;
            }

            int marbleNumber = 0;
            for(Marble m: config.marbles.keySet()) {
                marbleNumber += config.marbles.get(m);
            }
            if(marbleNumber!=(config.marketRows*config.marketColumns)+1){
                System.err.println("Number of marbles different from (rows*column)+1. Loading default option for the market");
                config.leaderCardPerPlayerToChoose = leaderCardPerPlayerToChooseDefault;
                config.marketRows = marketRowsDefault;
                config.marketColumns = marketColumnsDefault;
            }

            if(config.personalPowers.size()!=config.maxPlayers){
                System.err.println("Number of personal powers different from number of max players. Loading default option for the personal powers and max players");
                config.personalPowers = personalPowersDefault;
                config.maxPlayers = maxPlayersDefault;
            }

            if(config.faithPointHandicap.size()!=config.maxPlayers){
                System.err.println("Number of faith point handicap different from number of max players. Loading default option for the faith point handicap and max players");
                config.faithPointHandicap = faithPointHandicapDefault;
                config.maxPlayers = maxPlayersDefault;
            }

            if(config.resourcesHandicap.size()!=config.maxPlayers){
                System.err.println("Number of resources handicap different from number of max players. Loading default option for the resources handicaps and max players");
                config.resourcesHandicap = resourcesHandicapDefault;
                config.maxPlayers = maxPlayersDefault;
            }

            return config;
        } catch (Exception e) {
            System.err.println("Unable to load config from file. Loading default configuration");
            return new Config();
        }
    }

    private static ArrayList<AbstractCell> loadFaithTrack(Config config) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        ArrayList<AbstractCell> arrayOfCells;
        try {
            String faithTrackJSON = Files.readString(Paths.get("src\\main\\resources\\CompleteFaithTrack.json"));
            arrayOfCells = gson.fromJson(faithTrackJSON, new TypeToken<ArrayList<AbstractCell>>(){}.getType());
        } catch (IOException e) {
            System.err.println("Faith track corrupted. Loading default");
            return loadFaithTrackDefault();
        }
        return arrayOfCells;
    }

    private static ArrayList<AbstractCell> loadFaithTrackDefault() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        ArrayList<AbstractCell> arrayOfCells = new ArrayList<>();
        try {
            String faithTrackJSON = Files.readString(Paths.get("src\\main\\resources\\default\\CompleteFaithTrack.json"));
            arrayOfCells = gson.fromJson(faithTrackJSON, new TypeToken<ArrayList<AbstractCell>>(){}.getType());
        } catch (IOException e) {
            System.err.println("Default faith track corrupted");
            throw new IllegalArgumentException("Default faith track corrupted");
        }
        return arrayOfCells;
    }

    private static void checkLeaderCards(Config config) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        for(int i = 1; i<= config.leaderCardNumber; i++){
            try {
                String leaderCardJSON = Files.readString(Paths.get("src\\main\\resources\\LeaderCard" + i + ".json"));
                leaderCards.add(gson.fromJson(leaderCardJSON, LeaderCard.class));
            } catch (IOException e) {
                System.err.println("Leader cards corrupted. Checking defaults");
                config.leaderCardNumber = leaderCardNumberDefault;
                config.leaderCardDefault = true;
                checkLeaderCardsDefault();
            }
        }
        config.leaderCardDefault = false;
    }

    private static void checkLeaderCardsDefault() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        for(int i = 1; i<= leaderCardNumberDefault; i++){
            try {
                String leaderCardJSON = Files.readString(Paths.get("src\\main\\resources\\LeaderCard" + i + ".json"));
                leaderCards.add(gson.fromJson(leaderCardJSON, LeaderCard.class));
            } catch (IOException e) {
                System.err.println("Default leader cards corrupted");
                throw new IllegalArgumentException("Default leader cards corrupted");
            }
        }
    }

    private static ArrayList<DevCard> loadDevCards(Config config) {
        Gson gson = new Gson();
        ArrayList<DevCard> devCards = new ArrayList<>();
        for(int i = 1; i<= config.devCardNumber; i++){
            try {
                String DevCardJSON = Files.readString(Paths.get("src\\main\\resources\\DevCard" + i + ".json"));
                devCards.add(gson.fromJson(DevCardJSON, DevCard.class));
            } catch (IOException e) {
                System.err.println("Development cards corrupted. Loading defaults");
                config.devCardNumber = devCardNumberDefault;
                return loadDevCardsDefault();
            }
        }
        return devCards;
    }

    private static ArrayList<DevCard> loadDevCardsDefault() {
        Gson gson = new Gson();
        ArrayList<DevCard> devCards = new ArrayList<>();
        for(int i = 1; i<= devCardNumberDefault; i++){
            try {
                String DevCardJSON = Files.readString(Paths.get("src\\main\\resources\\default\\DevCard" + i + ".json"));
                devCards.add(gson.fromJson(DevCardJSON, DevCard.class));
            } catch (IOException e) {
                System.err.println("Default development cards corrupted");
                throw new IllegalArgumentException("Default development cards corrupted");
            }
        }
        return devCards;
    }

    public static Config getInstance(){
        if(instance == null) instance = loadFromJSON();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<AbstractCell> getFaithTrack() {
        return (ArrayList<AbstractCell>) faithTrack.clone();
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

    @SuppressWarnings("unchecked")
    public ArrayList<DevCard> getDevCards(){
        return (ArrayList<DevCard>) this.devCards.clone();
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

    public boolean isLeaderCardDefault() {
        return leaderCardDefault;
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
        faithTrack = loadFaithTrackDefault();
        maxPlayers = maxPlayersDefault;
        devCardNumber = devCardNumberDefault;
        devCardsToWin = devCardsToWinDefault;
        devCards = loadDevCardsDefault();
        leaderCardNumber = leaderCardNumberDefault;
        leaderCardPerPlayerToChooseFrom = leaderCardPerPlayerToChooseFromDefault;
        leaderCardPerPlayerToChoose = leaderCardPerPlayerToChooseDefault;
        leaderCardDefault = leaderCardDefaultDefault;
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

    public static void main(String[] args) {
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
    }
}
