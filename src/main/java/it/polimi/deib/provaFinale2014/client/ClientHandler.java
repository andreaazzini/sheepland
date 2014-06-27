package it.polimi.deib.provaFinale2014.client;

import it.polimi.deib.provaFinale2014.controller.Action;
import it.polimi.deib.provaFinale2014.controller.Command;
import it.polimi.deib.provaFinale2014.controller.Message;
import it.polimi.deib.provaFinale2014.gui.DynamicGUI;
import it.polimi.deib.provaFinale2014.gui.StaticGUI;
import it.polimi.deib.provaFinale2014.model.Bank;
import it.polimi.deib.provaFinale2014.model.DefaultBoard;
import it.polimi.deib.provaFinale2014.model.Sheep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce la comunicazione lato Client
 */
public class ClientHandler implements ClientCommunication {
	private enum TurnChoice {
		BOARD_STATE, PLAYER_INFO, MAP, ACTION;
	}
	enum GeneralSheepTypes {
		SHEEP, MUTTON, LAMB, BLACKSHEEP
	}
	
	private final static int PORT = 3000;
	private final static String address = "localhost";
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	private Client client;
	private	int playerIndex;
	private Object objectSyncronized = new Object();
	private String choice;
	private String road;
	private String region;
	private String sheepType;
	private String cardType;
	private Integer chosenCard;
	private Integer chosenShepherd;
	private String[] soldCard;
	private List<String[]> forSaleCards;
	private String nickname;
	private String shepherdNumber;

	/**
	 * Crea un gestore della Comunicazione lato Client
	 */
	public ClientHandler() {
		LOGGER.setUseParentHandlers(false);
		this.forSaleCards = new ArrayList<String[]>();
	}
	
	
	/**
	 * Fa partire il Client
	 */
	public void run() throws RuntimeException {
		Scanner input = new Scanner(System.in);
		boolean valid = false;
		
		do {
			System.out.println("Insert the nickname");
			nickname = input.next();
			
			System.out.print("Choose how to connect: (C)LC, (S)TATIC_GUI, (D)YNAMIC_GUI: ");
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
		
		// Inizializzo i mezzi di comunicazione
		try {
			while (true) {
				socket = new Socket(address, PORT);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				
				out.println(nickname);
				System.out.println("Searching for players to start a game with..");
				String nextLine = in.readLine();
				try {
					playerIndex = Integer.parseInt(nextLine);
					break;
				} catch (Exception e) {
					LOGGER.log(Level.INFO, "Nickname already in use", e);
					System.out.println(Message.valueOf(nextLine).getMessage());
					System.out.println("Insert a different nickname");
					nickname = input.next();
				}
			}
			if (client instanceof StaticGUI) {
				((StaticGUI) client).createAndShowGUI();
			} else if (client instanceof DynamicGUI) {
				((DynamicGUI) client).createAndShowGUI();
			}
			client.printWelcomeMessage(playerIndex);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Can't connect to server", e);
			throw new RuntimeException();
		}
		// Avvio la comunicazione
		communicate();
		// Chiudo i mezzi di comunicazione
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Can't close client socket", e);
		}
	}

	private void communicate() {
		try {
			String command = in.readLine();
			while (!command.equals(Command.END_GAME.toString())) {
				
				if (command.equals(Command.NICKNAMES.toString())) {
					List<String> playersNicknames = new ArrayList<String>();
					command = in.readLine();
					while (!command.equals(Command.END.toString())) {
						playersNicknames.add(command);
						command = in.readLine();
					}
					client.printPlayersInGame(playersNicknames);
				} else if (command.equals(Command.TURN_OF_PLAYER.toString())) {
					String playerIndex = in.readLine();
					client.printTurnOfPlayer(playerIndex);
				} else if (command.equals(Command.PING.toString())) {
					out.println("Pong");
				} else if (command.equals(Command.EVOLVED_LAMBS.toString())) {
					command = in.readLine();
					List<String> evolvedLambs = new ArrayList<String>();
					while (!command.equals(Command.END.toString())) {
						evolvedLambs.add(command);
						command = in.readLine();
					}
					client.printEvolvedLambs(evolvedLambs);
				} else if (command.equals(Command.WOLF_INFO.toString())) {
					String position = in.readLine();
					String eaten = in.readLine();
					client.printWolfPosition(position);
					client.printEatenSheep(eaten);
				} else if (command.equals(Command.BLACKSHEEP_INFO.toString())) {
					String position = in.readLine();
					client.printBlackSheepPosition(position);
				} else if (command.equals(Message.GAME_PAUSE.toString())) {
					String playerDisconnected = in.readLine();
					client.printWaitMessage(playerDisconnected);
				} else if (command.equals(Command.BOARD_STATE.toString())) {
					boardState();
				} else if (command.equals(Command.TURN_CHOICE.toString())) {
					if (client instanceof CommandLineClient) {
						String choice = makeChoice();
						out.println(choice);
					} else {
						out.println(TurnChoice.PLAYER_INFO);
						command = in.readLine();
						if (command.equals(Command.PERSONAL_INFO.toString())) {
							playerInfo();
						}
						// Lettura a vuoto (riceve TURN_CHOICE)
						in.readLine();
						out.println("ACTION");
					}
				} else if (command.equals(Command.CHOOSE_SHEPHERD.toString())) {
					chosenShepherd = null;
					client.chooseShepherd();
					synchronized (objectSyncronized) {
						while (chosenShepherd == null) {
							objectSyncronized.wait();
						}
					}
					out.println(chosenShepherd);
				} else if (command.equals(Command.PERSONAL_INFO.toString())) {
					playerInfo();
				} else if (command.equals(Command.DISPLAY_MAP.toString())){
					((CommandLineClient)client).printBoard();
				} else if (command.equals(Command.SELL_CARD.toString())) {
					sellCard();
				} else if (command.equals(Command.SOLD_CARDS.toString())) {
					buyCards();
				} else if (command.equals(Command.AVAILABLE_ACTIONS.toString())) {
					String chosenAction = chooseAction(getAvailableActions());
					out.println(chosenAction);
					// Invia i parametri in base all'azione eseguita
				} else if (command.equals(Command.PLACE_SHEPHERD.toString())) {
					road = null;
					client.insertRoad();
					synchronized (objectSyncronized) {
						while (road == null) {
							objectSyncronized.wait();
						}
					}
					out.println(road);
				} else if (command.equals(Command.INSERT_COUPLE_REGION.toString())) {
					region = null;
					client.insertRegion();
					synchronized (objectSyncronized) {
						while (region == null) {
							objectSyncronized.wait();
						}
					}
					out.println(region);
				} else if (command.equals(Command.INSERT_KILL_REGION.toString())) {
					region = null;
					client.insertRegion();
					synchronized (objectSyncronized) {
						while (region == null) {
							objectSyncronized.wait();
						}
					}
					out.println(region);
				} else if (command.equals(Command.SHEEPS_TO_KILL.toString())) {
					sheepType = null;
					client.insertSheepTypeToKill();
					synchronized (objectSyncronized) {
						while (sheepType == null) {
							objectSyncronized.wait();
						}
					}
					out.println(sheepType);
				} else if (command.equals(Command.SHEEPS_TO_MOVE.toString())) {
					sheepType = null;
					client.insertSheepTypeToMove();
					synchronized (objectSyncronized) {
						while (sheepType == null) {
							objectSyncronized.wait();
						}
					}
					out.println(sheepType);
				} else if (command.equals(Command.INSERT_STARTING_REGION.toString())) {
					region = null;
					client.insertRegion();
					synchronized (objectSyncronized) {
						while (region == null) {
							objectSyncronized.wait();
						}
					}
					out.println(region);
				} else if (command.equals(Command.INSERT_ROAD.toString())) {
					road = null;
					client.insertRoad();
					synchronized (objectSyncronized) {
						while (road == null) {
							objectSyncronized.wait();
						}
					}
					out.println(road);
				} else if (command.equals(Command.INSERT_CARD_TYPE.toString())) {
					cardType = null;
					client.insertCardType();
					synchronized (objectSyncronized) {
						while (cardType == null) {
							objectSyncronized.wait();
						}
					}
					out.println(cardType);
				} else if (command.equals(Command.SHEPHERD_REWARDED.toString())) {
					getShepherdRewarded();
				} else if (command.equals(Command.SHEPHERD_NUMBER.toString())) {
					shepherdNumber = in.readLine();
				} else if (command.equals(Command.BROADCAST.toString())) {
					broadcast();
				} else if (command.equals(Command.END_MARKET.toString())) {
					client.endMarket();
				} else {
					client.printErrorMessage(command);
				} command = in.readLine();
			}
			
			List<Integer> endGameInfos = new ArrayList<Integer>();
			int numberOfPlayers = Integer.parseInt(in.readLine());
			for (int i = 0; i < numberOfPlayers; i++) {
				endGameInfos.add(Integer.parseInt(in.readLine()));
			}
			client.printEndGame(endGameInfos);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Can't communicate with server", e);
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Can't synchronize", e);
		} 
	}
	/**
	 * Imposta il tipo di carta
	 */
	public void setCardType(String cardType) {
		synchronized (objectSyncronized) {
			this.cardType = cardType;
			objectSyncronized.notifyAll();
		}
	}
	/**
	 * Imposta il tipo di pecora
	 */
	public void setSheepType(String sheepType) {
		synchronized (objectSyncronized) {
			this.sheepType = sheepType;
			objectSyncronized.notifyAll();
		}
	}
	/**
	 * Imposta la regione
	 */
	public void setRegion(String region) {
		synchronized (objectSyncronized) {
			this.region = region;
			objectSyncronized.notifyAll();
		}
	}
	/**
	 * Imposta la strada
	 */
	public void setRoad(String road) {
		synchronized (objectSyncronized) {
			this.road = road;
			objectSyncronized.notifyAll();
		}
	}
	/**
	 * Chiede al client di rappresentare il rewarding dei pastori
	 * @throws IOException
	 */
	private void getShepherdRewarded() throws IOException {
		String nextLine = in.readLine();
		List<String> player = new ArrayList<String>();
		while (nextLine.equals(Command.END)) {
			player.add(nextLine);
			nextLine= in.readLine();
		}
		client.printRewarded(player);
	}

	
	/**
	 * Crea una Lista di Stringhe corrispondenti alle azioni che il Player può eseguire
	 * nel turno di gioco in corso
	 * @return List<String>, stringhe con le azioni possibili
	 * @throws IOException
	 */
	private List<String> getAvailableActions() throws IOException {
		List<String> availableActions = new ArrayList<String>();
		String nextAction = in.readLine();
		while (!nextAction.equals(Command.END.toString())) {
			availableActions.add(nextAction);
			nextAction = in.readLine();
		}
		return availableActions;
	}
	
	/**
	 * Metodo che gestisce la stampa delle possibili azioni
	 * e la scelta dell'utente.
	 * @param availableActions List<String> azioni disponibili nel turno
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
	 * Imposta l'azione scelta
	 */
	public void setChoice(String choice) {
		synchronized (objectSyncronized) {
			this.choice = choice;
			objectSyncronized.notifyAll();
		}
	}
	
	/**
	 * Metodo che gestisce la stampa delle possibili scelte nel turno
	 * e la scelta dell'utente.
	 * @return String scelta effettuata
	 */
	private String makeChoice() {
		String chosenAction = null;
		boolean validChoice = false;
		while (!validChoice) {
			chosenAction = ((CommandLineClient)client).makeTurnChoice();
			for (TurnChoice choice :TurnChoice.values()) {
				// Se l'azione scelta è tra le azioni di Turno viene scelta
				if (chosenAction.equals(choice.toString())) {
					validChoice = true;
					break;
				}
			}
		}
		return chosenAction;
	}
	
	private void boardState() throws IOException  {
		String nextLine = in.readLine();
		do {	
			if (nextLine.equals(Command.DISPLAY_REGIONS_STATE.toString())) {

				nextLine= in.readLine();
				String[][] message = new String[DefaultBoard.REGIONS][Sheep.SheepType.values().length];
				for (int i=0; !nextLine.equals(Command.END.toString()); i++) {
					for (int j=0; j<Sheep.SheepType.values().length; j++) {

						message[i][j] = nextLine;
						nextLine = in.readLine();
					}
				}
				client.printRegionState(message);

			} else if (nextLine.equals(Command.DISPLAY_BLACK_SHEEP_POSITION.toString())) {
				nextLine = in.readLine();
				client.printBlackSheepPosition(nextLine);
			} else if (nextLine.equals(Command.DISPLAY_WOLF_POSITION.toString())) {
				nextLine = in.readLine();
				client.printWolfPosition(nextLine);
			} else if (nextLine.equals(Command.DISPLAY_ROADS_STATE.toString())) {

				nextLine= in.readLine();

				String[][] message = new String[DefaultBoard.ROADS][2];

				for (int i=0; !nextLine.equals(Command.END.toString()); i++) {
					for (int j=0; j<2; j++) {
						message[i][j] = nextLine;
						nextLine=in.readLine();

					}
				}
				client.printRoadState(message);

			} else if (nextLine.equals(Command.DISPLAY_BANKCARDS_STATE.toString())) {
				nextLine= in.readLine();
				String[] message = new String[Bank.TYPES];
				for (int i=0; !nextLine.equals(Command.END.toString()); i++) {
					message[i] = nextLine;
					nextLine=in.readLine();
					
				}
				client.printBankCardState(message);
			} else if (nextLine.equals(Command.DISPLAY_BANKFENCES_STATE.toString())) {
				nextLine= in.readLine();
				client.printRemainingFences(nextLine);

			}
			nextLine=in.readLine();
		} while (!nextLine.equals(Command.END_BOARD_STATE.toString()));
	}

	private void playerInfo() throws IOException {
		String nextLine = in.readLine();
		do {
			if (nextLine.equals(Command.DISPLAY_PERSONAL_CARDS.toString())) {
				client.printPersonalCards(getPersonalCards());
			} else if (nextLine.equals(Command.DISPLAY_MONEY.toString())) {
				nextLine= in.readLine();
				client.printMoneyLeft(nextLine);
			}
			nextLine=in.readLine();
		} while (!nextLine.equals(Command.END_PERSONAL_INFO.toString()));
		
	}
	
	private List<String> getPersonalCards() throws IOException {
		String nextLine = in.readLine();
		List<String> message = new ArrayList<String>();
		while (!nextLine.equals(Command.END.toString())) {
			message.add(nextLine);
			nextLine = in.readLine();
		}
		return message;
	}
	
	
	private void broadcast() throws IOException {
		String nextLine = in.readLine();
		List<String> info = new ArrayList<String>();
		do {
			info.add(nextLine);
			nextLine=in.readLine();
			
		} while (!nextLine.equals(Command.END.toString()));
		
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
	
	private void buyCards() throws IOException {
		chosenCard = null;
		if (forSaleCards.isEmpty()) {
			out.println(Command.END);
		} else {
			client.chooseCard(forSaleCards.size());
			syncChooseCard();
			if (chosenCard != 0) {
				out.println(chosenCard - 1);
			} else {
				out.println(Command.END);
			}
		}
	}
	
	private void syncChooseCard() {
		synchronized (objectSyncronized) {
			while (chosenCard == null) {
				try {
					objectSyncronized.wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.SEVERE, "Cannot synchronize", e);
				}
			}
		}
	}
	
	/**
	 * Imposta la carta scelta
	 */
	public void setChosenCard(int chosenCard) {
		synchronized (objectSyncronized) {
			this.chosenCard = chosenCard;
			objectSyncronized.notifyAll();
		}
	}
	
	private void sellCard() throws IOException {
		String nextLine = in.readLine();
		if (nextLine.equals(Command.DISPLAY_PERSONAL_CARDS.toString())) {
			List<String> myCards = getPersonalCards();
			List<String[]> soldCards = new ArrayList<String[]>();
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
			if (myCards.size() == 1) {
				client.printPersonalCards(myCards);
			}
			// Per ogni carta nella lista invio tipo e prezzo
			for (String[] s : soldCards) {
				out.println(s[0]);
				out.println(s[1]);
			}
			// Infine invio il comando di fine
			out.println(Command.END);
		}
	}
	
	/**
	 * Imposta la carta venduta
	 */
	public void setSoldCard(String[] card) {
		synchronized (objectSyncronized) {
			this.soldCard = card;
			objectSyncronized.notifyAll();
		}
	}
	
	private void setForSaleAndSellers(List<String> info){
		this.forSaleCards.clear();
		for (int i=1; i<info.size()-1; i=i+3) {
			String[] card = new String[3];
			card[0] = info.get(i);
			card[1] = info.get(i+1);
			card[2] = info.get(i+2);
			this.forSaleCards.add(card.clone());

		}
	}
	
	/**
	 * Imposta il pastore scelto
	 */
	public void setChosenShepherd(int chosenShepherd) {
		synchronized (objectSyncronized) {
			this.chosenShepherd = chosenShepherd;
			objectSyncronized.notifyAll();
		}
	}

}
