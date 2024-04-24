package util;

import java.io.Console;
import java.io.File;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import bakery.Ingredient;
import bakery.Player;
import bakery.MagicBakery;
import bakery.CustomerOrder;
import bakery.MagicBakery.ActionType;

/**
 * Class ConsoleUtils is used to get input from user during the game
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class ConsoleUtils {

	/**
	 * A console object, instantiated to interact with the user through console
	 */
	private Console console;

	/**
	 * Constructor for ConsoleUtils class
	 */
	public ConsoleUtils() {
		this.console = System.console();
	}

	/**
	 * A wrapper method, which takes the input from Console and return as String
	 * 
	 * @return String taken from the user input
	 */
	public String readLine() {
		return this.console.readLine();
	}

	/**
	 * A wrapper method, which takes the input from Console and return as String
	 * 
	 * @param fmt Prompt to be printed on the console
	 * @param args Argument to be pass to the readLine method of the Console object
	 * @return String taken from the user input
	 */
	public String readLine(String fmt, Object... args) {
		return this.console.readLine(fmt, args);
	}

	/**
	 * Gets the ActionType from the user input
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @param bakery MagicBakery object of the game, where the ActionType is taken from
	 * @return ActionType that can be taken by the player in each turn, from the enum
	 */
	public ActionType promptForAction(String prompt, MagicBakery bakery) {
		Collection<Object> actionList = new ArrayList<>(Arrays.asList(ActionType.values()));
		return (ActionType) promptEnumerateCollection(prompt, actionList);
	}

	/**
	 * Gets the CustomerOrder from the user input
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @param customers Collection<CustomerOrder> to check whether the input is in the list
	 * @return CustomerOrder object as per the user input
	 */
	public CustomerOrder promptForCustomer(String prompt, Collection<CustomerOrder> customers) {
		Collection<Object> custList = new ArrayList<>(customers);
		return (CustomerOrder) promptEnumerateCollection(prompt, custList);
	}

	/**
	 * Gets the Player object from the user input
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @param bakery MagicBakery object to check whether the player is in the game
	 * @return Player object as per user input
	 */
	public Player promptForExistingPlayer(String prompt, MagicBakery bakery) {
		Collection<Object> playList = new ArrayList<>(bakery.getPlayers());
		Player currentPlayer = bakery.getCurrentPlayer();

		playList.remove(currentPlayer);

		return (Player) promptEnumerateCollection(prompt, playList);
	}
	
	/**
	 * Gets the File object from the user input
	 * @param prompt Prompt to be printed on the console
	 * @return File object instantiated using the user input
	 */
	public File promptForFilePath(String prompt) {
		String filePath = this.console.readLine(prompt);
		return new File(filePath);
	}

	/**
	 * Gets the Ingredient object from the user input
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @param ingredients Collection<Ingredient> to check whether the input is in the game
	 * @return Ingredient object based on user input
	 */
	public Ingredient promptForIngredient(String prompt, Collection<Ingredient> ingredients) {
		Collection<Object> ingList = new ArrayList<>(ingredients);
		return (Ingredient) promptEnumerateCollection(prompt, ingList);
	}
	
	/**
	 * Get the List of players to play the game
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @return List<String> of player's name
	 */
	public List<String> promptForNewPlayers(String prompt) {
		List<String> playerListRet = new ArrayList<String>();
		int inc = 0;

		while(inc < 5) {
			String readPlay = this.console.readLine(prompt);

			if((readPlay.isEmpty()) && inc > 1) {
				break;
			} else if((readPlay.isEmpty()) && inc <= 1) {
				continue;
			}

			playerListRet.add(readPlay);
			inc++;
		}

		return playerListRet;
	}
	
	/**
	 * Gets user input whether to start a new game or load an existing one
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @return boolean value, true for start, false for load
	 */
	public boolean promptForStartLoad(String prompt) {
		String startLoad = this.console.readLine(prompt + " [S]tart/[L]oad : ");

		if(startLoad.equals("S") || startLoad.equals("s")) {
			return true;
		} else if(startLoad.equals("l") || startLoad.equals("L")) {
			return false;
		} else {
			return promptForStartLoad(prompt);
		}
	}
	
	/**
	 * Gets use input for yes or no question
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @return boolean value, true for yes, false for no
	 */
	public boolean promptForYesNo(String prompt) {
		String startLoad = this.console.readLine(prompt + " [Y]es/[N]o : ");

		if(startLoad.equals("Y") || startLoad.equals("y")) {
			return true;
		} else if(startLoad.equals("N") || startLoad.equals("n")) {
			return false;
		} else {
			return promptForYesNo(prompt);
		}
	}

	/**
	 * Method to simplify repetitious task, to return an object from input
	 * 
	 * @param prompt Prompt to be printed on the console
	 * @param collection Collection<Object> to checlk whether the use input is in the collection
	 * @throws IllegalArgumenException when collection passed is either null or empty
	 * @return The object from user input
	 */
	private Object promptEnumerateCollection(String prompt, Collection<Object> collection) throws IllegalArgumentException {

		// handle exception
		if (collection == null) {
			throw new IllegalArgumentException();
		}

		if (collection.size() == 0) {
			throw new IllegalArgumentException();
		}

		// proceed if none was thrown
		List<Object> objectList = new ArrayList<>(collection);
		String playerChoice = this.console.readLine(prompt);

		if (objectList.size() > 1) {
			for (Object objects : objectList) {
				if (objects.toString().equals(playerChoice)) {
					return objects;
				}
			}
		} else {
			return objectList.iterator().next();
		}
		
		return promptEnumerateCollection(prompt, collection);
	}
}