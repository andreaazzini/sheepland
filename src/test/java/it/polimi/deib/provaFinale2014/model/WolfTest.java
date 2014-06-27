package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.Region;
import it.polimi.deib.provaFinale2014.model.Sheep;
import it.polimi.deib.provaFinale2014.model.Wolf;

import org.junit.Before;
import org.junit.Test;

public class WolfTest {
	private Wolf wolf;
	private Region region;
	
	@Before
	public void setUp() {
		wolf = new Wolf();
		region = new Region();
		// Aggiunge una pecora alla regione
		region.addAnimal(new Sheep());
		// Aggiunge il lupo alla regione
		region.addAnimal(wolf);
		wolf.setPosition(region);
	}
	
	@Test
	public void position() {
		assertEquals(region, wolf.getPosition());
	}
	
	@Test
	public void eatSheep() {
		assertTrue(region.getSheeps().size() == 1);
		wolf.eatRandomSheep();
		assertTrue(region.getAnimals().size() == 1);
		assertTrue(region.getSheeps().size() == 0);
	}
}
