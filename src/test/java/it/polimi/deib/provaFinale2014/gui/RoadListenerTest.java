package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;

import org.junit.Before;
import org.junit.Test;

public class RoadListenerTest {
	private ClientCommunication client;
	private RoadGUI road;
	private SheeplandPanel panel;
	private RoadListener listener;

	@Before
	public void setUp() {
		client = new ClientHandler();
		road = new RoadGUI(0);
		panel = new SheeplandPanel(client);
		listener = new RoadListener(client, panel, road);
	}

	@Test
	public void actionPerformed() {
		assertFalse(road.isEnabled());
		listener.actionPerformed(new ActionEvent(client, 0, null));
		assertFalse(road.isEnabled());
	}

	@Test
	public void mouse() {
		listener.mouseEntered(new MouseEvent(road, 0, 0, 0, 0, 0, 0, false));
		assertTrue(road.isEnabled());
		listener.mouseExited(new MouseEvent(road, 0, 0, 0, 0, 0, 0, false));
		assertFalse(road.isEnabled());
	}
}
