package bakery;

import java.lang.RuntimeException;

/**
 * Class to handle exception given when the pantry is completely empty, even after merging the pantryDeck and pantryDiscard
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class EmptyPantryException extends RuntimeException{

    /**
     * Wrapper method for RuntimeException
     * 
     * @param msg Error message to be thrown
     * @param e The cause of error
     */
    public EmptyPantryException(String msg, Throwable e) {
        super(msg, e);
    }
}
