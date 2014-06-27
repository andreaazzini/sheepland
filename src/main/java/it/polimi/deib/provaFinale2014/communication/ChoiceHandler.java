package it.polimi.deib.provaFinale2014.communication;

import it.polimi.deib.provaFinale2014.controller.Action;
import it.polimi.deib.provaFinale2014.controller.Command;
import it.polimi.deib.provaFinale2014.exceptions.ClientDisconnectedException;
import it.polimi.deib.provaFinale2014.exceptions.InvalidActionException;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce la scelta dell'utente e chiede ulteriori parametri
 * in funzione di essa
 */
public class ChoiceHandler {
	
	private enum SheepTypeToKill {
		SHEEP, MUTTON, LAMB
	}
	private enum SheepTypeToMove {
		SHEEP, MUTTON, LAMB, BLACKSHEEP
	}


	
	// Logger utile per loggare messaggi
	private static final Logger LOGGER = 
			Logger.getLogger(ChoiceHandler.class.getName());
	private Action handlingAction;
	
	/**
	 * Costruisce un gestore delle scelte in base all'azione
	 * che il client ha deciso di eseguire
	 * @param action
	 */
	public ChoiceHandler(Action action) {
		handlingAction = action;
		LOGGER.setUseParentHandlers(false);
	}
	/**
	 * Gestisce l'azione presa dall'utente
	 * @param in buffer d'ingresso
	 * @param out buffer d'uscita
	 * @return array di parametri aggiuntivi
	 * @throws InvalidActionException
	 * @throws ClientDisconnectedException 
	 */
	public int[] handleAction(BufferedReader in, PrintWriter out)
	throws InvalidActionException, ClientDisconnectedException {
		int[] parameters = null;
		try {
			if (handlingAction.equals(Action.MOVE_SHEEP)) {
				parameters = handleSheepMovement(in, out);
			} else if (handlingAction.equals(Action.MOVE_SHEPHERD)) {
				parameters = handleShepherdMovement(in, out);
			} else if (handlingAction.equals(Action.COUPLE) ||
					handlingAction.equals(Action.COUPLE_SHEEPS)) {
				parameters = handleCoupling(in, out);
			} else if (handlingAction.equals(Action.KILL)) {
				parameters = handleKilling(in, out);
			} else if (handlingAction.equals(Action.BUY_CARD)) {
				parameters = handleBuying(in , out);
			} else {
				throw new InvalidActionException();
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Can't communicate with client", e);
		}
		return parameters;
	}
	
	private int[] handleBuying(BufferedReader in, PrintWriter out) throws IOException, ClientDisconnectedException {
		int typeIndex;
		while (true) {
			// Segnala al client che deve fornire un ulteriore parametro
			out.println(Command.INSERT_CARD_TYPE);
			// Ottiene la riposta
			try {
				String nextLine = in.readLine();
				if (nextLine == null) {
					throw new ClientDisconnectedException();
				}
				typeIndex = TerrainType.valueOf(nextLine).ordinal();
				
				break;
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Expected integer", e);
			}
		}

		return new int[] {typeIndex};
	}
	
	private int[] handleKilling(BufferedReader in, PrintWriter out)
	throws IOException, ClientDisconnectedException {
		int region;
		while (true) {
			// Segnala al client che deve fornire un ulteriore parametro
			out.println(Command.INSERT_KILL_REGION);
			// Ottiene la riposta
			try {
				String nextLine = in.readLine();
				if (nextLine == null) {
					throw new ClientDisconnectedException();
				}

				region = Integer.parseInt(nextLine);
				break;
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Expected integer", e);
			}
		}
		
		int sheepIndex;
		// Finché non ottengo un intero non procedo
		while (true) {
			// Segnala al client che deve fornire un ulteriore parametro
			out.println(Command.SHEEPS_TO_KILL);
			// Ottiene la riposta
			String nextLine = in.readLine();
			if (nextLine == null) {
				throw new ClientDisconnectedException();
			}
			try {
				sheepIndex = SheepTypeToKill.valueOf(nextLine).ordinal();
				break;
			} catch (IllegalArgumentException e) {
				LOGGER.log(Level.WARNING, "Illegal argument", e);
			}
		}
		// Costruisce l'array di parametri da restituire
		return new int[] {region, sheepIndex};
	}
	
	
	private int[] handleCoupling(BufferedReader in, PrintWriter out) 
	throws IOException {
		int region;
		while (true) {
			// Segnala al client che deve fornire un ulteriore parametro
			out.println(Command.INSERT_COUPLE_REGION);
			// Riceve la risposta
			try {
				region = Integer.parseInt(in.readLine());
				break;
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Expected integer", e);
			}
		}
		// Costruisce l'array di parametri da restituire

		return new int[] {region};
	}
	
	private int[] handleShepherdMovement(BufferedReader in, PrintWriter out)
	throws IOException, ClientDisconnectedException {
		int destinationRoad;
		while (true) {
			// Segnala al client che deve fornire un ulteriore parametro
			out.println(Command.INSERT_ROAD);
			// Riceve la risposta
			try {
				String nextLine = in.readLine();
				if (nextLine == null) {
					throw new ClientDisconnectedException();
				}
				destinationRoad = Integer.parseInt(nextLine);
				break;
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Expected integer", e);
			}
		}
		// Costruisce l'array di parametri da restituire
		return new int[] {destinationRoad};
	}
	
	private int[] handleSheepMovement(BufferedReader in, PrintWriter out)
	throws IOException, ClientDisconnectedException {				
		int startingRegion;
		// Finché non ottengo un intero non procedo
		while (true) {
			// Segnala al client che deve fornire un ulteriore parametro
			out.println(Command.INSERT_STARTING_REGION);
			// Riceve la risposta
			try {
				String nextLine = in.readLine();
				if (nextLine == null) {
					throw new ClientDisconnectedException();
				}
				startingRegion = Integer.parseInt(nextLine);
				break;
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Expected integer", e);
			}
		}
		// Segnala al client che deve fornire un ulteriore parametro
		int sheepIndex;
		while (true) {
			out.println(Command.SHEEPS_TO_MOVE);
			// Ottiene la riposta
			// Finché non ottengo un intero non procedo
			String nextLine = in.readLine();
			if (nextLine == null) {
				throw new ClientDisconnectedException();
			}
			try {
				sheepIndex = SheepTypeToMove.valueOf(nextLine).ordinal();
				break;
			} catch (IllegalArgumentException e1) {
				LOGGER.log(Level.WARNING, "Illegal argument", e1);
			}
		}
		// Costruisce l'array di parametri da restituire
		return new int[] {startingRegion, sheepIndex};
	}
}
