package bakery;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.lang.IllegalArgumentException;

import util.CardUtils;
import util.StringUtils;

/**
 * Class for functional game actions
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.2
 */
public class MagicBakery implements Serializable {

	/**
	 * Customers object, handling the CustomerOrder
	 */
	private Customers customers;

	/**
	 * Layers initiated from the file
	 */
	private Collection<Layer> layers;

	/**
	 * List of active players
	 */
	private Collection<Player> players;

	/**
	 * Ingredient in the pantry, to be drawn by players
	 */
	private Collection<Ingredient> pantry;

	/**
	 * The remaining ingredients
	 */
	private Collection<Ingredient> pantryDeck;

	/**
	 * Used ingredients
	 */
	private Collection<Ingredient> pantryDiscard;

	/**
	 * Random object for reproducible shuffle
	 */
	private Random random;

	/**
	 * Unique version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * NOT IN UML list of players waiting for their turn
	 */
	private List<Player> currentList;

	/**
	 * NOT IN UML list of players who already took their turn
	 */
	private List<Player> doneList;

	/**
	 * NOT IN UML number of available actions left
	 */
	private int actions;

	/**
	 * Enum for actions that can be taken by players
	 */
	public enum ActionType {
		DRAW_INGREDIENT,
		PASS_INGREDIENT,
		BAKE_LAYER,
		FULFIL_ORDER,
		REFRESH_PANTRY
	}

	/**
	 * Class constructor, initiate a new game object (main object which connect every required classes)
	 * 
	 * @param seed Random seed for reproducible randomness
	 * @param ingredientDeckFile Path to ingredients file
	 * @param layerDeckFile Path to layers file
	 * @throws FileNotFoundException thrown when there is no file found in the specified path
	 */
	public MagicBakery(long seed, String ingredientDeckFile, String layerDeckFile) throws FileNotFoundException {
		try {
			this.pantryDeck = CardUtils.readIngredientFile(ingredientDeckFile);
		} catch (FileNotFoundException e) {
			throw e;
		}
        
		try {
			this.layers = CardUtils.readLayerFile(layerDeckFile);
		} catch (FileNotFoundException e) {
			throw e;
		}
        
        this.players = new ArrayList<Player>();
        this.random = new Random(seed);
        this.pantry = new ArrayList<Ingredient>();
        this.pantryDiscard = new ArrayList<Ingredient>();
        this.currentList = new ArrayList<Player>();
		this.doneList = new ArrayList<Player>();
	}

	/**
	 * Take all the ingredients, move to pantryDiscard and return the baked layers
	 * 
	 * @param layer Specified layer to be baked
	 * @throws TooManyActionsException When the player takes action while they don't have any left
	 * @throws WrongIngredientsException When the player doesn't have enough ingredient to bake the layer
	 */
	public void bakeLayer(Layer layer) throws TooManyActionsException, WrongIngredientsException {
		
		// check if can bake the layer, if not throw WrongIngredientsException immediately
		if (!(layer.canBake(getCurrentPlayer().getHand()))) {
			throw new WrongIngredientsException("Not enough ingredients to bake !");
		}

		if (this.actions > 0) {
			List<Ingredient> ingredientsNeed = new ArrayList<Ingredient>(layer.getRecipe());

			// remove all ingredients in the players hand
			for (Ingredient ingredient : layer.getRecipe()) {
				// if ingredients exist, remove ingredient
				if (getCurrentPlayer().getHand().contains(ingredient)) {
					getCurrentPlayer().removeFromHand(ingredient);
					ingredientsNeed.remove(ingredient);
					this.pantryDiscard.add(ingredient);
				// if not remove helpful duck
				} else {
					getCurrentPlayer().removeFromHand(Ingredient.HELPFUL_DUCK);
					this.pantryDiscard.add(Ingredient.HELPFUL_DUCK);
				}
			}

			// remove layer from the layers stack
			this.layers.remove(layer);
			// add layer to hand
			getCurrentPlayer().addToHand(layer);
			this.actions -= 1;
		} else {
			throw new TooManyActionsException("There is no actions left. Pass to the next person.");
		}

	}

	/**
	 * Take the top card from the pantryDeck, remove it and return
	 * 
	 * @return The top card on the pantryDeck
	 * @throws EmptyPantryException When the pantryDeck and pantryDiscard is empty, no additional materials left
	 */
	private Ingredient drawFromPantryDeck() throws EmptyPantryException {

		// pantry discard and pantry deck are both empty
		if (this.pantryDiscard.size() == 0 && this.pantryDeck.size() == 0) {
			throw new EmptyPantryException("There is no cards left in the pantry deck", null);
		}

		if (this.pantryDeck.size() == 0) {
			Collections.shuffle((List<Ingredient>) this.pantryDiscard, this.random);
			this.pantryDeck.addAll(this.pantryDiscard);
			this.pantryDiscard.clear();
		}

		return ((ArrayList<Ingredient>) this.pantryDeck).remove(this.pantryDeck.size()-1);
	}

	/**
	 * Take a specified ingredient from the pantry
	 * 
	 * @param ingredientName Specified ingredient to be taken
	 * @throws TooManyActionsException When the player takes action while they don't have any left
	 */
	public void drawFromPantry(String ingredientName) throws TooManyActionsException {
		Ingredient newToPull = new Ingredient(ingredientName);

		try {
			drawFromPantry(newToPull);
		} catch (TooManyActionsException e) {
			throw e;
		}
	}

	/**
	 * Take a specified ingredient from the pantry
	 * 
	 * @param ingredient Specified ingredient to be taken
	 * @throws TooManyActionsException When the player takes action while they don't have any left
	 */
	public void drawFromPantry(Ingredient ingredient) throws TooManyActionsException {
		
		if (this.actions > 0) {
			if (this.pantry.contains(ingredient)) {
				getCurrentPlayer().addToHand(ingredient);
				((ArrayList<Ingredient>) this.pantry).remove(ingredient);
				this.pantry.add(drawFromPantryDeck());
			} else {
				throw new WrongIngredientsException("Ingredient is not in the pantry");
			}
			this.actions -= 1;
		} else {
			throw new TooManyActionsException();
		}

	}

	/**
	 * Initiate the end of a turn, move currentList to doneList, check end of the round and end of game
	 * 
	 * @return True if the game is not over yet, false if the game is over
	 */
	public boolean endTurn() {
		
		// move the player
		this.doneList.add(this.currentList.remove(0));

		// check if the end of the turn
		// add new customer if end, if empty, just pass time
		if (this.currentList.size() == 0) {

			// re-move everyhting to initial state is broken 
			// need to set the iteration to fixed (don't use doneList)
			for (int i = 0; i < this.players.size(); i++) {
	
				this.currentList.add(this.doneList.get(0));
				this.doneList.remove(0);

			}

			// time passes when everyone took their turn
			if (this.customers.getCustomerDeck().size() != 0) {
				this.customers.addCustomerOrder();
			} else {
				this.customers.timePasses();
			}

		}
		
		// check if the customer deck and customer empty after time passed, end game if true
		// initiate game end condition and return false
		if (this.customers.size() == 0 && this.customers.getCustomerDeck().size() == 0) {
			return false;
		} 

		this.actions = getActionsPermitted();
		return true;

	}

	/**
	 * Fulfill the specified order
	 * 
	 * @param customer Specified order to be fulfilled
	 * @param garnish True if want to garnish, false if fulfill without garnish
	 * @return List of Ingredients drawn due to the fulfillment
	 * @throws TooManyActionsException When the player takes action while there is none left
	 * @throws WrongIngredientsException There is not enough ingredients in player's hand to fulfill the order
	 */
	public List<Ingredient> fulfillOrder(CustomerOrder customer, boolean garnish) throws TooManyActionsException, WrongIngredientsException {
		List<Ingredient> ingredientList = customer.fulfill(getCurrentPlayer().getHand(), garnish);
		List<Ingredient> garnishList = new ArrayList<Ingredient>();

		if (this.actions == 0) {
			throw new TooManyActionsException();
		}

		// status was set in the ingredientList call
		try {
			// remove from hand
			for (Ingredient ingredient : ingredientList) {
				if (ingredient instanceof Layer) {
					((ArrayList<Layer>) this.layers).add((Layer) ingredient);
				} else {
					this.pantryDiscard.add(ingredient);
				}
				getCurrentPlayer().removeFromHand(ingredient);
			}

			// add two cards to pplayer's hand when garnising an order
			if (garnish) {
				for (int i = 0; i < 2; i++) {
					Ingredient drawn = drawFromPantryDeck();
					getCurrentPlayer().addToHand(drawn);
					garnishList.add(drawn);
				}
			}

			// move to inactive customers
			this.customers.remove(customer);
			this.actions -= 1;

		} catch (WrongIngredientsException e) {
			throw e;
		}

		// update status after completion ?
		this.customers.customerWillLeaveSoon();
		return garnishList;
	}

	/**
	 * Count the numbers of actions per turn that can be taken by players
	 * 
	 * @return Number of actions per turn
	 */
	public int getActionsPermitted() {
		if((this.players.size() == 2) || (this.players.size() == 3)) {
			return 3;
		} else if((this.players.size() == 4) || (this.players.size() == 5)) {
			return 2;
		} else {
			return 0;
		}
	}

	/**
	 * Count the number of actions left for the current player
	 * 
	 * @return Number of actions left for the current player
	 */
	public int getActionsRemaining() {
		return this.actions;
	}

	/**
	 * Get layers that can be baked by the current player
	 * 
	 * @return Layers that can be baked by the players
	 */
	public Collection<Layer> getBakeableLayers() {
		Collection<Layer> bakeables = new ArrayList<Layer>();

		for (Layer lay : getLayers()) {
			if (lay.canBake(getCurrentPlayer().getHand())) {
				bakeables.add(lay);
			}
		}

		return bakeables;
	}
	
	/**
	 * Gets the current player in the round
	 * 
	 * @return Player object representing the current player of the game
	 */
	public Player getCurrentPlayer() {
		return this.currentList.get(0);
	}

	/**
	 * Gets the Customers object instantiated in the current game
	 * 
	 * @return Customer object of the game, controlling the CustomerOrder
	 */
	public Customers getCustomers() {
		return this.customers;
	}

	/**
	 * Gets the active customers that can be fulfilled by the current players
	 * 
	 * @return Active customers from the Customers object that can be fulfilled
	 */
	public Collection<CustomerOrder> getFulfilableCustomers() {
		return this.customers.getFulfillable(getCurrentPlayer().getHand());
	}

	/**
	 * Gets the active customers that can be fulfilled and garnished by the current players
	 * 
	 * @return Active customers from the Customers object that can be fulfilled and garnished
	 */
	public Collection<CustomerOrder> getGarnishableCustomers() {	
		List<CustomerOrder> customerNeed = new ArrayList<CustomerOrder>(getFulfilableCustomers());
		List<Ingredient> playerHand = new ArrayList<Ingredient>(getCurrentPlayer().getHand());
		Collection<CustomerOrder> garnishables = new ArrayList<CustomerOrder>();
		
		// if hand is empty, obviously we cannot garnish the order
		if (playerHand == null || playerHand.size() == 0) {
			return garnishables;
		}

		// handling null customers
		customerNeed.removeIf(element -> element == null);
		
		// if the list is now empty, return empty
		if (customerNeed.size() == 0) {
			return garnishables;
		}

		for (CustomerOrder customerOrder : customerNeed) {
			if (customerNeed != null && playerHand != null) {
				if (customerOrder.canGarnish(playerHand)) {
					garnishables.add(customerOrder);
				}
			}
		}

		return garnishables;
	}

	/**
	 * Introduced to check if a layer is in a list, redundant I know
	 * 
	 * @param layer Layer object to be checked
	 * @param collection List to be checked against
	 * @return True if in List, false if otherwise
	 */
	private boolean checkInLayer(Layer layer, Collection<Layer> collection) {
		for (Layer lay : collection) {
			if (lay.toString().equals(layer.toString())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets all the layers in the game, making sure there is no repetition
	 * 
	 * @return List of layers in the game, no repetition
	 */
	public Collection<Layer> getLayers() {
		Collection<Layer> uniqueLayer = new ArrayList<Layer>();

		for (Layer lay : this.layers) {
			if (checkInLayer(lay, uniqueLayer)) {
				uniqueLayer.add(lay);
			}
		}

		return uniqueLayer;
	}

	/**
	 * Gets the pantry in the game
	 * 
	 * @return List of ingredients in the pantry
	 */
	public Collection<Ingredient> getPantry() {
		return this.pantry;
	}

	/**
	 * Gets the list of players playing the game
	 * 
	 * @return List of players playing the game
	 */
	public Collection<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Load a saved file form the file
	 * Implementation of serialisation
	 * @param file Specified path to be loaded from
	 * @throws IOException Thrown when there isproblem reading the specified file
	 * @throws ClassNotFoundException Thrown when the class to class the object is not found
	 * @return MagicBakery object from file
	 */
	public static MagicBakery loadState(File file) throws IOException, ClassNotFoundException {
		try {
			FileInputStream fileStream = new FileInputStream(file);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);

			MagicBakery bakery = (MagicBakery) objectStream.readObject();

			objectStream.close();
			fileStream.close();

			return bakery;

		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}
	}

	/**
	 * Allows player to pass card to another player
	 * 
	 * @param ingredient Specified ingredient to be passed
	 * @param recipient Specified player to receive the card
	 * @throws WrongIngredientsException When the specified ingredient is not even in the current player's hand
	 * @throws TooManyActionsException When the player tries to take action when there is none left
	 */
	public void passCard(Ingredient ingredient, Player recipient) throws WrongIngredientsException, TooManyActionsException {
		
		if (!(this.getCurrentPlayer().getHand().contains(ingredient))) {
			throw new WrongIngredientsException("You don't have the ingredient to be passed !");
		}

		if (this.actions > 0) {
			try {
				recipient.addToHand(ingredient);
				getCurrentPlayer().removeFromHand(ingredient);
			} catch (WrongIngredientsException e) {
				throw e;
			}

			this.actions -= 1;
		} else {
			throw new TooManyActionsException();
		}
	}

	/**
	 * Prints the indication of how many customers had their orders fulfilled and garnished
	 * and how many left the bakery without their order being completed
	 */
	public void printCustomerServiceRecord() {
		int numberFulfilledGarnished = this.customers.getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.FULFILLED).size() + this.customers.getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GARNISHED).size();
		String fulfilledCustomer = "With happy stomach : " + numberFulfilledGarnished + " ("
			+ this.customers.getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GARNISHED).size() + " garnished)" + "\n";
		String dissapointedCustomer = "Dissapointed as if their world ends, just because they didn't gey their usual pastry and decided to project that anger towards unsuspecting passer-by : " 
			+ this.customers.getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GIVEN_UP).size() + "\n";

		System.out.println("Left the bakery ------------------------------------------------------------------------------------- \n" + fulfilledCustomer + dissapointedCustomer);
	}
	
	/**
	 * Prints the information needed for a player to make decisions during their turn
	 * Includes all layers available for baking, the current contents of the pantry and player's hand 
	 * and the collection of CustomerOrders that represent customers waiting in the game
	 */
	public void printGameState() {

		// print the customer layer of the game
		String customers = "Customer : \n";
		LinkedList<CustomerOrder> listToPrint = new LinkedList<CustomerOrder>();
		for (CustomerOrder customerOrder : this.customers.getActiveCustomers()) {
			listToPrint.add(customerOrder);
		}
		for (String stringCustomer : StringUtils.customerOrdersToStrings(listToPrint)) {
			customers += stringCustomer + "\n";
		}

		// print the layers layer of the game
		String layers = "Layers : \n";
		for (String stringLayers : StringUtils.layersToStrings(getLayers())) {
			layers += stringLayers + "\n";
		}

		String pantry = "Pantry : \n";
		for (String stringPantry : StringUtils.ingredientsToStrings(getPantry())) {
			pantry += stringPantry + "\n";
		}

		String hand = "Hand : \n";
		for (String stringHand : StringUtils.ingredientsToStrings(getCurrentPlayer().getHand())) {
			hand += stringHand + "\n";
		}

		System.out.println(customers + layers + pantry + hand);

	}

	/**
	 * Removes all the card in the pantry and moves it to pantryDiscrad, draws 5 new cards
	 * 
	 * @throws TooManyActionsException When the player tries to take an action when there is none left
	 */
	public void refreshPantry() throws TooManyActionsException {
		
		if (this.actions > 0) {
			this.pantryDiscard.addAll(this.pantry);
			this.pantry.clear();
			
			for (int i = 0; i < 5; i++) {
				this.pantry.add(drawFromPantryDeck());
			}

			this.actions -= 1;
		} else {
			throw new TooManyActionsException();
		}

	}

	/**
	 * Save the current state of the game (the object MagicBakery) into the specified file
	 * Implementation of serialisation
	 * @param file Specified file to save the current state of the game
	 * @throws FileNotFoundException When the specified file cannot be found in the system
	 */
	public void saveState(File file) throws FileNotFoundException {
		try {
			FileOutputStream fileStream = new FileOutputStream(file);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

			objectStream.writeObject(this);

			objectStream.close();
			fileStream.close();

		} catch (IOException e) {
			throw new FileNotFoundException();
		}
	}

	/**
	 * Starts the whole game by instantiating the Customers object and set up the decks
	 * 
	 * @param playerNames List of playerNames that plays the game
	 * @param customerDeckFile Path of the customers file
	 * @throws FileNotFoundException When the path specified for Customers is not found 
	 * @throws IllegalArgumentException When the number of players more than 5 or less than 2 (illegal)
	 */
	public void startGame(List<String> playerNames, String customerDeckFile) throws FileNotFoundException, IllegalArgumentException {

		// check for number of players
		if (playerNames.size() < 2 || playerNames.size() > 5 ) {
			throw new IllegalArgumentException();
		}

		// adding players to the game
		for (String playerName : playerNames) {
			Player newPlay = new Player(playerName);
			this.players.add(newPlay);
		}

		for (Player playPlay : this.players) {
			this.currentList.add(playPlay);
		}

		this.actions = getActionsPermitted();

		// add customer here
		try {
			this.customers = new Customers(customerDeckFile, this.random, this.layers, this.players.size());
		} catch (FileNotFoundException e) {
			throw e;
		}

		// reveal the customer layer after initialize
		if (this.players.size() == 3 || this.players.size() == 5) {
			this.customers.addCustomerOrder();
		}
		this.customers.addCustomerOrder();
		
		// shuffles the pantryDeck
		Collections.shuffle((List<Ingredient>) this.pantryDeck, this.random);

		// move from pantryDeck to pantry, 5 total
		for (int i = 0; i < 5; i++) {
			this.pantry.add(drawFromPantryDeck());
		}

		// deals each player cards from the pantrydeck, 3 each
		for (Player play : this.players) {
			for (int i = 0; i < 3; i++) {
				play.addToHand(drawFromPantryDeck());
			}
		}

	}
}