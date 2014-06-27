package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener personalizzato di un ActionButton
 */
public class ActionButtonListener implements ActionListener {
	private ClientCommunication client;
	private ActionButtonPanel actionPanel;
	private int action;
	
	/**
	 * Costruisce un ActionButtonListener dati uno SheeplandPanel e un'azione
	 * @param gui GUI cui il listener si riferisce
	 * @param boardPanel panel contenente la plancia di gioco
	 * @param action azione eseguita
	 */
	public ActionButtonListener(ClientCommunication client, ActionButtonPanel actionPanel, int action) {
		this.client = client;
		this.actionPanel = actionPanel;
		this.action = action;
	}
	/**
	 * Al click esegue l'azione corrispondente all'ActionButton cliccato
	 */
	public void actionPerformed(ActionEvent e) {
		switch (action) {
		case 0:
			client.setChoice("MOVE_SHEPHERD");
			break;
		case 1:
			client.setChoice("MOVE_SHEEP");
			break;
		case 2:
			client.setChoice("COUPLE");
			break;
		case 3:
			client.setChoice("COUPLE_SHEEPS");
			break;
		case 4:
			client.setChoice("KILL");
			StaticGUI.setKill(true);
			break;
		case 5:
			client.setChoice("BUY_CARD");
			break;
		default:
			break;
		}
		for (ActionButton button : actionPanel.getActionButtons()) {
			button.setEnabled(false);
		}
	}
}
