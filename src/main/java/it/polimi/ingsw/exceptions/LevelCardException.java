package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when the level of a development card is incompatible with an operation
 */
public class LevelCardException extends Exception{
    public LevelCardException(){super();}
    public LevelCardException(String message){ super(message);}
}
