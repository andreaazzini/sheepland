package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InfoButtonTest {
	private InfoButton infoButton;

	@Before
	public void setUp() {
		infoButton = new InfoButton(null);
	}

	@Test
	public void size() {
		assertEquals(Default.getActionButtonDimension(), infoButton.getSize());
		assertEquals(Default.getActionButtonDimension(), infoButton.getPreferredSize());
	}

	@Test
	public void number() {
		assertEquals(0, infoButton.getNumber().getNumber());
		infoButton.setNumber(7);
		assertEquals(7, infoButton.getNumber().getNumber());
	}
}
