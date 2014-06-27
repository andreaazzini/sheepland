package it.polimi.deib.provaFinale2014.controller;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import it.polimi.deib.provaFinale2014.controller.Action;
import it.polimi.deib.provaFinale2014.controller.Turn;
import it.polimi.deib.provaFinale2014.exceptions.ActionLimitExceededException;

import org.junit.Before;
import org.junit.Test;

public class TurnTest {
	private Turn turn;
	private Game game;
	private List<Socket> sockets;
	
	@Before
	public void setUp() {
		sockets = new ArrayList<Socket>();
		for (int i = 0; i < 4; i++) {
			sockets.add(new Socket());
		}
		game = new Game(sockets.size());
		turn = new Turn();
		game.getActivePlayer().getMainShepherd().setPosition(game.getGameBoard().getRoads()[1]);
		}
	
	@Test
	public void availableActions() throws ActionLimitExceededException {
		assertEquals(4, turn.availableActions(game.getActivePlayer(), game.getGameBank()).size());
		turn.setChosenAction(Action.MOVE_SHEPHERD);
		assertEquals(4, turn.availableActions(game.getActivePlayer(),game.getGameBank()).size());
		turn.setChosenAction(Action.BUY_CARD);
		assertEquals(3, turn.availableActions(game.getActivePlayer(),game.getGameBank()).size());
		turn.setChosenAction(Action.MOVE_SHEPHERD);
		assertEquals(4, turn.availableActions(game.getActivePlayer(),game.getGameBank()).size());
	}
	
	@Test
	public void lastAction() throws ActionLimitExceededException {
		turn.setChosenAction(Action.KILL);
		turn.setChosenAction(Action.COUPLE);
		assertTrue(turn.availableActions(game.getActivePlayer(), game.getGameBank()).contains(Action.MOVE_SHEPHERD) &&
				turn.availableActions(game.getActivePlayer(), game.getGameBank()).size() == 1);
	}
	
	@Test
	public void lastActionWhenShepherdMoved() throws ActionLimitExceededException {
		turn.setChosenAction(Action.MOVE_SHEPHERD);
		turn.setChosenAction(Action.COUPLE);
		assertEquals(4, turn.availableActions(game.getActivePlayer(),game.getGameBank()).size());
	}
}
