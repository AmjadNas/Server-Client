package View;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Client_Server {
	private static HashMap<Integer, String> ingredients = new HashMap<>();
	private static ArrayList<Thread> handleList = new ArrayList<>(Constants.NUM_OF_CLIENTS);

	public static void main(String[] args) {

		int i = 0;

		String part[] = {};

		try (BufferedReader br = new BufferedReader(new FileReader("inventory.txt"))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				part = sCurrentLine.split(" ");
				ingredients.put(Integer.parseInt(part[0]), part[1]);
			}
				while (i < Constants.NUM_OF_CLIENTS) {
					handleList.add(new Client(i));
					handleList.get(i).start();
					i++;
				}
			

		} catch (IOException  e) {
			e.printStackTrace();
		}
	}

	public static String getIngredientName(int ingredientNumber) {
		if (ingredients.containsKey(ingredientNumber)) {
			return ingredients.get(ingredientNumber);
		}
		return null;
	}

}
