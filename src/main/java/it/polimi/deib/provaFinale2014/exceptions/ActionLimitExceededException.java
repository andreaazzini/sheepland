package it.polimi.deib.provaFinale2014.exceptions;

/**
 * Eccezione che gestisce il superamento del limite
 * di azioni eseguibili in un turno di gioco
 */
public class ActionLimitExceededException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Action limit exceeded";
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}
}
