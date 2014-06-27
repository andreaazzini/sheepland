package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FenceGUITest {
	private FenceGUI fence;
	private int roadIndex;

	@Before
	public void setUp() {
		roadIndex = 0;
		fence = new FenceGUI(roadIndex);
	}

	@Test
	public void initialization() {
		assertEquals((int)(Default.roadHooks[roadIndex][0] * Default.scaleValue), fence.getX());
		assertEquals((int)(Default.roadHooks[roadIndex][1] * Default.scaleValue), fence.getY());
	}

}
