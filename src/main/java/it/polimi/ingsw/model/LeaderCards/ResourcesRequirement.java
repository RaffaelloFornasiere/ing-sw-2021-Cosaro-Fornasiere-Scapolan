package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class specifying the quantity of resources of each type required needed to activate the leader card
 */
public class ResourcesRequirement extends Requirement{

    private final HashMap<Resource, Integer> resources;

    /**
     * Constructor for the class
     * @param resources the quantity of resources of each type needed to activate the leader cards
     */
    public ResourcesRequirement(HashMap<Resource, Integer> resources){
        this.resources = new HashMap<>(resources);
    }

    /**
     * Getter for the resources required to activate the leader card
     * @return the quantity required for each resource type
     */
    public HashMap<Resource, Integer> getResources() {
        return new HashMap<>(resources);
    }

    @Override
    public boolean isEquivalent(Requirement other) {
        return this.getClass().isInstance(other);
    }

    @Override
    public Requirement merge(Requirement other) throws IllegalOperation {
        if(!this.isEquivalent(other)) throw new IllegalOperation("The requirements to merge must be equivalent");

        HashMap<Resource, Integer> merged = ((ResourcesRequirement)other).getResources();

        for(Resource r: this.resources.keySet())
            merged.put(r, this.resources.get(r)+merged.getOrDefault(r, 0));

        return new ResourcesRequirement(merged);
    }

    @Override
    public boolean checkRequirement(Player p) {
        HashMap<Resource, Integer> playerResources = p.getAllPayerResources();
        for(Resource r: this.resources.keySet())
            if(this.resources.get(r)>playerResources.getOrDefault(r, 0))
                return false;

        return true;
    }
}
