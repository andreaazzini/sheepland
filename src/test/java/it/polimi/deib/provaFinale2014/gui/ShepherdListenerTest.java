package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class ShepherdListenerTest {
	private ShepherdListener listener;
	private ClientCommunication client;
	private TurnPanel panel;
	private int player;

	@Before
	public void setUp() {
		client = new ClientHandler();
		panel = new TurnPanel();
		player = 0;
		listener = new ShepherdListener(client, panel, player);
	}

	@Test
	public void initialization() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field client = ShepherdListener.class.getDeclaredField("client");
		Field panel = ShepherdListener.class.getDeclaredField("panel");
		Field player = ShepherdListener.class.getDeclaredField("player");
		client.setAccessible(true);
		panel.setAccessible(true);
		player.setAccessible(true);
		assertSame(this.client, client.get(listener));
		assertSame(this.panel, panel.get(listener));
		assertEquals(this.player, player.get(listener));
	}
	
	@Test
	public void actionPerformed() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		listener.actionPerformed(new ActionEvent(client, player, null));
		Field player0 = TurnPanel.class.getDeclaredField("player1");
		player0.setAccessible(true);
		assertTrue(((PlayerButton)player0.get(panel)).isEnabled());
		
		Field chosenShepherd = DynamicGUI.class.getDeclaredField("chosenShepherd");
		chosenShepherd.setAccessible(true);
		assertEquals(player % 2, chosenShepherd.get(DynamicGUI.class));
	}
}
