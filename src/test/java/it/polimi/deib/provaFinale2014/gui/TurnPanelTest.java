package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class TurnPanelTest {
	private TurnPanel panel;
	private Color background;

	@Before
	public void setUp() {
		panel = new TurnPanel();
		background = new Color(37, 163, 252);
	}

	@Test
	public void initialization() {
		assertEquals(background, panel.getBackground());
		assertNotNull(panel.getAvailableMoney());
		assertNotNull(panel.getRemainingFences());
	}
}
