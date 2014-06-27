package it.polimi.deib.provaFinale2014.gui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

/**
 * Classe dedicata alla gestione della GUI dinamica
 */
public class DynamicGUI extends StaticGUI {
	private static int chosenShepherd;
	private static final Logger LOGGER = Logger.getLogger(DynamicGUI.class.getName());
	
	/**
	 * Costruisce una nuova GUI dinamica dal gestore di comunicazione del client
	 * @param clientCommunication gestore di comunicazione del client
	 */
	public DynamicGUI(ClientCommunication clientCommunication) {
		super(clientCommunication);
		
		LOGGER.setUseParentHandlers(false);
	}
	
	@Override
	/**
	 * Rappresenta nella GUI il movimento di una pecora
	 */
	public void printMoveSheep(String player, String start,
		String generalSheepType, String destination) {
		final String myStart = start;
		final String mySheepType = generalSheepType;
		final String myDestination = destination;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Esegue la run dell'invokeLater
			 */
			public void run() {
				// Prende l'ovino che deve essere mosso
				AnimalGUI sheep = boardPanel.getSheep(mySheepType, Integer.parseInt(myStart));
				// Muove l'ovino nella strada di destinazione
				boardPanel.moveSheepWithAnimation(sheep, Integer.parseInt(myDestination));
			}
		});
		
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			LOGGER.log(Level.INFO, "Timer stopped", e);
		}
		
	}
	
	@Override
	/**
	 * Inserisce la strada nella quale muovere un pastore
	 */
	public void insertRoad() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Esegue la run dell'invokeLater
			 */
			public void run() {
				if (playersNicknames.size() == 2) {
					if (boardPanel.getShepherd(getColor(playerNumber)) == null ||
							boardPanel.getShepherd(getColor(playerNumber + 1)) == null) {
						textPanel.appendText("Choose a road to place your shepherd");
						boardPanel.addRoadListeners();
					} else {
						DraggableComponent shepherd = boardPanel.getShepherds().get(
								playerNumber * 2 + chosenShepherd);
						textPanel.appendText("Drag and drop your shepherd to the"
								+ "destination road");
						// Attiva i listener di drag and drop
						shepherd.setDraggable(true);
						shepherd.addMouseListener(new ShepherdDragListener(clientCommunication, shepherd));
					}
				} else {
					if (boardPanel.getMyShepherd() == null) {
						textPanel.appendText("Choose a road to place your shepherd");
						// Attiva i listener delle road
						boardPanel.addRoadListeners();
					} else {
						DraggableComponent shepherd = boardPanel.getMyShepherd();
						textPanel.appendText("Drag and drop your shepherd to the destination road");
						// Attiva i listener di drag and drop
						shepherd.setDraggable(true);
						shepherd.addMouseListener(new ShepherdDragListener(clientCommunication, shepherd));
					}
				}

			}
		});
	}

	@Override
	/**
	 * Rappresenta nella GUI i giocatori
	 */
	public void printPlayersInGame(List<String> players) {
		final List<String> playersNicks = players;
		SwingUtilities.invokeLater( new Runnable() {
			/**
			 * Esegue la run dell'invokeLater
			 */
			public void run() {
				for (int i =0 ; i< playersNicks.size(); i++) {
					playersNicknames.add(playersNicks.get(i));
				}
			}
		});
	}
	/**
	 * Imposta il pastore scelto
	 * @param shepherd pastore
	 */
	public static void setChosenShepherd(int shepherd) {
		chosenShepherd = shepherd;
	}
}
