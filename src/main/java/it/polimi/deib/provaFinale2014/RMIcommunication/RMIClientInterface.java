package it.polimi.deib.provaFinale2014.RMIcommunication;

import it.polimi.deib.provaFinale2014.controller.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaccia del client RMI
 */
public interface RMIClientInterface extends Remote {
	
	/**
	 * Ottiene il Nickname del Client
	 * @return nickname del giocatore
	 */
	String getNickname() throws RemoteException;
	
	/**
	 * Imposta il Gestore della Communicazione (lato Server) della Partita
	 * @params RMIHandlerInterface, interfaccia del gestore della comunicazione
	 * 
	 */
	void setRMIHandler(RMIHandlerInterface clientServerHandler) throws RemoteException;
	
	/**
	 * Imposta l'indice del giocatore corrente
	 * e fa partire le GUI
	 * @params index, indice del giocatore
	 */
	void setPlayerIndexAndWelcome (int index)  throws RemoteException;
	
	/**
	 * Ottiene lo stato delle regioni
	 * Chiama metodo di stampa
	 * @param regionState, Array di n = numero di regioni, 
	 * contenente un array di 3 elementi per regione,
	 * corrispondenti al numero di pecore, montoni e agnelli.
	 */
	void displayRegionsState(String[][] regionState) throws RemoteException;
	
	/**
	 * Ottiene la posizione della pecora nera
	 * Chiama metodo di stampa
	 * @param position
	 */
	void displayBlacksheepPosition(String position) throws RemoteException;
	
	/**
	 * Ottiene la posizione del Lupo
	 * Chiama metodo di stampa
	 * @param position
	 * @throws RemoteException
	 */
	void displayWolfPosition(String Position) throws RemoteException;
	
	/**
	 * Ottiene lo stato delle Strade
	 * Chiama metodo di stampa
	 * @param roadState, array di lunghezza uguale al numero di strade,
	 * contente un array di informazioni sulla presenza di recinti e di pastori
	 * @throws RemoteException
	 */
	void displayRoadsState(String[][] roadState) throws RemoteException;
	
	/**
	 * Ottiene lo stato delle carte del Banco
	 * Chiama metodo di stampa
	 * @param cardState, prezzo della carta più economica del tipo all'indice i
	 * nell'enum TerrainType
	 * @throws RemoteException
	 */
	void displayBankCardsState(String[] cardState) throws RemoteException;
	
	/**
	 * Ottiene il numero di recinti rimanenti
	 * Chiama metodo di stampa
	 * @param state, stato dei recinti
	 * @throws RemoteException
	 */
	void displayBankFencesState(String state) throws RemoteException;
	
	/**
	 * Ottiene le carte possedute dal gicoatore
	 * Chiama metodo di stampa
	 * @param personalCards, lista di tipi di carte
	 * @throws RemoteException
	 */
	void displayPersonalCards(List<String> personalCards) throws RemoteException;
	
	/**
	 * Ottiene i soldi rimanenti del giocatore
	 * Chiama metodo di stampa
	 * @param money
	 * @throws RemoteException
	 */
	void displayPersonalMoney(String money) throws RemoteException;
	
	/**
	 * Sveglia il Client per il posizionamento dei pastori.
	 * Imposta i messaggi di broadcast lato Server
	 * @throws RemoteException
	 */
	void wakeUpInizialTurn() throws RemoteException;
	
	/**
	 * Sveglia il CLient per l'acquisto delle carte nel turno
	 * di Market.
	 * @return chosenCard, indice della carta prescelta, null se nessuna
	 * @throws RemoteException
	 */
	String wakeUpBuyMarket() throws RemoteException;
	
	/**
	 * Sveglia il Client per la vendita delle carte nel turno di Market
	 * @return chosenCards, lista di : Tipo di carta e nuovo prezzo.
	 * @throws RemoteException
	 */
	List<String[]> wakeUpSellMarket() throws RemoteException;
	
	/**
	 * Sveglia il Client per il normale turno di gioco.
	 * Imposta i messaggi di Brodcast.
	 * @throws RemoteException
	 */
	void wakeUpMyTurn() throws RemoteException;
	
	/**
	 * Ottiene i messaggi di broacast
	 * @param infoBroadcast
	 * @throws RemoteException
	 */
	void printBroadcastMessage(List<String> infoBroadcast) throws RemoteException;
	
	/**
	 * Ping per verifica connessione attiva
	 * @throws RemoteException
	 */
	public void ping() throws RemoteException;
	
	/**
	 * Ottiene punteggio finale
	 * @param scores
	 * @throws RemoteException
	 */
	public void getEndGameScores(List<Integer> scores) throws RemoteException;
	
	/**
	 * Ottiene il giocatore che è passato Offline, 
	 * per cui la partita è momentaneamente sospesa
	 * @param string
	 * @throws RemoteException
	 */
	void gamePaused(String string) throws RemoteException;

	/**
	 * Imposta il pastore scelto
	 * @param chosenShepherd
	 * @throws RemoteException
	 */
	void setShepherdNumber(int chosenShepherd) throws RemoteException;

	/**
	 * Sveglia il Client per scegliere quale pastore utilizzare
	 * @return
	 * @throws RemoteException
	 */
	int wakeUpChooseShepherd() throws RemoteException;

	/**
	 * Ottiene le informazioni degli agnelli che si sono evoluti
	 * @param evolvedLambs
	 * @throws RemoteException
	 */
	void getEvolvedLambs(List<String> evolvedLambs) throws RemoteException;

	/**
	 * Ottiene informazioni riguardo il tipo di ovino che è stato mangiato dal lupo
	 * @param type
	 * @throws RemoteException
	 */
	void getEatenSheep(String type) throws RemoteException;
	
	/**
	 * Ottiene il nickname del giocatore di turno
	 * Chiama metodo di stampa
	 * @param player
	 * @throws RemoteException
	 */
	void printPlayerTurn(String activePlayerNickname) throws RemoteException;
	
	/**
	 * Ottiene i nicknames dei giocatori della partita.
	 * @param nicknames
	 * @throws RemoteException
	 */
	void setNicknames (List<String> nicknames) throws RemoteException;
	
	/**
	 * Invia Segnale di fine Market
	 * @throws RemoteException
	 */
	public void endMarketSignal () throws RemoteException;
	
	/**
	 * Ottiene il messaggio di errore
	 * @throws RemoteException
	 */
	public void getError(Message error) throws RemoteException;
}
