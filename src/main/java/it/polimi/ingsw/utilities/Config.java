package it.polimi.ingsw.utilities;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.faithTrack.AbstractCell;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderPower;
import it.polimi.ingsw.model.leaderCards.Requirement;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.model.singlePlayer.SoloActionTokenDiscard;
import it.polimi.ingsw.model.singlePlayer.SoloActionTokenMove;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class responsible for loading custom configuration for the matches
 */
@SuppressWarnings({"FieldCanBeLocal", "UnstableApiUsage"})
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

    private ArrayList<SoloActionToken> soloActionTokens;

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

    private transient static final ArrayList<SoloActionToken> soloActionTokensDefault = new ArrayList<>(){{
        add(new SoloActionTokenDiscard(new HashMap<>(){{put(CardColor.GREEN, 2);}}));
        add(new SoloActionTokenDiscard(new HashMap<>(){{put(CardColor.VIOLET, 2);}}));
        add(new SoloActionTokenDiscard(new HashMap<>(){{put(CardColor.YELLOW, 2);}}));
        add(new SoloActionTokenDiscard(new HashMap<>(){{put(CardColor.BLUE, 2);}}));
        add(new SoloActionTokenMove(2, false));
        add(new SoloActionTokenMove(2, false));
        add(new SoloActionTokenMove(1, true));
    }};

    /**
     * Builds a default config instance
     */
    private Config(){
        loadDefault();
    }

    /**
     * Loads a config configuration from Json. If some parameters are problematic, it will load the default configuration for them
     * @return The config loaded from Json
     */
    private static Config loadFromJSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SoloActionToken.class, new GsonInheritanceAdapter<SoloActionToken>());
        Gson gson = gsonBuilder.create();
        try {
            String configJSON = Files.readString(Paths.get("src/main/resources/Config.json"));
            Config config = gson.fromJson(configJSON, Config.class);

            if(config.soloActionTokens.stream().filter(x -> x.getClass() == SoloActionTokenMove.class).map(x -> (SoloActionTokenMove) x).noneMatch(SoloActionTokenMove::reshuffle)){
                System.err.println("There must be at least one reshuffle token");
                config.soloActionTokens = soloActionTokensDefault;
            }


            config.faithTrack = loadFaithTrack();

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

    /**
     * Loads the faith track from Json. If it's badly written or it encounters any problem, it will load the default configuration instead.
     * @return The loaded faith track
     * @throws IllegalArgumentException If the default configuration presents any problem
     */
    private static ArrayList<AbstractCell> loadFaithTrack() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        ArrayList<AbstractCell> arrayOfCells;
        try {
            String faithTrackJSON = Files.readString(Paths.get("src/main/resources/CompleteFaithTrack.json"));
            arrayOfCells = gson.fromJson(faithTrackJSON, new TypeToken<ArrayList<AbstractCell>>(){}.getType());
        } catch (IOException e) {
            System.err.println("Faith track corrupted. Loading default");
            return loadFaithTrackDefault();
        }
        return arrayOfCells;
    }

    /**
     * Loads the default faith track from Json
     * @return The default faith track
     * @throws IllegalArgumentException If the default configuration presents any problem
     */
    private static ArrayList<AbstractCell> loadFaithTrackDefault() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        ArrayList<AbstractCell> arrayOfCells;
        try {

            String faithTrackJSON = Files.readString(Paths.get("src/main/resources/default/CompleteFaithTrack.json"));
            arrayOfCells = gson.fromJson(faithTrackJSON, new TypeToken<ArrayList<AbstractCell>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Default faith track corrupted");
            throw new IllegalArgumentException("Default faith track corrupted");
        }
        return arrayOfCells;
    }

    /**
     * Checks the leader cards Json to see if they can be loaded without any problem.
     * If any problem arises, it will check the default leader cards and it will set a flag
     * @param config The config instance where the flag will be set
     * @throws IllegalArgumentException If the default configuration presents any problem
     */
    private static void checkLeaderCards(Config config) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        for(int i = 1; i<= config.leaderCardNumber; i++){
            try {
                String leaderCardJSON = Files.readString(Paths.get("src/main/resources/LeaderCard" + i + ".json"));
                gson.fromJson(leaderCardJSON, LeaderCard.class);
            } catch (Exception e) {
                System.err.println("Leader cards corrupted. Checking defaults");
                config.leaderCardNumber = leaderCardNumberDefault;
                config.leaderCardDefault = true;
                checkLeaderCardsDefault();
            }
        }
        config.leaderCardDefault = false;
    }

    /**
     * Check if the default leader cards can be loaded without any problem
     * @throws IllegalArgumentException If the default configuration presents any problem
     */
    private static void checkLeaderCardsDefault() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        for(int i = 1; i<= leaderCardNumberDefault; i++){
            try {
                String leaderCardJSON = Files.readString(Paths.get("src/main/resources/LeaderCard" + i + ".json"));
                gson.fromJson(leaderCardJSON, LeaderCard.class);
            } catch (Exception e) {
                System.err.println("Default leader cards corrupted");
                throw new IllegalArgumentException("Default leader cards corrupted");
            }
        }
    }

    /**
     * Loads the development cards from Json. If any is badly written or the method encounters any problem, it will load the default configuration instead.
     * @return The loaded development cards
     * @throws IllegalArgumentException If the default configuration presents any problem
     */
    private static ArrayList<DevCard> loadDevCards(Config config) {
        Gson gson = new Gson();
        ArrayList<DevCard> devCards = new ArrayList<>();
        for(int i = 1; i<= config.devCardNumber; i++){
            try {
                String DevCardJSON = Files.readString(Paths.get("src/main/resources/DevCard" + i + ".json"));
                devCards.add(gson.fromJson(DevCardJSON, DevCard.class));
            } catch (Exception e) {
                System.err.println("Development cards corrupted. Loading defaults");
                config.devCardNumber = devCardNumberDefault;
                return loadDevCardsDefault();
            }
        }
        return devCards;
    }

    /**
     * Loads the default development cards from Json
     * @return The default development cards
     * @throws IllegalArgumentException If the default configuration presents any problem
     */
    private static ArrayList<DevCard> loadDevCardsDefault() {
        Gson gson = new Gson();
        ArrayList<DevCard> devCards = new ArrayList<>();
        for(int i = 1; i<= devCardNumberDefault; i++){
            try {
                String DevCardJSON = Files.readString(Paths.get("src/main/resources/default/DevCard" + i + ".json"));
                devCards.add(gson.fromJson(DevCardJSON, DevCard.class));
            } catch (Exception e) {
                System.err.println("Default development cards corrupted");
                throw new IllegalArgumentException("Default development cards corrupted");
            }
        }
        return devCards;
    }

    /**
     * Getter for the instance of Config. It also creates it is the first time it is invoked
     * @return The common Config instance
     */
    public static Config getInstance(){
        if(instance == null) instance = loadFromJSON();
        return instance;
    }

    /**
     * Getter for the array of abstract cells representing the faith track
     * @return The array of abstract cells representing the faith track
     */
    public ArrayList<AbstractCell> getFaithTrack() {
        return new ArrayList<>(faithTrack);
    }

    /**
     * Getter for the maximum amount of players in a match
     * @return The maximum amount of players in a match
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Getter for the number of development cards
     * @return The number of development cards
     */
    public int getDevCardNumber() {
        return devCardNumber;
    }

    /**
     * Getter for the number of development cards needed to end the match
     * @return The number of development cards needed to end the match
     */
    public int getDevCardsToWin() {
        return devCardsToWin;
    }

    /**
     * Getter for the development cards
     * @return The development cards
     */
    public ArrayList<DevCard> getDevCards(){
        return new ArrayList<>(devCards);
    }

    /**
     * Getter for the number of leader cards
     * @return The number of leader cards
     */
    public int getLeaderCardNumber() {
        return leaderCardNumber;
    }

    /**
     * Getter for the number of leader cards each player can choose from at the beginning of the game
     * @return The number of leader cards each player can choose from at the beginning of the game
     */
    public int getLeaderCardPerPlayerToChooseFrom() {
        return leaderCardPerPlayerToChooseFrom;
    }

    /**
     * Getter for the number of leader cards each player needs to choose at the beginning of the game
     * @return The number of leader cards each player needs to choose at the beginning of the game
     */
    public int getLeaderCardPerPlayerToChoose() {
        return leaderCardPerPlayerToChoose;
    }

    /**
     * Returns true if the default leader cards must be used
     * @return If the default leader cards must be used
     */
    public boolean isLeaderCardDefault() {
        return leaderCardDefault;
    }

    /**
     * Getter for the number of card slots in the dashboard
     * @return The number of card slots in the dashboard
     */
    public int getNumberOfCardSlots() {
        return numberOfCardSlots;
    }

    /**
     * Getter for the faith points every other player obtains when someone discards a resource
     * @return The faith points every other player obtains when someone discards a resource
     */
    public int getFaithPointPerDiscardedResource() {
        return faithPointPerDiscardedResource;
    }

    /**
     * Getter for the number of resources that equals an additional victory point at the end of a match
     * @return The number of resources that equals an additional victory point at the end of a match
     */
    public int getResourcesPerVictoryPoint() {
        return resourcesPerVictoryPoint;
    }

    /**
     * Getter for the capacity of each deposit of the warehouse
     * @return The capacity of each deposit of the warehouse
     */
    public ArrayList<Integer> getDepotCapacities() {
        return depotCapacities;
    }

    /**
     * Getter for the number of rows in the market
     * @return The number of rows in the market
     */
    public int getMarketRows() {
        return marketRows;
    }

    /**
     * Getter for the number of columns in the market
     * @return The number of columns in the market
     */
    public int getMarketColumns() {
        return marketColumns;
    }

    /**
     * Getter for the marbles that will go in the market
     * @return The marbles that will go in the market
     */
    public HashMap<Marble, Integer> getMarbles() {
        return new HashMap<>(marbles);
    }

    /**
     * Getter for the personal production power of each player
     * @return The personal production power of each player
     */
    public ArrayList<ProductionPower> getPersonalPowers() {
        return new ArrayList<>(personalPowers);
    }

    /**
     * Getter for the faith points that each player starts the game with (order based on turn order)
     * @return The faith points that each player starts the game with (order based on turn order)
     */
    public ArrayList<Integer> getFaithPointHandicap() {
        return new ArrayList<>(faithPointHandicap);
    }

    /**
     * Getter for the resources that each player starts the game with (order based on turn order)
     * @return The resources that each player starts the game with (order based on turn order)
     */
    public ArrayList<Integer> getResourcesHandicap() {
        return new ArrayList<>(resourcesHandicap);
    }

    /**
     * Getter for the tokens corresponding to Lorenzo il Magnifico actions in a single player game
     * @return The tokens corresponding to Lorenzo il Magnifico actions in a single player game
     */
    public ArrayList<SoloActionToken> getSoloActionTokens() {
        return new ArrayList<>(soloActionTokens);
    }

    /**
     * Loads the default configurations of the configs
     */
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

        soloActionTokens = soloActionTokensDefault;
    }

    private static Config defaultConfig = null;

    /**
     * Returns a default instance of config
     * @return A default instance of config
     */
    public static Config getDefaultConfig(){
        if(defaultConfig==null) defaultConfig = new Config();
        return defaultConfig;
    }

    /*public static void main(String[] args) {
        Config config = new Config();
        config.loadDefault();
        //Gson gson = new GsonBuilder().create();
        Gson gson = new GsonBuilder().registerTypeAdapter(SoloActionToken.class, new GsonInheritanceAdapter<SoloActionToken>()).setPrettyPrinting().create();
        String s = gson.toJson(config);

        String path = "src/main/resources/Config.json";
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
