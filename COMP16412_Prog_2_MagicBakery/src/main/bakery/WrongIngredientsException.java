package bakery;

import java.lang.IllegalArgumentException;

/**
 * Class to handle exception related to Ingredients
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class WrongIngredientsException extends IllegalArgumentException {

    /**
     * Wrapper method for IllegalArgumentException
     * @param errorMessage Error message to be thrown
     */
    public WrongIngredientsException(String errorMessage) {
        super(errorMessage);
    }
}
