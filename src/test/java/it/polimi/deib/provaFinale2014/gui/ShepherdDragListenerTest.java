package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;

import org.junit.Before;
import org.junit.Test;

public class ShepherdDragListenerTest {
	private ShepherdDragListener dragListener;
	private ClientCommunication client;
	private DraggableComponent handle;

	@Before
	public void setUp() {
		client = new ClientHandler();
		handle = new DraggableComponent();
		dragListener = new ShepherdDragListener(client, handle);
	}

	@Test
	public void initialization() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field client = ShepherdDragListener.class.getDeclaredField("client");
		Field handle = ShepherdDragListener.class.getDeclaredField("handle");
		client.setAccessible(true);
		handle.setAccessible(true);
		assertSame(this.client, client.get(dragListener));
		assertSame(this.handle, handle.get(dragListener));
	}
}
