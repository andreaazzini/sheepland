package it.polimi.deib.provaFinale2014.communication;

import it.polimi.deib.provaFinale2014.controller.Command;
import it.polimi.deib.provaFinale2014.controller.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server principale dedicato alla gestione
 * della connessione con i client per il gioco
 * Sheepland
 */
public class SheeplandServer implements Runnable{
	/**
	 * Timer utilizzato per le scadenze nell'attesa
	 * delle connessioni
	 */
	public class TimerThread extends Thread {
		private static final int TIMEOUT = 30000;
		
		/**
		 * Fa partire il timer
		 */
		@Override
		public void run() {
			LOGGER.log(Level.INFO, "Timer started");
			try {
				Thread.sleep(TIMEOUT);
				if (gameCanStart()) {
					startNewGame(players);
				} else {
					LOGGER.log(Level.INFO, "No players available right now...");
				}
				LOGGER.log(Level.INFO, "Timer stopped");
				activeConnections = 0;
			} catch (InterruptedException e) {
				// Non fa nulla, può essere interrotto
				LOGGER.log(Level.INFO, "Timer interrupted", e);
			}
		}
	}
	
	private static final int PORT = 3000;
	private static final int PLAYERSPERGAME = 4;
	
	private static final Logger LOGGER = Logger.getLogger(SheeplandServer.class.getName());
	
	private ExecutorService executor;
	private static List<ClientInfo> playersInGame;
	private TimerThread timer;
	private int activeConnections;
	private List<ClientInfo> players;
	
	/**
	 * Costruisce un oggetto server iniziando la porta
	 * sulla quale si mette in ascolto e un array di
	 * socket dei client cui accetterà la connessione
	 * @param port porta d'ascolto
	 */
	public SheeplandServer() {
		playersInGame = new ArrayList<ClientInfo>();
	}
	/**
	 * Si occupa di mettere in ascolto il server e gestire
	 * le dinamiche di attesa e di accettazione dei client
	 * @throws IOException
	 */
	public void run() {
		ServerSocket serverSocket = null;
		executor = Executors.newCachedThreadPool();
		
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Server won't start", e);
		}
		
		while (true) {
			try {
				players = new ArrayList<ClientInfo>();
				for (activeConnections = 0; activeConnections < PLAYERSPERGAME;) {
					Socket socket = serverSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String nickname = in.readLine();
					boolean newPlayer = true;
					for (ClientInfo client: playersInGame) {
						if (nickname.equals(client.getNickname())) {
							if (!client.isOnline()) {
								playerBackInGame(client,socket);
								newPlayer= false;
							
							} else {
								String response = null;
								try {
									client.getOutputStream().println(Command.PING);
									response = client.getInputStream().readLine();
								} catch (IOException e1) {
									LOGGER.log(Level.INFO, "Ping unsuccessful", e1);
								}
								
								if (response == null) {
									playerBackInGame(client,socket);
								}
								
								PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
								out.println(Message.NICK_ALREADY_IN_USE);
								newPlayer = false;
							}
						}
					}
					
					if (newPlayer && !players.isEmpty()) {
						for (ClientInfo player : players) {
							if (player.getNickname().equals(nickname)) {
								newPlayer=false;
								PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
								out.println(Message.NICK_ALREADY_IN_USE);
							}
						}
						
					}
					if (newPlayer) {
						if (activeConnections == 0) {
							startNewTimer();
						}
						ClientInfo client = new ClientInfo(nickname, socket);
						players.add(client);
						activeConnections++;
					}
				}
				
				if (timer.isAlive()) {
					timer.interrupt();
					startNewGame(players);
				}
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Can't estabilish connection", e);
				break;
			}
		}
		executor.shutdown();
		try {
			// Chiude la comunicazione lato server
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Can't close server socket", e);
		}
	}
	/**
	 * Inizializza un ClientServerHandler, e quindi
	 * una nuova partita. Infine reinizializza l'array di socket.
	 * @param executor pool di thread eseguiti
	 * @return 
	 */
	private synchronized void startNewGame(List<ClientInfo> players) {
		ClientServerHandler clientServerHandler = new ClientServerHandler(players);
		for (ClientInfo player : players) {
			playersInGame.add(player);
		}
		executor.submit(clientServerHandler);
	}
	/**
	 * Reinizializza il timer
	 */
	private void startNewTimer() {
		timer = new TimerThread();
		timer.start();
	}
	
	/**
	 * Restituisce un valore di verità se è presente
	 * più di un socket connesso
	 * @return true, se più di un socket è connesso; false, altrimenti
	 */
	private boolean gameCanStart() {
		return activeConnections != 1;
	}
	
	/**
	 * Rimuove i giocatori dalla lista dei giocatori attivi
	 * @param players giocatori da rimuovere
	 */
	public static void removePlayers(List<ClientInfo> players) {
		for (ClientInfo player : players ) {
			playersInGame.remove(player);
		}
	}
	
	private void playerBackInGame(ClientInfo client, Socket socket) throws IOException {
		client.setNewSocket(socket);
		client.setOnline(true);
		client.setStreams();
		client.getOutputStream().println(client.getPlayerIndex());
	}
}
