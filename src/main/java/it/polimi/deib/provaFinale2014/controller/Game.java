package it.polimi.deib.provaFinale2014.controller;

import it.polimi.deib.provaFinale2014.model.Bank;
import it.polimi.deib.provaFinale2014.model.BlackSheep;
import it.polimi.deib.provaFinale2014.model.Card;
import it.polimi.deib.provaFinale2014.model.DefaultBoard;
import it.polimi.deib.provaFinale2014.model.Player;
import it.polimi.deib.provaFinale2014.model.Region;
import it.polimi.deib.provaFinale2014.model.Sheep;
import it.polimi.deib.provaFinale2014.model.Sheep.SheepType;
import it.polimi.deib.provaFinale2014.model.Shepherd;
import it.polimi.deib.provaFinale2014.model.TerrainType;
import it.polimi.deib.provaFinale2014.model.Wolf;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che funge da controllore. Contiene i metodi per modificare
 * il modello.
 */
public class Game {
	private enum GeneralSheepTypes {
		SHEEP, MUTTON, LAMB, BLACKSHEEP
	}
	
	private DefaultBoard gameBoard;
	private Bank gameBank;
	private List<Player> players;
	private Market market;
	private boolean twoPlayers;
	private List<Sheep> evolvedLambs;
	
	/**
	 * Costruisce un oggetto partita, inizializzando
	 * il modello e i giocatori a partire dai socket
	 * che hanno effettuto con successo la connessione
	 * al server
	 * @param sockets socket connessi alla partita
	 */
	public Game(int numberOfPlayers) {
		evolvedLambs = new ArrayList<Sheep>();
		gameBoard = new DefaultBoard();
		gameBank = new Bank();
		market = new Market();
		
		initializeGame(numberOfPlayers);
	}
	/**
	 * Inizializza la partita prima di iniziare i turni
	 * @param sockets socket dei client connessi
	 */
	private void initializeGame(int numberOfPlayers) {
		initializePlayers(numberOfPlayers);
		placeAnimals();
		giveInitialCards();
	}
	/**
	 * Assegna ad ogni giocatore una carta iniziale
	 */
	private void giveInitialCards() {
		List<Card> initialCards = gameBank.getInitialCards();
		for (Player player : players) {
			// Assegna casualmente una carta iniziale ad un giocatore
			int index = (int) (Math.random() * initialCards.size());
			player.giveCard(initialCards.get(index));
			initialCards.remove(index);
		}
	}
	/**
	 * Inizializza il market
	 */
	public void initializeMarket() {
		market = new Market();
	}
	
	/**
	 * Posiziona tutti gli animali sulla plancia di gioco
	 */
	private void placeAnimals() {
		for (int i = 0; i < gameBoard.getRegions().length - 1; i++) {
			// Aggiunge l'animale alla regione
			gameBoard.getRegions()[i].addAnimal(new Sheep());
			// Imposta il parametro regione dell'animale
			gameBoard.getRegions()[i].getAnimals().get(0).setPosition(gameBoard.getRegions()[i]);
		}
		// Aggiunge pecora nera e lupo a Sheepsburg
		gameBoard.getSheepsburg().addAnimal(new BlackSheep());
		gameBoard.getSheepsburg().setBlackSheep(true);
		gameBoard.getSheepsburg().addAnimal(new Wolf());
		gameBoard.getSheepsburg().setWolf(true);
		// Imposta Sheepsburg come posizione della pecora nera
		// e del lupo
		gameBoard.getSheepsburg().getBlackSheep().setPosition(gameBoard.getSheepsburg());
		gameBoard.getSheepsburg().getWolf().setPosition(gameBoard.getSheepsburg());
	}
	
	/**
	 * Inizializza i giocatori. Essi sono in corrispondenza biunivoca
	 * con i socket che hanno stabilito una connessione
	 * @param sockets i socket che hanno stabilito una connessione
	 */
	private void initializePlayers(int numberOfPlayers) {
		// Imposta se la partita è fra due o più giocatori
		if (numberOfPlayers == 2) {
			this.twoPlayers = true;
		} else {
			this.twoPlayers = false;
		}
		// Inizializza i giocatori
		players = new ArrayList<Player>();
		for (int i = 0; i < numberOfPlayers; i++) {
			players.add(new Player(twoPlayers));
		}
		// Dichiara attivo il primo della lista
		players.get(0).becomeActive();
	}
	/**
	 * Esegue l'azione richiesta
	 * @param action azione richiesta
	 * @param parameters parametri ottenuti
	 * @param infoBroadcast 
	 * @return true, se l'azione è eseguita con successo; false altrimenti
	 */
	public Message executeAction(Action action, int[] parameters, List<String> infoBroadcast) {

		if (action.equals(Action.MOVE_SHEPHERD)) {
			// Il solo parametro è la strada di destinazione
			return moveShepherd(parameters[0]);
		
		} else if (action.equals(Action.MOVE_SHEEP)) {
			// I parametri sono la regione di partenza e la pecora all'interno della regione
			return moveSheep(parameters[0],parameters[1]);
			
		} else if (action.equals(Action.COUPLE)) {
			// Il solo parametro è la regione nella quale si vuole eseguire un accoppiamento
			return couple(parameters[0]);
		} else if (action.equals(Action.COUPLE_SHEEPS)) {
			// Il solo parametro è la regione nella quale si vuole eseguire un accoppiamento tra pecore
			return coupleSheeps(parameters[0]);

		} else if (action.equals(Action.KILL)) {
			// I parametri sono la regione nella quale si vuole eseguire un'azione di abbattimento e la pecora da abbattere
			return kill(parameters[0],parameters[1],infoBroadcast);

		} else if (action.equals(Action.BUY_CARD)) {
			// Il parametro è la regione sul cui tipo terreno si vuole investire comprando una carta terreno
			return buyCard(parameters[0]);
		}
		return Message.INVALID_VALUE;
	}
	/**
	 * Compra una carta
	 * @param terrainTypeIndex indice del tipo di carta
	 * @return messaggio di controllo
	 */
	public Message buyCard (int terrainTypeIndex) {
		if (getActivePlayer().getMainShepherd().canBuyCard(TerrainType.values()[terrainTypeIndex])) {
			if (getActivePlayer().getMainShepherd().getController().hasMoneyForBuyingCard(gameBank.getCheaperCard(TerrainType.values()[terrainTypeIndex]))) {
				gameBank.giveCard(getActivePlayer(), TerrainType.values()[terrainTypeIndex]);
				return Message.NONE;	
			} else {
				return Message.NOT_ENOUGH_MONEY;
			}
		} return Message.INVALID_VALUE;	
	}
	/**
	 * Abbatte una pecora
	 * @param regionIndex indice della regione della pecora
	 * @param sheepTypeIndex indice del tipo di pecora
	 * @param infoBroadcast pacchetto di broadcast
	 * @return
	 */
	public Message kill(int regionIndex, int sheepTypeIndex, List<String> rewardedShepherds) {
		if (gameBoard.regionInBound(regionIndex)) {
			if (getActivePlayer().getMainShepherd().isAdjacent(gameBoard.getRegion(regionIndex))) {
				Sheep sheep = gameBoard.getRegions()[regionIndex].typeInRegion(sheepTypeIndex); 
				if (sheep == null) {
					return Message.NO_MATCHING_TYPE;
				} else {
					if (getActivePlayer().getMainShepherd().kill(gameBoard.getRegions()[regionIndex], sheep)) {
						List<Shepherd> rewarded = getActivePlayer().getMainShepherd().kill2();
						for (Shepherd s: rewarded) {
							rewardedShepherds.add(String.valueOf(players.indexOf(s.getController())));
						}
						return Message.SUCCESS;
					}
					return Message.FAIL;
				}
			} else {
				return Message.NOT_ADJACENT_REGION;
			}
		} else {
			return Message.INVALID_VALUE;
		}
	}
	/**
	 * Accoppia due pecore
	 * @param regionIndex indice della regione
	 * @return messaggio di controllo
	 */
	public Message coupleSheeps(int regionIndex) {
		if ( gameBoard.regionInBound(regionIndex)) {
			if (getActivePlayer().getMainShepherd().isAdjacent(gameBoard.getRegion(regionIndex))) {
				if (getActivePlayer().getMainShepherd().coupleSheeps(gameBoard.getRegions()[regionIndex])) {
					return Message.SUCCESS;
				} else {
					return Message.FAIL;
				}
			} else {
				return Message.NOT_ADJACENT_REGION;
			}
		} else {
			return Message.INVALID_VALUE;
		}	
	}
	/**
	 * Accoppia un montone e una pecora
	 * @param regionIndex indice della regione
	 * @return messaggio di controllo
	 */
	public Message couple(int regionIndex) {
		if (gameBoard.regionInBound(regionIndex)) {
			if (getActivePlayer().getMainShepherd().isAdjacent(gameBoard.getRegion(regionIndex))) {
				if (gameBoard.getRegions()[regionIndex].canCoupleHere()){
					if (getActivePlayer().getMainShepherd().couple(gameBoard.getRegions()[regionIndex])) {
						return Message.SUCCESS;
					} else {
						return Message.FAIL;
					}
				} else {
					return Message.INVALID_VALUE;
				}
			} else {
				return Message.NOT_ADJACENT_REGION;
			}
		} else { 
			return Message.INVALID_VALUE;
		}
	}
	/**
	 * Muove una pecora
	 * @param startingRegion indice della strada di partenza
	 * @param sheepTypeIndex indice del tipo di pecora
	 * @return messaggio di controllo
	 */
	public Message moveSheep(int startingRegion, int sheepTypeIndex) {
		if (gameBoard.regionInBound(startingRegion)) {
			if (getActivePlayer().getMainShepherd().isAdjacent(gameBoard.getRegion(startingRegion))) {
				Sheep sheep;
				if (sheepTypeIndex == GeneralSheepTypes.BLACKSHEEP.ordinal()) {
						if (gameBoard.getRegions()[startingRegion].hasBlackSheep()) {
							sheep = gameBoard.getRegions()[startingRegion].getBlackSheep();
						} else {
							return Message.NO_BLACKSHEEP_HERE;
						}
					} else {
						sheep = gameBoard.getRegions()[startingRegion].typeInRegion(sheepTypeIndex); 
						if (sheep == null) {
						return Message.NO_MATCHING_TYPE;
					}
				 }
				getActivePlayer().getMainShepherd().moveSheep(gameBoard.getRegions()[startingRegion], sheep);
				return Message.NONE;
			} else {
				return Message.NOT_ADJACENT_REGION;
			}
		} else {
			return Message.INVALID_VALUE;
		}
	}
	/**
	 * Muove un pastore in una strada
	 * @param road indice della strada di destinazione
	 * @return messaggio di controllo
	 */
	public Message moveShepherd(int road) {
		if (gameBoard.roadInBound(road)) {
			if (gameBoard.getRoads()[road].isOccupied()) {
				return Message.ROAD_ALREADY_OCCUPIED;
			} else {
				if (getActivePlayer().getMainShepherd().canMoveTo(gameBoard.getRoads()[road])) {
					getActivePlayer().getMainShepherd().moveTo(gameBoard.getRoads()[road], gameBank.getFirstFence());
					return Message.NONE;
				} else {
					return Message.NOT_ENOUGH_MONEY;
				}
			}
				
		} else {
			return Message.INVALID_VALUE;
		}
	}
	
	/**
	 * Restituisce la plancia di gioco utilizzata nella
	 * partita
	 * @return plancia di gioco
	 */
	public DefaultBoard getGameBoard() {
		return gameBoard;
	}
	/**
	 * Restituisce il banco utilizzato dalla partita
	 * @return banco di gioco
	 */
	public Bank getGameBank() {
		return gameBank;
	}
	/**
	 * Restituisce la lista di giocatori coinvolti nella partita
	 * @return lista di giocatori
	 */
	public List<Player> getPlayers() {
		return players;
	}
	/**
	 * Restituisce il giocatore attivo all'interno
	 * della lista di giocatori della partita
	 * @return giocatore attivo
	 * @throws NoActivePlayerException
	 */
	public Player getActivePlayer() {
		for (Player player : players) {
			if (player.isActive()) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Restituisce i danari di uno specifico giocatore
	 * @param int indice del giocatore
	 * @return danari del giocatore attivo
	 */
	public String getPersonalMoney(int playerIndex) {
		return String.valueOf(players.get(playerIndex).getAvailableMoney());
	}
		
	/**
	 * Restituisce le carte di uno specifico giocatore
	 * @param int indice del giocatore
	 * @return lista di carte del giocatore attivo
	 */
	public List<String> getPersonalCards(int player) {
		List<String> messages = new ArrayList<String>();
		List<Card> cards = players.get(player).getCards();
		for (Card card : cards) {
			int index = card.getTerrainType().ordinal();
			messages.add(String.valueOf(index));
		}
		return messages;

	}
	/**
	 * Restituisce la posizione della pecora nera
	 * @return posizione della pecora nera
	 */
	public String getBlackSheepPosition(){
		for (int i=0; i < gameBoard.getRegions().length; i++) {
			if (gameBoard.getRegions()[i].hasBlackSheep()) {
				return String.valueOf(i);
			}
		}
		return null; 	 
	}
	/**
	 * Restituisce la posizione del lupo
	 * @return posizione del lupo
	 */
	public String getWolfPosition() {
		for (int i = 0; i < gameBoard.getRegions().length; i++) {
			if (gameBoard.getRegions()[i].hasWolf()) {
				return String.valueOf(i);
			}
		}
		return null; 
	}
	/**
	 * Restituisce lo stato delle regioni all'interno della plancia di gioco
	 * @return contenuto delle regioni
	 */
	public String[][] getRegionState() {
		String[] regionInfo;
		String[][] message = new String[gameBoard.getRegions().length][3];
		//Controllo le Regioni
		for (int i = 0; i < gameBoard.getRegions().length ; i++) {
			regionInfo = new String[3];
			regionInfo[0] = String.valueOf(gameBoard.getRegions()[i].getSheepTypeNumber());
			regionInfo[1] = String.valueOf(gameBoard.getRegions()[i].getMuttonTypeNumber());
			regionInfo[2] = String.valueOf(gameBoard.getRegions()[i].getLambTypeNumber());
			message[i] = regionInfo;
		}
		return message;
	}
	/**
	 * Restituisce lo stato delle strade all'interno della plancia di gioco
	 * @return contenuto delle strade
	 */
	public String[][] getRoadState() {
		String[][] message = new String[gameBoard.getRoads().length][2];
		//Controllo le Strade
		for (int i = 0; i < gameBoard.getRoads().length ; i++) {
			message[i][0] = String.valueOf(gameBoard.getRoads()[i].hasFence());
			if (gameBoard.getRoads()[i].hasShepherd()) {
				message[i][1] = String.valueOf(players.indexOf(gameBoard.getRoads()[i].getShepherd().getController()));
			} else {

				message[i][1]=String.valueOf(false);
			}

		}
		return message;
	}
	/**
	 * Restituisce lo stato delle carte nel banco
	 * @return carte del banco
	 */
	public String[] getBankCardState() {
		String[] message = new String[Bank.TYPES];
		//controllo le Carte disponibili del Banco
		for (int i = 0; i < Bank.TYPES; i++) {
			message[i]= String.valueOf(gameBank.getCheaperCard(TerrainType.values()[i]).getCost());
		}
		return message;
	}
	/**
	 * Restituisce lo stato dei recinti nel banco
	 * @return recinti nel banco
	 */
	public String getBankFencesState() {
		return String.valueOf(gameBank.getAvailableFenceNumber());
	}
	/**
	 * Evolve gli agnelli che hanno due turni di vita di montone o pecora
	 */
	public void evolveLambs() {
		evolvedLambs.clear();
		for (Region region : gameBoard.getRegions()) {
			for (Sheep sheep : region.getSheeps()) {
				if (sheep.getType().equals(SheepType.LAMB)) {
					sheep.increaseTurnCounter();
					if (sheep.evolve()) {
						evolvedLambs.add(sheep);
					}
				}
			}
		} 
	}
	
	/**
	 * Restituisce la lista di Lambs che si sono evoluti nel turno
	 * @return List<Sheep> evolvedLambs
	 */
	public List<Sheep> getEvolvedLambs() {
		return evolvedLambs;
	}
	
	/**
	 * Restituisce le informazioni dei Lambs che si sono evoluti nel turno.
	 * Le informazioni sono il Tipo in cui si è evoluto l'agnello e la sua posizione
	 * in formato String, per la comunicazione.
	 * @return List<String> infos dei Lambs in formato String.
	 */
	public List<String> getEvolvedLambsInfos() {
		List<String> infos = new ArrayList<String>();
		for (Sheep sheep : evolvedLambs) {
			infos.add(String.valueOf(sheep.getType()));
			infos.add(String.valueOf(gameBoard.getRegionIndex(sheep.getPosition())));
		}
		return infos;	
	}
	/**
	 * Restituisce il market
	 * @return market
	 */
	public Market getMarket() {
		return market;
	}
	/**
	 * Vende le carte all'interno del sellTurn
	 * @param cards lista di carte vendute
	 * @param activePlayerIndex 
	 */
	public void sellCards(List<String[]> cards, int playerIndex) {
		for (String[] card : cards) {
			List<Card> cardOwned = players.get(playerIndex).getCards();
			for ( int i=0; i<cardOwned.size() ; i++) {
				if (cardOwned.get(i).getTerrainType().ordinal() == Integer.parseInt(card[0]) && !cardOwned.get(i).isInitial()) {
					market.addCard(cardOwned.get(i), Integer.parseInt(card[1]), playerIndex);
					players.get(playerIndex).getCards().remove(i);
					break;
					
				}
			}
		}
	}

	/**
	 * Compra le carte all'interno del marketTurn
	 * @param buyer indice del giocatore acquirente
	 * @param cardIndexes indici delle carte acquistate
	 */
	public void buyCards(int buyer, String cardIndex) {
		players.get(market.getSeller(Integer.parseInt(cardIndex))).receiveMoney(market.getCard(Integer.parseInt(cardIndex)).getCost());
		Card card = market.buyCard(Integer.parseInt(cardIndex));
		players.get(buyer).buyCard(card);
	}

	/**
	 * Muove la pecora nera
	 */
	public void moveBlackSheep() {
		BlackSheep blackSheep = (BlackSheep) gameBoard.getBlackSheep();
		blackSheep.moveBlackSheepRandmly();
	}
	/**
	 * Muove il lupo
	 */
	public void moveWolf() {
		gameBoard.getWolf().moveRandomly();
	}
	/**
	 * Posizione un pastore all'interno della plancia di gioco
	 * @param roadIndex indice della strada di posizionamento
	 * @return messaggio di controllo
	 */
	public Message placeShepherd(int roadIndex) {
		if (gameBoard.roadInBound(roadIndex)) {
			if (!gameBoard.getRoads()[roadIndex].isOccupied()) {
				this.getActivePlayer().getMainShepherd().setPosition(gameBoard.getRoads()[roadIndex]);
				return Message.NONE;
			} else {
				return Message.ROAD_ALREADY_OCCUPIED; 
			}
		} else {
			return Message.INVALID_VALUE;
		}
	}
	/**
	 * Calcola la lista dei punteggi dei giocatori
	 * @return lista dei punteggi dei giocatori
	 */
	public List<Integer> calculatePoints() {
		List<Integer> playerPoints = new ArrayList<Integer>();
		
		// Inizializza la lista dei punteggi con i danari rimanenti dei giocatori
		for (Player p : players) {
			playerPoints.add(p.getAvailableMoney());
		}
		// Per ogni giocatore
		for (int i = 0; i < playerPoints.size(); i++) {
			int cardPoints = 0;
			// Per ogni carta di quel giocatore
			for (Card card : players.get(i).getCards()) {
				// Per ogni regione avente lo stesso tipo terreno
				for (Region region : gameBoard.getRegions()) {
					if (card.getTerrainType().equals(region.getType())) {
						// Somma i punteggi delle pecore su quei terreni
						for (Sheep sheep : region.getSheeps()) {
							cardPoints += sheep.getValue();
						}
					}
				}
			}
			// Ai danari somma i punti delle carte
			playerPoints.set(i, playerPoints.get(i) + cardPoints);
		}
		
		return playerPoints;
	}
	/**
	 * Restituisce true se la partita è fra due giocatori
	 * @return true, se la partita è fra due giocatori; false altrimenti
	 */
	public boolean onlyTwoPlayers() {
		return twoPlayers;
	}
	
	/**
	 * Resituisce ai proprietari le carte rimaste invendute nel Market
	 */
	public void returnUnselledCards () {
		for (int i=0; i<market.getCards().size() ; i++) {
		 players.get(market.getSeller(i)).giveCard(market.getCard(i));
		}
		market.getCards().clear();
	}
}
