package it.polimi.deib.provaFinale2014.exceptions;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class ClientDisconnectedExceptionTest {
	private ClientDisconnectedException e;
	
	@Before
	public void setUp() {
		e = new ClientDisconnectedException();
	}
	
	@Test
	public void getMessage() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field message = ClientDisconnectedException.class.getDeclaredField("MESSAGE");
		message.setAccessible(true);
		assertEquals(message.get(e), e.getMessage());
	}
}
