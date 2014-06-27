package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.Bank;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Classe di test parametrica, che verifica le funzionalità
 * della classe Bank
 */
@RunWith(Parameterized.class)
public class BankTest {
	private Bank bank;
	private int index;
	private TerrainType expectedType;
	private int expectedCost;
	private int expectedFenceNumber;
	
	public BankTest(TerrainType expectedType, int expectedCost, int index) {
		this.bank = new Bank();
		this.expectedType = expectedType;
		this.expectedCost = expectedCost;
		this.index = index;
		expectedFenceNumber = 20;
	}
	
	/**
	 * Definizione dei parametri utilizzati nel test
	 * @return parametri utilizzati nel test
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]
				{{TerrainType.HILL, 0, 0}, {TerrainType.PLAIN, 0, 5},
				{TerrainType.LAKE, 0, 10}, {TerrainType.DESERT, 0, 15},
				{TerrainType.MOUNTAIN, 1, 21}, {TerrainType.COUNTRY, 2, 27}}
				);
	}
	
	/**
	 * Verifica che, alla creazione del banco, le carte abbiano
	 * inizializzato il proprio tipo terreno in modo corretto
	 */
	@Test
	public void terrainMatch() {
		assertEquals(expectedType, bank.getAvailableCards().get(index).getTerrainType());
	}
	
	/**
	 * Verifica che, alla creazione del banco, le carte abbiano
	 * inizializzato il proprio costo in modo corretto
	 */
	@Test
	public void costMatch() {
		assertEquals(expectedCost, bank.getAvailableCards().get(index).getCost());
	}
	
	/**
	 * Verifica che il numero dei recinti all'interno del banco
	 * sia inizializzato correttamente
	 */
	@Test
	public void fenceNumber() {
		assertEquals(expectedFenceNumber, bank.getAvailableFenceNumber());
	}
}