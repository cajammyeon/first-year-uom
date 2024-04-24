package bakery;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import java.io.Serializable;

/**
 * Class for CustomerOrder, which defined the Customer cards
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class CustomerOrder implements Serializable {

	/**
	 * The list of garnish (additional ingredients) for the order
	 */
	private List<Ingredient> garnish;

	/**
	 * The level of the order, from 1 to 3
	 */
	private int level;

	/**
	 * Name of the order
	 */
	private String name;

	/**
	 * Ingredients required to fulfill the order
	 */
	private List<Ingredient> recipe;

	/**
	 * Defines the status of customer based on the enum
	 */
	private CustomerOrderStatus status;

	/**
	 * Unique version ID of the object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Enum class which describes the behaviour of customer throughout the game
	 */
	public enum CustomerOrderStatus {
		WAITING,
		FULFILLED,
		GARNISHED,
		IMPATIENT,
		GIVEN_UP
	}

	/**
	 * The constructor for CustomerOrder class
	 * @param name Name of the order
	 * @param recipe Main ingredients required to create the order
	 * @param garnish Additional ingredients for the order
	 * @param level Level of the order, from 1 to 3
	 * @throws WrongIngredientsException If the file path specified is not found
	 */
	public CustomerOrder(String name, List<Ingredient> recipe, List<Ingredient> garnish,  int level) throws WrongIngredientsException {

		this.name = name.trim();
		this.recipe = recipe;
		this.garnish = garnish;
		this.level = level;
		this.status = CustomerOrderStatus.WAITING;

		//handle exception when the recipe is null
		if (recipe == null) {
			throw new WrongIngredientsException("Empty recipe was given");
		}

		// handle when the recipe is empty
		if (this.recipe.size() == 0) {
			throw new WrongIngredientsException("Empty recipe was given");
		}
	}

	/**
	 * Simply updates the status of customer to given up
	 */
	public void abandon() {
		this.status = CustomerOrderStatus.GIVEN_UP;
	}
	
	/**
	 * Check if the CustomerOrder can be fulfilled by the list of ingredients
	 * 
	 * @param ingredients List<Ingredient> to be checked against
	 * @return True if the CustomerOrder can be fulfilled (with Helpful Ducks), false if otherwise
	 */
	public boolean canFulfill(List<Ingredient> ingredients) {
		List<Ingredient> ingredientsNeed = new ArrayList<Ingredient>(this.recipe);
		List<Ingredient> ingredientsHave = new ArrayList<Ingredient>(ingredients);
		
		if (ingredients == null) {
			return false;
		}
		
		for (Ingredient ingredient : this.recipe) {
			if (ingredientsHave.contains(ingredient)) {
				ingredientsHave.remove(ingredient);
				ingredientsNeed.remove(ingredient);
			}
		}

		if (ingredientsNeed.size() == 0) {
			return true;
		} else {
			// if there is layer in the ingredientsNeed, return false as duck cannot replace layer
			if ((ingredientsNeed.size() - ingredientsCount(ingredientsNeed)) > 0) {
				return false;
			}
			// check number of ducks compared to the ingredientsNeed
			if (duckCount(ingredientsHave) < ingredientsCount(ingredientsNeed)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Check if the CustomerOrder can be garnished by the list of ingredients
	 * 
	 * @param ingredients List<Ingredient> to be checked against
	 * @return True if the CustomerOrder can be garnished, false if otherwise
	 */
	public boolean canGarnish(List<Ingredient> ingredients) {
		List<Ingredient> ingredientsNeed = new ArrayList<Ingredient>(this.garnish);
		List<Ingredient> ingredientsHave = new ArrayList<Ingredient>(ingredients);
	
		for (Ingredient ingredient : this.garnish) {
			if (ingredientsHave.contains(ingredient)) {
				ingredientsHave.remove(ingredient);
				ingredientsNeed.remove(ingredient);
			}
		}

		if (ingredientsNeed.size() == 0) {
			return true;
		} else {
			// if there is layer in the ingredientsNeed, return false as duck cannot replace layer
			if ((ingredientsNeed.size() - ingredientsCount(ingredientsNeed)) > 0) {
				return false;
			}
			// check number of ducks compared to the ingredientsNeed
			if (duckCount(ingredientsHave) < ingredientsCount(ingredientsNeed)) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Count the number of Ingredients in the List<Ingredient> not including Layer
	 * 
	 * @param ingredient List<Ingredient> to be counted from
	 * @return number of Ingredient ONLY (not including Layer object)
	 */
	private int ingredientsCount(List<Ingredient> ingredient) {
		int layerCount = 0;
		int ingCount = 0;
		// separate ingredients and layers
		for (Ingredient ing : ingredient) {
			if (ing instanceof Layer) {
				layerCount += 1;
			}
		}
		ingCount = ingredient.size() - layerCount;
		return ingCount;
	}

	/**
	 * Count the number of Helpful Ducks from a list of Ingredient
	 * 
	 * @param ingredients List<Ingredient> to be counted from
	 * @return The number of Helpful Ducks in the list
	 */
	private int duckCount(List<Ingredient> ingredients) {
		int i = 0;
		// check hand and calculate number of ducks
		for (Ingredient ingredient : ingredients) {
			if(ingredient.equals(Ingredient.HELPFUL_DUCK)) {
				i++;
			}
		}
		return i;
	}
	
	/**
	 * Create a list of ingredients used from hand to fulfill (just bake or include garnish) an order
	 * 
	 * @param ingredients List of ingredients to be checked from
	 * @param garnish Indicate whether the CustomerOrder wants to be garnished (true if garnish)
	 * @throws WrongIngredientsException thrown when the order cannot be fulfilled
	 * @return List<Ingredient> used to fulfill the order
	 */
	public List<Ingredient> fulfill(List<Ingredient> ingredients, boolean garnish) throws WrongIngredientsException {
		List<Ingredient> fulfillIngredients = new ArrayList<Ingredient>();
		List<Ingredient> fulfillHand = new ArrayList<Ingredient>(ingredients);

		// check if can fulfill, if can add all ingredients
		if (canFulfill(ingredients)) {
			for (Ingredient ingredient : this.recipe) {
				if (fulfillHand.contains(ingredient)) {
					fulfillIngredients.add(ingredient);
					fulfillHand.remove(ingredient);
				} else {
					fulfillIngredients.add(Ingredient.HELPFUL_DUCK);
					fulfillHand.remove(Ingredient.HELPFUL_DUCK);
				}
			}
			this.status = CustomerOrderStatus.FULFILLED;
		} else {
			throw new WrongIngredientsException("The order cannot be fulfilled !");
		}

		// check if can and need garnish, if can add all garnish
		if (garnish) {
			if (canGarnish(fulfillHand)) {
				// check if there is garnish
				if (this.garnish.size() > 0) {
					this.status = CustomerOrderStatus.GARNISHED;
				}
				for (Ingredient ingredient : this.garnish) {
					if (fulfillHand.contains(ingredient)) {
						fulfillIngredients.add(ingredient);
						fulfillHand.remove(ingredient);
					} else {
						fulfillIngredients.add(Ingredient.HELPFUL_DUCK);
						fulfillHand.remove(Ingredient.HELPFUL_DUCK);
					}
				}
			}
		}

		Collections.sort(fulfillIngredients);
		return fulfillIngredients;
	}

	/**
	 * Simply return the list of garnish of the order
	 * 
	 * @return List<Ingredient> of garnish in the order
	 */
	public List<Ingredient> getGarnish() {
		return this.garnish;
	}

	/**
	 * Get a description of garnish in string form
	 * 
	 * @return String of garnishes in the CustomerOrder
	 */
	public String getGarnishDescription() {
		String return_garnish = "";

		for (int i = 0; i < this.garnish.size(); i++) {
			if (i != this.garnish.size() - 1) {
				return_garnish += this.garnish.get(i) + ", ";
 			} else {
 				return_garnish += this.garnish.get(i);
 			}
		}

		return return_garnish;
	}

	/**
	 * Get the level of the order
	 * 
	 * @return int value of the level of the order
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Gets the main ingredients of the order
	 * 
	 * @return List<Ingredient> of the CustomerOrder object
	 */
	public List<Ingredient> getRecipe() {
		return this.recipe;
	}

	/**
	 * Get a description of main ingredients in string form
	 * 
	 * @return String of main ingredients in the CustomerOrder
	 */
	public String getRecipeDescription() {
		String return_recipe = "";

		for (int i = 0; i < this.recipe.size(); i++) {
			if (i != this.recipe.size() - 1) {
				return_recipe += this.recipe.get(i) + ", ";
 			} else {
 				return_recipe += this.recipe.get(i);
 			}
		}

		return return_recipe;
	}

	/**
	 * Get the status of CustomerOrder
	 * 
	 * @return CustomerStatusOrder of the CustomerOrder
	 */
	public CustomerOrderStatus getStatus() {
		return this.status;
	}

	/**
	 * Change the status of the CustomerOrder
	 * 
	 * @param status New status of the CustomerOrder
	 */
	public void setStatus(CustomerOrderStatus status) {
		this.status = status;
	}

	/**
	 * Gets the name of the order
	 * 
	 * @return String of name of CustomerOrder
	 */
	public String toString() {
		return this.name;
	}
}