package it.polimi.deib.provaFinale2014.model;

/**
 * Dado a 6 facce presente nel gioco Sheepland
 */
public class Dice {
	/**
	 * Numero delle facce del dado
	 */
	private static final int VALUES = 6;
	
	private Dice() {
		/*
		 * Costruttore privato che impedisce che Dice,
		 * classe utilità, sia inizializzato in alcun
		 * modo
		 */
	}
	/**
	 * Tira il dado
	 * @return valore generato dal tiro del dado
	 */
	public static int roll() {
		return (int) (Math.random() * VALUES) + 1;
	}
}
