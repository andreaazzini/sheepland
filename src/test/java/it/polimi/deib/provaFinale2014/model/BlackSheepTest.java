package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.BlackSheep;
import it.polimi.deib.provaFinale2014.model.Region;
import it.polimi.deib.provaFinale2014.model.Sheep;

import org.junit.Before;
import org.junit.Test;

/**
 * Classe di test che verifica la correttezza delle
 * funzionalità implementate dalla classe BlackSheep
 */
public class BlackSheepTest {
	private BlackSheep blackSheep;
	private Region region;
	
	/**
	 * Inizializza una pecora nera e una regione,
	 * settata come posizione della pecora nera
	 */
	@Before
	public void setUp() {
		blackSheep = new BlackSheep();
		region = new Region();
		// Imposta la posizione della pecora nera
		blackSheep.setPosition(region);
	}
	
	@Test
	public void isSheep() {
		assertTrue(blackSheep instanceof Sheep);
	}
	/**
	 * Verifica che, alla creazione di una pecora nera, il suo
	 * tipo sia assegnato correttamente
	 */
	@Test
	public void typeTest() {
		assertEquals(blackSheep.getType(), Sheep.SheepType.SHEEP);
	}
	
	/**
	 * Verifica che funzioni il metodo setPosition all'interno
	 * della classe BlackSheep
	 */
	@Test
	public void positionTest() {
		assertEquals(blackSheep.getPosition(), region);
	}
	
	/**
	 * Verifica che, alla creazione di una pecora nera, il suo
	 * valore sia assegnato correttamente
	 */
	@Test
	public void valueTest() {
		assertEquals(2, blackSheep.getValue());
	}

}
