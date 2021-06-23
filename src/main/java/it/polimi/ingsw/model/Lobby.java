package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Observable;

import java.util.ArrayList;

public class Lobby extends Observable {
    private String leaderID;
    private final ArrayList<String> otherPLayersID;
    private final int maxPlayers;
    private boolean canAcceptPlayers;

    /**
     * Constructor for the class
     * @param maxPlayers The number of players the lobby can contain at most
     * @throws IllegalArgumentException If the number of max players is smaller than 1
     */
    public Lobby(int maxPlayers){
        if(maxPlayers<1) throw new IllegalArgumentException("The lobby must at least contain one player");
        this.leaderID = null;
        otherPLayersID = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        canAcceptPlayers = true;
    }

    /**
     * Getter for the ID of the player that is the leader of this lobby
     * @return The ID of the player that is the leader of this lobby
     */
    public String getLeaderID() {
        return leaderID;
    }

    /**
     * Sets the leader of the lobby
     * @param leaderID The ID of the leader
     */
    public void setLeaderID(String leaderID) {
        this.leaderID = leaderID;
        notifyObservers();
    }

    /**
     * Getter for the IDs of the players that are not the leaders of this lobby
     * @return The IDs of the players that are not the leaders of this lobby
     */
    public ArrayList<String> getOtherPLayersID() {
        return new ArrayList<>(otherPLayersID);
    }

    /**
     * Returns whether this lobby is full
     * @return Whether this lobby is full
     */
    public boolean isFull(){
        return otherPLayersID.size() + (leaderID == null ? 0 : 1) >= maxPlayers;
    }

    /**
     * Adds a player to this lobby. If there's no leader, sets it also as the leader
     * @param playerID The ID of the player to add
     * @throws IllegalOperation When the lobby can't accept any more players
     */
    public void addPlayerID(String playerID) throws IllegalOperation {
        if(isFull() || !canAcceptPlayers) throw new IllegalOperation("The lobby can't accept any more players");
        if(this.leaderID == null) leaderID = playerID;
        else this.otherPLayersID.add(playerID);
        notifyObservers();
    }

    /**
     * Removes the player given from the lobby if present. If it is the leader, it also passes the leadership of the lobby to another player
     * @param playerId The player to remove
     * @return The number of players left in the lobby
     */
    public int removePlayer(String playerId) {
        if(playerId.equals(leaderID)){
            if(otherPLayersID.size()>0){
                leaderID = otherPLayersID.get(0);
                otherPLayersID.remove(0);
            }
            else{
                leaderID = null;
            }
        }
        else{
            otherPLayersID.remove(playerId);
        }
        notifyObservers();
        return otherPLayersID.size() + (leaderID == null ? 0 : 1);
    }

    /**
     * Returns whether the lobby can still accept players
     * @return Whether the lobby can still accept players
     */
    public boolean canAcceptPlayers() {
        return canAcceptPlayers;
    }

    /**
     * Sets if the lobby can accept players
     * @param canAcceptPlayers Whether the lobby can accept players
     */
    public void setCanAcceptPlayers(boolean canAcceptPlayers) {
        this.canAcceptPlayers = canAcceptPlayers;
    }
}
