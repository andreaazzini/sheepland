package it.polimi.deib.provaFinale2014.RMIcommunication;

import it.polimi.deib.provaFinale2014.client.Client;
import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.CommandLineClient;
import it.polimi.deib.provaFinale2014.controller.Action;
import it.polimi.deib.provaFinale2014.controller.Command;
import it.polimi.deib.provaFinale2014.controller.Message;
import it.polimi.deib.provaFinale2014.exceptions.NicknameAlreadyInUseException;
import it.polimi.deib.provaFinale2014.gui.DynamicGUI;
import it.polimi.deib.provaFinale2014.gui.StaticGUI;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import java.io.IOException;
import java.io.PrintStream;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestore della comunicazione RMI lato Client
 *
 */
public class RMIClient extends UnicastRemoteObject implements
		RMIClientInterface, ClientCommunication {
	private static final long serialVersionUID = 1L;

	enum GeneralSheepTypes {
		SHEEP, MUTTON, LAMB, BLACKSHEEP
	}

	private enum TurnChoice {
		PLAYER_INFO, MAP, ACTION, BOARD_STATE;
	}

	private static final Logger LOGGER = Logger.getLogger(RMIClient.class
			.getName());
	private final static String REMOTE = "RMI_SERVER";
	private final static String MYNAME = "CLIENT_RMI";
	Registry registry;
	RMIServerInterface server;
	RMIHandlerInterface clientServerHandler;
	Client client = new CommandLineClient(this);

	private Object objectSyncronized = new Object();
	private String choice;
	private String road;
	private String region;
	private String sheepType;
	private String cardType;
	private Integer chosenCard;
	private String[] soldCard;
	private int playerIndex;
	private List<String[]> forSaleCards;
	private Integer chosenShepherd;
	private String shepherdNumber;
	private String nickname;
	private String myNickname;

	private static final int PORT = 3001;
	private static final PrintStream OUT = System.out;

	/**
	 * Crea un nuovo RMIClient, inizializzando la view scelta,
	 * e si connette al server.
	 */
	public RMIClient() throws RemoteException {
		Scanner input = new Scanner(System.in);
		boolean valid = false;
		LOGGER.setUseParentHandlers(false);
		this.forSaleCards = new ArrayList<String[]>();
		do {
			OUT.println("Insert the nickname");
			nickname = input.next();
			
			OUT.print("Choose how to connect: (C)LC, (S)TATIC_GUI, (D)YNAMIC_GUI: ");
			String kindOfClient = input.next();
			
			// Chiede all'utente di scegliere un client a linea di comando
			// o GUIs
			if ("CLC".equals(kindOfClient) || "C".equals(kindOfClient) || 
					"clc".equals(kindOfClient) || "c".equals(kindOfClient)) {
				client = new CommandLineClient(this);
				valid = true;
			} else if ("STATIC_GUI".equals(kindOfClient) || "S".equals(kindOfClient) || 
					"static_gui".equals(kindOfClient) || "s".equals(kindOfClient)) {
				client = new StaticGUI(this);
				valid = true;
			} else if ("DYNAMIC_GUI".equals(kindOfClient) || "D".equals(kindOfClient) || 
					"dynamic_gui".equals(kindOfClient) || "d".equals(kindOfClient)) {
				client = new DynamicGUI(this);
				valid = true;
			}
		} while (!valid);

		try {
			registry = LocateRegistry.getRegistry(PORT);
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, "Can't locate registry on port " + PORT, e);
		}
		while (true) {
			try {
				server = (RMIServerInterface) registry.lookup(REMOTE);

				registry.rebind(MYNAME, this);
				// Connessione al SERVER RMI
				server.connect(this);
				break;
			} catch (AccessException e) {
				LOGGER.log(Level.SEVERE, "Can't lookup registry " + REMOTE, e);
			} catch (RemoteException e) {
				LOGGER.log(Level.SEVERE, "Can't connect to server", e);
			} catch (NotBoundException e) {
				LOGGER.log(Level.SEVERE, "Can't rebind registry", e);
			} catch (NicknameAlreadyInUseException e) {
				LOGGER.log(Level.INFO, "Nickname Already in use", e);
				OUT.print("Nickname Already in use. Please Insert a different nickname:");
				nickname = input.next();
			}
		}
		OUT.println("Searching for players to start a game with..");
	}

	/**
	 * Imposta il Gestore della Communicazione (lato Server) della Partita
	 * @params RMIHandlerInterface, interfaccia del gestore della comunicazione
	 * 
	 */
	public void setRMIHandler(RMIHandlerInterface clientServerHandler) {
		this.clientServerHandler = clientServerHandler;

	}
	
	/**
	 * Imposta l'indice del giocatore corrente
	 * e fa partire le GUI
	 * @params index, indice del giocatore
	 */
	public void setPlayerIndexAndWelcome (int index) {
		this.playerIndex = index;
		if (client instanceof StaticGUI) {
			((StaticGUI) client).createAndShowGUI();
		} else if (client instanceof DynamicGUI) {
			((DynamicGUI) client).createAndShowGUI();
		}
		client.printWelcomeMessage(index);
	}
	
	/**
	 * Ottiene i nicknames dei giocatori della partita.
	 * @param nicknames
	 * @throws RemoteException
	 */
	public void setNicknames (List<String> nicknames) {
		myNickname = nicknames.get(playerIndex);
		client.printPlayersInGame(nicknames);
	}
	

	/**
	 * Invia Segnale di fine Market
	 * @throws RemoteException
	 */
	public void endMarketSignal () {
		client.endMarket();
	}
	
	/**
	 * Sveglia il Client per il posizionamento dei pastori.
	 * Imposta i messaggi di broadcast lato Server
	 * @throws RemoteException
	 */
	public void wakeUpInizialTurn()  {
		List<String> infoBroadcast = new ArrayList<String>();
		Message error = null;
		infoBroadcast.clear();
		infoBroadcast.add(myNickname);
		infoBroadcast.add(Command.PLACE_SHEPHERD.toString());
		try {
			do {
				waitForRoad();
				error = clientServerHandler.placeShepherd(Integer.parseInt(road));
			
			} while (!error.equals(Message.NONE));
			infoBroadcast.add(road);
			clientServerHandler.setBroadcastMessage(infoBroadcast);	
		} catch (NumberFormatException e) {
			LOGGER.log(Level.INFO, "Invalid integer", e);
			client.println("Please Insert a Number");
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Remote exception", e);
		}
		
	}


	
	/**
	 * Sveglia il Client per l'acquisto delle carte nel turno
	 * di Market.
	 * @return chosenCard, indice della carta prescelta, null se nessuna
	 * @throws RemoteException
	 */
	public String wakeUpBuyMarket() {
		if (forSaleCards.isEmpty()) {
			return null;
		}
		chosenCard = null;
		client.chooseCard(forSaleCards.size());
		synchronized (objectSyncronized) {
			while (chosenCard == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.SEVERE, "Cannot synchronize", e);
				}
			}
		}
		
		if (chosenCard != 0) {
			return String.valueOf(chosenCard-1);
		}
			
		return null;
	
	}
	
	/**
	 * Ottiene il messaggio di errore
	 * @throws RemoteException
	 */
	public void getError(Message error) {
		client.printErrorMessage(String.valueOf(error));
	}

	/**
	 * Sveglia il Client per la vendita delle carte nel turno di Market
	 * @return chosenCards, lista di : Tipo di carta e nuovo prezzo.
	 * @throws RemoteException
	 */
	public List<String[]> wakeUpSellMarket() {
		List<String[]> soldCards = new ArrayList<String[]>();
		List<String> myCards = null;
		try {
			myCards = clientServerHandler.getPersonalCards();
		} catch (RemoteException e1) {
			LOGGER.log(Level.SEVERE, "Can't connect to server", e1);
		}
		client.println("Market is now open");

		// Riempio la lista di carte da vendere

		while (myCards.size()>1) {
			String[] noCard = new String[2];
			client.printPersonalCards(myCards);
			soldCard = noCard;
			client.sellCard();
			synchronized (objectSyncronized) {
				while (soldCard == noCard) {
					try {
						objectSyncronized.wait();
					} catch (InterruptedException e) {
						LOGGER.log(Level.SEVERE, "Cannot synchronize", e);
					}
				}
			}

			if (soldCard != null) {
				boolean found=false;
				if (myCards.get(0).equals(soldCard[0])) {
					for (int i=1; i<myCards.size() && !found; i++){
						if (myCards.get(i).equals(soldCard[0])) {
							myCards.remove(i);
							found= true;
							}
					}
					if (found) {
						soldCards.add(soldCard.clone());
					} else {
						client.println("INVALID CHOICE: You cannot sell your initial card!");
					}
				} else if (!myCards.remove(soldCard[0])) {
					client.println("INVALID CHOICE: You don't own that card");
				} else {
					soldCards.add(soldCard.clone());
				}
			} else {
				break;
			}
		}
		if (myCards.size() == 1){
			client.printPersonalCards(myCards);
		}
		return soldCards;
	}

	/**
	 * Sveglia il Client per il normale turno di gioco.
	 * Imposta i messaggi di Brodcast.
	 * @throws RemoteException
	 */
	public void wakeUpMyTurn() {

		if (client instanceof CommandLineClient) {
			boolean action;
			do {
				action = turnChoice();
			} while (!action);
		}

		List<String> availableActions;
		try {
			availableActions = clientServerHandler.getAvailableActions();

			String chosenAction = chooseAction(availableActions);
			List<String> infoBroadcast = new ArrayList<String>();
			Message error = null;
			do {
				infoBroadcast.clear();
				infoBroadcast.add(String.valueOf(myNickname));
				infoBroadcast.add(chosenAction);
				if (chosenAction.equals(Action.MOVE_SHEPHERD.toString())) {
					error = clientServerHandler
							.moveShepherd(handleMoveShepherd());	
					infoBroadcast.add(road);
				} else if (chosenAction.equals(Action.MOVE_SHEEP.toString())) {
					error = clientServerHandler.moveSheep(handleMoveSheep());
					infoBroadcast.add(region);
					infoBroadcast.add(String.valueOf(GeneralSheepTypes.valueOf(sheepType).ordinal()));
				} else if (chosenAction.equals(Action.COUPLE.toString())) {
					error = clientServerHandler.couple(handleCouple());
					infoBroadcast.add(region);
					infoBroadcast.add(error.toString());
				} else if (chosenAction.equals(Action.COUPLE_SHEEPS.toString())) {
					error = clientServerHandler.coupleSheep(handleCouple());
					infoBroadcast.add(region);
					infoBroadcast.add(error.toString());
				} else if (chosenAction.equals(Action.KILL.toString())) {
					error = clientServerHandler.kill(handleKill());
					infoBroadcast.add(region);
					infoBroadcast.add(String.valueOf(GeneralSheepTypes.valueOf(sheepType).ordinal()));
					infoBroadcast.add(error.toString());
				} else if (chosenAction.equals(Action.BUY_CARD.toString())) {
					error = clientServerHandler.buyCard(handleBuyCardFromBank());
					infoBroadcast.add(String.valueOf(TerrainType.valueOf(cardType).ordinal()));
				}

				client.printErrorMessage(error.toString());
			} while (!(error.equals(Message.NONE)
					|| error.equals(Message.SUCCESS) || error
						.equals(Message.FAIL)));

			clientServerHandler.setMadeActions(chosenAction);
			clientServerHandler.setBroadcastMessage(infoBroadcast);

		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Remote exception", e);
		}

	}

	private boolean turnChoice() {
		String choice = makeChoice();
		try {
			if (choice.equals(TurnChoice.BOARD_STATE.toString())) {
					clientServerHandler.boardState(this);
			}
			if (choice.equals(TurnChoice.MAP.toString())) {
				((CommandLineClient) client).printBoard();
			} else if (choice.equals(TurnChoice.PLAYER_INFO.toString())) {
				clientServerHandler.personalInfo(this);
			} else if (choice.equals(TurnChoice.ACTION.toString())) {
				return true;
			}
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Remote exception", e);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Can't receive infos", e);
		}
		return false;
	}

	private int handleCouple() {
		waitForRegion();
		return Integer.parseInt(region);
	}

	private int[] handleMoveSheep() {
		waitForRegion();
		waitForSheepTypeToMove();
		int[] parameters = { Integer.parseInt(region),
				GeneralSheepTypes.valueOf(sheepType).ordinal() };
		return parameters;
	}

	private int[] handleKill() {
		waitForRegion();
		waitForSheepTypeToKill();
		int[] parameters = { Integer.parseInt(region),
				GeneralSheepTypes.valueOf(sheepType).ordinal() };
		return parameters;
	}
	
	private int handleBuyCardFromBank() {
		waitForCardType();
		return TerrainType.valueOf(cardType).ordinal();
	}

	private int handleMoveShepherd() {
		waitForRoad();
		return Integer.parseInt(road);
	}

	private void waitForRoad() {
		road = null;
		client.insertRoad();
		synchronized (objectSyncronized) {
			while (road == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Waiting interrupted", e);
				}
			}
		}
	}

	private void waitForRegion() {
		region = null;
		client.insertRegion();
		synchronized (objectSyncronized) {
			while (region == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Waiting interrupted", e);
				}
			}
		}
	}

	private void waitForSheepTypeToMove() {
		sheepType = null;
		client.insertSheepTypeToMove();
		synchronized (objectSyncronized) {
			while (sheepType == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Waiting interrupted", e);
				}
			}
		}
	}
	private void waitForSheepTypeToKill() {
		sheepType = null;
		client.insertSheepTypeToKill();
		synchronized (objectSyncronized) {
			while (sheepType == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Waiting interrupted", e);
				}
			}
		}
	}

	private void waitForCardType() {
		cardType = null;
		client.insertCardType();
		synchronized (objectSyncronized) {
			while (cardType == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Waiting interrupted", e);
				}
			}
		}
	}

	/**
	 * Imposta il valore dell'attributo cardType
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String cardType tipo della carta.
	 */
	public void setCardType(String cardType) {
		synchronized (objectSyncronized) {
			this.cardType = cardType;
			objectSyncronized.notifyAll();
		}
	}

	/**
	 * Imposta il valore dell'attributo sheepType
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String sheepType tipo dell'ovino.
	 */
	public void setSheepType(String sheepType) {
		synchronized (objectSyncronized) {
			this.sheepType = sheepType;
			objectSyncronized.notifyAll();
		}
	}

	/**
	 * Imposta il valore dell'attributo region
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String region, regione scelta.
	 */
	public void setRegion(String region) {
		synchronized (objectSyncronized) {
			this.region = region;
			objectSyncronized.notifyAll();
		}
	}

	/**
	 * Imposta il valore dell'attributo road
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String road, strada scelta.
	 */
	public void setRoad(String road) {
		synchronized (objectSyncronized) {
			this.road = road;
			objectSyncronized.notifyAll();
		}
	}

	
	/**
	 * Imposta il valore dell'attributo choice
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String choice, scelta effettuata.
	 */
	public void setChoice(String choice) {
		synchronized (objectSyncronized) {
			this.choice = choice;
			objectSyncronized.notifyAll();
		}
	}
	
	
	/**
	 * Imposta il valore dell'attributo chosenShepherd
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param int chosenShepherd, indice del pastore scelto.
	 */
	public void setChosenShepherd(int chosenShepherd) {
		synchronized (objectSyncronized) {
			this.chosenShepherd = chosenShepherd;
			objectSyncronized.notifyAll();
		}
	}

	/**
	 * Metodo che gestisce la stampa delle possibili azioni e la scelta
	 * dell'utente.
	 * 
	 * @param availableActions
	 *            List<String> azioni disponibili nel turno
	 * @return String azione scelta
	 */
	private String chooseAction(List<String> availableActions) {
		boolean validChoice = false;
		while (!validChoice) {
			// Stampa le azioni disponibili
			client.printAvailableActions(availableActions);
			// Legge la risposta del client
			choice = null;
			client.makeChoice();
			synchronized (objectSyncronized) {
				while (choice == null) {
					try {
						objectSyncronized.wait();
					} catch (InterruptedException e) {
						LOGGER.log(Level.SEVERE, "Interrupted exception", e);
					}
				}

			}
			// Determina il tipo di azione
			for (String action : availableActions) {
				// Se l'azione scelta è tra le azioni di gioco viene scelta
				if (choice.equals(action)) {
					validChoice = true;
					break;
				}
			}
		}
		return choice;
	}

	/**
	 * Ottiene i messaggi di broacast
	 * @param infoBroadcast
	 * @throws RemoteException
	 */
	public void printBroadcastMessage(List<String> info) {
		
		if (client instanceof StaticGUI || client instanceof DynamicGUI 
				|| !info.get(0).equals(String.valueOf(playerIndex))) {
		
			if (info.get(0).equals(Command.SELL_CARD.toString())) {
				setForSaleAndSellers(info);
				client.printForSaleCards(forSaleCards); 
			} else if (info.get(1).equals(Action.MOVE_SHEPHERD.toString())
					|| info.get(1).equals(Command.PLACE_SHEPHERD.toString())) {
				if (shepherdNumber == null) {
					client.printMoveShepherd(info.get(0),info.get(2));
				} else {
					client.printMoveShepherd(shepherdNumber, info.get(0), info.get(2));
				}
			} else if (info.get(1).equals(Action.MOVE_SHEEP.toString())) {
				client.printMoveSheep(info.get(0),info.get(2),String.valueOf(GeneralSheepTypes.values()[Integer.parseInt(info.get(3))]) , info.get(4));
			} else if (info.get(1).equals(Action.COUPLE.toString())) {
				client.printCouple(info.get(0), info.get(2),info.get(3));
			} else if (info.get(1).equals(Action.COUPLE_SHEEPS.toString())) {
				client.printCoupleSheep(info.get(0), info.get(2),info.get(3));
			} else if (info.get(1).equals(Action.KILL.toString())) {
				client.printKill(info);
			} else if (info.get(1).equals(Action.BUY_CARD.toString())) {
				client.printBuyCard(info.get(0), info.get(2));
			} else if (info.get(1).equals(Command.SOLD_CARDS.toString()))	{
				client.printBoughtCard(info);
			}
		}
	
	}

	/**
	 * Ottiene lo stato delle regioni
	 * Chiama metodo di stampa
	 * @param regionState, Array di n = numero di regioni, 
	 * contenente un array di 3 elementi per regione,
	 * corrispondenti al numero di pecore, montoni e agnelli.
	 */
	public void displayRegionsState(String[][] regionState) {
		client.printRegionState(regionState);

	}
	
	/**
	 * Ottiene la posizione della pecora nera
	 * Chiama metodo di stampa
	 * @param position
	 */
	public void displayBlacksheepPosition(String position) {
		client.printBlackSheepPosition(position);

	}
	
	/**
	 * Ottiene la posizione del Lupo
	 * Chiama metodo di stampa
	 * @param position
	 * @throws RemoteException
	 */
	public void displayWolfPosition(String position) {
		client.printWolfPosition(position);

	}

	/**
	 * Ottiene lo stato delle Strade
	 * Chiama metodo di stampa
	 * @param roadState, array di lunghezza uguale al numero di strade,
	 * contente un array di informazioni sulla presenza di recinti e di pastori
	 * @throws RemoteException
	 */
	public void displayRoadsState(String[][] roadState) {
		client.printRoadState(roadState);

	}
	
	/**
	 * Ottiene lo stato delle carte del Banco
	 * Chiama metodo di stampa
	 * @param cardState, prezzo della carta più economica del tipo all'indice i
	 * nell'enum TerrainType
	 * @throws RemoteException
	 */
	public void displayBankCardsState(String[] cardState) {
		client.printBankCardState(cardState);

	}

	
	/**
	 * Ottiene il numero di recinti rimanenti
	 * Chiama metodo di stampa
	 * @param state, stato dei recinti
	 * @throws RemoteException
	 */
	public void displayBankFencesState(String state) {
		client.printRemainingFences(state);
	}

	
	/**
	 * Ottiene le carte possedute dal gicoatore
	 * Chiama metodo di stampa
	 * @param personalCards, lista di tipi di carte
	 * @throws RemoteException
	 */
	public void displayPersonalCards(List<String> personalCards) {
		client.printPersonalCards(personalCards);

	}

	/**
	 * Ottiene i soldi rimanenti del giocatore
	 * Chiama metodo di stampa
	 * @param money
	 * @throws RemoteException
	 */
	public void displayPersonalMoney(String money) {
		client.printMoneyLeft(money);

	}

	
	/**
	 * Imposta il valore dell'attributo chosenCard
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String chosen, numero della carta scelta.
	 */
	public void setChosenCard(int index) {
		synchronized (objectSyncronized) {
			this.chosenCard = index;
			objectSyncronized.notifyAll();
		}
	}

	/**
	 * Imposta il valore dell'attributo card
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String[] card, attributi della carta scelta : tipo di carta e prezzo di vendita.
	 */
	public void setSoldCard(String[] card) {
		synchronized (objectSyncronized) {
			this.soldCard = card;
			objectSyncronized.notifyAll();
		}
	}

	/**
	 *Gestisce la stampa delle possibili scelte nel turno e la
	 * scelta dell'utente.
	 * 
	 * @return String scelta effettuata
	 */
	private String makeChoice() {
		String chosenAction = null;
		boolean validChoice = false;
		while (!validChoice) {
			chosenAction = ((CommandLineClient) client).makeTurnChoice();
			for (TurnChoice choice : TurnChoice.values()) {
				// Se l'azione scelta è tra le azioni di Turno viene scelta
				if (chosenAction.equals(choice.toString())) {
					validChoice = true;
					break;
				}
			}
		}
		return chosenAction;
	}
	
	private void setForSaleAndSellers(List<String> info){
		forSaleCards.clear();
		for (int i = 1; i < info.size(); i += 3) {
			String[] card = new String[3];
			card[0] = info.get(i);
			card[1] = info.get(i+1);
			card[2] = info.get(i+2);
			this.forSaleCards.add(card.clone());
		}
	}


	public String getNickname()  {
		return nickname;
	}
	
	/**
	 * Ping per verifica connessione attiva
	 * @throws RemoteException
	 */
	public void ping() {
		// Non utilizzato da RMI
	}

	/**
	 * Ottiene punteggio finale
	 * @param scores
	 * @throws RemoteException
	 */
	public void getEndGameScores(List<Integer> scores) throws RemoteException {
		client.printEndGame(scores);
		
	}

	/**
	 * Ottiene il giocatore che è passato Offline, 
	 * per cui la partita è momentaneamente sospesa
	 * @param string
	 * @throws RemoteException
	 */
	public void gamePaused(String player) {
		client.printWaitMessage(player);
		
	}

	/**
	 * Imposta il pastore scelto
	 * @param chosenShepherd
	 * @throws RemoteException
	 */
	public void setShepherdNumber(int shepherdIndex) {
		this.shepherdNumber = String.valueOf(shepherdIndex);
		
	}

	/**
	 * Sveglia il Client per scegliere quale pastore utilizzare
	 * @return
	 * @throws RemoteException
	 */
	public int wakeUpChooseShepherd() {
		chosenShepherd = null;
		client.chooseShepherd();
		synchronized (objectSyncronized) {
			while (chosenShepherd == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Waiting interrupted", e);
				}
			}
		}
		return chosenShepherd;
	}

	/**
	 * Ottiene le informazioni degli agnelli che si sono evoluti
	 * @param evolvedLambs
	 * @throws RemoteException
	 */
	public void getEvolvedLambs(List<String> evolvedLambs) throws RemoteException {
		client.printEvolvedLambs(evolvedLambs);
		
	}

	/**
	 * Ottiene informazioni riguardo il tipo di ovino che è stato mangiato dal lupo
	 * @param type
	 * @throws RemoteException
	 */
	public void getEatenSheep(String type) throws RemoteException {
		client.printEatenSheep(type);
	}

	/**
	 * Ottiene il nickname del giocatore di turno
	 * Chiama metodo di stampa
	 * @param player
	 * @throws RemoteException
	 */
	public void printPlayerTurn(String player) throws RemoteException {
		client.printTurnOfPlayer(player);
		
	}
	

}

