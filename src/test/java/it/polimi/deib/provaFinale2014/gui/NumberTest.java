package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;

public class NumberTest {
	private Number number;
	private JComponent component;

	@Before
	public void setUp() {
		component = new JButton();
		number = new Number(component);
	}

	@Test
	public void initialization() {
		assertEquals(1, number.getNumber());
		number.initialize();
		assertEquals(0, number.getNumber());
	}
	
	@Test
	public void increase() {
		int i = number.getNumber();
		number.increaseNumber();
		assertEquals(i + 1, number.getNumber());
	}
	
	@Test
	public void decrease() {
		int i = number.getNumber();
		number.decreaseNumber();
		assertEquals(i - 1, number.getNumber());
	}

}
