package it.polimi.deib.provaFinale2014.communication;

import it.polimi.deib.provaFinale2014.RMIcommunication.RMIServer;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server principale
 */
public class MainServer {
	private static final Logger LOGGER = Logger.getLogger(MainServer.class.getName());
	private static HashMap<String, String> registeredUsers;
	/**
	 * Istanzia un SheeplandServer e un ServerRMI e li fa partire
	 * @param args
	 */
	public static void main(String[] args) {
		registeredUsers = new HashMap<String, String>();
		// Avvia il server RMI
		try {
			new Thread(new RMIServer()).start();
			LOGGER.log(Level.INFO, "RMI SERVER started");
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, "Can't find the connection!", e);
		}
		// Avvia il server socket
		new Thread(new SheeplandServer()).start();
		LOGGER.log(Level.INFO, "SOCKET SERVER started");
	}
	
	protected static HashMap<String, String> getRegisteredUsers() {
		return registeredUsers;
	}
}
