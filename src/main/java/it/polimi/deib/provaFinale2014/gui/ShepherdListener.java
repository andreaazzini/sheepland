package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener dedicato alla scelta di un pastore
 */
public class ShepherdListener implements ActionListener {
	private ClientCommunication client;
	private TurnPanel panel;
	private int player;

	/**
	 * Costruisce uno ShepherdListener dal gestore della comunicazione del client,
	 * dal turnPanel e dal player
	 * @param client gestore della comunicazione del client
	 * @param panel turnPanel
	 * @param player giocatore
	 */
	public ShepherdListener(ClientCommunication client, TurnPanel panel, int player) {
		this.client = client;
		this.panel = panel;
		this.player = player;
	}
	/**
	 * Sceglie il pastore a cui dare il turno
	 */
	public void actionPerformed(ActionEvent e) {
		panel.giveTurnTo(player);
		DynamicGUI.setChosenShepherd(player % 2);
		client.setChosenShepherd(player % 2);
	}

}
