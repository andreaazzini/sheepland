package it.polimi.deib.provaFinale2014.client;

import it.polimi.deib.provaFinale2014.RMIcommunication.RMIClient;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main del Gioco Sheepland, lato Client
 *
 */
public class MainClient {
	private static final String[] SOCKET = {"SOCKET", "socket", "S", "s"};
	private static final String[] RMI = {"RMI", "rmi", "R", "r"};
	
	private MainClient() {
		// Non devono esistere istanze di MainClient
	}
	
	private static final PrintStream OUT = System.out;
	private static final Logger LOGGER = Logger
			.getLogger(MainClient.class.getName());
	
	/**
	 * Esegue il client
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		boolean valid = false;
		do {
			OUT.print("Choose how to connect: (S)OCKET, (R)MI: ");
			String connection = in.next();
			if (Arrays.asList(SOCKET).contains(connection)) {
				new ClientHandler().run();
				valid = true;
			} else if (Arrays.asList(RMI).contains(connection)) {
				try {
					new RMIClient();
					valid = true;
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, "Can't connect to client", e);
				}
			}
		} while (!valid);
	}
}
