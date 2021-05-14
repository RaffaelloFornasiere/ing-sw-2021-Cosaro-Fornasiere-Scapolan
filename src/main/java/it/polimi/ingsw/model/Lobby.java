package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.utilities.Observable;

import java.util.ArrayList;

public class Lobby extends Observable {
    private String leaderID;
    private ArrayList<String> otherPLayersID;

    /**
     * The maximum players that can play the game together
     */
    public static final int MAX_PLAYERS = 4;

    /**
     * constructor for the class
     */
    public Lobby(){
        otherPLayersID = new ArrayList<>();
    }

    /**
     * getter for the ID of the player that is the leader of this lobby
     * @return the ID of the player that is the leader of this lobby
     */
    public String getLeaderID() {
        return leaderID;
    }

    /**
     * sets the leader of the lobby
     * @param leaderID the ID of the leader
     */
    public void setLeaderID(String leaderID) {
        this.leaderID = leaderID;
        notifyObservers();
    }

    /**
     * getter for the IDs of the players that are not the leaders of this lobby
     * @return the IDs of the players that are not the leaders of this lobby
     */
    public ArrayList<String> getOtherPLayersID() {
        return (ArrayList<String>)otherPLayersID.clone();
    }

    /**
     * returns whether this lobby is full
     * @return whether this lobby is full
     */
    public boolean isFull(){
        return otherPLayersID.size() + 1 > MAX_PLAYERS;
    }

    /**
     * adds a player to this lobby
     * @param playerID the ID of the player to add
     * @throws IllegalOperation when the lobby is full
     */
    public void addPlayerID(String playerID) throws IllegalOperation {
        if(isFull()) throw new IllegalOperation("The lobby is full");
        this.otherPLayersID.add(playerID);
        notifyObservers();
    }
}
