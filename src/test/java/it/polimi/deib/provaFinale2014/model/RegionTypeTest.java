package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.DefaultBoard;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RegionTypeTest {
	private int index;
	private TerrainType expectedType;
	
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]
				{{TerrainType.HILL, 0}, {TerrainType.PLAIN, 3},
				{TerrainType.LAKE, 6}, {TerrainType.DESERT, 9},
				{TerrainType.MOUNTAIN, 12}, {TerrainType.COUNTRY, 15}}
				);
	}
	
	public RegionTypeTest(TerrainType expectedType, int index) {
		this.expectedType = expectedType;
		this.index = index;
	}
	
	@Test
	public void terrainMatch() {
		assertEquals(expectedType, new DefaultBoard().getRegions()[index].getType());
	}
}
