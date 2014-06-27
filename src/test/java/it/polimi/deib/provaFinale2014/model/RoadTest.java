package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.Player;
import it.polimi.deib.provaFinale2014.model.Region;
import it.polimi.deib.provaFinale2014.model.Road;
import it.polimi.deib.provaFinale2014.model.Shepherd;

import org.junit.Before;
import org.junit.Test;

public class RoadTest {
	private Road road;
	private Region[] adjacentRegions;
	private int value;
	
	@Before
	public void setUp() {
		value = 1;
		adjacentRegions = new Region[2];
		for (int i = 0; i < 2; i++) {
			adjacentRegions[i] = new Region();
		}
		road = new Road(value, adjacentRegions[0], adjacentRegions[1]);
	}
	
	@Test
	public void initialization() {
		assertEquals(value, road.getValue());
		assertFalse(road.hasFence());
		assertFalse(road.hasShepherd());
		assertSame(adjacentRegions[0], road.getAdjacentRegions()[0]);
		assertSame(adjacentRegions[1], road.getAdjacentRegions()[1]);
	}
	
	@Test
	public void adjacentRoads() {
		Road[] roads = {
				new Road(value, new Region(), new Region()), 
				new Road(value, new Region(), new Region()),
				new Road(value, new Region(), new Region())
		};
		road.configureAdjacentRoads(roads);
		assertTrue(road.getAdjacentRoads().size() == 3);
	}
	
	@Test
	public void occupy() {
		Shepherd occupier = new Shepherd(new Player(false));
		road.setOccupier(occupier);
		assertSame(occupier, road.getShepherd());
	}
	
	@Test
	public void fences() {
		assertFalse(road.hasFence());
	}
}
