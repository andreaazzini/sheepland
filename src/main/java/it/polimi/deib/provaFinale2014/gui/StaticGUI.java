package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.Client;
import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.controller.Message;
import it.polimi.deib.provaFinale2014.gui.AnimalGUI.AnimalType;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * GUI statica del gioco Sheepland
 */
public class StaticGUI implements Client {
	SheeplandPanel boardPanel;
	JPanel mainPanel;
	static String color;
	int playerNumber;
	TurnPanel turnPanel;
	ActionButtonPanel actionPanel;
	BankPanel bankPanel;
	static TextPanel textPanel;
	MarketPanel marketPanel;
	ClientCommunication clientCommunication;
	String wolfPosition;
	String blacksheepPosition;
	static boolean kill;
	SheeplandFrame frame;

	
	static final String BLUE = "blue";
	static final String GREEN = "green";
	static final String RED = "red";
	static final String YELLOW = "yellow";
	
	private static final String MOVED = " moved";
	private static final String PLACED = " placed his shepherd";
	private static final String PLAYER = "Player ";
	List<String> playersNicknames;

	enum GeneralSheepTypes {
		SHEEP, MUTTON, LAMB, BLACKSHEEP
	}
	
	/**
	 * Costruisce la GUI statica associandole l'handler della
	 * comunicazione lato client
	 * @param clientCommunication handler della comunicazione lato client
	 */
	public StaticGUI(ClientCommunication clientCommunication) {
		this.clientCommunication = clientCommunication;
		playersNicknames = new ArrayList<String>();
		kill = false;
	}
	/**
	 * Crea e rende visibile la plancia di gioco e il suo contenuto
	 */
	public void createAndShowGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				// Crea il frame e imposta il layout
				frame = new SheeplandFrame("Sheepland");
				frame.setLayout(new BorderLayout());
				// Crea i panel principali
				mainPanel = new JPanel();
				JPanel leftPanel = new JPanel();
				turnPanel = new TurnPanel();
				// Crea i panel secondari
				boardPanel = new SheeplandPanel(clientCommunication);
				bankPanel = new BankPanel();
				actionPanel = new ActionButtonPanel();
				textPanel = new TextPanel();
				marketPanel = new MarketPanel(clientCommunication);
				// Assegna la gui al giocatore
				turnPanel.giveTurnTo(playerNumber);
				// Imposta il layout del panel di sinistra
				leftPanel.setLayout(new FlowLayout());
				leftPanel.setPreferredSize(new Dimension(100, Default.getBoardDimension().height));
				// Aggiunge i panel contenenti i button al pannel di sinistra
				leftPanel.add(actionPanel);
				leftPanel.add(bankPanel);
				leftPanel.add(marketPanel);
				// Aggiunge al mainPanel la plancia di gioco
				mainPanel.add(boardPanel);
				// Aggiunge i panel al frame, posizionandoli opportunamente
				frame.add(leftPanel, BorderLayout.WEST);
				frame.add(mainPanel, BorderLayout.CENTER);
				frame.add(textPanel, BorderLayout.EAST);
				frame.add(turnPanel, BorderLayout.SOUTH);
				
				frame.pack();
				frame.setVisible(true);
			}
		});
    }
	
	/**
	 * Imposta l'indice del giocatore cui è associata la GUI
	 * @param playerIndex indice del giocatore
	 */
	public void setPlayerIndex(int playerIndex) {
		playerNumber = playerIndex;
	}
	/**
	 * Imposta il valore della variabile kill
	 * @param k valore da assegnare a kill
	 */
	public static void setKill(boolean k) {
		kill = k;
	}
	/**
	 * Restituisce il valore della variabile kill
	 * @return valore della variabile kill
	 */
	public static boolean getKill() {
		return kill;
	}
	/**
	 * Imposta il colore del pastore in base al playerNumber
	 */
	void setColor() {
		switch (playerNumber) {
		case 0:
			color = BLUE;
			break;
		case 1:
			color = GREEN;
			break;
		case 2:
			color = RED;
			break;
		case 3:
			color = YELLOW;
			break;
		default:
			color = null;
			break;
		}
	}
	/**
	 * Restituisce il colore associato al pastore
	 * @return colore associato al pastore
	 */
	public static String getColor() {
		return color;
	}
	/**
	 * Restituisce il colore assegnato ad un giocatore specifico
	 * @param playerIndex indice del giocatore
	 * @return colore associato al giocatore
	 */
	public static String getColor(int playerIndex) {
		String color;
		switch (playerIndex) {
		case 0:
			color = BLUE;
			break;
		case 1:
			color = GREEN;
			break;
		case 2:
			color = RED;
			break;
		case 3:
			color = YELLOW;
			break;
		default:
			color = null;
			break;
		}
		return color;
	}
	/**
	 * Restituisce l'handler della comunicazione lato client
	 * @return handler della comunicazione lato client
	 */
	public ClientCommunication getClientCommunication() {
		return clientCommunication;
	}
	/**
	 * Esegue le paint degli oggetti sulle regioni della plancia di gioco
	 */
	public void printRegionState(String[][] message) {
		final String[][] myMessage = message.clone();
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				for (int i = 0; i < myMessage.length; i++) {
					for (int j = 0; j < Integer.parseInt(myMessage[i][0]); j++) {
						boardPanel.paintAnimal(AnimalType.SHEEP, i);
					}
					for (int j = 0; j < Integer.parseInt(myMessage[i][1]); j++) {
						boardPanel.paintAnimal(AnimalType.MUTTON, i);
					}
					for (int j = 0; j < Integer.parseInt(myMessage[i][2]); j++) {
						boardPanel.paintAnimal(AnimalType.LAMB, i);
					}
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Esegue le paint degli oggetti sulle strade della plancia di gioco
	 */
	public void printRoadState(String[][] message) {
		final String[][] myMessage = message.clone();
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				for (int i = 0; i < myMessage.length; i++) {
					if (myMessage[i][0].equals(String.valueOf(true))) {
						boardPanel.paintFence(i);
					}
					if (!myMessage[i][1].equals(String.valueOf(false))) {
						boardPanel.paintShepherd(getColor(playersNicknames.indexOf(
								myMessage[i][1])), i);
					}
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Esegue la paint della pecora nera sulla plancia di gioco
	 */
	public void printBlackSheepPosition(String position) {
		final String myPosition = position;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				if (blacksheepPosition != null) {
					boardPanel.removeSheep(AnimalType.BLACKSHEEP, Integer.parseInt(blacksheepPosition));
				}
				boardPanel.paintAnimal(AnimalType.BLACKSHEEP, Integer.parseInt(myPosition));
				blacksheepPosition = myPosition;
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Esegue la paint del lupo sulla plancia di gioco
	 */
	public void printWolfPosition(String position) {
		final String myPosition = position;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				if (wolfPosition != null) {
					boardPanel.removeSheep(AnimalType.WOLF, Integer.parseInt(wolfPosition));
				}
				boardPanel.paintAnimal(AnimalType.WOLF, Integer.parseInt(myPosition));
				wolfPosition = myPosition;
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Stampa un messaggio di benvenuto
	 */
	public void printWelcomeMessage(int index) {
		final int myIndex = index;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				playerNumber = myIndex;
				setColor();
				// Rappresenta i pastori controllati dal player
				turnPanel.giveTurnTo(playerNumber);
				turnPanel.repaint();
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Stampa un messaggio d'errore
	 */
	public void printErrorMessage(String command) {
		final String myCommand = command;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				if (!"NONE".equals(myCommand)) {
					textPanel.appendText(Message.valueOf(myCommand).getMessage());
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Modifica l'informazione relativa al numero di recinti rimanenti
	 */
	public void printRemainingFences(String nextLine) {
		final String message = nextLine;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				turnPanel.getRemainingFences().setNumber(Integer.parseInt(message));
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Modifica l'informazione relativa ai danari rimanenti
	 */
	public void printMoneyLeft(String nextLine) {
		final String message = nextLine;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				turnPanel.getAvailableMoney().setNumber(Integer.parseInt(message));
				turnPanel.revalidate();
				turnPanel.repaint();
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Muove un pastore associato ad un player specifico nella strada
	 * di destinazione
	 */
	public void printMoveShepherd(String playerX, String destinationX) {
		final String player = playerX;
		final String destination = destinationX;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				int playerIndex = playersNicknames.indexOf(player);
				// Prende il pastore che si è mosso
				ShepherdGUI shepherd = boardPanel.getShepherd(getColor(playerIndex));
				
				if (shepherd != null) {
					// Aggiunge un recinto alla strada precedentemente occupata dal pastore
					int precedingOccupiedRoad = shepherd.getRoadIndex();
					boardPanel.setFreeRoad(precedingOccupiedRoad);
					// Muove il pastore nella strada di destinazione
					shepherd.move(Integer.parseInt(destination));
					// Imposta la destinazione come occupata
					boardPanel.setOccupiedRoad(shepherd.getRoadIndex());
					// Diminuisce il numero di recinti
					if (turnPanel.getRemainingFences().getNumber().getNumber() > 0) {
						turnPanel.getRemainingFences().getNumber().decreaseNumber();
					}
					// Stampa nel textPanel l'accaduto
					textPanel.appendText(PLAYER + player + MOVED);
				} else {
					boardPanel.paintShepherd(StaticGUI.getColor(playerIndex), Integer.parseInt(destination));
					
					// Rappresenta i pastori controllati dal player
					turnPanel.giveTurnTo(playerNumber);
					
					// Stampa nel textPanel l'accaduto
					textPanel.appendText(PLAYER + player + PLACED);
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Muove una pecora da una regione ad un'altra
	 */
	public void printMoveSheep(String player, String start,
		String generalSheepType, String destination) {
		final String myPlayer = player;
		final String myStart = start;
		final String mySheepType = generalSheepType;
		final String myDestination = destination;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				// Prende l'ovino che deve essere mosso
				AnimalGUI sheep = boardPanel.getSheep(mySheepType, Integer.parseInt(myStart));
				// Muove l'ovino nella strada di destinazione
				boardPanel.moveSheep(sheep, Integer.parseInt(myDestination));
				// Scrive nel textPanel l'accaduto
				textPanel.appendText(PLAYER + myPlayer + " moved a " + mySheepType);
				
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Stampa sul textPanel le informazioni su quali player sono stati
	 * pagati
	 */
	public void printRewarded(List<String> player) {
		if (player.isEmpty()) {
			textPanel.appendText("No Shepherds have been rewarded");
		} else {
			for (String p : player) {
				// Scrive l'accaduto su textPanel
				textPanel.appendText(PLAYER + p + " has been rewarded of 2 coins");
			}
		}
	}
	/**
	 * Rende disponibili i bottoni relativi alle azioni disonibili
	 */
	public void printAvailableActions(List<String> actions) {
		// Rimuove i listener dalle pecore
		boardPanel.reinitializeSheeps();
		
		for (String action : actions) {
			actionPanel.getButton(action).setEnabled(true);
		}
		
		boardPanel.repaint();
	}
	/**
	 * Imposta i numeri delle carte nel bankPanel
	 */
	public void printBankCardState(String[] message) {
		final String[] myMessage = message.clone();
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				for (int i = 0; i < myMessage.length; i++) {
					bankPanel.getCardButton(i).setNumber(Integer.parseInt(myMessage[i]));
				}
			}
		});
	}
	/**
	 * Stampa le carte personali nel turnPanel
	 */
	public void printPersonalCards(List<String> message) {
		final List<String> myMessage = message;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				turnPanel.getCardPanel().removeAll();
				turnPanel.repaint();
				turnPanel.getTypes().clear();
				for (String card : myMessage) {
					turnPanel.addCardButton(Integer.parseInt(card));
				}
				turnPanel.repaint();
			}
		});
	}
	/**
	 * Rappresenta l'avvenuto accoppiamento di due pecore
	 */
	public void printCoupleSheep(String p, String region, String output) {
		final String myP = p;;
		final String myRegion = region;
		final String myOutput = output;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				if (myOutput.equals(Message.SUCCESS.toString())) {
					// Accoppiamento riuscito
					boardPanel.paintAnimal(AnimalType.SHEEP, 
							Integer.parseInt(myRegion));
					textPanel.appendText(PLAYER + myP + 
							" coupled two sheeps. A new sheep is born!");
				} else {
					textPanel.appendText(PLAYER + myP + 
							" failed in coupling two sheeps...");
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Rapppresenta l'avvenuto accoppiamento di un montone e una pecora
	 */
	public void printCouple(String p, String region, String output) {
		final String myP = p;;
		final String myRegion = region;
		final String myOutput = output;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				if (myOutput.equals(Message.SUCCESS.toString())) {
					// Accoppiamento riuscito
					boardPanel.paintAnimal(AnimalType.LAMB, 
							Integer.parseInt(myRegion));
					textPanel.appendText(PLAYER + myP + 
							" coupled a Sheep and a Mutton. A new lamb is born!");
				} else {
					textPanel.appendText(PLAYER + myP + 
							" failed in coupling a Sheep and a Mutton...");
				}
			}
		});
	}
	/**
	 * Rappresenta l'avvenuto abbattimento di una pecora
	 */
	public void printKill(List<String> info) {
		setKill(false);
		
		final List<String> myInfo = info;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				// L'ultimo elemento della lista è il messaggio di successo o fallimento
				if (myInfo.get(myInfo.size() - 1).equals(Message.SUCCESS.toString())) {
					// Abbattimento avvenuto con successo
					boardPanel.removeSheep(AnimalType.values()[Integer.parseInt(
							myInfo.get(3))], Integer.parseInt(myInfo.get(2)));
					// Stampa l'avvenuto sul textPanel
					textPanel.appendText(PLAYER + myInfo.get(0) + " has killed a " + 
										GeneralSheepTypes.values()[Integer.parseInt(
										myInfo.get(3))]);
					// Stampa i rewarded players
					List<String> players = new ArrayList<String>();
					for (int i = 4; i < myInfo.size() - 1 ;i++) {
						players.add(myInfo.get(i));
					}
					printRewarded(players);
				} else {
					textPanel.appendText(PLAYER + myInfo.get(0) + 
							" has failed in killing a " + GeneralSheepTypes.values()[Integer.parseInt(
									myInfo.get(3))]);
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Stampa l'avvenuto acquisto di una carta
	 */
	public void printBuyCard(String p, String terrainTypeIndex) {
		final String myP = p;
		final String myTerrainTypeIndex = terrainTypeIndex;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				textPanel.appendText(PLAYER + myP + " has just bought a " + 
						TerrainType.values()[Integer.parseInt(myTerrainTypeIndex)] + 
						" card");
				
				// Aumenta il valore della carta più economica nel banco
				bankPanel.getCardButton(Integer.parseInt(myTerrainTypeIndex))
						.getNumber().increaseNumber();
				
				for (int i = 0; i < 6; i++) {
					bankPanel.getCardButton(i).setEnabled(false);
					for (ActionListener al : bankPanel.getCardButton(i)
							.getActionListeners()) {
						bankPanel.getCardButton(i).removeActionListener(al);
					}
				}
			}
		});
	}
	/**
	 * Rappresenta le carte messe in vendita dai player
	 */
	public void printForSaleCards(List<String[]> forSaleCards) {
		final List<String[]> cards = forSaleCards;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				marketPanel.getMarketCards().clear();
				marketPanel.removeAll();
				marketPanel.repaint();
				marketPanel.addEndMarketButton();
				for (String[] card : cards) {
					marketPanel.addCardButton(TerrainType.valueOf(card[0]).ordinal(), 
							Integer.parseInt(card[1]));
				}
				textPanel.appendText("There are " + cards.size() + 
							" cards you can buy");
				for (int i = 0; i < cards.size(); i++) {
					textPanel.appendText((i + 1) + ") " + cards.get(i)[0] + 
							" card, for sale for " + cards.get(i)[1] + 
							" coins (SELLER: player "+ cards.get(i)[2] + ")");
				}
				marketPanel.repaint();
			}
		});
	}
	/**
	 * Stampa un messaggio sul textPanel
	 */
	public void println(String message) {
		textPanel.appendText(message);
	}
	/**
	 * Esegue la scelta sull'azione da eseguire
	 */
	public void makeChoice() {
		// Attiva i listener degli ActionButton
		actionPanel.activateListeners(clientCommunication);
	}
	/**
	 * Inserisce la strada nella quale muovere un pastore
	 */
	public void insertRoad() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				textPanel.appendText("Choose a road to place your shepherd");
				// Attiva i listener delle roads
				boardPanel.addRoadListeners();
			}
		});
	}
	/**
	 * Attiva i listener relativi alle pecore
	 */
	public void insertRegion() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				// Attiva i listener delle sheeps
				boardPanel.addSheepListeners();
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Attiva i listener relativi alle pecore
	 */
	public void insertSheepTypeToKill() {
		clientCommunication.setSheepType(boardPanel.getSheepType());
	}
	/**
	 * Attiva i listener relativi alle pecore
	 */
	public void insertSheepTypeToMove() {
		clientCommunication.setSheepType(boardPanel.getSheepType());
	}
	/**
	 * Attiva i listener relativi alle carte del bankPanel
	 */
	public void insertCardType() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				// Attiva i listener delle cards
				bankPanel.activateCardListeners(clientCommunication, bankPanel, turnPanel);
			}
		});
	}
	/**
	 * Attiva i listener delle carte possedute dal player
	 */
	public void sellCard() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				marketPanel.addEndSellTurnButtons();
				textPanel.appendText("Choose a card you want to sell");
				turnPanel.activateCardListeners(clientCommunication, marketPanel);
			}
		});
	}
	/**
	 * Attiva i listener del market e modifica le sue carte
	 */
	public void chooseCard(int cardsNumber) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				textPanel.appendText("Choose a card you want to buy");
				marketPanel.activateCardListeners();
				marketPanel.repaint();
			}
		});
	}
	/**
	 * Stampa a video la plancia di gioco
	 * @throws IOException
	 */
	public void printBoard() throws IOException {
		// Inutilizzato dalla GUI
	}
	/**
	 * Rappresenta l'acquisto di una carta durante la fase del market
	 */
	public void printBoughtCard(List<String> info) {
		if (info.size() > 2) {
			textPanel.appendText(info.get(0) + " has bought: a " + 
					info.get(2) + " card for " + info.get(3) + 
					" coins, sold by Player " + info.get(4));
		}
	}
	/**
	 * Stampa le informazioni di fine partita
	 */
	public void printEndGame(List<Integer> endGameInfos) {
		for (int i = endGameInfos.size() - 1; i >= 0; i--) {
			textPanel.appendText(PLAYER + i + " scored " + endGameInfos.get(i) + 
					" points");
		}
		textPanel.appendText("END OF THE GAME!");
	}
	/**
	 * Stampa il messaggio di avvenuta disconnessione di un player
	 */
	public void printWaitMessage(String playerIndex) {
		textPanel.appendText(PLAYER + playerIndex + 
				" seems disconnected. Give us 30 seconds to find him!");
	}
	/**
	 * Attiva i listener dei pastori nelle partite a 2 giocatori
	 */
	public void chooseShepherd() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				textPanel.appendText("Choose a shepherd before starting your turn");
				turnPanel.giveTurnTo(playerNumber * 2, playerNumber * 2 + 1);
				// Se questo metodo viene chiamato la partita è 
				// composta da due giocatori
				turnPanel.addShepherdListeners(clientCommunication);
			}
		});
	}
	/**
	 * Stampa i messaggi di movimento di un pastore
	 */
	public void printMoveShepherd(String shepherd, String player,
			String destination) {
		final String myShepherd = shepherd;
		final String myPlayer = player;
		final String myDestination = destination;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				// Prende il pastore che si è mosso
				ShepherdGUI mainShepherd = boardPanel.getShepherd(getColor((
						playersNicknames.indexOf(myPlayer) * 2) + 
						Integer.parseInt(myShepherd)));
				
				if (mainShepherd != null) {
					// Aggiunge un recinto alla strada precedentemente 
					// occupata dal pastore
					int precedingOccupiedRoad = mainShepherd.getRoadIndex();
					boardPanel.setFreeRoad(precedingOccupiedRoad);
					// Muove il pastore nella strada di destinazione
					mainShepherd.move(Integer.parseInt(myDestination));
					// Imposta la destinazione come occupata
					boardPanel.setOccupiedRoad(mainShepherd.getRoadIndex());
					// Diminuisce il numero di recinti
					if (turnPanel.getRemainingFences().getNumber().getNumber() > 0) {
						turnPanel.getRemainingFences().getNumber().decreaseNumber();
					}
					// Stampa nel textPanel l'accaduto
					textPanel.appendText("Shepherd " + myShepherd + 
							" controlled by Player " + myPlayer + MOVED);
				} else {
					boardPanel.paintShepherd(StaticGUI.getColor(((playersNicknames.indexOf(myPlayer)) 
							* 2) + Integer.parseInt(myShepherd)),
							Integer.parseInt(myDestination));
					turnPanel.giveTurnTo(playerNumber * 2, playerNumber * 2 + 1);
					// Stampa nel textPanel l'accaduto
					textPanel.appendText(PLAYER + myPlayer + PLACED);
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Rappresenta a video la rimozione di un ovino a causa movimento del lupo
	 */
	public void printEatenSheep(String eaten) {
		final String myEaten = eaten;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				if (!"null".equals(myEaten)) {
					textPanel.appendText("Wolf ate a " + myEaten);
					boardPanel.removeSheep(AnimalType.valueOf(myEaten), 
							Integer.parseInt(wolfPosition));
				} else {
					textPanel.appendText("Wolf didn't eat any sheep");
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Rappresenta l'evoluzione degli agnelli
	 */
	public void printEvolvedLambs(List<String> evolvedLambs) {
		final List<String> myLambs = evolvedLambs;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				for (int i = 0; i < myLambs.size(); i = i + 2){
					textPanel.appendText("A lamb evolved into a " + myLambs.get(i));
					boardPanel.removeSheep(AnimalType.LAMB, 
							Integer.parseInt(myLambs.get(i+1)));
					boardPanel.paintAnimal(AnimalType.valueOf(myLambs.get(i)), 
							Integer.parseInt(myLambs.get(i+1)));
				}
				boardPanel.repaint();
			}
		});
	}
	/**
	 * Restituisce il textPanel
	 * @return textPanel
	 */
	public static TextPanel getTextPanel() {
		return textPanel;
	}
	/**
	 * Stampa sul textPanel il giocatore di turno
	 */
	public void printTurnOfPlayer(String player) {
		final String playerNick = player;
		SwingUtilities.invokeLater( new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				textPanel.appendText("It's player " + playerNick+ " turn");
			}
		});
	}
	/**
	 * Rappreseneta i player
	 */
	public void printPlayersInGame(List<String> players) {
		final List<String> playersNicks = players;
		SwingUtilities.invokeLater( new Runnable() {
			/**
			 * Lancia il runnable dell'invokeLater
			 */
			public void run() {
				textPanel.appendText("There are "+playersNicks.size()+" players in this Game. Say hello to them!");
				for (int i =0 ; i< playersNicks.size(); i++) {
					textPanel.appendText(playersNicks.get(i)+" is player "+i);
					playersNicknames.add(playersNicks.get(i));
				}
			}
		});
	}
	/**
	 * Termina il market
	 */
	public void endMarket() {
		textPanel.appendText("Market is closed!");
		marketPanel.removeAll();
		marketPanel.repaint();
	}
}
