package it.polimi.deib.provaFinale2014.controller;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

public class MessageTest {
	@Test
	/**
	 * Verifica che getMessage restituisca l'attributo private message
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void getMessage() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		for (Message msg : Message.values()) {
			Field message = Message.class.getDeclaredField("message");
			message.setAccessible(true);
			assertEquals(message.get(msg), msg.getMessage());
		}
	}

}
