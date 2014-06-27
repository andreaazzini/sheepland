package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardButtonTest {
	private CardButton cardButton;

	@Before
	public void setUp() {
		cardButton = new CardButton(null);
	}

	@Test
	public void size() {
		assertEquals(Default.getActionButtonDimension(), cardButton.getSize());
		assertEquals(Default.getActionButtonDimension(), cardButton.getPreferredSize());
	}
	
	@Test
	public void number() {
		assertEquals(0, cardButton.getNumber().getNumber());
		cardButton.setNumber(7);
		assertEquals(7, cardButton.getNumber().getNumber());
	}
}
