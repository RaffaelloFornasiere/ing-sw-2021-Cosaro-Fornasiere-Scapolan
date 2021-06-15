package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.utilities.LockWrap;
import it.polimi.ingsw.utilities.Pair;
import org.reflections.vfs.Vfs;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class PlayerInfo {





    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(String leaderID) {
        this.leaderID = leaderID;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getMarketBuyInfo() {
        return marketBuyInfo;
    }

    public void setMarketBuyInfo(String marketBuyInfo) {
        this.marketBuyInfo = marketBuyInfo;
    }

    public String getBuyDevCardInfo() {
        return buyDevCardInfo;
    }

    public void setBuyDevCardInfo(String buyDevCardInfo) {
        this.buyDevCardInfo = buyDevCardInfo;
    }

    public Marble[] getBoughResources() {
        return boughResources;
    }

    public void setBoughResources(Marble[] boughResources) {
        this.boughResources = boughResources;
    }


    private String playerID;
    private String leaderID;
    private InetAddress serverAddress;
    private Integer serverPort;
    private String marketBuyInfo;
    private String buyDevCardInfo;
    private Marble[] boughResources;
    private ArrayList<String> prodPowerDevCards;
    private boolean activatePersonalPower;
    private Pair<Direction, Integer> boughtResourcesInfo;



    public ArrayList<String> getProdPowerDevCards() {
        return prodPowerDevCards;
    }

    public void setProdPowerDevCards(ArrayList<String> prodPowerDevCards) {
        this.prodPowerDevCards = prodPowerDevCards;
    }

    public boolean isActivatePersonalPower() {
        return activatePersonalPower;
    }

    public void setActivatePersonalPower(boolean activatePersonalPower) {
        this.activatePersonalPower = activatePersonalPower;
    }




    public Pair<Direction, Integer> getBoughtResourcesInfo() {
        return boughtResourcesInfo;
    }

    public void setBoughtResourcesInfo(Pair<Direction, Integer> boughtResourcesInfo) {
        this.boughtResourcesInfo = boughtResourcesInfo;
    }


    
    


}
