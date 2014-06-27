package it.polimi.deib.provaFinale2014.exceptions;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class OccupiedPortExceptionTest {
	private OccupiedPortException e;
	
	@Before
	public void setUp() {
		int port = 2014;
		e = new OccupiedPortException(port);
	}
	
	@Test
	public void getMessage() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field message = OccupiedPortException.class.getDeclaredField("message");
		message.setAccessible(true);
		assertEquals(message.get(e), e.getMessage());
	}
}
