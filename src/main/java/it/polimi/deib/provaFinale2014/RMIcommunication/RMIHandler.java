package it.polimi.deib.provaFinale2014.RMIcommunication;


import it.polimi.deib.provaFinale2014.controller.Action;
import it.polimi.deib.provaFinale2014.controller.Command;
import it.polimi.deib.provaFinale2014.controller.Game;
import it.polimi.deib.provaFinale2014.controller.Message;
import it.polimi.deib.provaFinale2014.controller.TurnHandler;
import it.polimi.deib.provaFinale2014.exceptions.ActionLimitExceededException;
import it.polimi.deib.provaFinale2014.exceptions.ClientOfflineException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestore della comunicazione lato client in RMI
 */
public class RMIHandler extends UnicastRemoteObject implements Runnable, RMIHandlerInterface {
	private static final long serialVersionUID = -6103118327106513602L;
	
	private List<RMIClientInfo> clients;
	private TurnHandler turnHandler;
	private Game game;
	private int activePlayerIndex;
	private static final Logger LOGGER = Logger.getLogger(RMIHandler.class.getName());
	private RMIClientInterface activeClient;
	static final int PLAYERACTIONS = 3;
	private List<String> broadcastParameters;
	private List<String> extraParametersInKill;
	private List<Boolean> statusOnline;
	private String activePlayerNickname;
	
	
	/**
	 * Cotruisce un gestore della Comunicazione RMI lato Server
	 * 
	 * @param clients
	 * @throws RemoteException
	 */
	public RMIHandler(List<RMIClientInfo> clients) throws RemoteException {
		LOGGER.setUseParentHandlers(false);
		broadcastParameters = new ArrayList<String>();
		statusOnline = new ArrayList<Boolean>();
		this.clients = new ArrayList<RMIClientInfo>();
		for (RMIClientInfo client : clients){
			int index = clients.indexOf(client);
			this.clients.add(client);
			this.statusOnline.add(true);
		try {
			client.getClientInterface().setRMIHandler(this);
			client.setPlayerIndex(index);
			client.getClientInterface().setPlayerIndexAndWelcome(index);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Active player disconnected", e);
			try {
				disconnectionTimer(activePlayerIndex);
			} catch (ClientOfflineException e1) {
				LOGGER.log(Level.WARNING, "Active player disconnected");
			}
			activeClient = clients.get(activePlayerIndex).getClientInterface();
			refreshStatus();
		}
		}
		game = new Game(this.clients.size());
		turnHandler = new TurnHandler(game);
		
	}


	
	/**
	 * Fa partire il gestore della Comunicazione RMI lato Server
	 */
	public void run() {
	
		for (RMIClientInfo client: clients) {
			if (client.isOnline()) {
				try {
					sendNicknames(client.getClientInterface());
					boardState(client.getClientInterface());
					personalInfo(client.getClientInterface());
				
				} catch (RemoteException e) {
					LOGGER.log(Level.WARNING, "Someone disconnected", e);
					try {
						disconnectionTimer(client.getPlayerIndex());
					} catch (ClientOfflineException e1) {
						LOGGER.log(Level.INFO, "Someone left the game", e1);
					}
					refreshStatus();
				}
			}
		}
		 
		
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
					activeClient = clients.get(activePlayerIndex).getClientInterface();
					activePlayerNickname = clients.get(activePlayerIndex).getNickname();
					online = true;
				} 
			}
			
			if (activePlayerIndex == 0 && !turnHandler.isInitialTurn() 
					&& !turnHandler.isMarketTurn() && !turnHandler.isSellTurn())  {
				
				game.moveWolf();
				String type = String.valueOf(game.getGameBoard().getWolf().eatRandomSheep());
				for (RMIClientInfo client : clients) {
					if (this.statusOnline.get(client.getPlayerIndex())){
						while (true) {
							try {
								
								if (turnHandler.needEndSignal()) {
									client.getClientInterface().endMarketSignal();
									personalInfo(client.getClientInterface());
								}
								client.getClientInterface().displayWolfPosition(game.getWolfPosition());
								client.getClientInterface().getEatenSheep(type);
								break;
							
							
							} catch (RemoteException e) {
								LOGGER.log(Level.WARNING, "Remote exception", e);
								try {
									disconnectionTimer(client.getPlayerIndex());
									refreshStatus();
								} catch (ClientOfflineException e1) {
									LOGGER.log(Level.WARNING, "Client offline", e);
								}
								refreshStatus();
							}
						}
					}
				}
			}
				

			
			if (online) {
				// Esegue la comunicazione
				try {
					communicate();
				} catch (ClientOfflineException e) {
					LOGGER.log(Level.INFO, "Active player disconnected", e);
					//Il client si è disconnesso, passo il suo turno di gioco
					this.broadcastParameters.clear();
				}
			}
			
			// Do il turno al prossimo giocatore
			turnHandler.changeTurn();
					
		}
		sendEndSignal();
	}
	
	private void sendNicknames (RMIClientInterface out) throws RemoteException {
		List<String> nicknames = new ArrayList<String>();
		for (int i = 0 ; i<clients.size(); i++) {
			nicknames.add(clients.get(i).getNickname());
		}
		out.setNicknames(nicknames);
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
	
	private synchronized void communicate() throws ClientOfflineException {
		broadcastParameters.clear();
		
		for (RMIClientInfo client : clients) {
			if (this.statusOnline.get(client.getPlayerIndex())){
				while (true) {
					try {
						client.getClientInterface().printPlayerTurn(activePlayerNickname);
						break;
					} catch (RemoteException e) {
						LOGGER.log(Level.INFO, "Starts disconnection timer", e);
						disconnectionTimer(client.getPlayerIndex());
						refreshStatus();
					}	
				}
			}
		}
		
		
		if (turnHandler.isInitialTurn()) {
			initialTurn();

		} else if (turnHandler.isMarketTurn()) {

			String chosenCard = new String();
			while (true) {
				personalInfo(this.activeClient);
				sendForSaleCardBroadcast();
				try {
					chosenCard = activeClient.wakeUpBuyMarket();
					if (chosenCard != null) {
						int cardIndex = Integer.parseInt(chosenCard);
							if (game.getActivePlayer().hasMoneyForBuyingCard(game.getMarket().getCard(cardIndex))) {
							broadcastParameters.clear();
							broadcastParameters.add(String.valueOf(activePlayerNickname));
							broadcastParameters.add(Command.SOLD_CARDS.toString());
							broadcastParameters.add(String.valueOf(game.getMarket().getCard(cardIndex).getTerrainType()));
							broadcastParameters.add(String.valueOf(game.getMarket().getCard(cardIndex).getCost()));
							broadcastParameters.add(clients.get(game.getMarket().getSellers().get(cardIndex)).getNickname());
							sendBroadcastMessage();
							game.buyCards(activePlayerIndex, chosenCard);
						} else
							activeClient.getError(Message.NOT_ENOUGH_MONEY);
					} else
						break;
				} catch (RemoteException e) {
					LOGGER.log(Level.INFO, "Starts disconnection timer", e);
					disconnectionTimer(activePlayerIndex);
					activeClient = clients.get(activePlayerIndex).getClientInterface();
					refreshStatus();
				}

			}

			
		} else if (turnHandler.isSellTurn()) {

			List<String[]> soldCards;
			while (true) {
				try {
					soldCards = activeClient.wakeUpSellMarket();
					break;
			
				} catch (RemoteException e) {
					LOGGER.log(Level.INFO, "Starts disconnection timer", e);
					disconnectionTimer(activePlayerIndex);
					activeClient = clients.get(activePlayerIndex).getClientInterface();
					refreshStatus();
				}
			}
			game.sellCards(soldCards,activePlayerIndex);
			
		//Se è un turno nomale	
		} else {
			
			if (game.getActivePlayer().getMainShepherd().getPosition() == null 
					|| game.getActivePlayer().getMainShepherd().getPosition() == null) {
				initialTurn();
			}
		
			if (!game.getEvolvedLambs().isEmpty()) {
				List<String> lambInfos = game.getEvolvedLambsInfos();
				for (RMIClientInfo client : clients) {
					if (this.statusOnline.get(client.getPlayerIndex())){
						while (true) {
							try {
								client.getClientInterface().getEvolvedLambs(lambInfos);
								break;
							} catch (RemoteException e) {
								LOGGER.log(Level.INFO, "Starts disconnection timer", e);
								disconnectionTimer(client.getPlayerIndex());
								refreshStatus();
							}
						}
					}
				}
			}
			

			
			game.moveBlackSheep();
			for (RMIClientInfo client : clients) {
				if (this.statusOnline.get(client.getPlayerIndex())){
					while (true) {
						try {
							client.getClientInterface().displayBlacksheepPosition(game.getBlackSheepPosition());
							break;
						} catch (RemoteException e) {
							LOGGER.log(Level.WARNING, "Starts disconnection timer", e);
							disconnectionTimer(client.getPlayerIndex());
							refreshStatus();
						}	
					}
				}
			}
			

			
			if (game.onlyTwoPlayers()) {
				int chosenShepherd ;
				while (true) {
					try {
						chosenShepherd = activeClient.wakeUpChooseShepherd();
						break;
					} catch (RemoteException e) {
						LOGGER.log(Level.INFO, "Starts disconnection timer", e);
						disconnectionTimer(activePlayerIndex);
						activeClient = clients.get(activePlayerIndex).getClientInterface();
						refreshStatus();
					}
				}
				game.getActivePlayer().setUsedShepherd(chosenShepherd);
					
				for (RMIClientInfo client : clients) {
					while (true) {
						try {
							client.getClientInterface().setShepherdNumber(chosenShepherd);
							break;
						} catch (RemoteException e) {
							LOGGER.log(Level.INFO, "Starts disconnection timer", e);
							disconnectionTimer(client.getPlayerIndex());
							refreshStatus();
						}
					}
				}
			}

			
			for (int numberAction= 0; numberAction < PLAYERACTIONS; numberAction++ ) {
				broadcastParameters.clear();
				while (true) {
					try {
						activeClient.wakeUpMyTurn();
						break;
					} catch (RemoteException e) {
						LOGGER.log(Level.INFO, "Starts disconnection timer", e);
						disconnectionTimer(activePlayerIndex);
						activeClient = clients.get(activePlayerIndex).getClientInterface();
						refreshStatus();
					}
				}
				personalInfo(activeClient);
				sendBroadcastMessage();
			}
			
		}
	}

	private void initialTurn() throws ClientOfflineException {
		
		int counter = 0;
		boolean onlyTwoPlayers = game.onlyTwoPlayers();
		do {
			while (true) {
				try {
					activeClient.wakeUpInizialTurn();
					break;
				} catch (RemoteException e) {
					LOGGER.log(Level.INFO, "Starts disconnection timer", e);
					disconnectionTimer(activePlayerIndex);
					activeClient = clients.get(activePlayerIndex).getClientInterface();
					refreshStatus();
				}
			}
			if (onlyTwoPlayers) {
				for (RMIClientInfo client : clients) {
					if (this.statusOnline.get(client.getPlayerIndex())){
						while (true) {
							try {
								client.getClientInterface().setShepherdNumber(counter);
								break;
							} catch (RemoteException e) {
								LOGGER.log(Level.INFO, "Starts disconnection timer", e);
								disconnectionTimer(client.getPlayerIndex());
								refreshStatus();
							}
						}
					}
				}
			}
			counter++;
			sendBroadcastMessage();
	} while ((counter == 0 && !onlyTwoPlayers) || (onlyTwoPlayers && counter < 2));
		
	}



	/**
	 * Ottiene le carte del giocatore
	 * @return personalCards
	 * @throws RemoteException
	 */
	public List<String> getPersonalCards(){
		return game.getPersonalCards(this.activePlayerIndex);
	}
	
	/**
	 * Ottiene i soldi del giocatore
	 * @return money
	 * @throws RemoteException
	 */
	public String getPersonalMoney(){
		return game.getPersonalMoney(this.activePlayerIndex);
	}

	/**
	 * Invia le informazioni personali al  giocatore
	 * @param client
	 * @throws RemoteException
	 */
	public void personalInfo(RMIClientInterface client) {
		int index = 0;
		for (RMIClientInfo c : clients) {
			if (c.getClientInterface().equals(client)) {
				index = c.getPlayerIndex();
				break;
			}
		}
		try {
			client.displayPersonalCards(game.getPersonalCards(index));
			client.displayPersonalMoney(game.getPersonalMoney(index));
		}catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Remote exception", e);
		}	
	}



	/**
	 * Invia le informazioni sulla plancia di gioco al gicoatore
	 * @param client
	 * @throws RemoteException
	 */
	public void boardState(RMIClientInterface client) throws RemoteException {
				client.displayRegionsState(game.getRegionState());
				client.displayBlacksheepPosition(game.getBlackSheepPosition());
				client.displayWolfPosition(game.getWolfPosition());
				String[][] roadState = game.getRoadState();
				for (int i = 0; i < roadState.length; i++) {
					if (!"false".equals(roadState[i][1])) {
						roadState[i][1] = clients.get(Integer.parseInt(roadState[i][1])).getNickname();
					}
				}
				client.displayRoadsState(roadState);
				client.displayBankCardsState(game.getBankCardState());
				client.displayBankFencesState(game.getBankFencesState());
	}

	private synchronized void sendBroadcastMessage() {
		for (RMIClientInfo client : clients){
			if (this.statusOnline.get(client.getPlayerIndex())){
				try {
					client.getClientInterface().printBroadcastMessage(broadcastParameters);
				} catch (RemoteException e) {
					LOGGER.log(Level.WARNING, "Remote exception", e);
					try {
						disconnectionTimer(client.getPlayerIndex());
						refreshStatus();
					} catch (ClientOfflineException e1) {
						LOGGER.log(Level.INFO, "Client offline", e);
					}
				}
			}
		}
	}



	private synchronized void sendEndSignal() {
		for (RMIClientInfo client : this.clients) {
			if (client.isOnline()){
				try {
					client.getClientInterface().getEndGameScores(game.calculatePoints());
				} catch (RemoteException e) {
					//se un player si disconnette alla fine del gioco lo ignoro
					LOGGER.log(Level.INFO, "Disconnection ignored", e);
				}
			}
		}
		RMIServer.removePlayers(clients);
	}




	private synchronized void obtainActivePlayerIndex() {
		activePlayerIndex = game.getPlayers().indexOf(game.getActivePlayer());
	}


	/**
	 * Ottiene le azioni disponibili
	 * @return availableActiorns, azioni disponibili
	 * @throws RemoteException
	 */
	public List<String> getAvailableActions() throws RemoteException {
		List<String> availableActions = new ArrayList<String>();
		try {
			for (Action a :turnHandler.getActualTurn().availableActions(game.getActivePlayer(), game.getGameBank())) {
				availableActions.add(a.toString());
			}
		} catch (ActionLimitExceededException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return availableActions;
	}


	/**
	 * Muove un pastore in una strada
	 * @param roadIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	public Message moveShepherd(int roadIndex) throws RemoteException {
		return game.moveShepherd(roadIndex);
	}

	/**
	 * Muove un certo tipo di ovino da una regione a opposta alla posizione del pastore
	 * @param parameters
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	public Message moveSheep(int[] parameters) throws RemoteException {
		return game.moveSheep(parameters[0], parameters[1]);	
	}

	/**
	 * Uccide un ovino in una regione
	 * @param parameters
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	public Message kill(int[] parameters) throws RemoteException {
		extraParametersInKill = new ArrayList<String>();
		return game.kill(parameters[0], parameters[1], extraParametersInKill);

	}



	/**
	 * Accoppia una pecora e un montone in una regione
	 * @param regionIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	public Message couple(int regionIndex) throws RemoteException {
		return game.couple(regionIndex);
	}



	/**
	 * Accoppia due pecore in una regione
	 * @param regionIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	public Message coupleSheep(int regionIndex) throws RemoteException {
		return game.coupleSheeps(regionIndex);
	}

	/**
	 * Acquista la carta più economica di un certo tipo di terreno
	 * @param terrainTypeIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	public Message buyCard(int terrainTypeIndex) throws RemoteException {
		return game.buyCard(terrainTypeIndex);
	}
	/**
	 * Imposta i messaggi di Broadcast
	 * @param parameters
	 * @throws RemoteException
	 */
	public void setBroadcastMessage( List<String> parameters) {
		for (String s : parameters) {
			broadcastParameters.add(s);
		}
		//CONTROLLO PER PARAMETRI AGGIUNTIVI
		if (broadcastParameters.get(1).equals(Action.KILL.toString())) {
			// se durante l'abbattimento il player ha dovuto ripagare dei pastori,
			// invio il loro indice al Player Attivo
			if (broadcastParameters.get(4).equals(Message.SUCCESS)) {
				for (String shepherd : extraParametersInKill) {
					broadcastParameters.add(clients.get(Integer.parseInt(shepherd)).getNickname());
				}
			}
		} else if (broadcastParameters.get(1).equals(Action.MOVE_SHEEP.toString()) ) {
			int i;
			if (game.getActivePlayer().getMainShepherd().getPosition().getAdjacentRegions()[0].equals(game.getGameBoard().getRegion(Integer.parseInt(broadcastParameters.get(2))))) {
				i = Arrays.asList(game.getGameBoard().getRegions()).indexOf(game.getActivePlayer().getMainShepherd().getPosition().getAdjacentRegions()[1]);
			} else {
				i= Arrays.asList(game.getGameBoard().getRegions()).indexOf(game.getActivePlayer().getMainShepherd().getPosition().getAdjacentRegions()[0]);
			}
			broadcastParameters.add(String.valueOf(i));
		}
	}



	/**
	 * Imposta l'azione scelta
	 * @param chosenAction
	 * @throws RemoteException
	 */
	public void setMadeActions(String chosenAction) {
		turnHandler.getActualTurn().setChosenAction(Action.valueOf(chosenAction));
		
	}

	/**
	 * Posiziona un pastore in una strada
	 * @param roadIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	public Message placeShepherd(int road){
		return game.placeShepherd(road);
	}


	private synchronized void disconnectionTimer(int playerIndex) throws ClientOfflineException {
		this.clients.get(playerIndex).setOnline(false);
		this.statusOnline.set(playerIndex, false);
		
		// Aspetta per 30 secondi con controllo ogni 5 sec
		for (RMIClientInfo client: this.clients) {
			if (this.statusOnline.get(client.getPlayerIndex())) {
				try {
					client.getClientInterface().gamePaused(clients.get(playerIndex).getNickname());
				} catch (RemoteException e) {
					// gestire riconnessione --> ATTENZIONE A NON ANDARE IN LOOP INFINITO!
					LOGGER.log(Level.INFO, "Game paused", e);
				}
			}
		}
		
		LOGGER.log(Level.INFO, "Timer started");
		int counter= 0;
		do {
			try {
				Thread.sleep(5000);
				
			} catch (InterruptedException e1) {
				LOGGER.log(Level.INFO, "Timer interrupted", e1);
			}
			counter++;
			LOGGER.log(Level.INFO, counter * 5 + " seconds passed...");
		} while (!this.clients.get(playerIndex).isOnline() && counter<6);
		System.out.println("fine timer");
		
		if (!this.clients.get(playerIndex).isOnline()) {
			throw new ClientOfflineException();
		}
	}
	
	private void refreshStatus() {
		for (int i=0 ; i<clients.size(); i++) {
			//se il client è passato da offline a online
			if (clients.get(i).isOnline() && !statusOnline.get(i).booleanValue()) {
				try {
					this.statusOnline.set(i, true);
					clients.get(i).getClientInterface().setRMIHandler(this);
					sendNicknames(clients.get(i).getClientInterface());
					boardState(clients.get(i).getClientInterface());
					personalInfo(clients.get(i).getClientInterface());
					
					
				} catch (RemoteException e) {
					LOGGER.log(Level.INFO, "Remote exception ignored", e);
				}
				
			}
		}	
	}
}