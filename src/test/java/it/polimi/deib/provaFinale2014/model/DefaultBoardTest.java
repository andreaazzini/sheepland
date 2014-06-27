package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.DefaultBoard;
import it.polimi.deib.provaFinale2014.model.Region;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DefaultBoardTest {
	private static DefaultBoard board;
	private int index;
	private int expectedValue;
	private Region expectedRegion1;
	private Region expectedRegion2;
	
	@Parameters
	public static Collection<Object[]> data() {
		board = new DefaultBoard();
		return Arrays.asList(new Object[][] 
				{{2, board.getRegions()[0], board.getRegions()[16], 0},
				{1, board.getRegions()[3], board.getRegions()[4], 7},
				{6, board.getRegions()[7], board.getRegions()[8], 14},
				{3, board.getRegions()[11], board.getRegions()[12], 21},
				{6, board.getRegions()[14], board.getRegions()[16], 28},
				{4, board.getRegions()[5], board.getSheepsburg(), 35},
				});
	}
	
	public DefaultBoardTest(int expectedValue, Region expectedRegion1, Region expectedRegion2, int index) {
		this.expectedValue = expectedValue;
		this.expectedRegion1 = expectedRegion1;
		this.expectedRegion2 = expectedRegion2;
		this.index = index;
	}
	
	@Test
	public void valueMatch() {
		assertEquals(expectedValue, board.getRoads()[index].getValue());
	}
	
	@Test
	public void adjacency() {
		assertEquals(expectedRegion1, board.getRoads()[index].getAdjacentRegions()[0]);
		assertEquals(expectedRegion2, board.getRoads()[index].getAdjacentRegions()[1]);
	}
}
