package it.polimi.deib.provaFinale2014.controller;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.controller.Game;
import it.polimi.deib.provaFinale2014.controller.TurnHandler;
import it.polimi.deib.provaFinale2014.exceptions.ActionLimitExceededException;
import it.polimi.deib.provaFinale2014.model.Shepherd;

import org.junit.Before;
import org.junit.Test;

public class TurnHandlerTest {
	private TurnHandler turnHandler, turnHandler2;
	private Game game, game2;
	
	@Before
	public void setUp() {
		game = new Game(4);
		turnHandler = new TurnHandler(game);
		game.getActivePlayer().getMainShepherd().setPosition(game.getGameBoard().getRoads()[1]);
		
		game2 = new Game(2);
		turnHandler2 = new TurnHandler(game2);
		game2.getActivePlayer().getMainShepherd().setPosition(game2.getGameBoard().getRoads()[0]);
	}
	
	@Test
	public void initialization() throws ActionLimitExceededException {
		assertEquals(4, turnHandler.getActualTurn().availableActions(game.getActivePlayer(), game.getGameBank()).size());
		assertEquals(4, turnHandler2.getActualTurn().availableActions(game2.getActivePlayer(), game2.getGameBank()).size());
	}
	
	@Test
	public void changeTurn() {
		for (int i = 0; i < game.getPlayers().size(); i++) {
			assertSame(game.getActivePlayer(), game.getPlayers().get(i));
			turnHandler.changeTurn();
		}
		assertSame(game.getActivePlayer(), game.getPlayers().get(0));
		
		game2.getActivePlayer().setUsedShepherd(1);
		Shepherd opt = game2.getActivePlayer().getMainShepherd();
		for (int i = 0; i < game2.getPlayers().size(); i++) {
			turnHandler2.changeTurn();
		}
		game2.getActivePlayer().setUsedShepherd(0);
		// Se è avvenuta correttamente la restore
		assertNotSame(opt, game2.getActivePlayer().getMainShepherd());
	}
	
	@Test
	public void lambEvolution() {
		turnHandler.disableInitialTurn();
		assertFalse(turnHandler.isInitialTurn());
		
		// Cambia turno due volte
		for (int i = 0; i < 3; i++) {
			turnHandler.changeTurn();
		}
		int lambs = 0;
		for (int i = 0; i < game.getGameBoard().getRegions().length; i++) {
			lambs += game.getGameBoard().getRegions()[i].getLambTypeNumber();
		}
		// Se gli agnelli si sono evoluti
		assertEquals(0, lambs);
	}
	
	@Test
	public void consentTurnChanging() {
		assertTrue(turnHandler.consentTurnChanging());
		for (int i = 20; i < 24; i++) {
			game.getActivePlayer().getMainShepherd().setPosition(
					game.getGameBoard().getRoads()[i]);
			turnHandler.changeTurn();
		}
		for (int i = 0; i < 20; i++) {
			game.getActivePlayer().getMainShepherd().moveTo(
					game.getGameBoard().getRoads()[i], game.getGameBank().getFirstFence());
			turnHandler.changeTurn();
		}
		// Se cambia 20 volte il turno
		assertEquals(0, game.getGameBank().getAvailableFenceNumber());
		assertFalse(turnHandler.consentTurnChanging());
	}
}
