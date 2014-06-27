package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlayerButtonTest {
	private PlayerButton pb;

	@Before
	public void setUp() {
		pb = new PlayerButton(null);
	}

	@Test
	public void size() {
		assertEquals(Default.getActionButtonDimension(), pb.getSize());
		assertEquals(Default.getActionButtonDimension(), pb.getPreferredSize());
	}

}
