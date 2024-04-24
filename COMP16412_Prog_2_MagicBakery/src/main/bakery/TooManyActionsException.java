package bakery;

import java.lang.IllegalStateException;

/**
 * Class to handle exception when the current player has run out of actions and tries to take an action
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class TooManyActionsException extends IllegalStateException {

    /**
     * Empty constructor with no parameter
     */
    public TooManyActionsException() {
        super();
    }
    
    /**
     * Wrapper method for IllegalStateException (one parameter)
     * 
     * @param errorMessage Error message to be thrown
     */
    public TooManyActionsException(String errorMessage) {
        super(errorMessage);
    }

}
