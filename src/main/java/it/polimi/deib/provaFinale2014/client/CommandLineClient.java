package it.polimi.deib.provaFinale2014.client;

import it.polimi.deib.provaFinale2014.model.TerrainType;
import it.polimi.deib.provaFinale2014.controller.Message;
import it.polimi.deib.provaFinale2014.client.ClientHandler.GeneralSheepTypes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * View da Linea di Comando del Gioco Sheepland
 *
 */
public class CommandLineClient implements Client {
	private static final String WELCOME = "Welcome to Sheepland!";
	private static final String BOARD = "Images/GameBoardMap.txt";
	
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	
	private Scanner in;
	private PrintStream out;
	
	private BufferedReader boardReader;
	private ClientCommunication clientHandler;
	
	/**
	 * Costruisce un client a riga di comando inizializzando lo scanner
	 */
	public CommandLineClient(ClientCommunication clientHandler) {
		LOGGER.setUseParentHandlers(false);
		
		this.clientHandler = clientHandler;
		in = new Scanner(System.in);
		out = System.out;
	}
	/**
	 * Stampa la plancia di gioco dal file
	 * @throws IOException
	 */
	public void printBoard() throws IOException {
		boardReader = new BufferedReader(new FileReader(BOARD));
		String line = null;
		while ((line = boardReader.readLine()) != null) {
		   out.println(line);
		 }
	}
	/**
	 * Inserisce un'azione da eseguire
	 * Chiama un metodo di setting
	 */
	public void makeChoice() {
		out.print("Insert the action you want to perform: ");
		clientHandler.setChoice(in.nextLine());
	}
	/**
	 * Inserisce una regione
	 * Chiama metodo di setting
	 */
	public void insertRegion() {
		out.print("Insert the region number: ");
		clientHandler.setRegion(in.nextLine());
	}
	/**
	 * Inserisce una strada
	 * Chiama metodo di setting
	 */
	public void insertRoad() {
		String road;
		out.println("Insert the destination road number");
		out.print("Choose a road from 0 to 41: ");
		road = in.nextLine();
		clientHandler.setRoad(road);
	}
	/**
	 * Inserisce un tipo di carta.
	 * Chiama metodo di setting.
	 */
	public void insertCardType() {
		clientHandler.setCardType(insertCardTypeClc());
	}
	
	private String insertCardTypeClc() {
		out.print("Choose a Type within: ");
		for (TerrainType type: TerrainType.values()) {
			if (!type.equals(TerrainType.SHEEPSBURG)) {
				out.print(type+", ");
			}
		}
		return  in.nextLine();
	}
	
	/**
	 * Inserisce azione da svolgere nel Turn Choice
	 * @return azione scelta;
	 */
	public String makeTurnChoice() {
		out.println("It's your turn");
		out.print("Choose what to do within: BOARD_STATE, PLAYER_INFO, MAP, ACTION ");
		return in.nextLine();
	}
	/**
	 * Inserisce un pastore per compiere le azioni.
	 * chiama metodo di setting.
	 */
	public void chooseShepherd() {
		out.print("Choose a shepherd, 0 or 1: ");
		String nextLine;
		while (true) {
			nextLine = in.nextLine();
			if ("0".equals(nextLine) || "1".equals(nextLine)) {
				break;
			} else {
				out.print("Choose a shepherd, 0 or 1: ");
			}
		}
		clientHandler.setChosenShepherd(Integer.parseInt(nextLine));
	}
	/**
	 * Inserisce un tipo di ovino che è possibile abbattere
	 * Chiama metodo di setting
	 */
	public void insertSheepTypeToKill() {
		out.print("Insert an animal type within SHEEP, MUTTON, LAMB: ");
		clientHandler.setSheepType(in.nextLine());
	}
	/**
	 * Inserisce un tipo di ovino che è possibile muovere
	 * Chiama metodo di setting
	 */
	public void insertSheepTypeToMove() {
		out.print("Insert an animal type within SHEEP, MUTTON, LAMB, BLACKSHEEP: ");
		clientHandler.setSheepType(in.nextLine());
	}
	/**
	 * Stampa le azioni disponibili
	 * @param actions azioni disponibili
	 */
	public void printAvailableActions(List<String> actions) {
		for (String action : actions) {
			out.println(action);
		} out.println();
		
	}
	/**
	 * Stampa messaggio di benvenuto
	 * @param playerIndex indice del Player
	 */
	public void printWelcomeMessage(int index) {
		out.println(WELCOME);
		out.print("You are PLAYER "+index);
		out.println();
		
	}
	/**
	 * Stampa lo stato delle regioni
	 */
	public void printRegionState(String[][] message) {
		for (int i=0; i< message.length; i++){
			out.println("In Region "+i+" there are: "+message[i][0]+" Sheeps, "+
					message[i][1]+" Muttons, "+message[i][2]+" Lambs");	
		}
		out.println();
	}
	/**
	 * Stampa lo stato delle regioni
	 * @param message, Array di n = numero di regioni, 
	 * contenente un array di 3 elementi per regione,
	 * corrispondenti al numero di pecore, montoni e agnelli.
	 */
	public void printRoadState(String[][] message) {
		for (int i = 0; i < message.length; i++){
			if (message[i][0].equals(String.valueOf(true))) {
				out.println("Road " + i + " is occupied by a fence");
			}
			if (!message[i][1].equals(String.valueOf(false))) {
				out.println("Road " + i + " is occupied by " + message[i][1]);
			}
		}
		out.println();
	}
	/**
	 * Stampa lo stato delle carte del Banco
	 * @param message, prezzo della carta più economica del tipo all'indice i
	 * nell'enum TerrainType
	 */
	public void printBankCardState(String[] message) {
		for (int i=0; i< message.length; i++){
			if ("null".equals(message[i])) {
				out.println("No more cards available for type "+TerrainType.values()[i]);
			} else {
				out.println("The first card available for the type "+TerrainType.values()[i]+" costs "+message[i]);
			}
		}
		out.println();
	}
	/**
	 * Stampa le carte possedute dal gicoatore
	 * @param message, lista di tipi di carte
	 */
	public void printPersonalCards(List<String> message) {
		out.println("Cards you own: ");
		for (String s: message){
			out.println("Type: "+TerrainType.values()[Integer.parseInt(s)]);
		}
		out.println();
	
	}
	/**
	 * Stampa la posizione della pecora nera
	 * @param position
	 */
	public void printBlackSheepPosition(String position) {
		out.println("Black Sheep is in region: "+position);
	}
	/**
	 * Stampa la posizione del Lupo
	 * @param position
	 */
	public void printWolfPosition(String position) {
		out.println("Wolf is in region: " + position);
		out.println();
	}
	/**
	 * Stampa il messaggio di errore
	 * @param command, nome dell'istanza dell'enum Message
	 * contentente il messaggio di errore
	 */
	public void printErrorMessage(String command) {
		out.println(Message.valueOf(command).getMessage());
		out.println();
		
	}
	/**
	 * Stampa il numero di recinti rimanenti
	 * @param numberOfFences
	 */
	public void printRemainingFences(String nextLine) {
		out.println("Remaining fances: "+nextLine);
		out.println();
		
	}
	/**
	 * Stampa i soldi rimanenti
	 * @param moneyLeft
	 */
	public void printMoneyLeft(String nextLine) {
		out.println("Money Left: "+nextLine);
		out.println();
	}
	/**
	 * Stampa il messaggio di avvenuto movimento di un pastore
	 * @param player, nickname del giocatore che controlla il pastore
	 * @param destination, strada di destinazione
	 */
	public void printMoveShepherd(String player, String destination) {
		out.println(player+" moved to "+destination);
		out.println();
	}
	/**
	 * Stampa il messaggio di avvenuto movimento di un ovino
	 * @param player, nickname del giocatore che controlla il pastore
	 * @param start, regione di partenza dell'ovino
	 * @param generalSheepType, tipo dell'ovino
	 * @param destination, regione di destinazione dell'ovino
	 */
	public void printMoveSheep(String player, String start, String generalSheepType , String destination) {
		out.println(player+" moved a "+generalSheepType+" from "+start+" to "+destination);
		out.println();
	}
	/**
	 * Stampa i giocatori ricompensati
	 * @param player
	 */
	public void printRewarded(List<String> player) {
		if (player.isEmpty()) {
			out.println("No Shepherd rewarded");
		} else {
			for (String p: player) {
				out.println(p+" has been rewarded");
			}
		}
		out.println();
	}
	/**
	 * stampa l'avvenuto accoppiamento fra due pecore
	 * @param player, giocatore che ha effettuato l'azione
	 * @param region, regione in cui è avvenuto l'accoppiamento
	 * @param output, risultato dell'accoppiamento
	 */
	public void printCoupleSheep(String p, String region, String output) {
		if (output.equals(Message.SUCCESS.toString())) {
			out.println(p+" coupled two sheeps in region "+region+". A new sheep is born");
		} else {
			out.println(p+" failed in coupling two sheeps in region "+region);
		}
		out.println();
	}
	/**
	 * stampa l'avvenuto accoppiamento fra una pecora e un montone
	 * @param player, giocatore che ha effettuato l'azione
	 * @param region, regione in cui è avvenuto l'accoppiamento
	 * @param output, risultato dell'accoppiamento
	 */
	public void printCouple(String p, String region, String output) {
		if (output.equals(Message.SUCCESS.toString())) {
			out.println(p+" coupled a Sheep and a Mutton in region "+region+". A new lamb is born");
		} else {
			out.println(p+" failed in coupling a Sheep and a Mutton in region "+region);
		}
		out.println();
	}
	/**
	 * stampa l'avvenuto abbattimento di un ovino
	 * @param info (indice 0 : player che ha effettuato l'azione,
	 * indice 2: Tipo di ovino abbattuto, indice 3 : regione , 
	 * indice 4 : risultato, indice >=5 : eventuali Giocatori ricompensati.)
	 */
	public void printKill(List<String> info) {
		if (info.get(4).equals(Message.SUCCESS.toString())) {
			out.println(info.get(0)+" has killed a "+ GeneralSheepTypes.values()[Integer.parseInt(info.get(3))] +" in region "+info.get(2));
			for (int i= 5; i< info.size()-1 ;i++) {
				out.println(info.get(i)+ " has been rewarded.");
			}
			
		} else {
			out.println(info.get(0)+" has failed in killing a "+GeneralSheepTypes.values()[Integer.parseInt(info.get(3))]+" in region "+info.get(2));
		}
		out.println();
	}
	/**
	 * Stampa l'avvenuto acquisto di una carta dal Banco
	 * @param player, giocatore che ha effettuato l'azione
	 * @param terrainTypeIndex, indice del tipo di carta comprato
	 */
	public void printBuyCard(String p, String terrainType) {
		out.println(p+" has bought a "+ TerrainType.values()[Integer.parseInt(terrainType)]+" card.");
		out.println();
	}
	/**
	 * Stampa le carte in vendita nel Market
	 * @param forSaleCards, lista di Array con le informazioni delle carte.
	 * (indice 0 : Tipo di carta, indice 1: prezzo, indice 2: venditore)
	 */
	public void printForSaleCards(List<String[]> forSaleCards) {
		out.println("There are " + forSaleCards.size() + " cards you can buy");
		for (int i = 0; i < forSaleCards.size(); i++) {
			out.println((i + 1) + ") " + forSaleCards.get(i)[0] + " card, for sale for " + forSaleCards.get(i)[1] + " coins (SELLER: player "+ forSaleCards.get(i)[2] + ")");
		}
	}
	/**
	 * Stampa un messaggio a video
	 * @param message
	 */
	public void println(String message) {
		out.println(message);
	}
	/**
	 * Inserisce l'indice di una carta del Market da acquistare.
	 * Chiama metodo di setting.
	 * @param size, numero di carte disponibili
	 */
	public void chooseCard(int cardsNumber) {
		int selection;
		while (true) {
			out.println("Select a card from 1 to " + cardsNumber + 
					" (0 to terminate): ");
			try {
			selection = Integer.parseInt(in.nextLine());
			if (selection > 0 && selection < cardsNumber) 
				break;
			} catch (NumberFormatException e) {
				LOGGER.log(Level.INFO, "Wrong number format", e);
			}
		}
		clientHandler.setChosenCard(selection);
	}
	/**
	 * Inserisce un tipo di carta e un nuovo costo.
	 * chiama metodo di setting.
	 */
	public void sellCard() {
		boolean valid = false;
		do {
			out.print("Would you like to sell one of your cards? YES, NO: ");
			String answer = in.nextLine();
			if ("YES".equals(answer) || "yes".equals(answer)) {
				valid = true;
				int price;
				String type = insertCardTypeClc();
				while (!Arrays.asList(TerrainType.values()).contains(TerrainType.valueOf(type))) {
					out.println("ERROR: card type does not exist!");
					type = insertCardTypeClc();
				}
				while (true) {
					out.print("Insert the sell price: ");
					try {
						price = Integer.parseInt(in.nextLine());
						if (price>0) {
							break;
						}
					} catch (NumberFormatException e) {
						LOGGER.log(Level.INFO, "Wrong number format", e);
					}
				}
				String[] soldCards = {String.valueOf(TerrainType.valueOf(type).ordinal()), String.valueOf(price)};
				clientHandler.setSoldCard(soldCards);
			} else  if ("NO".equals(answer) || "no".equals(answer)){
				valid = true;
				clientHandler.setSoldCard(null);
			}
		} while (!valid);
	}
	/**
	 * Stampa l'avvenuto acquisto di una carta
	 * @param info
	 * 	(indice 0 : acquirente, indice 1 : Tipo di carta, 
	 * 	indice 2: prezzo, indice 3: venditore)
	 */
	public void printBoughtCard(List<String> info) {
		if (info.size()>2) {
			out.println(info.get(0)+" has bought: a "+info.get(2)+" card for "+info.get(3)+" coins, sold by Player "+info.get(4));
		}
	}
	/**
	 * Stampa la classifica di gioco
	 * @param endGameInfos, punteggi dei giocatori
	 */
	public void printEndGame(List<Integer> endGameInfos) {
		out.println("END OF THE GAME!");
		for (int i = 0; i < endGameInfos.size(); i++) {
			out.println(+ i +
					" scored " + endGameInfos.get(i) + " points");
		}
	}
	/**
	 * Stampa messaggio di attesa di un player offline
	 * @param player
	 */
	public void printWaitMessage(String playerIndex) {
		out.println(playerIndex + " seems disconnected. Give us 30 seconds to find him!");
	}
	/**
	 * Stampa il messaggio di avvenuto movimento di un pastore
	 * @param shepherd, numero del pastore mosso
	 * @param player,  nickname del giocatore che controlla il pastore
	 * @param destination, strada di destinazione
	 */
	public void printMoveShepherd(String shepherd, String player, String destination) {
		out.println("Shepherd " + shepherd + " controlled by "+player+" moved to "+destination);
		out.println();
	}
	/**
	 * Stampa ovini mangiati dal lupo
	 * @param eaten, tipo di ovino
	 */
	public void printEatenSheep(String eaten) {
		if ("null".equals(eaten)) {
			out.println ("Wolf didn't eat any sheep");
		} else {
			out.println("Wolf ate a "+eaten);
		}
		out.println();
	}
	/**
	 * Stampa Informazioni suli agnelli che si sono evoluti in quel turno
	 * @param evolvedLambs, informazioni su tipo in cui si sono evoluti e posizione
	 */
	public void printEvolvedLambs(List<String> evolvedLambs) {
		for (int i=0; i<evolvedLambs.size(); i=i+2){
			out.println("A lamb evolved into a "+evolvedLambs.get(i)+ " in region "+evolvedLambs.get(i+1));
		}
	}
	/**
	 * Stampa il nickname del giocatore di turno
	 * @param player
	 */
	public void printTurnOfPlayer(String playerIndex) {
		System.out.println("It's "+playerIndex+" turn.");
		System.out.println();
		
	}
	/**
	 * Stampa tutti i nicknames dei giocatori della partita
	 * @param playersNicknames
	 */
	public void printPlayersInGame(List<String> playersNicknames) {
		System.out.println("There are "+playersNicknames.size()+" players in this Game. Say hello to them!");
		for (int i =0 ; i< playersNicknames.size(); i++) {
			System.out.println(playersNicknames.get(i)+" is player "+i);
		}
		System.out.println();
		
	}
	/**
	 * Stampa segnale di fine Market
	 */
	public void endMarket() {
		System.out.println("Market is closed!");
		System.out.println();	
	}
}
