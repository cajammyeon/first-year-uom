package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import bakery.Ingredient;
import bakery.Layer;
import bakery.CustomerOrder;

/**
 * Class providing some tools to read the .csv file from the specified path
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class CardUtils {

	/**
	 * Gets the List<Ingredient> from the specified file, line-by-line 
	 * and pass to stringToIngredients and append to the list
	 * 
	 * @param path The path used to specify the location of the file to be read
	 * @throws FileNotFoundException when the specified path does not exist
	 * @return The List<Ingredient> taken from the file
	 */
	public static List<Ingredient> readIngredientFile(String path) throws FileNotFoundException {

		List<Ingredient> return_ingredient = new ArrayList<Ingredient>();

		// read the file
		try (
			FileReader inFile = new FileReader(path);
			BufferedReader inBuff = new BufferedReader(inFile);
		) {
			// clearing the column title
			inBuff.readLine();

			String line;
			while ((line = inBuff.readLine()) != null){
				return_ingredient.addAll(stringToIngredients(line));
			}
			
			inFile.close();
			inBuff.close();
		} catch (IOException e) {
			throw new FileNotFoundException();
		}

		return return_ingredient;
	}

	/**
	 * Read the file, line-by-line and pass to stringToIngredients and append to the list
	 * 
	 * @param str String taken from a csv, categories are separated by "," and items are separated by ";"
	 * @return Returns a list of ingredients instantiated from the string
	 */
	private static List<Ingredient> stringToIngredients(String str) {

		List<Ingredient> return_ingredient = new ArrayList<Ingredient>();

		// 0 = name, 1 = count
		String[] parts = str.split(",", 2);
		Ingredient ref_ingredient = new Ingredient(parts[0].trim());
		String ingredientCount = parts[1].trim();

		for (int i = 0; i < Integer.parseInt(ingredientCount) ; i++) {
			return_ingredient.add(ref_ingredient);
		}

		return return_ingredient;
	}

	/**
	 * Convert the string to a list of ingredients
	 * @param path - the path used to specify the location of the file to be read
	 * @throws FileNotFoundException when the specified path does not exist
	 * @return Returns a list of layers taken from the file
	 */
	public static List<Layer> readLayerFile(String path) throws FileNotFoundException {
		List<Layer> return_layer = new ArrayList<Layer>();

		// read file
		try (
			FileReader inFile = new FileReader(path);
			BufferedReader inBuff = new BufferedReader(inFile);
		) {
			// clearing the column title
			inBuff.readLine();

			String line;
			while ((line = inBuff.readLine()) != null){
				return_layer.addAll(stringToLayers(line));
			}
			
			inFile.close();
			inBuff.close();
		} catch (IOException e) {
			throw new FileNotFoundException();
		}

		return return_layer;
	}

	/**
	 * Convert the string to a list of layers
	 * @param str String taken from a csv, categories are separated by "," and items are separated by ";"
	 * @return Returns a list of layers instantiated from the string
	 */
	private static List<Layer> stringToLayers(String str) {

		List<Layer> returnLayer = new ArrayList<Layer>();
		List<Ingredient> recipe = new ArrayList<Ingredient>();

		// 0 = name, 1 = recipe
		String[] parts = str.split(",");
		String[] partsIngredient = parts[1].split(";");

		for (int i = 0; i < partsIngredient.length; i++) {
			recipe.add(new Ingredient(partsIngredient[i].trim()));
		}

		Layer newLayerToPass = new Layer(parts[0].trim(), recipe);
		for (int i = 0; i < 4; i++) {
			returnLayer.add(newLayerToPass);
		}

		return returnLayer;
	}

	/**
	 * Read the file, line-by-line and pass to stringToCustomers and append to the list
	 * @param path - the path used to specify the location of the file to be read
	 * @param layers - layers used for comparison, to make sure that the ingredients used to instantiate customers are either a layer or ingredients
	 * @throws FileNotFoundException when the specified path does not exist
	 * @return Returns a list of ingredients taken fro the file
	 */
	public static List<CustomerOrder> readCustomerFile(String path, Collection<Layer> layers) throws FileNotFoundException {

		List<CustomerOrder> return_co = new ArrayList<CustomerOrder>();

		// read file
		try (
			FileReader inFile = new FileReader(path);
			BufferedReader inBuff = new BufferedReader(inFile);
		) {
			// clearing the column title
			inBuff.readLine();

			String line;
			while ((line = inBuff.readLine()) != null) {
				return_co.add(stringToCustomerOrder(line, layers));
			}
			
			inFile.close();
			inBuff.close();
		} catch (IOException e) {
			throw new FileNotFoundException();
		}

		return return_co;
	}

	/**
	 * Convert the string to a customer
	 * @param str - string taken from a csv, categories are separated by "," and items are separated by ";"
	 * @param layers - layers used for comparison, to make sure that the ingredients used to instantiate customers are either a layer or ingredients
	 * @return Returns a customer instantiated from the string
	 */
	private static CustomerOrder stringToCustomerOrder(String str, Collection<Layer> layers) {

		// second parameter is used to see whether the the ingredient is layer or not
		String[] split = str.split(",");

		if (split.length == 0) {
			return null;
		}

		// arrangement : level, name, recipe, garnish
		// 0 = level, 1 = name, 2 = recipe, 3 = garnish
		int level = Integer.parseInt(split[0]);
		String name = split[1].trim().trim().replaceAll("\t", "").replaceAll("\n", "").replaceAll("\r", "").replaceAll("\f", "").replaceAll("\u00A0", "");
		
		// change garnish to List
		List<Ingredient> garnish_array = new ArrayList<Ingredient>();
		if(split.length == 4) {

			String[] garnish = split[3].split(";");
			
			for (int i = 0; i < garnish.length; i++) {
				if(isItLayerOrNot(garnish[i], layers) != null) {
					Layer newL = new Layer(garnish[i].trim(), isItLayerOrNot(garnish[i], layers));
					garnish_array.add(newL);
				} else {
					garnish_array.add(new Ingredient(garnish[i].trim()));
				}
			}
		}

		// change recipe to List
		String[] recipe = split[2].split(";");
		List<Ingredient> recipe_array = new ArrayList<Ingredient>();
		for (int i = 0; i < recipe.length; i++) {
			if(isItLayerOrNot(recipe[i], layers) != null) {
				Layer newL = new Layer(recipe[i].trim(), isItLayerOrNot(recipe[i], layers));
				recipe_array.add(newL);
			} else {
				recipe_array.add(new Ingredient(recipe[i].trim()));
			}
		}		

		String nameTrim = name.trim();
		return new CustomerOrder(nameTrim, recipe_array, garnish_array, level);
	}

	/**
	 * Determine whether the ingredient in a layer or just an ingredient, to be passed as parameters in instantiating customer
	 * @param str - the name of an ingredient
	 * @param layers - list of layers available in the game
	 * @return a list of ingredient, separated form layers
	 */
	private static List<Ingredient> isItLayerOrNot(String str, Collection<Layer> layers) {

		for (Layer i : layers) {
			if((i.toString()).equals(str)) {return i.getRecipe();}
		}

		return null;
	}

	private CardUtils() {}
}