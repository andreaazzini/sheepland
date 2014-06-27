package it.polimi.deib.provaFinale2014.communication;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.controller.Action;

import org.junit.Test;

public class ChoiceHandlerTest {
	private ChoiceHandler choiceHandler;
	
	
	@Test
	public void initialize() {
		choiceHandler = new ChoiceHandler(Action.KILL);
		assertNotNull(choiceHandler);
	}
}
