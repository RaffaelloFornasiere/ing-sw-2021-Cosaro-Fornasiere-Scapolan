package it.polimi.ingsw.utilities;

import it.polimi.ingsw.exceptions.IllegalOperation;

import java.util.HashMap;

/**
 * Class for wrapping messages to be sent trough the socket
 */
public class MessageWrapper {
    private static final HashMap<String, String> substitutions = new HashMap<>();

    static {
        substitutions.put("/n", "çaq0r1ga");
    }

    /**
     * Getter for the HashMap containing the substitution that will be done in a string by this class
     * @return The HashMap containing the substitution that will be done in a string by this class
     */
    public static HashMap<String, String> getSubstitutions(){
        return new HashMap<>(substitutions);
    }

    /**
     * Substitutes all the part of the strings that makes it problematic with regards to being sent trough a socket
     * @param s The string to modify
     * @return The modified string
     * @throws IllegalOperation If the string contains any of the characters sequence that will substitute the problematic part
     */
    public static String wrap(String s) throws IllegalOperation {
        for(String from: substitutions.keySet()){
            String to = substitutions.get(from);
            if(s.contains(to)) throw new IllegalOperation("String cannot contain \"" + to + "\" to be wrappable");
            s = s.replaceAll(from, to);
        }
        return s;
    }

    /**
     * Restores a string to it's original state after it has been wrapped
     * @param s The string that needs to be restored
     * @return The restored string
     */
    public static String unwrap(String s){
        for(String from: substitutions.keySet()){
            s = s.replaceAll(substitutions.get(from), from);
        }
        return s;
    }
}
