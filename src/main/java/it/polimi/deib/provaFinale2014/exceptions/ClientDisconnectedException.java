package it.polimi.deib.provaFinale2014.exceptions;

/**
 * Eccezione che gestisce la disconnessione di un client
 */
public class ClientDisconnectedException extends Exception {
	private static final long serialVersionUID = 1L; 
	private static final String MESSAGE = "Client disconnected";
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}
}
