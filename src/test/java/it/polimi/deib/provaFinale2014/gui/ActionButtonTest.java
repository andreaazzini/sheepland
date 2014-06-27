package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

public class ActionButtonTest {
	private ActionButton actionButton;
	
	@Before
	public void setUp() {
		actionButton = new ActionButton(new ImageIcon());
	}

	@Test
	public void size() {
		assertEquals(Default.getActionButtonDimension(), actionButton.getSize());
		assertEquals(Default.getActionButtonDimension(), actionButton.getPreferredSize());
	}

}
