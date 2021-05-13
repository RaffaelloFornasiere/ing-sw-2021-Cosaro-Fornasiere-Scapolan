package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {
    private final NetworkAdapter client;
    private static PrintWriter out = new PrintWriter(System.out, true);
    private static Scanner in = new Scanner(System.in);

    public CLI( NetworkAdapter client) {
        this.client = client;

    }


    /**
     * Method that allows the display of the form for entering server data
     */
    void displaySetupForm(){};

    /**
     * Method that allows the display the failure of the setup with server
     */
    void displaySetupFailure(){};

    /**
     * Method that allows the display of the form for entering user data
     */
    void displayLogin(){};

    /**
     * Method that allows the display of successfully login of the user
     */
    void displayLoginSuccessful(){};

    /**
     * Method that allows the display of user login failure
     *
     * @param details the details of the failure
     */
    void displayLoginFailure(String details){};

    /**
     * Method that allows you to view the arrival of a new player in the match
     *
     * @param playerJoined    the name of the new player
     * @param remainingPlayer the players required remaining to start the match
     */
    void displayUserJoined(String playerJoined, Integer remainingPlayer){};

    /**
     * Method that allows you to view the arrival of a new player in the match in a existing lobby
     *
     * @param otherPlayer     the name of the new others player
     * @param remainingPlayer the players required remaining to start the match
     */
    void displayAddedToQueue(List<String> otherPlayer, Integer remainingPlayer){};

    /**
     * Method that allows you to propose to the user the recovery of an old game
     */
    void displayProposeRestoreMatch(){};

    /**
     * Method that allows you to see that the game is starting
     */
    void displayStartingMatch(){};

    /**
     * Method that allows you to view the disconnection of a player
     *
     * @param username the name of disconnected player
     */
    void displayDisconnectedUser(String username){};

    /**
     * Method that notifies disconnection from the server
     */
    void displayDisconnected(){};

    /**
     * Method that allows the display of a generic message
     */
    void displayGenericMessage(String message){};
//------------------------------------------------------------------------------------------------------
    /**
     * Method that allows the display of the form for the choice of the initial LeaderCard
     *
     * @param availableCards a list with the available cards
     */
    void displayChoicePersonalLeaderCard(List<LeaderCard> availableCards){};

    /**
     * Method that allows the display of the form for choosing the first player of the match
     *
     * @param allPlayers list of players to choose from
     */
    void displayAskFirstPlayer(List<Player> allPlayers){};


    /**
     * Method that notifies the automatic assignment of the resources
     *
     * @param resources the assigned card
     */
    void displayStartingResources(ArrayList<Resource> resources){};

//---------------------------------------------------------------------------------------------------------
    /**
     * Method that gives freedom of action to the player who is in turn.
     * In the CLI also allows who is in turn to choose an action between takeResources, buyDevCard,  produce.
     */
    void displayWhoseTurnIsIt(){};

    /**
     * Method that asks you if you want to activate a LeaderCard.
     * and allows the display of the form for the choice of LeaderCards to activate in cli
     */
    void displayAskForLeaderCardActivation(){};


    /**
     * Method that allows the display of the form for the choice of LeaderCard to use currently
     */
    void displayLeaderCardSelection(){};
//----------------------------------------------------------------------------------------------------

    /**
     * Method that allows you to choose a row/column from market
     */
    void displayChoiceOfMarketResources(){};


    /**
     * Method that displays the form to assign a depot to each resource obtained

     */
    void displayAskForResourceDestinationInDepot(){};

    /**
     * Method that allows the display of the resources in depots.

     */
    void displayDepotUpdated(){};
//------------------------------------------------------------------------------------------------------
    /**
     * Method that allows you to choose an available devCard to buy from DevCardGrid
     */
    void displayChoiceOfAvailableDevCards(){};

    /**
     * Method that displays the form to assign the bought card a destination in dashboard.

     */
    void displayAskForDevCardDestinationInSlots(){};

    /**
     * Method that allows the display of the slots in dashboard.

     */
    void displayDevCardSlotsUpdated(){};
//--------------------------------------------------------------------------------------------------------

    /**
     * Method that displays the form that allows you to choose a card for production, including the base production
     */
    void displayChoiceOfAvailableProductions(){};

//----------------------------------------------------------------------------------------------------------------------

    /**
     * Method that displays the new position in FaithTrack
     */
    void displayFaithTrackUpdated(){};

    /**
     * Method that displays the new  favorPopeCard acquired.
     */
    void displayFavorPopeCardUpdated(){};

//--------------------------------------------------------------------------------------------------------------------------


    /**
     * Method that allows you to view players' updates within the match
     */
    void displayPlayersUpdated(){};
//---------------------------------------------------------------------------------------------------
    /**
     * Method of displaying the victory message
     */
    void displayWinnerMessage(){};
    //--------------------------------------------------------

    /**
     * Method that notifies the end of current turn
     */
    void displayEndTurn(){};

    /**
     * Method that allows you to display the lobby
     */
    void displayLobbyCreated(String playersWaiting){};

    /**
     * Method that allows the display of a recovered match
     */
    void displayRestoredMatch(){};



}
