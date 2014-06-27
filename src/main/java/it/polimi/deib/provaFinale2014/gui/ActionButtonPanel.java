package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.controller.Action;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel che contiene gli ActionButton
 */
public class ActionButtonPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ActionButton moveShepherdButton;
	private ActionButton moveSheepButton;
	private ActionButton coupleButton;
	private ActionButton coupleSheepsButton;
	private ActionButton killButton;
	private ActionButton buyCardButton;
	
	/**
	 * Costruisce un nuovo ActionButtonPanel
	 */
	public ActionButtonPanel() {
		this.setBackground(new Color(33, 162, 246));
		this.setLayout(new FlowLayout());
		
		moveShepherdButton = new ActionButton(new ImageIcon(Default.moveShepherdButtonUrl));
		moveSheepButton = new ActionButton(new ImageIcon(Default.moveSheepButtonUrl));
		coupleButton = new ActionButton(new ImageIcon(Default.coupleButtonUrl));
		coupleSheepsButton = new ActionButton(new ImageIcon(Default.coupleSheepsButtonUrl));
		killButton = new ActionButton(new ImageIcon(Default.killButtonUrl));
		buyCardButton = new ActionButton(new ImageIcon(Default.buyCardButtonUrl));
		
		this.add(moveShepherdButton);
		this.add(moveSheepButton);
		this.add(coupleButton);
		this.add(coupleSheepsButton);
		this.add(killButton);
		this.add(buyCardButton);
		
		moveShepherdButton.setEnabled(false);
		moveSheepButton.setEnabled(false);
		coupleButton.setEnabled(false);
		coupleSheepsButton.setEnabled(false);
		killButton.setEnabled(false);
		buyCardButton.setEnabled(false);
	}
	/**
	 * Restituisce l'ActionButton corrispondente all'azione
	 * selezionata
	 * @param action azione selezionata
	 * @return ActionButton dell'azione
	 */
	public ActionButton getButton(String action) {
		ActionButton returnButton;
		if (action.equals(Action.MOVE_SHEPHERD.toString())) {
			returnButton = moveShepherdButton;
		} else if (action.equals(Action.MOVE_SHEEP.toString())) {
			returnButton = moveSheepButton;
		} else if (action.equals(Action.COUPLE.toString())) {
			returnButton = coupleButton;
		} else if (action.equals(Action.COUPLE_SHEEPS.toString())) {
			returnButton = coupleSheepsButton;
		} else if (action.equals(Action.KILL.toString())) {
			returnButton = killButton;
		} else if (action.equals(Action.BUY_CARD.toString())) {
			returnButton = buyCardButton;
		} else {
			returnButton = null;
		}
		return returnButton;
	}
	/**
	 * Attiva i listener degli ActionButton
	 */
	public void activateListeners(ClientCommunication client) {
		moveShepherdButton.addActionListener(new ActionButtonListener(client, this, 0));
		moveSheepButton.addActionListener(new ActionButtonListener(client, this, 1));
		coupleButton.addActionListener(new ActionButtonListener(client, this, 2));
		coupleSheepsButton.addActionListener(new ActionButtonListener(client, this, 3));
		killButton.addActionListener(new ActionButtonListener(client, this, 4));
		buyCardButton.addActionListener(new ActionButtonListener(client, this, 5));
	}
	/**
	 * Restituisce gli ActionButton appartenenti al panel
	 * @return array di ActionButton
	 */
	public ActionButton[] getActionButtons() {
		return new ActionButton[] {
				moveShepherdButton, moveSheepButton, coupleButton,
				coupleSheepsButton, killButton, buyCardButton
		};
	}

	@Override
	public Dimension getSize() {
		return Default.getActionButtonPanelDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getActionButtonPanelDimension();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return Default.getActionButtonPanelDimension();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return Default.getActionButtonPanelDimension();
	}
}

