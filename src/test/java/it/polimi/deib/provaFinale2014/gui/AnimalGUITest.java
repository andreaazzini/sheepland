package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.gui.AnimalGUI.AnimalType;

import org.junit.Before;
import org.junit.Test;

public class AnimalGUITest {
	private AnimalGUI sheep;
	private AnimalType sheepType;
	private int sheepRegionIndex;
	
	private AnimalGUI lamb;
	private AnimalType lambType;
	private int lambRegionIndex;
	
	private AnimalGUI mutton;
	private AnimalType muttonType;
	private int muttonRegionIndex;
	
	private AnimalGUI blacksheep;
	private AnimalType blacksheepType;
	private int blacksheepRegionIndex;

	@Before
	public void setUp() {
		sheepType = AnimalType.SHEEP;
		sheepRegionIndex = 0;
		sheep = new AnimalGUI(sheepType, sheepRegionIndex);
		
		lambType = AnimalType.LAMB;
		lambRegionIndex = 0;
		lamb = new AnimalGUI(lambType, lambRegionIndex);
		
		muttonType = AnimalType.MUTTON;
		muttonRegionIndex = 0;
		mutton = new AnimalGUI(muttonType, muttonRegionIndex);
		
		blacksheepType = AnimalType.BLACKSHEEP;
		blacksheepRegionIndex = 1;
		blacksheep = new AnimalGUI(blacksheepType, blacksheepRegionIndex);
	}

	@Test
	public void sheepInitialization() {
		assertEquals(sheepType, sheep.getType());
		assertEquals(sheepRegionIndex, sheep.getRegionIndex());
		assertEquals((int)(Default.sheepRegionHooks[sheepRegionIndex][0] * Default.scaleValue), sheep.getX());
		assertEquals((int)(Default.sheepRegionHooks[sheepRegionIndex][1] * Default.scaleValue), sheep.getY());
		assertNotNull(sheep.getSheepTypeNumber());
	}
	
	@Test
	public void lambInitialization() {
		assertEquals(lambType, lamb.getType());
		assertEquals(lambRegionIndex, lamb.getRegionIndex());
		assertEquals((int)(Default.lambRegionHooks[lambRegionIndex][0] * Default.scaleValue), lamb.getX());
		assertEquals((int)(Default.lambRegionHooks[lambRegionIndex][1] * Default.scaleValue), lamb.getY());
		assertNotNull(lamb.getSheepTypeNumber());
	}
	
	@Test
	public void muttonInitialization() {
		assertEquals(muttonType, mutton.getType());
		assertEquals(muttonRegionIndex, mutton.getRegionIndex());
		assertEquals((int)(Default.muttonRegionHooks[muttonRegionIndex][0] * Default.scaleValue), mutton.getX());
		assertEquals((int)(Default.muttonRegionHooks[muttonRegionIndex][1] * Default.scaleValue), mutton.getY());
		assertNotNull(mutton.getSheepTypeNumber());
	}
	
	@Test
	public void blacksheepInitialization() {
		assertEquals(blacksheepType, blacksheep.getType());
		assertEquals(blacksheepRegionIndex, blacksheep.getRegionIndex());
		assertEquals((int)(Default.blacksheepRegionHooks[blacksheepRegionIndex][0] * Default.scaleValue), blacksheep.getX());
		assertEquals((int)(Default.blacksheepRegionHooks[blacksheepRegionIndex][1] * Default.scaleValue), blacksheep.getY());
		assertNull(blacksheep.getSheepTypeNumber());
	}
	
	@Test
	public void listener() {
		SheepListener listener = new SheepListener(null, null, sheep);
		sheep.setListener(listener);
		assertTrue(sheep.isEnabled());
		sheep.removeListener();
		assertFalse(sheep.isEnabled());
	}
}
