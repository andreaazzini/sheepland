package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;

import java.util.Arrays;

import it.polimi.deib.provaFinale2014.model.Sheep.SheepType;

import org.junit.Before;
import org.junit.Test;

public class SheepTest {
	private Sheep sheep;

	@Before
	public void setUp() {
		sheep = new Sheep(SheepType.LAMB);
	}

	@Test
	public void initialization() {
		assertTrue(Arrays.asList(SheepType.values()).contains(sheep.getType()));
	}
	
	@Test
	public void increaseTurnCounter() {
		int turn = sheep.getTurnCounter();
		sheep.increaseTurnCounter();
		assertEquals(turn + 1, sheep.getTurnCounter());
	}

}
