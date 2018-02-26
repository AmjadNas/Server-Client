package View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is a client representation. Each instance provides a separate
 * process
 *
 */
public class Client extends Thread {

	private HashMap<Integer, Integer> myOrder;
	private DataInputStream input;
	private DataOutputStream output;
	private int clientNum;
	private Socket connection;

	public Client(int clientNum) throws IOException {
		this.clientNum = clientNum + 1;
		myOrder = new HashMap<>();
		connection = new Socket("127.0.0.1", 9999);
		input = new DataInputStream(connection.getInputStream());
		output = new DataOutputStream(connection.getOutputStream());
	}

	public void run() {
		try {
			String[] str;
			int amount = Constants.TOT_PARTS;
			int attempts = 0, iNum = 0, iCount = 0;

			while (true) {
				iCount = 0;
				while (iCount < Constants.ITEMS_IN_ORD) {
					iNum = getRandomPartNum();
					amount = Constants.TOT_PARTS;
					attempts = 0;
					while (attempts < Constants.ATTMPTS_PER_ITEM) {
						amount = getRandomQuantity(amount);
						str = sendRequest(iNum, amount);
						if (str[0].equals("ACK")) {
							if (myOrder.containsKey(iNum)) {
								int tmp = myOrder.get(iNum);
								myOrder.put(iNum, amount + tmp);
							} else {
								myOrder.put(iNum, amount);
								iCount++;
							}
							break;
						}
						amount = Integer.parseInt(str[3]);
						if (str[0].equals("NACK") && amount == 0) {
							break;
						}
						attempts++;
					}
				}
				if (sendBUY()) {
					sendFIN();
					break;
				} else {
					myOrder.clear();

				}
			}

			printOrder();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int getRandomPartNum() {
		return new Random().nextInt(Constants.TOT_PARTS) + 1;
	}

	private int getRandomQuantity(int totalAmount) {
		return ThreadLocalRandom.current().nextInt(totalAmount) + 1;
	}

	// sends a BUY REQUEST message to to server and returns a boolean response
	private synchronized boolean sendBUY() throws IOException {
		for (Map.Entry<Integer, Integer> map : myOrder.entrySet()) {
			output.writeUTF(clientNum + " ORD " + map.getKey() + " " + map.getValue());
			if (input.readUTF().split(" ")[0].equals("NACK")) {
				return false;
			}
		}
		return true;

	}

	// sends an ORD REQUEST message for a single ingredient and returns the
	// server's RESPONSE message
	private synchronized String[] sendRequest(int ingredientNumber, int quantity) throws IOException {

		final String out = clientNum + " # " + ingredientNumber + " " + quantity;

		output.writeUTF(out);
		return input.readUTF().split(" ");

	}

	// send a FIN REQUEST message to the server and closes the server socket
	private void sendFIN() {
		try {
			output.writeUTF(clientNum + " FIN 0 0");
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printOrder() {

		String output = "\nClient No.: " + clientNum + "\n---------------\n";
		for (Map.Entry<Integer, Integer> ingredient : myOrder.entrySet()) {
			output += "Ingredient: " + Client_Server.getIngredientName(ingredient.getKey()) + ", Quantity: "
					+ ingredient.getValue() + "\n";
		}
		System.out.println(output);
	}

}
