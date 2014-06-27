package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Motion Listener per la gestione del drag & drog
 * degli shepherd
 */
public class ShepherdDragListener extends MouseAdapter {
	private ClientCommunication client;
	private DraggableComponent handle;
	
	private static final int RADIUS = 18;
	private static final int SHIFTING = 20;
	
	/**
	 * Costruisce un listener per un pastore
	 * @param panel panel contenente i pastori
	 * @param handle pastore a cui è associato il listener
	 */
	public ShepherdDragListener(ClientCommunication client, DraggableComponent handle) {
		this.client = client;
		this.handle = handle;
	}
	
	@Override
    public void mouseReleased(MouseEvent e) {
    	int roadIndex = ((ShepherdGUI)handle).getRoadIndex();
    	int i;
    	for (i = 0; i < Default.roadHooks.length; i++) {
    		int x = (int)((Default.roadHooks[i][0] - SHIFTING) * Default.scaleValue);
    		int y = (int)((Default.roadHooks[i][1] - SHIFTING) * Default.scaleValue);
    		if (x - RADIUS < handle.getPosition().x && handle.getPosition().x < x + RADIUS &&
    				y - RADIUS < handle.getPosition().y && handle.getPosition().y < y + RADIUS) {
    			break;
    		}
    	}
    	
    	if (roadIndex != i && i < Default.roadHooks.length) {
        	client.setRoad(String.valueOf(i));
        	handle.setDraggable(false);
    	} else {
    		((ShepherdGUI)handle).move(roadIndex);
    		int x = (int)((Default.roadHooks[roadIndex][0] - SHIFTING) * Default.scaleValue);
    		int y = (int)((Default.roadHooks[roadIndex][1] - SHIFTING) * Default.scaleValue);
    		((ShepherdGUI)handle).setLocation(x, y);
    		((ShepherdGUI)handle).getParent().repaint();
    	}
    }
}
