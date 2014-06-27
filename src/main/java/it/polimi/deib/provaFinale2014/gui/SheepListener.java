package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.gui.AnimalGUI.AnimalType;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Listener personalizzato associato alle pecore
 */
public class SheepListener extends MouseAdapter {
	private ClientCommunication client;
	private AnimalGUI sheep;
	private SheeplandPanel panel;
	
	/**
	 * Costruisce uno SheepListener
	 * @param panel panel principale
	 * @param sheep pecora cui viene associato il listener
	 */
	public SheepListener(ClientCommunication client, SheeplandPanel panel, AnimalGUI sheep) {
		this.client = client;
		this.panel = panel;
		this.sheep = sheep;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (StaticGUI.getKill()) {
			if (!sheep.getType().equals(AnimalType.WOLF) && !sheep.getType().equals(AnimalType.BLACKSHEEP)) {
				sheep.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		} else {
			if (!sheep.getType().equals(AnimalType.WOLF)) {
				sheep.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		sheep.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// Muove una pecora
		// Inserire una regione e un tipo di pecora
		if (StaticGUI.getKill()) {
			if (!sheep.getType().equals(AnimalType.WOLF) && !sheep.getType().equals(AnimalType.BLACKSHEEP)) {
				client.setRegion(String.valueOf(((AnimalGUI) e.getSource()).getRegionIndex()));
				panel.setSheepType(String.valueOf(((AnimalGUI) e.getSource()).getType()));
			}
		} else {
			if (!sheep.getType().equals(AnimalType.WOLF)) {
				client.setRegion(String.valueOf(((AnimalGUI) e.getSource()).getRegionIndex()));
				panel.setSheepType(String.valueOf(((AnimalGUI) e.getSource()).getType()));
			}
		}
		((AnimalGUI) e.getSource()).removeListener();
		panel.revalidate();
		panel.repaint();
	}

}
