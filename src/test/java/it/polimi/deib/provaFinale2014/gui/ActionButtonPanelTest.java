package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class ActionButtonPanelTest {
	private ActionButtonPanel abp;
	private Color background;
	
	@Before
	public void setUp() {
		abp = new ActionButtonPanel();
		background = new Color(33, 162, 246);
	}

	@Test
	public void initialization() {
		assertEquals(background, abp.getBackground());
		assertFalse(abp.getButton("MOVE_SHEPHERD").isEnabled());
		assertFalse(abp.getButton("MOVE_SHEEP").isEnabled());
		assertFalse(abp.getButton("COUPLE").isEnabled());
		assertFalse(abp.getButton("COUPLE_SHEEPS").isEnabled());
		assertFalse(abp.getButton("KILL").isEnabled());
		assertFalse(abp.getButton("BUY_CARD").isEnabled());
	}
	
	@Test
	public void getButtons() {
		assertEquals(abp.getButton("MOVE_SHEPHERD"), abp.getActionButtons()[0]);
		assertEquals(abp.getButton("MOVE_SHEEP"), abp.getActionButtons()[1]);
		assertEquals(abp.getButton("COUPLE"), abp.getActionButtons()[2]);
		assertEquals(abp.getButton("COUPLE_SHEEPS"), abp.getActionButtons()[3]);
		assertEquals(abp.getButton("KILL"), abp.getActionButtons()[4]);
		assertEquals(abp.getButton("BUY_CARD"), abp.getActionButtons()[5]);
	}

}
