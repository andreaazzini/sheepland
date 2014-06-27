package it.polimi.deib.provaFinale2014.communication;

import it.polimi.deib.provaFinale2014.controller.Action;
import it.polimi.deib.provaFinale2014.controller.Command;
import it.polimi.deib.provaFinale2014.controller.Game;
import it.polimi.deib.provaFinale2014.controller.Message;
import it.polimi.deib.provaFinale2014.controller.TurnHandler;
import it.polimi.deib.provaFinale2014.exceptions.ActionLimitExceededException;
import it.polimi.deib.provaFinale2014.exceptions.ClientDisconnectedException;
import it.polimi.deib.provaFinale2014.exceptions.ClientOfflineException;
import it.polimi.deib.provaFinale2014.exceptions.InvalidActionException;
import it.polimi.deib.provaFinale2014.model.DefaultBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce la logica di comunicazione tra i client e il server
 */
public class ClientServerHandler implements Runnable {
	private enum TurnChoice {
		BOARD_STATE, PLAYER_INFO, MAP, ACTION;
	}

	private static final Logger LOGGER = Logger
			.getLogger(ClientServerHandler.class.getName());
	/**
	 * Numero di azioni massime per turno che un giocatore può eseguire
	 */
	static final int PLAYERACTIONS = 3;

	// Dichiara i canali di comunicazione
	private BufferedReader in;
	private PrintWriter out;
	
	private TurnHandler turnHandler;
	private Game game;
	private int activePlayerIndex;

	private List<String> broadcastParameters;
	private List<ClientInfo> clients;
	private List<Boolean> statusOnline;
	private String activePlayerNickname;

	
	/**
	 * Costruisce un nuovo gestore di comunicazione tra client e server,
	 * inizializzando una partita dalla lista di socket connessi
	 * 
	 * @param List<ClientInfo> informazioni dei clients connessi
	 */
	public ClientServerHandler(List<ClientInfo> clients) {
		LOGGER.setUseParentHandlers(false);
		
		broadcastParameters = new ArrayList<String>();
		statusOnline = new ArrayList<Boolean>();
		this.clients = new ArrayList<ClientInfo>();
		for (ClientInfo client: clients) {
			this.clients.add(client);
			int playerIndex = this.clients.indexOf(client);
			client.setPlayerIndex(playerIndex);
			client.getOutputStream().println(playerIndex);
			statusOnline.add(true);
		}
		game = new Game(this.clients.size());
		turnHandler = new TurnHandler(game);

	}

	private synchronized void sendEndSignal() {
		for (ClientInfo client : this.clients) {
			if (client.isOnline()){
				// Invia il segnale
				client.getOutputStream().println(Command.END_GAME);
				// Invia le informazioni sul punteggio
				List<Integer> points = game.calculatePoints();
				client.getOutputStream().println(points.size());
				for (int point : points) {
					client.getOutputStream().println(point);
				}
			}
		}
		SheeplandServer.removePlayers(clients);
	}
		

	/**
	 * Dà inizio al processo di comunicazione tra client e server
	 */
	public void run() {
	
		synchronized(this) {
			for (ClientInfo client : this.clients) {
				if (client.isOnline()){
					out = client.getOutputStream();
					sendNicknames(client.getPlayerIndex());
					sendBoardState(client.getPlayerIndex());
					sendPlayerInfo(client.getPlayerIndex());
					
				}
			}
		}

		// Cicla finché la partita non è terminata

		while (turnHandler.consentTurnChanging()) {
			refreshStatus();
			if (!this.statusOnline.contains(true)) {
				break;
			}
			boolean online = false;
			// Ottiene il canale di comunicazione del giocatore attivo
			synchronized (this) {
				obtainActivePlayerIndex();
				//controllo se il player del turno è attivo o meno
				if (this.statusOnline.get(activePlayerIndex)) {
					activePlayerNickname = clients.get(activePlayerIndex).getNickname();
					in = clients.get(activePlayerIndex).getInputStream();
					out = clients.get(activePlayerIndex).getOutputStream();
					online = true;
				} 
			}
			
			if (activePlayerIndex == 0 && !turnHandler.isInitialTurn() 
					&& !turnHandler.isMarketTurn() && !turnHandler.isSellTurn()) {
				//Il lupo di muove e mangia un ovino
				game.moveWolf();
				String type = String.valueOf(game.getGameBoard().getWolf().eatRandomSheep());
				
				for (ClientInfo client : clients) {
					if (statusOnline.get(client.getPlayerIndex())) {
						if (turnHandler.needEndSignal()) {
						client.getOutputStream().println(Command.END_MARKET);
						//invio a tutti le loro personalInfo prima dopo il turno di Market
						sendPlayerInfo(client.getPlayerIndex());
						}
						client.getOutputStream().println(Command.WOLF_INFO);
						client.getOutputStream().println(game.getWolfPosition());
						client.getOutputStream().println(type);
					}
				}
			}
			
			if (online) {
			// Esegue la comunicazione
				try {
					communicate();
				} catch (ClientOfflineException e) {
					LOGGER.log(Level.INFO, "Active player left the game", e);
					//Il client si è disconnesso, passo il suo turno di gioco
					this.broadcastParameters.clear();
				}
			}

			// Do il turno al prossimo giocatore
			turnHandler.changeTurn();
		}
		sendEndSignal();
	}

	private void sendNicknames(int playerIndex) {
		PrintWriter clientOut = clients.get(playerIndex).getOutputStream();
		clientOut.println(Command.NICKNAMES);
		for (int i = 0 ; i<clients.size(); i++) {
			clientOut.println(clients.get(i).getNickname());
		}
		clientOut.println(Command.END);
		
	}

	private void sendForSaleCardBroadcast() {
		broadcastParameters.clear();
		broadcastParameters.add(Command.SELL_CARD.toString());
		for (int i = 0; i < game.getMarket().getCards().size(); i++) {
			broadcastParameters.add(String.valueOf(game.getMarket().getCards().get(i).getTerrainType()));
			broadcastParameters.add(String.valueOf(game.getMarket().getCards().get(i).getCost()));
			broadcastParameters.add(clients.get(game.getMarket().getSellers().get(i)).getNickname());
		}
		sendBroadcastMessage();
	}

	private void refreshStatus() {
		for (int i=0 ; i<clients.size(); i++) {
			//se il client è passato da offline a online
			if (clients.get(i).isOnline() && !statusOnline.get(i).booleanValue()) {
				this.statusOnline.set(i, true);
				sendNicknames(i);
				sendBoardState(i);
				sendPlayerInfo(i);
				
			}
		}
	}

	private void obtainActivePlayerIndex() {
		activePlayerIndex = game.getPlayers().indexOf(game.getActivePlayer());
	}

	private synchronized void communicate() throws ClientOfflineException {
		try {
			
			for (ClientInfo client : clients) {
				if (statusOnline.get(client.getPlayerIndex())) {
					client.getOutputStream().println(Command.TURN_OF_PLAYER);
					client.getOutputStream().println(activePlayerNickname);
				}
			}
		
			if (turnHandler.isInitialTurn()) {
				initialTurn();
			} else if (turnHandler.isMarketTurn()) {
				while (true) {
					try {
						sendPlayerInfo(this.activePlayerIndex);
						sendForSaleCardBroadcast();
						out.println(Command.SOLD_CARDS);
						String nextLine = in.readLine();
						if (nextLine != null && !nextLine.equals(Command.END.toString())) {
							int chosenCard = Integer.parseInt(nextLine);
							if (game.getActivePlayer().hasMoneyForBuyingCard(game.getMarket().getCard(chosenCard))) {
								broadcastParameters.clear();
								broadcastParameters.add(activePlayerNickname);
								broadcastParameters.add(Command.SOLD_CARDS.toString());
								broadcastParameters.add(String.valueOf(game.getMarket().getCard(chosenCard).getTerrainType()));
								broadcastParameters.add(String.valueOf(game.getMarket().getCard(chosenCard).getCost()));
								broadcastParameters.add(clients.get(game.getMarket().getSellers().get(chosenCard)).getNickname());
								sendBroadcastMessage();
								game.buyCards(activePlayerIndex, nextLine);
							} else  {
								out.println(Message.NOT_ENOUGH_MONEY);
							}
						} else if (nextLine == null) {
							disconnectionTimer(activePlayerIndex);
							out=this.clients.get(activePlayerIndex).getOutputStream();
							in=this.clients.get(activePlayerIndex).getInputStream();
							refreshStatus();
						} else {
							break;
						}
					} catch (IOException e) {
						disconnectionTimer(activePlayerIndex);
						out = this.clients.get(activePlayerIndex).getOutputStream();
						in = this.clients.get(activePlayerIndex).getInputStream();
						refreshStatus();
						LOGGER.log(Level.INFO, "Starts disconnection timer", e);
					}
				}
				
			} else if (turnHandler.isSellTurn()) {
				List<String[]> soldCards = new ArrayList<String[]>();
				while (true) {
					try {
						soldCards.clear();
						out.println(Command.SELL_CARD);
						sendPersonalCards(this.activePlayerIndex);
						String nextLine = in.readLine();
						while (nextLine != null && !nextLine.equals(Command.END.toString())) {
							String[] soldCard = new String[2];
							soldCard[0] = nextLine;
							soldCard[1] = in.readLine();
							soldCards.add(soldCard);
							nextLine = in.readLine();
						}
						if (nextLine == null) {
							disconnectionTimer(activePlayerIndex);
							out=this.clients.get(activePlayerIndex).getOutputStream();
							in=this.clients.get(activePlayerIndex).getInputStream();
							refreshStatus();
						} else {
							break;
						}
					} catch (IOException e) {
						LOGGER.log(Level.INFO, "Starts disconnection timer", e);
						disconnectionTimer(activePlayerIndex);
						out=this.clients.get(activePlayerIndex).getOutputStream();
						in=this.clients.get(activePlayerIndex).getInputStream();
						refreshStatus();
					}
				}
				
				game.sellCards(soldCards, this.activePlayerIndex);
				
				// Se è un turno normale di gioco
			} else {
				
				if (game.getActivePlayer().getMainShepherd().getPosition() == null 
						|| game.getActivePlayer().getMainShepherd().getPosition() == null) {
					initialTurn();
				}
				
				if (!game.getEvolvedLambs().isEmpty()) {
					List<String> evolvedLambsInfos = game.getEvolvedLambsInfos();
					for (ClientInfo client : clients) {
						if (statusOnline.get(client.getPlayerIndex())) {
							client.getOutputStream().println(Command.EVOLVED_LAMBS);
							for (int i=0; i<evolvedLambsInfos.size(); i=i+2) {
								client.getOutputStream().println(evolvedLambsInfos.get(i));
								client.getOutputStream().println(evolvedLambsInfos.get(i+1));
							}
							client.getOutputStream().println(Command.END);
						} 
					}
				}
				
				
				game.moveBlackSheep();
				for (ClientInfo client : clients) {
					if (statusOnline.get(client.getPlayerIndex())) {
						client.getOutputStream().println(Command.BLACKSHEEP_INFO);
						client.getOutputStream().println(game.getBlackSheepPosition());
					}
				}
				
				
				/*
				 * Se la partita è composta unicamente da due giocatori,
				 * chiede al giocatore di scegliere tra i due pastori
				 */
				if (game.onlyTwoPlayers()) {
					String chosenShepherd;
					while (true) {
						out.println(Command.CHOOSE_SHEPHERD);
					
						try {
							chosenShepherd = in.readLine();
							if ( chosenShepherd == null) {
								disconnectionTimer(activePlayerIndex);
								out=this.clients.get(activePlayerIndex).getOutputStream();
								in=this.clients.get(activePlayerIndex).getInputStream();
								refreshStatus();
							} else {
								break;
							}
						} catch (IOException e) {
							LOGGER.log(Level.INFO, "Starts disconnection timer", e);
							disconnectionTimer(activePlayerIndex);
							out=this.clients.get(activePlayerIndex).getOutputStream();
							in=this.clients.get(activePlayerIndex).getInputStream();
							refreshStatus();
						}
					}
					game.getActivePlayer().setUsedShepherd(Integer.parseInt(chosenShepherd));
						
					for (ClientInfo client : clients) {
						if (statusOnline.get(client.getPlayerIndex())){
							client.getOutputStream().println(Command.SHEPHERD_NUMBER);
							client.getOutputStream().println(chosenShepherd);
						}
					}
				}

				
				for (int numberAction = 0; numberAction < PLAYERACTIONS; numberAction++) {
					broadcastParameters.clear();
					broadcastParameters.add(activePlayerNickname);
					boolean action = false;
					do {
						String command;
						while (true) {
							out.println(Command.TURN_CHOICE);
							try {
								command = in.readLine();
								if (command == null) {
									disconnectionTimer(activePlayerIndex);
									out=this.clients.get(activePlayerIndex).getOutputStream();
									in=this.clients.get(activePlayerIndex).getInputStream();
									refreshStatus();
								} else {
									break;
								}
							} catch (IOException e) {
								disconnectionTimer(activePlayerIndex);
								out=this.clients.get(activePlayerIndex).getOutputStream();
								in=this.clients.get(activePlayerIndex).getInputStream();
								refreshStatus();
								LOGGER.log(Level.INFO, "Starts disconnection timer", e);
							}
						}
						
						if (command.equals(TurnChoice.BOARD_STATE.toString())) {
							sendBoardState(this.activePlayerIndex);
						} else if (command.equals(TurnChoice.PLAYER_INFO.toString())) {
							sendPlayerInfo(this.activePlayerIndex);
						} else if (command.equals(TurnChoice.MAP.toString())) {
							out.println(Command.DISPLAY_MAP);
						} else if (command.equals(TurnChoice.ACTION.toString())) {
							action = true;
							
							// Invio al client la lista di azioni disponibili
							String chosenAction;
							while (true) {
								sendAvailableActions();
								try {
									chosenAction = in.readLine();
									if (chosenAction == null) {
										disconnectionTimer(this.activePlayerIndex);
										out=this.clients.get(activePlayerIndex).getOutputStream();
										in=this.clients.get(activePlayerIndex).getInputStream();
										refreshStatus();
									} else {
										break;
									}
								} catch (IOException e) {
									LOGGER.log(Level.INFO, "Starts disconnection timer", e);
									disconnectionTimer(activePlayerIndex);
									out=this.clients.get(activePlayerIndex).getOutputStream();
									in=this.clients.get(activePlayerIndex).getInputStream();
									refreshStatus();
								}
							}
							
							turnHandler.getActualTurn().setChosenAction(
									Action.valueOf(chosenAction));
							broadcastParameters.add(chosenAction);
							boolean validAction = false;
							// Gestisce la scelta presa dall'utente
							do {
								try {
									validAction = performAction(broadcastParameters);
								} catch (InvalidActionException e) {
									LOGGER.log(Level.SEVERE, e.getMessage(), e);
								}
							} while (!validAction);
							sendPlayerInfo(this.activePlayerIndex);
							pingClients();
							sendBroadcastMessage();
						}
					} while (!action);

				}
			}
		} catch (ActionLimitExceededException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private synchronized boolean performAction(List<String> infoBroadcast)
			throws InvalidActionException, ClientOfflineException {
		// Gestisce la scelta presa dall'utente
		ChoiceHandler choiceHandler = new ChoiceHandler(
				Action.valueOf(infoBroadcast.get(1)));
		// Prova ad eseguire l'azione
		int[] parameters = null;
		try {
			parameters = choiceHandler.handleAction(in, out);
		} catch (ClientDisconnectedException e) {
			LOGGER.log(Level.WARNING, "Active player disconnected during action", e);
			disconnectionTimer(activePlayerIndex);
			out = this.clients.get(activePlayerIndex).getOutputStream();
			in = this.clients.get(activePlayerIndex).getInputStream();
		}
		Message message = game
				.executeAction(Action.valueOf(infoBroadcast.get(1)),
						parameters, infoBroadcast);
		out.println(message);

		if (message.equals(Message.NONE) || message.equals(Message.SUCCESS)
				|| message.equals(Message.FAIL)) {
			for (int i : parameters) {
				infoBroadcast.add(String.valueOf(i));
			}
			if (infoBroadcast.get(1).equals(Action.KILL.toString())) {
				// se durante l'abbattimento il player ha dovuto ripagare dei
				// pastori,
				// invio il loro nick al Player Attivo
				if (message.equals(Message.SUCCESS)) {
					out.println(Command.SHEPHERD_REWARDED);
					for (int i = 4; i < infoBroadcast.size(); i++) {
						String nick = clients.get(Integer.parseInt(infoBroadcast.get(i))).getNickname();
						infoBroadcast.set(i, nick);
						out.println(nick);
					}
					out.println(Command.END);
				}
			} else if (infoBroadcast.get(1)
					.equals(Action.MOVE_SHEEP.toString())) {
				int i;
				if (game.getActivePlayer().getMainShepherd().getPosition()
						.getAdjacentRegions()[0].equals(game.getGameBoard()
						.getRegion(Integer.parseInt(infoBroadcast.get(2))))) {
					i = Arrays
							.asList(game.getGameBoard().getRegions())
							.indexOf(
									game.getActivePlayer().getMainShepherd()
											.getPosition().getAdjacentRegions()[1]);
				} else {
					i = Arrays
							.asList(game.getGameBoard().getRegions())
							.indexOf(
									game.getActivePlayer().getMainShepherd()
											.getPosition().getAdjacentRegions()[0]);
				}
				infoBroadcast.add(String.valueOf(i));
			}
			infoBroadcast.add(String.valueOf(message));
			return true;
		}
		return false;
	}

	private synchronized void sendBoardState(int playerIndex) {
		PrintWriter playerOut = clients.get(playerIndex).getOutputStream();

		playerOut.println(Command.BOARD_STATE);

		playerOut.println(Command.DISPLAY_REGIONS_STATE);
		String[][] regionState = game.getRegionState();
		for (int i = 0; i < DefaultBoard.REGIONS; i++) {
			for (int j = 0; j < 3; j++) {
				playerOut.println(regionState[i][j]);
			}
		}
		playerOut.println(Command.END);

		playerOut.println(Command.DISPLAY_BLACK_SHEEP_POSITION);
		playerOut.println(game.getBlackSheepPosition());

		playerOut.println(Command.DISPLAY_WOLF_POSITION);
		playerOut.println(game.getWolfPosition());

		playerOut.println(Command.DISPLAY_ROADS_STATE);
		String[][] roadState = game.getRoadState();

		for (int i = 0; i < roadState.length; i++) {
			playerOut.println(roadState[i][0]);
			if ("false".equals(roadState[i][1])) {
			playerOut.println(roadState[i][1]);
			} else {
				playerOut.println(clients.get(Integer.parseInt(roadState[i][1])).getNickname()); 
			}
		}
		playerOut.println(Command.END);

		playerOut.println(Command.DISPLAY_BANKCARDS_STATE);
		String[] cardState = game.getBankCardState();
		for (String card : cardState) {
			playerOut.println(card);
		}
		playerOut.println(Command.END);

		playerOut.println(Command.DISPLAY_BANKFENCES_STATE);
		playerOut.println(game.getBankFencesState());

		playerOut.println(Command.END_BOARD_STATE);

	}

	private synchronized void sendPlayerInfo(int playerIndex) {
		PrintWriter clientOut = clients.get(playerIndex).getOutputStream();

		clientOut.println(Command.PERSONAL_INFO);

		sendPersonalCards(playerIndex);

		clientOut.println(Command.DISPLAY_MONEY);
		clientOut.println(game.getPersonalMoney(playerIndex));

		clientOut.println(Command.END_PERSONAL_INFO);

	}

	private synchronized void sendPersonalCards(int playerIndex) {
		PrintWriter out= clients.get(playerIndex).getOutputStream();
		out.println(Command.DISPLAY_PERSONAL_CARDS);
		List<String> personalCards = game.getPersonalCards(playerIndex);
		for (String s : personalCards) {
			out.println(s);
		}
		out.println(Command.END);
	}

	private synchronized void sendAvailableActions()
			throws ActionLimitExceededException {
		out.println(Command.AVAILABLE_ACTIONS);
		for (Action action : turnHandler.getActualTurn().availableActions(
				game.getActivePlayer(), game.getGameBank())) {
			out.println(action);
		}
		out.println(Command.END);
	}

	private synchronized void sendBroadcastMessage() {
		for (ClientInfo client : this.clients) {
			if (this.statusOnline.get(client.getPlayerIndex())) {
				PrintWriter outS=client.getOutputStream();
				outS.println(Command.BROADCAST);
				
				for (String s : this.broadcastParameters) {
					outS.println(s);
				}
				outS.println(Command.END);
			}
		}
	}
	
	private synchronized void disconnectionTimer(int playerIndex) throws ClientOfflineException {
		this.clients.get(playerIndex).setOnline(false);
		this.statusOnline.remove(playerIndex);
		this.statusOnline.add(playerIndex, false);
		
		// Aspetta per 30 secondi con controllo ogni 5 sec
		for (ClientInfo client: this.clients) {
			if (this.statusOnline.get(client.getPlayerIndex())) {
				client.getOutputStream().println(Message.GAME_PAUSE);
				client.getOutputStream().println(clients.get(playerIndex).getNickname());
			}
		}
		LOGGER.log(Level.INFO, "Timer started");
		int counter = 0;
		do {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				LOGGER.log(Level.INFO, "Timer interrupted", e1);
			}
			counter++;
			LOGGER.log(Level.INFO, counter * 5 + " seconds passed...");
		} while (!this.clients.get(playerIndex).isOnline() && counter<6);
		LOGGER.log(Level.INFO, "Timer ended");
		
		if (!this.clients.get(playerIndex).isOnline()) {
			throw new ClientOfflineException();
		}
	}
	
	private synchronized void pingClients() {
		for (ClientInfo client : this.clients){

			if (client.isOnline()) {
				client.getOutputStream().println(Command.PING);
				try {
					 if (client.getInputStream().readLine() == null) {
						 disconnectionTimer(client.getPlayerIndex());
					 }
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Can't read from input stream", e);
					try {
						disconnectionTimer(client.getPlayerIndex());
					} catch (ClientOfflineException e1) {
						// Se si è disconnesso non devo fare altro
						LOGGER.log(Level.INFO, "Someone left the game", e1);
					}
					
				} catch (ClientOfflineException e) {
					LOGGER.log(Level.INFO, "Client is offline", e);
				}
			
			}
		}
			
	}
	
	private void initialTurn() throws ClientOfflineException{
		String road;
		Message error;
		int counter = 0;
		boolean onlyTwoPlayers = game.onlyTwoPlayers();
		do {
			while (true) {
				out.println(Command.PLACE_SHEPHERD);
				try {
					road = in.readLine();
					if (road == null) {
						disconnectionTimer(activePlayerIndex);
						out = this.clients.get(activePlayerIndex).getOutputStream();
						in = this.clients.get(activePlayerIndex).getInputStream();
						refreshStatus();
					} else {
						break;
					}
				} catch (IOException e) {
					LOGGER.log(Level.INFO, "Starts disconnection timer", e);
					disconnectionTimer(activePlayerIndex);
				}
			}
			error = game.placeShepherd(Integer.parseInt(road));
			if (error.equals(Message.NONE)) {
				if (onlyTwoPlayers) {
					for (ClientInfo client : clients) {
						client.getOutputStream().println(Command.SHEPHERD_NUMBER);
						client.getOutputStream().println(counter);
					}
				}
				counter++;
				if (counter == 1 && onlyTwoPlayers) {
					game.getActivePlayer().setUsedShepherd(counter);
				}
				broadcastParameters.clear();
				broadcastParameters.add(String.valueOf(activePlayerNickname));
				broadcastParameters.add(Command.PLACE_SHEPHERD.toString());
				broadcastParameters.add(road);
				pingClients();
				sendBroadcastMessage();
			}
			out.println(error);
		} while ((counter == 0 && !onlyTwoPlayers) || (onlyTwoPlayers && counter < 2));
	}
	

}
