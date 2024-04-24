package bakery;

import java.io.Serializable;

/**
 * Class of Ingredient is used to describe ingredient in the game
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class Ingredient implements Comparable<Ingredient>, Serializable {

	/**
	 * Name to describe the ingredient
	 */
	private String name;

	/**
	 * Helpful Duck card, which can be used as substitute for other ingredients
	 */
	public static final Ingredient HELPFUL_DUCK = new Ingredient("helpful duck 𓅭");

	/**
	 * Unique ID for Serializable object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the Ingredient object
	 * 
	 * @param name Name to describe the ingredient
	 */
	public Ingredient(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the Ingredient in String form
	 * 
	 * @return The name of the Ingredient
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * Compare ingredients, by implementing the Comparable class which imposes a total ordering of Ingredient
	 * 
	 * @param ingredient Ingredient to be compared to
	 * @return Negative integer if less than, zero if equal, positive integer if greater than the specified object
	 */
	public int compareTo(Ingredient ingredient) {
		return this.name.compareTo(ingredient.toString());
	}

	/**
	 * Check if the the Ingredient is equal to the specified object
	 * 
	 * @param o The specified object to be compared to
	 * @return True if equal, false if not equal
	 */
	public boolean equals(Object o) {
		// if null return false as there is no object in comparison
		if (o == null) {
			return false;
		}

		// check the hascode generated
		if (this.name.hashCode() == o.hashCode()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the hashcode og Ingredient object
	 * 
	 * @return Value of the hashcode of the object
	 */
	public int hashCode() {
		return this.name.hashCode();
	}
}