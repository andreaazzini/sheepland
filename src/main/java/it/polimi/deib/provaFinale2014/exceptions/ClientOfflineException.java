package it.polimi.deib.provaFinale2014.exceptions;

/**
 * Eccezione che gestisce il caso di client offline
 */
public class ClientOfflineException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Client is Offline";
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}
}
