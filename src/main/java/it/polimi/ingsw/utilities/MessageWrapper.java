package it.polimi.ingsw.utilities;

import it.polimi.ingsw.exceptions.IllegalOperation;

public class MessageWrapper {
    public static final String MESSAGE_START = "#-- BEGIN MESSAGE --#";
    public static final String MESSAGE_END = "#-- END MESSAGE --#";

    public static String getScannerPattern(){
        return '(' + MESSAGE_START + "(.)*" + MESSAGE_END + ")?";
    }

    public static String wrap(String s) throws IllegalOperation {
        if(s.contains(MESSAGE_START) || s.contains(MESSAGE_END))
            throw new IllegalOperation("String can't contain \""+ MESSAGE_START + "\" or \"" + MESSAGE_END + "\"");

        return MESSAGE_START + '\n' + s + '\n' + MESSAGE_END;
    }

    public static String unwrap(String s) throws IllegalOperation {
        if(!s.startsWith(MESSAGE_START) || !s.endsWith(MESSAGE_END))
            throw new IllegalOperation("String must begin with \""+ MESSAGE_START + "\" and end with \"" + MESSAGE_END + "\"");

        return s.replaceAll(MESSAGE_START, "").replaceAll(MESSAGE_END, "");
    }

}
