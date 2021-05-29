package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceSelectionEvent extends ClientEvent{
    private final ArrayList<Resource> required;
    private final ArrayList<Resource> usedFromStrongbox;
    private final ArrayList<Resource> usedFromWarehouse;

    public ResourceSelectionEvent(String playerId, ArrayList<Resource> required, ArrayList<Resource> usedFromStrongbox, ArrayList<Resource> usedFromWarehouse) {
        super(playerId);
        this.required = required;
        this.usedFromStrongbox = usedFromStrongbox;
        this.usedFromWarehouse = usedFromWarehouse;
    }
    public ArrayList<Resource> getRequired() {
        return required;
    }

    public ArrayList<Resource> getUsedFromStrongbox() {
        return usedFromStrongbox;
    }

    public ArrayList<Resource> getUsedFromWarehouse() {
        return usedFromWarehouse;
    }

}
