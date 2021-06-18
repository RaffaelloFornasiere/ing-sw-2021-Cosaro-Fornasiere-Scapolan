package it.polimi.ingsw.exceptions;

/**
 * Checked exception throw when the operation is illegal because of some constraint
 */
public class IllegalOperation extends Exception {

    public IllegalOperation(){
        super();
    }

    public IllegalOperation(String message){
        super(message);
    }

}
