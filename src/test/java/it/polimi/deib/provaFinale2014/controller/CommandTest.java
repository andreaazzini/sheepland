package it.polimi.deib.provaFinale2014.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommandTest {
	@Test
	/**
	 * Verifica che il metodo name equivalga al toString se
	 * non specificato diversamente
	 */
	public void name() {
		for (Command cmd : Command.values()) {
			assertEquals(cmd.name(), cmd.toString());
		}
	}

}
