package Model;

import java.util.HashMap;
import java.util.TreeSet;

public class Inventory {

	private static volatile HashMap<Integer, Ingredient> ingredients;
	private static Inventory instance;

	public Inventory() {
		ingredients = new HashMap<>();
	}

	public static Inventory getInventory() {

		if (instance == null) {
			instance = new Inventory();
		}
		return instance;
	}

	public synchronized void addIngredient(Ingredient p) {
		ingredients.put(p.getIngredientNumber(), p);
	}

	public synchronized void removeIngredient(int ingredientNumber, int quantity) {
		Ingredient partToRemove = ingredients.get(ingredientNumber);
		partToRemove.setIngredientQuantity(partToRemove.getIngredientQuantity() - quantity);
	}

	public synchronized boolean checkIngredient(int ingredientNumber, int quantity) {
		if (ingredients.containsKey(ingredientNumber) && ingredients.get(ingredientNumber).getIngredientQuantity() >= quantity){
			return true;
		}
		return false;
	}
	
	public synchronized int amountLeft(int ingredientNumber){
		if (ingredients.containsKey(ingredientNumber)){
			return ingredients.get(ingredientNumber).getIngredientQuantity();
		}
		return 0;
	}

	public String toString() {
		TreeSet<Ingredient> sorted = new TreeSet<>(ingredients.values());
		String output = "Inventory\n------------------\n";
		for (Ingredient p : sorted) {
			output += "Ingredient: " + p.getIngredientName() + ", Quantity: " + p.getIngredientQuantity() + "\n";
		}
		return output;
	}
}
