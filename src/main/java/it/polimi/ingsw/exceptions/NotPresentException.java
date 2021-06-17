package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when the operation can't find the elements it needs
 */
public class NotPresentException extends Exception {

    public NotPresentException(){
        super();
    }

    public NotPresentException(String message){
        super(message);
    }

}
