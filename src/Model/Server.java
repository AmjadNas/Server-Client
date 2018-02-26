package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import View.Constants;

public class Server {
	private static ServerSocket serverSocket;
	/*
	 * hashmap of orders<client,order> and order is <partNum,quantity>
	 */
	private static volatile HashMap<Integer, HashMap<Integer, Integer>> clientCarts;
	private static ArrayList<Thread> handleList = new ArrayList<>(Constants.NUM_OF_CLIENTS);

	public static void main(String[] args) throws IOException {
		int  i = 0;
		
		clientCarts = new HashMap<>();
		initInventory();
		System.out.println("Inventory is initialized");
		try {
			serverSocket = new ServerSocket(9999, Constants.NUM_OF_CLIENTS);
			System.out.println("Server is running...");
			while (i < Constants.NUM_OF_CLIENTS){
				try {
					Socket cSocket = serverSocket.accept();
					handleList.add(new Thread(new ClientHandler(cSocket)));
					handleList.get(i).start();
					System.out.println("Active clients: " + (Thread.activeCount()-1));
				} catch (IOException e) {
					System.out.println("Something went wrong when starting the client handler !");
					e.printStackTrace();
				}
				i++;
			}
			i=0;
			while (i < Constants.NUM_OF_CLIENTS){
				handleList.get(i).join();
				i++;
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Something went wrong when starting the server !");
			e.printStackTrace();
		}finally {
			System.out.println("Active clients: " + (Thread.activeCount()-1));
			serverSocket.close();
			System.out.println("Server Closed !");
			System.out.println(Inventory.getInventory().toString());
		}
	}

	// adds an item to the client's cart
	public static void addItem(int clientNumber, int itemNumber, int quantity) {
		if(!clientCarts.containsKey(clientNumber)){
			clientCarts.put(clientNumber, new HashMap<>(itemNumber,quantity));
		}else if(!clientCarts.get(clientNumber).containsKey(itemNumber))
			clientCarts.get(clientNumber).put(itemNumber, quantity);
		else if(clientCarts.get(clientNumber).containsKey(itemNumber)){
			int tmp = clientCarts.get(clientNumber).get(itemNumber);
			clientCarts.get(clientNumber).put(itemNumber,quantity + tmp);
		}
	}

	// removes the client's entire cart
	public static void removeCart(int clientNumber) {
		if(clientCarts.containsKey(clientNumber))
			clientCarts.remove(clientNumber);
	}

	// gets a client's cart
	public static HashMap<Integer, Integer> getClientCart(int clientNumber) {
		if(clientCarts.containsKey(clientNumber))
			return clientCarts.get(clientNumber);
		return null;
	}

	private static void initInventory() {
		String part[] = {};

		try (BufferedReader br = new BufferedReader(new FileReader("inventory.txt"))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				part = sCurrentLine.split(" ");
				Inventory.getInventory()
						.addIngredient(new Ingredient(Integer.parseInt(part[0]), part[1], Integer.parseInt(part[2])));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
