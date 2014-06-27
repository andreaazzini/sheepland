package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TextPanelTest {
	private TextPanel panel;
	
	@Before
	public void setUp() {
		panel = new TextPanel();
	}

	@Test
	public void initialization() {
		assertNotNull(panel.getTextArea());
	}
	
	@Test
	public void appendText() {
		String text = "text";
		String before = panel.getTextArea().getText();
		panel.appendText(text);
		assertEquals(text + "\n\n" + before, panel.getTextArea().getText());
	}

	@Test
	public void size() {
		assertEquals(Default.getTextPanelDimension(), panel.getSize());
		assertEquals(Default.getTextPanelDimension(), panel.getPreferredSize());
		assertEquals(Default.getTextPanelDimension(), panel.getMinimumSize());
		assertEquals(Default.getTextPanelDimension(), panel.getMaximumSize());
	}
}
