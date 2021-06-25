package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.controller.modelChangeHandlers.*;
import it.polimi.ingsw.events.ClientEvents.*;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEventWithNetworkData;
import it.polimi.ingsw.events.ControllerEvents.QuitGameEvent;
import it.polimi.ingsw.events.ControllerEvents.StartMatchEvent;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.singlePlayer.SinglePlayerMatchState;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import it.polimi.ingsw.Server.RequestsElaborator;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class responsible for handling all the data unrelated to any game
 *
 * note: handler for events don't follow the same naming scheme of other methods (in particular they have the firs letter in upper case) because they have to, in order to be automatically registered for handling the right event
 */
public class PreGameController {
    private ArrayList<Lobby> lobbies;
    private HashMap<String, RequestsElaborator> networkData;

    /**
     * Constructor for the class
     * @param subject The object responsible for registering and notify the arrival of events uncorrelated to any match
     */
    public PreGameController(PropertyChangeSubject subject){
        subject.addPropertyChangeListener(NewPlayerEventWithNetworkData.class.getSimpleName(), this::NewPlayerEventHandler);
        subject.addPropertyChangeListener(StartMatchEvent.class.getSimpleName(), this::StartMatchEventHandler);
        subject.addPropertyChangeListener(QuitGameEvent.class.getSimpleName(), this::QuitGameEventHandler);
        this.lobbies = new ArrayList<>();
        this.networkData = new HashMap<>();
    }

    /**
     * Handler for NewPlayerEvent
     */
    public synchronized void NewPlayerEventHandler(PropertyChangeEvent evt){
        NewPlayerEventWithNetworkData event = (NewPlayerEventWithNetworkData) evt.getNewValue();
        System.out.println("Handling NewPlayerEvent");

        if(event.getPlayerId().equals("*")){
            event.getRequestsElaborator().getClientHandlerSender().sendObject(new UsernameError(event.getPlayerId(),
                    "Username cannot be \"*\""));
            return;
        }

        if(networkData.containsKey(event.getPlayerId())){
            System.out.println("Username already taken");
            event.getRequestsElaborator().getClientHandlerSender().sendObject(new UsernameError(event.getPlayerId(),
                    "Username already taken"));
            return;
        }
        System.out.println("The username si free");

        if(event.getLobbyLeaderID().equals("*")){
            int lobbyIndex = searchFirstAvailableLobby();
            if(lobbyIndex==-1){
                System.out.println("Creating new Lobby");
                Lobby lobby = new Lobby(Config.getInstance().getMaxPlayers());
                lobby.addObserver(new LobbyHandler(this.networkData));
                networkData.put(event.getPlayerId(), event.getRequestsElaborator());
                lobby.setLeaderID(event.getPlayerId());
                lobbies.add(lobby);
                event.getRequestsElaborator().setOwnerUserID(event.getPlayerId());
            }
            else {
                try {
                    networkData.put(event.getPlayerId(), event.getRequestsElaborator());
                    lobbies.get(lobbyIndex).addPlayerID(event.getPlayerId());
                    event.getRequestsElaborator().setOwnerUserID(event.getPlayerId());
                } catch (IllegalOperation illegalOperation) {
                    //impossible
                    illegalOperation.printStackTrace();
                }
            }
            return;
        }

        if(event.getPlayerId().equals(event.getLobbyLeaderID())){
            System.out.println("Creating new Lobby");
            Lobby lobby = new Lobby(Config.getInstance().getMaxPlayers());
            lobby.addObserver(new LobbyHandler(this.networkData));
            networkData.put(event.getPlayerId(), event.getRequestsElaborator());
            lobby.setLeaderID(event.getPlayerId());
            lobbies.add(lobby);
            event.getRequestsElaborator().setOwnerUserID(event.getPlayerId());
            return;
        }

        int lobbyIndex = searchLobbyByLeader(event.getLobbyLeaderID());
        if(lobbyIndex == -1)
            event.getRequestsElaborator().getClientHandlerSender().sendObject(new LobbyError(event.getPlayerId(),
                    "No lobby with the given Leader"));
        else{
            try {
                networkData.put(event.getPlayerId(), event.getRequestsElaborator());
                lobbies.get(lobbyIndex).addPlayerID(event.getPlayerId());
                event.getRequestsElaborator().setOwnerUserID(event.getPlayerId());
            } catch (IllegalOperation illegalOperation) {
                event.getRequestsElaborator().getClientHandlerSender().sendObject(new LobbyError(event.getPlayerId(),
                        "The lobby can't accept any more players"));
            }
        }
    }

    /**
     * Utility method that searches for the first lobby that can still host some players
     * @return The index of the first lobby that can still host some players, or -1 if all the lobbies are full
     */
    private int searchFirstAvailableLobby() {
        for(int i = 0; i< lobbies.size(); i++) {
            Lobby l = lobbies.get(i);
            if (!l.isFull() && l.canAcceptPlayers())
                return i;
        }
        return -1;
    }

    /**
     * Utility method that searches the lobby from it's leader ID
     * @param lobbyLeaderID The ID of the leader of the lobby
     * @return The index of the searched lobby, or -1 if there's no lobby with the given leader
     */
    private int searchLobbyByLeader(String lobbyLeaderID) {
        for(int i = 0; i< lobbies.size(); i++)
            if(lobbies.get(i).getLeaderID().equals(lobbyLeaderID))
                return i;

        return -1;
    }

    /**
     * Utility method that searches the lobby from one of it's player IDs
     * @param playerID The ID of one of the player in the lobby
     * @return The index of the searched lobby, or -1 if there's no lobby containing the given player
     */
    private int searchLobby(String playerID) {
        for(int i = 0; i< lobbies.size(); i++)
            if(lobbies.get(i).getLeaderID().equals(playerID) || lobbies.get(i).getOtherPLayersID().contains(playerID))
                return i;

        return -1;
    }

    /**
     * Handler for StartMatchEvent
     */
    public synchronized void StartMatchEventHandler(PropertyChangeEvent evt){
        StartMatchEvent event = (StartMatchEvent) evt.getNewValue();
        RequestsElaborator re = networkData.getOrDefault(event.getPlayerId(), null);

        int lobbyIndex = searchLobbyByLeader(event.getPlayerId());

        if(lobbyIndex==-1 && re!=null){
            re.getClientHandlerSender().sendObject(new BadRequestEvent(event.getPlayerId(),
                    "The player is not a leader of any lobby", event));
            return;
        }

        Lobby lobby = lobbies.get(lobbyIndex);
        if(lobby.getOtherPLayersID().size()<=0 && re!=null) {
            re.getClientHandlerSender().sendObject(new BadRequestEvent(event.getPlayerId(),
                    "Not enough player to start", event));
            return;
        }

        lobby.setCanAcceptPlayers(false);
        prepareMatch(lobby);
    }

    /**
     * Prepares the match
     * @param lobby The lobby of players that will take part in the match
     */
    private void prepareMatch(Lobby lobby) {
        //Decide player Order
        ArrayList<String> playerOrder= new ArrayList<>();
        playerOrder.add(lobby.getLeaderID());
        playerOrder.addAll(lobby.getOtherPLayersID());
        Collections.shuffle(playerOrder);

        //Get the involved players networkData
        HashMap<String, Sender> involvedClientHandlerSenders = new HashMap<>();
        EventRegistry matchEventHandlerRegistry = new EventRegistry();
        GameStartingEvent gameStartingEvent = new GameStartingEvent(lobby.getLeaderID(), playerOrder);

        for (String playerID: playerOrder) {
            Sender playerSender = networkData.get(playerID).getClientHandlerSender();
            involvedClientHandlerSenders.put(playerID, playerSender);
            playerSender.sendObject(gameStartingEvent);
            RequestsElaborator requestsElaborator = networkData.get(playerID);
            requestsElaborator.setMatchEventHandlerRegistry(matchEventHandlerRegistry);
        }

        setupMatch(playerOrder, involvedClientHandlerSenders, matchEventHandlerRegistry);
    }

    /**
     * Creates a match with the given data, and sends to all the involved players the choices they must make before the match starts
     * @param playerOrder A list containing all the involved players IDs in turn order
     * @param involvedPlayersSenders An HashMap containing the Senders for each players
     * @param matchEventHandlerRegistry The event registry responsible for all the events for the match that it's being created
     */
    public static void setupMatch(ArrayList<String> playerOrder, HashMap<String, Sender> involvedPlayersSenders, EventRegistry matchEventHandlerRegistry) {
        //Initialize faith track
        FaithTrack faithTrack = FaithTrack.initFaithTrack(Config.getInstance().getFaithTrack());

        //initialize the dashboards and each player
        //ArrayList<DashBoard> dashBoards = new ArrayList<>();
        ArrayList<Player> players= new ArrayList<>();
        PlayerHandler playerHandler = new PlayerHandler(involvedPlayersSenders);
        for (int i = 0, playerOrderSize = playerOrder.size(); i < playerOrderSize; i++) {
            String s = playerOrder.get(i);
            DashBoard dashBoard = new DashBoard(Config.getInstance().getNumberOfCardSlots(),
                    Config.getInstance().getDepotCapacities(), Config.getInstance().getPersonalPowers().get(i));
            Player player = new Player(s, dashBoard);
            dashBoard.addObserver(new DashBoardHandler(involvedPlayersSenders, player));
            player.addObserver(playerHandler);
            //dashBoards.add(dashBoard);
            players.add(player);
        }

        //Initialize match state
        ArrayList<DevCard> devCards = Config.getInstance().getDevCards();
        Collections.shuffle(devCards);
        MatchState matchState;
        if(playerOrder.size() == 1){
            ArrayList<SoloActionToken> soloActionTokens = Config.getInstance().getSoloActionTokens();
            Collections.shuffle(soloActionTokens);
            matchState = new SinglePlayerMatchState(players.get(0), devCards, Config.getInstance().getMarketRows(),
                    Config.getInstance().getMarketColumns(), Config.getInstance().getMarbles(), soloActionTokens);
        }
        else {
            matchState = new MatchState(players, devCards, Config.getInstance().getMarketRows(),
                    Config.getInstance().getMarketColumns(), Config.getInstance().getMarbles());
        }
        matchState.getMarket().addObserver(new MarketHandler(involvedPlayersSenders));
        matchState.getDevCardGrid().addObserver(new DevCardGridHandler(involvedPlayersSenders));
        FaithTrackDataHandler faithTrackDataHandler = new FaithTrackDataHandler(involvedPlayersSenders, matchState);
        matchState.getPlayers().forEach(p -> p.getDashBoard().getFaithTrackData().addObserver(faithTrackDataHandler));
        matchState.addObserver(new MatchStateHandler(involvedPlayersSenders));
        if(playerOrder.size() == 1) matchState.addObserver(new LorenzoPositionHandler(involvedPlayersSenders));

        //Initialize the controller
        Controller matchController = new Controller(matchEventHandlerRegistry, matchState, involvedPlayersSenders);

        //Initialize the leader cards
        ArrayList<String> leaderCardsIDs = new ArrayList<>();
        for(int i=1; i<=Config.getInstance().getLeaderCardNumber(); i++)
            leaderCardsIDs.add("leaderCard" + i);
        Collections.shuffle(leaderCardsIDs);

        FaithTrackManager faithTrackManager = new FaithTrackManager(matchState);

        //Notify the clients on the initial state of the game
        matchState.getMarket().notifyObservers();
        matchState.getDevCardGrid().notifyObservers();
        for (int i = 0; i< playerOrder.size(); i++){
            Player p = matchState.getPlayers().get(i);
            p.getDashBoard().notifyObservers();
            p.notifyObservers();
            for(Sender c: involvedPlayersSenders.values())
                c.sendObject(new PersonalProductionPowerStateEvent(p.getPlayerId(), p.getDashBoard().getPersonalPower()));

            faithTrackManager.incrementFaithTrackPosition(players.get(i), Config.getInstance().getFaithPointHandicap().get(i));
        }

        //Send to the players what they need to chose
        HashMap<String, InitialChoicesEvent> initialChoices = new HashMap<>();
        matchController.setInitialChoices(initialChoices);
        for (int i = 0; i< playerOrder.size(); i++){
            ArrayList<String> cardsToChoseFrom = new ArrayList<>();
            for(int j = 0; j<Config.getInstance().getLeaderCardPerPlayerToChooseFrom(); j++) {
                cardsToChoseFrom.add(leaderCardsIDs.get(leaderCardsIDs.size()-1));
                leaderCardsIDs.remove(leaderCardsIDs.size()-1);
            }
            InitialChoicesEvent initialChoicesEvent = new InitialChoicesEvent(playerOrder.get(i), cardsToChoseFrom,
                    Config.getInstance().getLeaderCardPerPlayerToChoose(), Config.getInstance().getResourcesHandicap().get(i));
            initialChoices.put(playerOrder.get(i), initialChoicesEvent);
            involvedPlayersSenders.get(playerOrder.get(i)).sendObject(initialChoicesEvent);
        }

        System.out.println("All initial choices sent");
    }

    /**
     * Handler for QuitGameEvent
     */
    public synchronized void QuitGameEventHandler(PropertyChangeEvent evt){
        QuitGameEvent event = (QuitGameEvent) evt.getNewValue();

        RequestsElaborator requestsElaborator = networkData.get(event.getPlayerId());
        if(requestsElaborator==null) return;
        networkData.remove(event.getPlayerId());

        Lobby lobby = lobbies.get(searchLobby(event.getPlayerId()));
        removeLobbyIfEmpty(lobby);

        EventRegistry matchEventRegistry = requestsElaborator.getMatchEventHandlerRegistry();
        if(matchEventRegistry==null){
            if(lobby.removePlayer(event.getPlayerId()) == 0)
                lobbies.remove(lobby);
        }
        else
            matchEventRegistry.sendEvent(event);

        requestsElaborator.closeConnection();
    }

    /**
     * Methods that checks if a lobby is empty, and if so removes it from the list
     * @param lobby The lobby to check
     */
    private void removeLobbyIfEmpty(Lobby lobby) {
        boolean allAFK = true;
        if(networkData.containsKey(lobby.getLeaderID()))
            allAFK = false;
        else {
            for (String playerID : lobby.getOtherPLayersID()) {
                if (networkData.containsKey(playerID)) {
                    allAFK = false;
                    break;
                }
            }
        }

        if(allAFK)
            lobbies.remove(lobby);
    }

    /*public static void main(String[] args) {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        ArrayList<CellWithEffect> cellsWithEffectArray = new ArrayList<>();
        try {
            String cellsEffectJSON = Files.readString(Paths.get("src\\main\\resources\\CellsWithEffectArray.json"));
            cellsEffectJSON = cellsEffectJSON.substring(1,cellsEffectJSON.length()-1);
            String[] cells = cellsEffectJSON.split("(,)(?=\\{)");

            for (String s : cells) {
                CellWithEffect cell = (CellWithEffect)gson.fromJson(s, AbstractCell.class);
                cellsWithEffectArray.add(cell);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> victoryPoints = new ArrayList<>();
        try {
            String victoryPointsJSON = Files.readString(Paths.get("src\\main\\resources\\VictoryPoints.json"));
            Type integerList = new TypeToken<ArrayList<Integer>>(){}.getType();
            victoryPoints= gson.fromJson(victoryPointsJSON, integerList);
        } catch (IOException e) {
            e.printStackTrace(); //use default configuration
        }
        FaithTrack faithTrack = FaithTrack.initFaithTrack(victoryPoints.size(), cellsWithEffectArray, victoryPoints);

        StringBuilder s= new StringBuilder();
        s.append("["+ gson.toJson(FaithTrack.getArrayOfCells().get(0),AbstractCell.class));
        for(int i=1; i< FaithTrack.getArrayOfCells().size(); i++){
            s.append(","+ gson.toJson(FaithTrack.getArrayOfCells().get(i),AbstractCell.class));
        }
        s.append("]");
        System.out.println(s.toString());
        String path = "src\\main\\resources\\CompleteFaithTrack.json";
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            fw.write(s.toString());
            fw.flush();
            fw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public static void main(String[] args) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();

        ArrayList<AbstractCell> arrayOfCells = new ArrayList<>();
        try {
            String faithTrackJSON = Files.readString(Paths.get("src\\main\\resources\\CompleteFaithTrack.json"));
            arrayOfCells = gson.fromJson(faithTrackJSON, new TypeToken<ArrayList<AbstractCell>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace(); //use default configuration
        }

        for(AbstractCell c: arrayOfCells){
            System.out.println(c.getIndex() + " " + c.getVictoryPoints());
        }
    }*/
}
