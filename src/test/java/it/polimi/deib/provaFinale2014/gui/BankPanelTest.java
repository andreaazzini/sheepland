package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class BankPanelTest {
	private BankPanel bankPanel;
	private Color background;

	@Before
	public void setUp() throws Exception {
		bankPanel = new BankPanel();
		background = Default.getBlue();
	}

	@Test
	public void initialization() {
		assertEquals(background, bankPanel.getBackground());
		assertFalse(bankPanel.getCardButton(0).isEnabled());
		assertFalse(bankPanel.getCardButton(1).isEnabled());
		assertFalse(bankPanel.getCardButton(2).isEnabled());
		assertFalse(bankPanel.getCardButton(3).isEnabled());
		assertFalse(bankPanel.getCardButton(4).isEnabled());
		assertFalse(bankPanel.getCardButton(5).isEnabled());
	}

}
