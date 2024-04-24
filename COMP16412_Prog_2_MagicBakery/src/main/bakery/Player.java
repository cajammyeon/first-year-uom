package bakery;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.io.Serializable;

/**
 * Player class, which stores attributes related to the Player in the game
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class Player implements Serializable {

	/**
	 * List of Ingredient cards in Player's hand
	 */
	private List<Ingredient> hand;

	/**
	 * Name of the player
	 */
	private String name;

	/**
	 * Unique ID for Player
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the Player object, with empty hand
	 * 
	 * @param name Name of the player
	 */
	public Player(String name) {
		this.name = name;
		this.hand = new ArrayList<Ingredient>();
	}

	/**
	 * Add cards to Player's hand
	 * 
	 * @param ingredients List<Ingredient> to be added to Player's hand
	 */
	public void addToHand(List<Ingredient> ingredients) {
		this.hand.addAll(ingredients);
	}

	/**
	 * Add card to Player's hand
	 * 
	 * @param ingredient Ingredient to be added to Player's hand
	 */
	public void addToHand(Ingredient ingredient) {
		this.hand.add(ingredient);
	}

	/**
	 * Check if the specified ingredient in Player's hand
	 * 
	 * @param ingredient Ingredients to be checked, whether it's in hand
	 * @return True if in hand, false if otherwise
	 */
	public boolean hasIngredient(Ingredient ingredient) {
		return this.hand.contains(ingredient);
	}

	/**
	 * Remove card from Player's hand
	 * 
	 * @param ingredient Ingredient to be removed from Player's hand
	 * @throws WrongIngredientsException when the specified ingredient is not in hand
	 */
	public void removeFromHand(Ingredient ingredient) throws WrongIngredientsException {
		if (this.hand.contains(ingredient)) {
			this.hand.remove(ingredient);
		} else {
			throw new WrongIngredientsException("There is no such ingredient");
		}
		
	}

	/**
	 * Get the list of cards in Player's hand
	 * 
	 * @return List<Ingredient> in Player's hand
	 */
	public List<Ingredient> getHand() {
		Collections.sort(this.hand);
		return this.hand;
	}
	
	/**
	 * Get the list of Ingredients in Player's hand in proper order (sorted) with the repeated items numbered
	 * 
	 * @return Description of Player's  hand
	 */
	public String getHandStr() {
		String handString = "";

		if (this.hand.size() == 0) {
			return "";
		}

		// sort the hand first (change to string, insert in list and sort)
		List<String> handSort = new ArrayList<String>();
		for (Ingredient string : this.hand) {
			handSort.add(string.toString());
		}
		Collections.sort(handSort);

		for (String ingredient : handSort) {
			String caps_Ing = ingredient.substring(0, 1).toUpperCase() + ingredient.substring(1, ingredient.length());
			if (checkDuplicate(ingredient, this.hand) == 1) {
				handString = handString + caps_Ing + ", ";
			} else {
				if (!handString.contains(caps_Ing)) {
					handString = handString + caps_Ing + " (x" + checkDuplicate(ingredient, this.hand) + "), ";
				}
			}
		}

		return handString.substring(0, handString.length()-2);	
	}

	/**
	 * Private method introduced to check if there is any duplicate of items in hand for description
	 * 
	 * @param nameOfIng Specified ingredient to be counted
	 * @param hand List<Ingredient> in Player's hand 
	 * @return Number of occurence of the ingredient's in Player's hand
	 */
	private int checkDuplicate(String nameOfIng, List<Ingredient> hand) {
		int i = 0;
		for (Ingredient ingredient : hand) {
			if (ingredient.toString().equals(nameOfIng)) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Get the name of the Player
	 * 
	 * @return Return the name of the Player
	 */
	public String toString() {
		return this.name;
	}
}