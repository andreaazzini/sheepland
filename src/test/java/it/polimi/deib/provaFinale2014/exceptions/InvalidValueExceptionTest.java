package it.polimi.deib.provaFinale2014.exceptions;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class InvalidValueExceptionTest {
	private InvalidValueException e;
	
	@Before
	public void setUp() {
		e = new InvalidValueException();
	}
	
	@Test
	public void getMessage() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field message = InvalidValueException.class.getDeclaredField("MESSAGE");
		message.setAccessible(true);
		assertEquals(message.get(e), e.getMessage());
	}
}
