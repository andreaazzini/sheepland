package it.polimi.deib.provaFinale2014.client;

import java.util.List;

/**
 * Interfaccia dei metodi di presentazione
 * invocabili dal Controllore lato Client
 *
 */
public interface Client {
	/**
	 * Stampa messaggio di benvenuto
	 * @param playerIndex indice del Player
	 */
	void printWelcomeMessage(int playerIndex);
	/**
	 * Stampa lo stato delle regioni
	 * @param message, Array di n = numero di regioni, 
	 * contenente un array di 3 elementi per regione,
	 * corrispondenti al numero di pecore, montoni e agnelli.
	 */
	void printRegionState(String[][] message);
	
	/**
	 * Stampa il messaggio di errore
	 * @param command, nome dell'istanza dell'enum Message
	 * contentente il messaggio di errore
	 */
	void printErrorMessage(String command);
	
	/**
	 * Stampa il numero di recinti rimanenti
	 * @param numberOfFences
	 */
	void printRemainingFences(String numberOfFences);
	
	/**
	 * Stampa i soldi rimanenti
	 * @param moneyLeft
	 */
	void printMoneyLeft(String moneyLeft);
	
	/**
	 * Stampa il messaggio di avvenuto movimento di un pastore
	 * @param player, nickname del giocatore che controlla il pastore
	 * @param destination, strada di destinazione
	 */
	void printMoveShepherd(String player, String destination);
	
	/**
	 * Stampa il messaggio di avvenuto movimento di un pastore
	 * @param shepherd, numero del pastore mosso
	 * @param player,  nickname del giocatore che controlla il pastore
	 * @param destination, strada di destinazione
	 */
	void printMoveShepherd(String shepherd, String player, String destination);
	
	/**
	 * Stampa il messaggio di avvenuto movimento di un ovino
	 * @param player, nickname del giocatore che controlla il pastore
	 * @param start, regione di partenza dell'ovino
	 * @param generalSheepType, tipo dell'ovino
	 * @param destination, regione di destinazione dell'ovino
	 */
	void printMoveSheep(String player, String start, String generalSheepType , String destination);
	
	/**
	 * Stampa i giocatori ricompensati
	 * @param player
	 */
	void printRewarded(List<String> player);
	
	/**
	 * Stampa le azioni disponibili
	 * @param actions azioni disponibili
	 */
	void printAvailableActions(List<String> actions);
	
	/**
	 * Stampa lo stato delle Strade
	 * @param message, array di lunghezza uguale al numero di strade,
	 * contente un array di informazioni sulla presenza di recinti e di pastori
	 */
	void printRoadState(String[][] message);
	
	/**
	 * Stampa lo stato delle carte del Banco
	 * @param message, prezzo della carta più economica del tipo all'indice i
	 * nell'enum TerrainType
	 */
	void printBankCardState(String[] message);
	
	/**
	 * Stampa le carte possedute dal gicoatore
	 * @param message, lista di tipi di carte
	 */
	void printPersonalCards(List<String> message);
	
	/**
	 * Stampa la posizione della pecora nera
	 * @param position
	 */
	void printBlackSheepPosition(String position);
	
	/**
	 * stampa l'avvenuto accoppiamento fra due pecore
	 * @param player, giocatore che ha effettuato l'azione
	 * @param region, regione in cui è avvenuto l'accoppiamento
	 * @param output, risultato dell'accoppiamento
	 */
	void printCoupleSheep(String player, String region, String output);
	
	/**
	 * stampa l'avvenuto accoppiamento fra una pecora e un montone
	 * @param player, giocatore che ha effettuato l'azione
	 * @param region, regione in cui è avvenuto l'accoppiamento
	 * @param output, risultato dell'accoppiamento
	 */
	void printCouple(String player, String region, String output);
	
	/**
	 * stampa l'avvenuto abbattimento di un ovino
	 * @param info (indice 0 : player che ha effettuato l'azione,
	 * indice 2: Tipo di ovino abbattuto, indice 3 : regione , 
	 * indice 4 : risultato, indice >=5 : eventuali Giocatori ricompensati.)
	 */
	void printKill(List<String> info);
	
	/**
	 * Stampa l'avvenuto acquisto di una carta dal Banco
	 * @param player, giocatore che ha effettuato l'azione
	 * @param terrainTypeIndex, indice del tipo di carta comprato
	 */
	void printBuyCard(String player, String terrainTypeIndex);
	
	/**
	 * Stampa le carte in vendita nel Market
	 * @param forSaleCards, lista di Array con le informazioni delle carte.
	 * (indice 0 : Tipo di carta, indice 1: prezzo, indice 2: venditore)
	 */
	void printForSaleCards(List<String[]> forSaleCards);
	
	/**
	 * Stampa un messaggio a video
	 * @param message
	 */
	void println(String message);
	
	/**
	 * Inserisce un'azione da eseguire
	 * Chiama un metodo di setting
	 */
	void makeChoice();
	
	/**
	 * Inserisce una strada
	 * Chiama metodo di setting
	 */
	void insertRoad();
	
	/**
	 * Inserisce una regione
	 * Chiama metodo di setting
	 */
	void insertRegion();
	
	/**
	 * Inserisce un tipo di ovino che è possibile abbattere
	 * Chiama metodo di setting
	 */
	void insertSheepTypeToKill();
	
	/**
	 * Inserisce un tipo di ovino che è possibile muovere
	 * Chiama metodo di setting
	 */
	void insertSheepTypeToMove();
	
	/**
	 * Inserisce un tipo di carta.
	 * Chiama metodo di setting.
	 */
	void insertCardType();
	
	/**
	 * Inserisce un tipo di carta e un nuovo costo.
	 * chiama metodo di setting.
	 */
	void sellCard();
	
	/**
	 * Stampa la posizione del Lupo
	 * @param position
	 */
	void printWolfPosition(String position);
	
	/**
	 * Stampa l'avvenuto acquisto di una carta
	 * @param info
	 * 	(indice 0 : acquirente, indice 1 : Tipo di carta, 
	 * 	indice 2: prezzo, indice 3: venditore)
	 */
	void printBoughtCard(List<String> info);
	
	/**
	 * Stampa la classifica di gioco
	 * @param endGameInfos, punteggi dei giocatori
	 */
	void printEndGame(List<Integer> endGameInfos);
	
	/**
	 * Stampa messaggio di attesa di un player offline
	 * @param player
	 */
	void printWaitMessage(String player);
	
	/**
	 * Inserisce un pastore per compiere le azioni.
	 * chiama metodo di setting.
	 */
	void chooseShepherd();
	
	/**
	 * Stampa ovini mangiati dal lupo
	 * @param eaten, tipo di ovino
	 */
	void printEatenSheep(String eaten);
	
	/**
	 * Stampa Informazioni suli agnelli che si sono evoluti in quel turno
	 * @param evolvedLambs, informazioni su tipo in cui si sono evoluti e posizione
	 */
	void printEvolvedLambs(List<String> evolvedLambs);
	
	/**
	 * Stampa il nickname del giocatore di turno
	 * @param player
	 */
	void printTurnOfPlayer(String player);
	
	/**
	 * Stampa tutti i nicknames dei giocatori della partita
	 * @param playersNicknames
	 */
	void printPlayersInGame(List<String> playersNicknames);
	
	/**
	 * Inserisce l'indice di una carta del Market da acquistare.
	 * Chiama metodo di setting.
	 * @param size, numero di carte disponibili
	 */
	void chooseCard(int size);
	
	/**
	 * Stampa segnale di fine Market
	 */
	void endMarket();
}
