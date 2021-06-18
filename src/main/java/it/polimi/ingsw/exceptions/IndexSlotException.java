package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when there are problem with the index of a card slot
 */
public class IndexSlotException extends Exception{
    public IndexSlotException(){super();}
    public IndexSlotException(String message){ super(message);}
}
