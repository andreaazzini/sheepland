package it.polimi.deib.provaFinale2014.exceptions;

/**
 * Eccezione che gestisce i casi di nickname già in uso
 */
public class NicknameAlreadyInUseException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Client is Offline";
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}
}
