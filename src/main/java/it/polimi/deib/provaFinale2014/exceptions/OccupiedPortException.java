package it.polimi.deib.provaFinale2014.exceptions;

/**
 * Eccezione che gestisce il mancato accesso ad una porta
 */
public class OccupiedPortException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String message;
	
	/**
	 * Costruisce l'eccezione indicando a quale porta
	 * ci si riferisce
	 * @param port
	 */
	public OccupiedPortException(int port) {
		message = "Port" + port + " occupied";
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
