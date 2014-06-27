package it.polimi.deib.provaFinale2014.RMIcommunication;

import it.polimi.deib.provaFinale2014.controller.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaccia del gestore della comunicazione lato client RMI
 */
public interface RMIHandlerInterface extends Remote{
	
	/**
	 * Posiziona un pastore in una strada
	 * @param roadIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	Message placeShepherd(int roadIndex) throws RemoteException;
	
	/**
	 * Muove un pastore in una strada
	 * @param roadIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	Message moveShepherd(int roadIndex) throws RemoteException;
	
	/**
	 * Muove un certo tipo di ovino da una regione a opposta alla posizione del pastore
	 * @param parameters
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	Message moveSheep(int parameters[]) throws RemoteException;
	
	/**
	 * Uccide un ovino in una regione
	 * @param parameters
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	Message kill(int parameters[]) throws RemoteException;
	
	/**
	 * Accoppia una pecora e un montone in una regione
	 * @param regionIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	Message couple(int regionIndex) throws RemoteException;
	
	/**
	 * Accoppia due pecore in una regione
	 * @param regionIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	Message coupleSheep(int regionIndex) throws RemoteException;
	
	/**
	 * Acquista la carta più economica di un certo tipo di terreno
	 * @param terrainTypeIndex
	 * @return message, messaggio di esito
	 * @throws RemoteException
	 */
	Message buyCard(int terrainTypeIndex) throws RemoteException;
	
	/**
	 * Ottiene le azioni disponibili
	 * @return availableActiorns, azioni disponibili
	 * @throws RemoteException
	 */
	List<String> getAvailableActions() throws RemoteException;
	
	/**
	 * Imposta l'azione scelta
	 * @param chosenAction
	 * @throws RemoteException
	 */
	void setMadeActions(String chosenAction) throws RemoteException;
	
	/**
	 * Imposta i messaggi di Broadcast
	 * @param parameters
	 * @throws RemoteException
	 */
	void setBroadcastMessage(List<String> parameters) throws RemoteException;
	
	/**
	 * Ottiene le carte del giocatore
	 * @return personalCards
	 * @throws RemoteException
	 */
	List<String> getPersonalCards() throws RemoteException;
	
	/**
	 * Ottiene i soldi del giocatore
	 * @return money
	 * @throws RemoteException
	 */
	String getPersonalMoney() throws RemoteException;
	
	/**
	 * Invia le informazioni sulla plancia di gioco al gicoatore
	 * @param client
	 * @throws RemoteException
	 */
	void boardState(RMIClientInterface client) throws RemoteException;
	
	/**
	 * Invia le informazioni personali al  giocatore
	 * @param client
	 * @throws RemoteException
	 */
	void personalInfo(RMIClientInterface client) throws RemoteException;
}
