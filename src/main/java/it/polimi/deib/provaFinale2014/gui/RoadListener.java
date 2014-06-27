package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Listener personalizzato per la gestione del click su una strada
 */
public class RoadListener extends MouseAdapter implements ActionListener {
	private ClientCommunication client;
	private RoadGUI road;
	private SheeplandPanel panel;
	
	/**
	 * Costruisce un listener per una strada
	 * @param panel panel contenente le strade
	 * @param road strada a cui è associato il listener
	 */
	public RoadListener(ClientCommunication client, SheeplandPanel panel, RoadGUI road) {
		this.client = client;
		this.panel = panel;
		this.road = road;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (!panel.getOccupiedRoads()[road.getRoadIndex()] && !((RoadGUI)e.getSource()).hasFence()) {
			road.setEnabled(true);
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		road.setEnabled(false);
	}

	/**
	 * Invia il segnale del click della strada al gestore della
	 * comunicazione lato client
	 */
	public void actionPerformed(ActionEvent e) {
		client.setRoad(String.valueOf(road.getRoadIndex()));
		for (RoadGUI road : panel.getRoads()) {
			road.setEnabled(false);
			panel.remove(road);
		}
		panel.revalidate();
		panel.repaint();
	}
}
