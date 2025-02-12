package bakery;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import bakery.CustomerOrder.CustomerOrderStatus;

import java.io.FileNotFoundException;
import java.io.Serializable;

import util.CardUtils;

/**
 * Class Customers which conntains all the method to include and ccontrol the CustomerOrders in the game
 * @author Ahmad Syahrul Azim Bin Ahmad Azmi
 * @version 1.0.0
 */
public class Customers implements Serializable {

	/**
	 * List of active customers in the game (controlled through null method) which maintained at size 3
	 */
	private Collection<CustomerOrder> activeCustomers;

	/**
	 * Deck of all possible CustomerOrder in the game
	 */
	private Collection<CustomerOrder> customerDeck;

	/**
	 * List of CustomerOrder which has GIVEN_UP (moved throguhout the active layer)
	 */
	private List<CustomerOrder> inactiveCustomers;

	/**
	 * Random object for reproducible shuffling
	 */
	private Random random;

	/**
	 * Unique version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for Customers class
	 * 
	 * @param deckFile Descriptor of the file location
	 * @param random Random object fo reproducible shuffling
	 * @param layers Layer objects from Layers file
	 * @param numPlayers Number of players in the game
	 * @throws FileNotFoundException when the path deckFile not found 
	 */
	public Customers(String deckFile, Random random, Collection<Layer> layers, int numPlayers) throws FileNotFoundException {

		this.inactiveCustomers = new ArrayList<CustomerOrder>();
		this.activeCustomers = new ArrayList<CustomerOrder>(Collections.nCopies(3, null));
		this.customerDeck = new Stack<CustomerOrder>();
		this.random = random;

		// handle file reading first
		try {
			initialiseCustomerDeck(deckFile, layers, numPlayers);
		} catch (FileNotFoundException e) {
			throw e;
		}
		
	}

	/**
	 * Open up space in the game and, draw one card from customerDeck and add to leftmost part of the customer layer
	 *  
	 * @return CustomerOrder object removed from the customer layer, null if none removed
	 */
	public CustomerOrder addCustomerOrder() {
		CustomerOrder passed = timePasses();
		((ArrayList<CustomerOrder>) this.activeCustomers).set(2, drawCustomer());
		
		return passed;
	}

	/**
	 * Set the rightmost CustomerOrder in the customer layer and check if it will leave when time passed
	 * 
	 * @return True if it will leave when time passed, false if otherwise
	 */
	public boolean customerWillLeaveSoon() {
		if (this.customerDeck.size() != 0 ) {
			if (size() == 3) {
				((ArrayList<CustomerOrder>) this.activeCustomers).get(0).setStatus(CustomerOrderStatus.IMPATIENT);
				return true;
			} else {
				return false;
			}
		} else {
			// set if the right most is not empty
			if (((ArrayList<CustomerOrder>) this.activeCustomers).get(0) != null) {
				if ((((ArrayList<CustomerOrder>) this.activeCustomers).get(0) != null) && (((ArrayList<CustomerOrder>) this.activeCustomers).get(1) != null)) {
					((ArrayList<CustomerOrder>) this.activeCustomers).get(0).setStatus(CustomerOrderStatus.IMPATIENT);
					return true;
				} else if ((((ArrayList<CustomerOrder>) this.activeCustomers).get(1) == null) && (((ArrayList<CustomerOrder>) this.activeCustomers).get(2) == null)) {
					((ArrayList<CustomerOrder>) this.activeCustomers).get(0).setStatus(CustomerOrderStatus.IMPATIENT);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Take the customer on the top of the customerDeck
	 * 
	 * @return Top customerOrder of the customerDeck 
	 */
	public CustomerOrder drawCustomer() {
		return ((Stack<CustomerOrder>) this.customerDeck).pop();
	}

	/**
	 * Return the customer layer
	 * 
	 * @return The Collection<CustomerOrder> in the customer layer
	 */
	public Collection<CustomerOrder> getActiveCustomers() {
		return this.activeCustomers;
	}

	/**
	 * Get the whole customerDeck
	 * 
	 * @return The Collection<CustomerOrder> of the shuffled deck
	 */
	public Collection<CustomerOrder> getCustomerDeck() {
		return this.customerDeck;
	}

	/**
	 * Check for fulfillable customer in the active customer layer
	 * 
	 * @param hand List of Ingredients to be checked against
	 * @return Collection<CustomerOrder> that can be fulfilled by the current Ingredients
	 */
	public Collection<CustomerOrder> getFulfillable(List<Ingredient> hand) {
		List<CustomerOrder> customerNeed = new ArrayList<CustomerOrder>(this.activeCustomers);
		List<CustomerOrder> customerCan = new ArrayList<CustomerOrder>();

		if (hand.size() == 0) {
			return customerCan;
		}

		customerNeed.removeIf(element -> element == null);

		if (customerNeed.size() == 0) {
			return customerCan;
		}

		for (CustomerOrder customerOrder : customerNeed) {
			if (customerNeed != null && hand != null) {
				if (customerOrder.canFulfill(hand)) {
					customerCan.add(customerOrder);
				}
			}
		}

		return customerCan;
	}

	/**
	 * Introduced for the ease of checking only
	 * 
	 * @return Collection<CustomerOrder> in the inactive list (GIVEN_UP)
	 */
	public Collection<CustomerOrder> getInactiveCustomer() {
		return this.inactiveCustomers;
	}
	
	/**
	 * Check each of the inactive customer for specific status
	 * 
	 * @param status CustomerORderStatus to be checked agains
	 * @return Collection<CustomerOrder> with specific status
	 */
	public Collection<CustomerOrder> getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus status) {
		Collection<CustomerOrder> customerOrderWithStatus = new ArrayList<CustomerOrder>();

		for (CustomerOrder customerOrder : this.inactiveCustomers) {
			if (customerOrder.getStatus().equals(status)) {
				customerOrderWithStatus.add(customerOrder);
			}
		}

		return customerOrderWithStatus;
	}
	
	/**
	 * Read the customers.csv file, shuffle, sort through different levels, introduce into the customerDeck and shuffle again
	 * 
	 * @param deckFile Path of file that will be read from
	 * @param layers List of layers in the game
	 * @param numPlayers Number of players in the game
	 */
	private void initialiseCustomerDeck(String deckFile, Collection<Layer> layers, int numPlayers) throws FileNotFoundException {
		
		List<CustomerOrder> customerList = new ArrayList<CustomerOrder>();

		// read customer file, parsed from deckFile
		try {
			customerList.addAll(CardUtils.readCustomerFile(deckFile, layers));
		} catch (FileNotFoundException e) {
			throw e;
			
		}
		
		List<CustomerOrder> lastDeck = new ArrayList<CustomerOrder>();
		
		// initial shuffling
		Collections.shuffle(customerList, this.random);

		// number of cards
		int level_one = 0;
		int level_two = 0;
		int level_three = 0;

		if (numPlayers == 2) {
			level_one += 4;
			level_two += 2;
			level_three += 1;
		} else if (numPlayers == 3 || numPlayers == 4) {
			level_one += 1;
			level_two += 2;
			level_three += 4;
		} else {
			level_one += 0;
			level_two += 1;
			level_three += 6;
		}

		// level 1 object
		lastDeck.addAll(checkLevelAndReturn(customerList, 1, level_one));
		// level 2 object
		lastDeck.addAll(checkLevelAndReturn(customerList, 2, level_two));
		// level 3 object
		lastDeck.addAll(checkLevelAndReturn(customerList, 3, level_three));
		
		// last shuffle
		Collections.shuffle(lastDeck, this.random);
		this.customerDeck.clear();
		this.customerDeck.addAll(lastDeck);
	}

	/**
	 * Introduced to sort through the list to find CustomerOrder with specific level and specific amount
	 * 
	 * @param listOfCustomers List to be sorted through
	 * @param level Level of CustomerOrder required
	 * @param amount Amount of the specified level of CustomeOrder required
	 * @return List<CustomerOrder> with a specific amount, based on specific level
	 */
	private List<CustomerOrder> checkLevelAndReturn(List<CustomerOrder> listOfCustomers, int level, int amount) {
		List<CustomerOrder> customerLevel = new ArrayList<CustomerOrder>();
		List<CustomerOrder> returnCustomer = new ArrayList<CustomerOrder>();

		// get all the levels
		for (CustomerOrder customerOrder : listOfCustomers) {
			if (customerOrder.getLevel() == level) {
				customerLevel.add(customerOrder);
			}
		}

		for (int i = 0; i < amount; i++) {
			returnCustomer.add(customerLevel.get(i));
		}

		return returnCustomer;
	}

	/**
	 * Check if the customer layer is empty
	 * 
	 * @return True if customer layer is empty, false if otherwise
	 */
	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check the rightmost CutomerOrder card in the customer layer
	 * 
	 * @return CustomerOrder on the rightmost part of the customer layer
	 */
	public CustomerOrder peek() {
		return ((ArrayList<CustomerOrder>) this.activeCustomers).get(0);
	}

	/**
	 * Remove allows an item to be replaced to null, and the item is moved to the inactive customer
	 * 
	 * @param customer The Object to be removed
	 */
	public void remove(CustomerOrder customer) {
		for (int i = 0; i < this.activeCustomers.size(); i++) {
			if (((ArrayList<CustomerOrder>) this.activeCustomers).get(i) == null) {
				continue;
			} else {
				if (((ArrayList<CustomerOrder>) this.activeCustomers).get(i).equals(customer)) {
					this.inactiveCustomers.add(((ArrayList<CustomerOrder>) this.activeCustomers).get(i));
					((ArrayList<CustomerOrder>) this.activeCustomers).set(i, null);
				}
			}
		}
	}
	
	/**
	 * Count the number of active customers which are not null
	 * 
	 * @return number of not null items
	 */
	public int size() {
		int i = 0;
		for (CustomerOrder customerOrder : this.activeCustomers) {
			if (customerOrder != null) {
				i++;
			}
		}

		return i;
	}

	/**
	 * Allows spaces in customer layer to progress according to the specification as the game progresses
	 * 
	 * @return The CustomerORder removed from the customer layer as the time progresses
	 */
	public CustomerOrder timePasses() {
		
		List<CustomerOrder> customerCopy = new ArrayList<CustomerOrder>(this.activeCustomers);
		
		// divide and conquer the case, halfing every check
		if (this.customerDeck.size() != 0) {
			// if the last is empty, customerDeck to last
			if (!(((ArrayList<CustomerOrder>) this.activeCustomers).get(2) == null)) {
				// if the middle is empty, move last to the middle (last not empty)
				// customerDeck to last
				if (((ArrayList<CustomerOrder>) this.activeCustomers).get(1) == null) {
					((ArrayList<CustomerOrder>) this.activeCustomers).set(1, customerCopy.get(2));
					((ArrayList<CustomerOrder>) this.activeCustomers).set(2, null);
					customerWillLeaveSoon();
				} else {
					// if first is not empty (middle not empty and last not empty), remove the first one
					if (((ArrayList<CustomerOrder>) this.activeCustomers).get(0) != null) {
						this.inactiveCustomers.add(((ArrayList<CustomerOrder>) this.activeCustomers).get(0));
						this.inactiveCustomers.get(this.inactiveCustomers.size() - 1).setStatus(CustomerOrderStatus.GIVEN_UP);
					}
					// middle not empty and last not empty, now the first has been emptied
					// move middle to first, last to middle, customerDeck to last
					((ArrayList<CustomerOrder>) this.activeCustomers).set(0, customerCopy.get(1));
					((ArrayList<CustomerOrder>) this.activeCustomers).set(1, customerCopy.get(2));
					((ArrayList<CustomerOrder>) this.activeCustomers).set(2, null);
					customerWillLeaveSoon();
					return customerCopy.get(0);
				}	
			} 
		// divide and conquer the case, halfing every check
		} else {
			// specific condition of [1, 0, 1] since I'm lazy, condition 11
			if (
				((ArrayList<CustomerOrder>) this.activeCustomers).get(0) != null &&
				((ArrayList<CustomerOrder>) this.activeCustomers).get(1) == null &&
				((ArrayList<CustomerOrder>) this.activeCustomers).get(2) != null
			) {
				((ArrayList<CustomerOrder>) this.activeCustomers).set(1, customerCopy.get(2));
				((ArrayList<CustomerOrder>) this.activeCustomers).set(2, null);
				customerWillLeaveSoon();
			} else {
				CustomerOrder returnCustomer = ((ArrayList<CustomerOrder>) this.activeCustomers).get(0);

				((ArrayList<CustomerOrder>) this.activeCustomers).set(0, customerCopy.get(1));
				((ArrayList<CustomerOrder>) this.activeCustomers).set(1, customerCopy.get(2));
				((ArrayList<CustomerOrder>) this.activeCustomers).set(2, null);
				customerWillLeaveSoon();

				// for some reason the removed customer is not giving up
				if (returnCustomer != null) {
					this.inactiveCustomers.add(returnCustomer);
					this.inactiveCustomers.get(this.inactiveCustomers.size() - 1).setStatus(CustomerOrderStatus.GIVEN_UP);
					customerWillLeaveSoon();
					return returnCustomer;
				}
			}
		}
		customerWillLeaveSoon();
		return null;
	}
}