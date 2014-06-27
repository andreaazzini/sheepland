package it.polimi.deib.provaFinale2014.RMIcommunication;


/**
 * Contenitore delle Informazioni riguardanti un Client in gioco
 *
 */
public class RMIClientInfo {
	private String nickname;
	private RMIClientInterface clientInterface;
	private boolean isOnline;
	private int playerIndex;
	
	/**
	 * Costruisce un nuovo contenitore di informazioni
	 * @param nickname
	 * @param client, interfaccia remota del client
	 */
	public RMIClientInfo(String nickname, RMIClientInterface client) {
		this.nickname = nickname;
		this.clientInterface = client;
		this.isOnline = true;
		
	}
	
	/**
	 * Imposta lo stato del client
	 */
	public void setOnline(boolean status) {
		this.isOnline = status;
	}
	
	/**
	 * Ottiene l'interfaccia 
	 * @return clientInterface
	 */
	public RMIClientInterface getClientInterface() {
		return this.clientInterface;
	}
	
	/**
	 * Imposta l'interfaccia
	 * @param client
	 */
	public void setClientInterface(RMIClientInterface client) {
		this.clientInterface = client;
		
	}
	
	/**
	 * Ottiene lo stato
	 * @return isOnline
	 */
	public boolean isOnline(){
		return this.isOnline;
	}

	/**
	 * Ottiene il Nickname
	 * @return nickname
	 */
	public String getNickname() {
		return this.nickname;
	}
	
	
	/**
	 * Imposta l'indice del giocatore
	 * @param index
	 */
	public void setPlayerIndex (int index) {
		this.playerIndex=index;
	}
	
	/**
	 * Ottiene l'indice del giocatore
	 * @return playerIndex
	 */
	public int getPlayerIndex () {
		return this.playerIndex;
	}
}

