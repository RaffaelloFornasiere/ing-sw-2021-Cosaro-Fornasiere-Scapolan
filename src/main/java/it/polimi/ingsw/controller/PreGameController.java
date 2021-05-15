package it.polimi.ingsw.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.modelChangeHandlers.*;
import it.polimi.ingsw.events.ClientEvents.BadRequestEvent;
import it.polimi.ingsw.events.ClientEvents.InitialChoicesEvent;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEvent;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEventWithNetworkData;
import it.polimi.ingsw.events.ControllerEvents.StartMatchEvent;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.AbstractCell;
import it.polimi.ingsw.model.FaithTrack.CellWithEffect;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import it.polimi.ingsw.virtualview.ClientHandlerSender;
import it.polimi.ingsw.virtualview.RequestsElaborator;
import it.polimi.ingsw.virtualview.VirtualView;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PreGameController {
    private ArrayList<Lobby> fillingLobbies;
    private HashMap<String, RequestsElaborator> networkData;

    public PreGameController(PropertyChangeSubject subject){
        subject.addPropertyChangeListener(NewPlayerEventWithNetworkData.class.getSimpleName(), this::NewPlayerEventHandler);
        subject.addPropertyChangeListener(StartMatchEvent.class.getSimpleName(), this::StartMatchEventHandler);
        this.fillingLobbies = new ArrayList<>();
        this.networkData = new HashMap<>();
    }

    public void NewPlayerEventHandler(PropertyChangeEvent evt){
        NewPlayerEventWithNetworkData event = (NewPlayerEventWithNetworkData) evt.getNewValue();
        System.out.println("Handling NewPlayerEvent");

        if(networkData.containsKey(event.getPlayerId())){
            event.getRequestsElaborator().getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                    "Username already taken", new NewPlayerEvent(event.getPlayerId(), event.getLobbyLeaderID())));
            return;
        }
        System.out.println("The username si free");

        if(event.getPlayerId().equals(event.getLobbyLeaderID())){
            System.out.println("Creating new Lobby");
            Lobby lobby = new Lobby();
            lobby.addObserver(new LobbyHandler(this.networkData));
            networkData.put(event.getPlayerId(), event.getRequestsElaborator());
            lobby.setLeaderID(event.getPlayerId());
            fillingLobbies.add(lobby);
            event.getRequestsElaborator().setOwnerUserID(event.getPlayerId());
            return;
        }

        int lobbyIndex = searchLobby(event.getLobbyLeaderID());
        if(lobbyIndex == -1)
            event.getRequestsElaborator().getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                    "No lobby with the given Leader", new NewPlayerEvent(event.getPlayerId(), event.getLobbyLeaderID())));
        else{
            try {
                fillingLobbies.get(lobbyIndex).addPlayerID(event.getPlayerId());
                networkData.put(event.getPlayerId(), event.getRequestsElaborator());
                event.getRequestsElaborator().setOwnerUserID(event.getPlayerId());
            } catch (IllegalOperation illegalOperation) {
                event.getRequestsElaborator().getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                        "The lobby is full", new NewPlayerEvent(event.getPlayerId(), event.getLobbyLeaderID())));
            }
        }
    }

    private int searchLobby(String lobbyLeaderID) {
        for(int i = 0; i<fillingLobbies.size(); i++)
            if(fillingLobbies.get(i).getLeaderID().equals(lobbyLeaderID))
                return i;

        return -1;
    }

    public void StartMatchEventHandler(PropertyChangeEvent evt){
        StartMatchEvent event = (StartMatchEvent) evt.getNewValue();
        RequestsElaborator re = networkData.getOrDefault(event.getPlayerId(), null);

        for(Lobby lobby: fillingLobbies){
            if(lobby.getLeaderID().equals(event.getEventName())) {
                if(lobby.getOtherPLayersID().size()>0) {
                    prepareMatch(lobby);
                    return;
                }
                if(re!=null)
                    re.getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                            "Not enough player to start", event));
            }
        }
        if(re != null){
            re.getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                    "The player is not a leader of any lobby", event));
        }
    }

    //TODO get parameters from configuration files(tip: in case of error resort to default conf.)
    //TODO set model answers to these modifications
    private void prepareMatch(Lobby lobby) {
        //Build Gson object
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();

        //Decide player Order
        ArrayList<String> playerOrder= new ArrayList<>();
        playerOrder.add(lobby.getLeaderID());
        playerOrder.addAll(lobby.getOtherPLayersID());
        Collections.shuffle(playerOrder);

        //Get the involved players networkData
        HashMap<String, RequestsElaborator> involvedPlayersNetworkData = new HashMap<>();
        for(String playerID: networkData.keySet())
            involvedPlayersNetworkData.put(playerID, networkData.get(playerID));

        //Initialize faith track
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

        //Initialize market
        HashMap<Marble, Integer> marbles = new HashMap<>() {{
            put(Marble.GRAY, 2);
            put(Marble.YELLOW, 2);
            put(Marble.PURPLE, 2);
            put(Marble.BLUE, 2);
            put(Marble.WHITE, 4);
            put(Marble.RED, 1);
        }};

        //Initialize development cards
        ArrayList<DevCard> devCards = new ArrayList<>();
        for(int i=1; i<=48; i++){ //48 configuration option
            try {
                String DevCardJSON = Files.readString(Paths.get("src\\main\\resources\\DevCard" + i + ".json"));
                devCards.add(gson.fromJson(DevCardJSON, DevCard.class));
            } catch (IOException e) {
                e.printStackTrace(); //how?!?!?!
            }
        }

        //initialize the dashboards and each player
        ArrayList<DashBoard> dashBoards = new ArrayList<>();
        ArrayList<Player> players= new ArrayList<>();
        for (String s : playerOrder) {
            ArrayList<Integer> depotCapacities = new ArrayList<>();
            depotCapacities.add(1);
            depotCapacities.add(2);
            depotCapacities.add(3);
            ProductionPower personalPower = new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1, 0);
            DashBoard dashBoard = new DashBoard(3, depotCapacities, personalPower, faithTrack); //3, depotCap and personalPower: configuration options
            Player player = new Player(s, dashBoard);
            dashBoard.addObserver(new DashBoardHandler(involvedPlayersNetworkData, player));
            player.addObserver(new PlayerHandler(involvedPlayersNetworkData));
            dashBoards.add(dashBoard);
            players.add(player);
        }

        //Initialize match state
        MatchState matchState= new MatchState(players, devCards, 4, 3, marbles); //missing configuration options for market
        matchState.getMarket().addObserver(new MarketHandler(involvedPlayersNetworkData));
        matchState.getDevCardGrid().addObserver(new DevCardGridHandler(involvedPlayersNetworkData));
        matchState.getPlayers().forEach(p -> {
            FaithTrackDataHandler faithTrackDataHandler = new FaithTrackDataHandler(involvedPlayersNetworkData, matchState);
            p.getDashBoard().getFaithTrackData().addObserver(faithTrackDataHandler);
        });
        matchState.addObserver(new MatchStateHandler(involvedPlayersNetworkData));

        //Initialize the controller
        VirtualView matchEventHandlerRegistry = new VirtualView();
        HashMap<String, ClientHandlerSender> clientHandlerSenders = new HashMap<>();
        for (String playerID: playerOrder) {
            RequestsElaborator requestsElaborator = networkData.get(playerID);
            clientHandlerSenders.put(playerID, requestsElaborator.getClientHandlerSender());
            requestsElaborator.setMatchEventHandlerRegistry(matchEventHandlerRegistry);
        }
        Controller matchController = new Controller(matchEventHandlerRegistry, matchState, clientHandlerSenders);

        //Initialize the leader cards
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        for(int i=1; i<=16; i++){ //16 configuration option
            try {
                String LeaderCardJSON = Files.readString(Paths.get("src\\main\\resources\\LeaderCard" + i + ".json"));
                leaderCards.add(gson.fromJson(LeaderCardJSON, LeaderCard.class));
            } catch (IOException e) {
                e.printStackTrace(); //how?!?!?!
            }
        }
        Collections.shuffle(leaderCards);

        //Send to the players what they need to chose
        for (int i=0; i<playerOrder.size(); i++){
            int numberResourcesOfChoice = 0;
            int faithPoints = 0;
            if(i>=2)
                numberResourcesOfChoice++;
            if(i>=3)
                faithPoints++;
            if(i>=4)
                numberResourcesOfChoice++;

            if(faithPoints > 0);
            //move the player on the faith track

            ArrayList<LeaderCard> cardsToChoseFrom = new ArrayList<>();
            for(int j=0; j<=4; j++) { //4 configuration option
                cardsToChoseFrom.add(leaderCards.get(leaderCards.size()-1));
                leaderCards.remove(leaderCards.size()-1);
            }

            InitialChoicesEvent initialChoicesEvent = new InitialChoicesEvent(playerOrder.get(i), cardsToChoseFrom, numberResourcesOfChoice);
            networkData.get(playerOrder.get(i)).getClientHandlerSender().sendEvent(initialChoicesEvent);
        }
    }
}
