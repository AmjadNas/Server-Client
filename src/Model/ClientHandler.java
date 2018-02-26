package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**

 */
public class ClientHandler implements Runnable {

	private final String ACK = "ACK";
	private final String NACK = "NACK";
	private Socket connection;
	private DataInputStream input;
	private DataOutputStream output;
	private int clientNum;
	private final Object lock1;

	public ClientHandler(Socket socket) throws IOException {
		connection = socket;
		lock1 = new Object();
	}

	@Override
	public void run() {
		try {
			input = new DataInputStream(connection.getInputStream());
			output = new DataOutputStream(connection.getOutputStream());
			while (!handleInput().equals("FIN")) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String handleInput() throws IOException {
		synchronized (lock1) {
			String action;
			String[] str = input.readUTF().split(" ");
			int clienNum = Integer.parseInt(str[0]);
			int itemNumber = Integer.parseInt(str[2]);
			int quantity = Integer.parseInt(str[3]);
			action = str[1];

			if (action.equals("#")) {

				if (Inventory.getInventory().checkIngredient(itemNumber, quantity)) {
					Server.addItem(clienNum, itemNumber, quantity);
					output.writeUTF(ACK + " " + action + " " + itemNumber + " " + quantity);

				} else {
					quantity = Inventory.getInventory().amountLeft(itemNumber);
					output.writeUTF(NACK + " " + action + " " + itemNumber + " " + quantity);
				}
			}

			else if (action.equals("ORD")) {
				if (Inventory.getInventory().checkIngredient(itemNumber, quantity)) {
					output.writeUTF(ACK + " " + action + " " + itemNumber + " " + quantity);
					Inventory.getInventory().removeIngredient(itemNumber, quantity);
				} else {
					output.writeUTF(NACK + " " + action + " " + itemNumber + " " + quantity);
					Server.removeCart(clientNum);
				}
			}

			else if (action.equals("FIN")) {
				closeConnection();
			}
			return action;
		}
	}

	private void closeConnection() {
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
