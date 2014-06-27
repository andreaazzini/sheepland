package it.polimi.deib.provaFinale2014.RMIcommunication;

import it.polimi.deib.provaFinale2014.exceptions.NicknameAlreadyInUseException;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestore della comunicazione lato server RMI
 */
public class RMIServer extends UnicastRemoteObject implements
		RMIServerInterface, Runnable {
	private static final long serialVersionUID = 1L;

	private class TimerThread extends Thread {
		private static final int TIMEOUT = 36000;

		/**
		 * Fa partire il timer
		 */
		@Override
		public void run() {
			LOGGER.log(Level.INFO, "Timer started");
			try {
				Thread.sleep(TIMEOUT);
				if (players.size() > 1) {
					startNewGame();
				} else {
					LOGGER.log(Level.INFO, "No players available right now...");
				}
				LOGGER.log(Level.INFO, "Timer stopped");
				reinitializeClientsList();
			} catch (InterruptedException e) {
				// Non fa nulla, può essere interrotto
				LOGGER.log(Level.INFO, "Timer interrupted", e);
			}
		}
	}

	private static final int PLAYERSPERGAME = 4;
	private static final Logger LOGGER = Logger.getLogger(RMIServer.class
			.getName());
	private static final String remoteObjectName = "RMI_SERVER";
	private static final int PORT = 3001;
	private ExecutorService executor;
	private TimerThread timer;
	private static List<RMIClientInfo> playersInGame;
	private List<RMIClientInfo> players;

	/**
	 * Costruisce un nuovo Server
	 * @throws RemoteException
	 */
	public RMIServer() throws RemoteException {
		playersInGame =  new ArrayList<RMIClientInfo>();
		players = new ArrayList<RMIClientInfo>();
		executor = Executors.newCachedThreadPool();
	}

	/**
	 * Reinizializza il timer
	 */
	private void startTimer() {
		timer = new TimerThread();
		timer.start();
	}

	private void stopTimer() {
		timer.interrupt();
	}

	/**
	 * Connette un client al Server
	 * @param client
	 * @throws RemoteException
	 * @throws NicknameAlreadyInUseException
	 */
	public void connect(RMIClientInterface client) throws RemoteException, NicknameAlreadyInUseException {
		String nickname = client.getNickname();
		boolean newPlayer=true;
		for (RMIClientInfo player : playersInGame) {
			if (player.getNickname().equals(nickname)) {
				try {
					//se trovo un client con lo stesso nome, sia che esso sia
					// con Status online o offline, provo a fare un Ping
					player.getClientInterface().ping();
					//se il Ping ha successo, il giocatore nuovo non puo partecipare
					//con lo stesso nickname
					newPlayer=false;
					throw new NicknameAlreadyInUseException();
				} catch (RemoteException e) {
					//se il client era disconnesso, resetto la sua interfaccia con il nuovo valore
					LOGGER.log(Level.INFO, "Resetting client interface", e);
					player.setClientInterface(client);
					player.getClientInterface().setPlayerIndexAndWelcome(player.getPlayerIndex());
					player.setOnline(true);
					newPlayer=false;
				}
			}
		}
			
		if (newPlayer && !players.isEmpty()) {
			for (RMIClientInfo player : players) {
				if (player.getNickname().equals(nickname)) {
					newPlayer=false;
					throw new NicknameAlreadyInUseException();
				}
			}
			
		}
		
		if (newPlayer) {
			RMIClientInfo newClient = new RMIClientInfo(nickname, client);
			players.add(newClient);
			if (this.players.size() == 1) {
				startTimer();
			} else
				if (this.players.size() == PLAYERSPERGAME) {
					stopTimer();
					startNewGame();
				}
		}
	}

	/**
	 * Inizializza un RMIHandler, e quindi una nuova partita. Infine
	 * reinizializza la lista di Clients.
	 * 
	 * @param executor
	 *            pool di thread eseguiti
	 * @throws RemoteException
	 */
	private synchronized void startNewGame() {
		RMIHandler clientServerHandler = null;
		for (RMIClientInfo client : players) {
			playersInGame.add(client);
		}
		try {
			clientServerHandler = new RMIHandler(players);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Remote exception during initialization", e);
		}
		executor.submit(clientServerHandler);
		reinitializeClientsList();
	}

	private void reinitializeClientsList() {
		players.clear();
	}
	
	/**
	 * Fa partire il Server RMI
	 */
	public void run() {
		Registry registry;
		try {
			registry = LocateRegistry.createRegistry(PORT);
			registry.rebind(remoteObjectName, this);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Remote exception", e);
		}
	}
	
	/**
	 * Rimuove i giocatori dalla lista dei giocatori attivi
	 * @param clients giocatori da rimuovere
	 */
	public static void removePlayers(List<RMIClientInfo> clients) {
		for (RMIClientInfo player : clients) {
			playersInGame.remove(player);
		}
	}

}
