package it.polimi.deib.provaFinale2014.exceptions;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class ActionLimitExceededExceptionTest {
	private ActionLimitExceededException e;
	
	@Before
	public void setUp() {
		e = new ActionLimitExceededException();
	}
	
	@Test
	public void getMessage() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field message = ActionLimitExceededException.class.getDeclaredField("MESSAGE");
		message.setAccessible(true);
		assertEquals(message.get(e), e.getMessage());
	}
}
