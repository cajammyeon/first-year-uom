package bakery;

import java.util.List;
import java.util.ArrayList;

/**
 * Class for Layer object, which inherit Ingredients
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class Layer extends Ingredient {

	/**
	 * List<Ingredient> which makes up the layer
	 */
	private List<Ingredient> recipe;

	/**
	 * serialVeresionUID for Serializable object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for Layer object
	 * 
	 * @param name Name of the Layer
	 * @param recipe Combination of Ingredient which makes up the Layer
	 * @throws WrongIngredientsException Thrown when the recipe given is an empty list
	 */
	public Layer(String name, List<Ingredient> recipe) throws WrongIngredientsException {
		super(name);
		this.recipe = recipe;

		// handle exception when the recipe is null
		if (recipe == null) {
			throw new WrongIngredientsException("Empty recipe was given");
		}

		// handle when the recipe is empty
		if (this.recipe.size() == 0) {
			throw new WrongIngredientsException("Empty recipe was given");
		}
	}

	/**
	 * Gets the recipe of the Layer (combination of Ingredients)
	 * 
	 * @return List<Ingredient> which makes up the Layer
	 */
	public List<Ingredient> getRecipe() {
		return this.recipe;
	}

	/**
	 * Gets the description of the layer in the from of String, including the number of items
	 * 
	 * @return String which describes the Ingredients of the Layer, sorted
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
	 * Check if the Layer can be baked with ingredients in the list
	 * 
	 * @param ingredients List<Ingredients> to be checked, is the Layer can be baked
	 * @return boolean value, true if it can be baked, false if otherwise
	 */
	public boolean canBake(List<Ingredient> ingredients) {
		List<Ingredient> ingredientsNeed = new ArrayList<Ingredient>(this.recipe);
		List<Ingredient> ingredientsHand = new ArrayList<Ingredient>(ingredients);

		// check if the ingredients needed is in the list, remove if exist
		for (Ingredient ingredient : this.recipe) {
			if(isIn(ingredient, ingredientsHand)) {
				ingredientsNeed.remove(ingredient);
				ingredientsHand.remove(ingredient);
			}
		}

		// if ingredients needed is empty, return true as everything was found
		// check duck if there is any ingredients left in ingredients needed
		if (ingredientsNeed.size() == 0) {
			return true;
		} else {
			if (duckCount(ingredients) < ingredientsNeed.size()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Count the number of ducks in the given list
	 * 
	 * @param ingredients List<Ingredients> to be checked
	 * @return number of Helpful Ducks in the form of integer
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
	 * Check if a particular ingredient is in the given list
	 * 
	 * @param ing The ingredients to be checked
	 * @param ingredients List<Ingredient> to be checked against
	 * @return true if in the list, false if otherwise
	 */
	private boolean isIn(Ingredient ing, List<Ingredient> ingredients) {
		for (Ingredient ingredient : ingredients) {
			if (ing.equals(ingredient)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Generate a hashcode based on the name, inherit method from parents
	 * 
	 * @return the value of the hashcode
	 */
	public int hashCode() {
		return super.hashCode();
	}
}