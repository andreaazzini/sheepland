package it.polimi.deib.provaFinale2014.exceptions;

/**
 * Eccezione che gestisce l'inserimento di una regione
 * non esistente
 */
public class InvalidValueException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Invalid value selected";
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}

}
