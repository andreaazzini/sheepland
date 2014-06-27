package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RoadGUITest {
	private int roadIndex;
	private RoadGUI road;
	
	@Before
	public void setUp() {
		roadIndex = 0;
		road = new RoadGUI(roadIndex);
	}
	
	@Test
	public void position() {
		int radius = 25;
		assertEquals((int)(Default.roadHooks[roadIndex][0] * Default.scaleValue) + 1 - radius / 2, 
				road.getX());
		assertEquals((int)(Default.roadHooks[roadIndex][1] * Default.scaleValue) + 1 - radius / 2,
				road.getY());
	}

	@Test
	public void enabled() {
		assertFalse(road.isEnabled());
	}
	
	@Test
	public void roadIndex() {
		assertEquals(roadIndex, road.getRoadIndex());
	}
	
	@Test
	public void hasFence() {
		assertFalse(road.hasFence());
	}
}
