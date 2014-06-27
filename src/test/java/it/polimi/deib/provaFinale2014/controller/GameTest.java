package it.polimi.deib.provaFinale2014.controller;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.controller.Game;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
	private final static int MAX_SOCKETS = 4;
	private List<Socket> sockets;
	private Game game;
	
	@Before
	public void setUp() {
		sockets = new ArrayList<Socket>();
		for (int i = 0; i < MAX_SOCKETS; i++) {
			sockets.add(new Socket());
		}
		game = new Game(sockets.size());
	}
	
	
	@Test
	public void animalsInRegions() {
		for (int i = 0; i < game.getGameBoard().getRegions().length - 1; i++) {
			assertEquals(1, game.getGameBoard().getRegions()[i].getAnimals().size());
		}
		assertEquals(2, game.getGameBoard().getSheepsburg().getAnimals().size());
	}
	
	@Test
	public void initialCards() {
		for (int i = 0; i < MAX_SOCKETS - 1; i++) {
			assertNotEquals(game.getPlayers().get(i).getCards().get(0),
					game.getPlayers().get(i + 1).getCards().get(0));
		}
	}
	
	@Test
	public void players() {
		assertEquals(sockets.size(), game.getPlayers().size());
		assertSame(game.getActivePlayer(), game.getPlayers().get(0));
		for (int i = 1; i < game.getPlayers().size(); i++) {
			assertFalse(game.getPlayers().get(i).isActive());
		}
		assertTrue(game.getPlayers().get(0).isActive());
	}
}
