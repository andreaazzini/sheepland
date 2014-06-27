package it.polimi.deib.provaFinale2014.exceptions;

/**
 * Eccezione che gestisce la scelta di un'azione non valida
 */
public class InvalidActionException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Chosen action does not exist";
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}
}
