package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class containing the data needed to use the ability of a leader card that give additional resource deposit space
 */
public class DepositLeaderPower extends LeaderPower{

    private final HashMap<Resource, Integer> maxResources;
    private final HashMap<Resource, Integer> currentResources;


    /**
     * Constructor for the class
     * @param maxResources the amount of extra resources that can be stored
     */
    public DepositLeaderPower(HashMap<Resource, Integer> maxResources) {
        this.maxResources = new HashMap<>(maxResources);

        this.currentResources = new HashMap<>();
        for (Resource r: maxResources.keySet()) {
            this.currentResources.put(r, 0);
        }
    }

    /**
     * Getter for the extra storage space for the resources
     * @return the extra space for each resource
     */
    public HashMap<Resource, Integer> getMaxResources() {
        return new HashMap<>(maxResources);
    }

    /**
     * Getter for the current resources stored thanks to this power
     * @return resources currently stored
     */
    public HashMap<Resource, Integer> getCurrentResources() {
        return new HashMap<>(currentResources);
    }

    /**
     * Method that ads the resources passed to the deposit granted by this power
     * @param resourcesAdded the resources to be added
     * @throws ResourcesLimitsException when there's not enough space for the resources to be added
     */
    public void addResources(HashMap<Resource, Integer> resourcesAdded) throws ResourcesLimitsException {
        for(Resource r: resourcesAdded.keySet()){
            if((!maxResources.containsKey(r) && resourcesAdded.get(r)!=0) || resourcesAdded.get(r)+currentResources.get(r)>maxResources.get(r))
                throw new ResourcesLimitsException();

            currentResources.compute(r, (key,val) -> val + resourcesAdded.get(r));
        }
        notifyObservers();
    }

    /**
     * Method that removes the resources passed to the deposit granted by this power
     * @param resourcesRemoved the resources to be removed
     * @throws ResourcesLimitsException when there are not enough resources in the deposit to be removed
     */
    public void removeResources(HashMap<Resource, Integer> resourcesRemoved) throws ResourcesLimitsException {
        for(Resource r: resourcesRemoved.keySet()){
            if((!currentResources.containsKey(r) && resourcesRemoved.get(r)!=0) || resourcesRemoved.get(r)>currentResources.get(r))
                throw new ResourcesLimitsException();

            currentResources.compute(r, (key,val) -> val - resourcesRemoved.get(r));
        }
        notifyObservers();
    }

    public boolean canStore(HashMap<Resource, Integer> resources){
        for(Resource r: resources.keySet()){
            if(!maxResources.containsKey(r)) return false;
            if(maxResources.get(r) - currentResources.getOrDefault(r, 0)<resources.get(r)) return false;
        }
        return true;
    }
}
