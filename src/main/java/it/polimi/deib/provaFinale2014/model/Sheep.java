package it.polimi.deib.provaFinale2014.model;

/**
 * Ovino, che può essere una pecora, un montone o un agnello.
 * Si può muovere solo grazie all'ausilio di un pastore. Si può
 * accoppiare e può essere abbattuta.
 */
public class Sheep extends Animal {
	private int turnCounter;
	/**
	 * Enum che elenca i diversi tipi di ovino presenti
	 * nella versione avanzata di Sheepland
	 */
	public static enum SheepType {
		SHEEP, MUTTON, LAMB
	}
	/**
	 * Tipo di ovino attuale assunto dall'animale
	 */
	SheepType type;
	
	/**
	 * Valore della pecora
	 */
	int value;

	/**
	 * Costruisce un ovino di un tipo determinato
	 * casualmente
	 */
	public Sheep() {
		setRandomType();
		if (this.type.equals(SheepType.LAMB)) {
			turnCounter = 0;
		}
		this.value = 1;
	}
	/**
	 * Costruisce un ovino del tipo stabilito
	 * @param type tipo di ovino
	 */
	public Sheep(SheepType type) {
		this.type = type;
		this.value = 1;
	}
	/**
	 * Restituisce il tipo assunto attualmente
	 * dall'ovino
	 * @return tipo dell'ovino
	 */
	public SheepType getType() {
		return type;
	}
	/**
	 * Assegna un tipo casuale ad un ovino
	 */
	private void setRandomType() {
		type = SheepType.values()[(int) (Math.random() * 3)];
	}
	/**
	 * Aumenta il contatore del turno
	 */
	public void increaseTurnCounter() {
		turnCounter++;
	}
	/**
	 * Restituisce il turnCounter
	 * @return turnCounter
	 */
	public int getTurnCounter() {
		return turnCounter;
	}
	/**
	 * Fa evolvere gli agnelli
	 */
	public boolean evolve() {
		/*
		 * Un agnello si evolve ogni due turni escludendo il primo
		 * turno di gioco
		 */
		if (turnCounter > 1 && turnCounter % 2 == 1) {
			type = SheepType.values()[(int) (Math.random() * 2)];
			return true;
		}
		return false;
	}
	/**
	 * Restituisce il valore in punti della pecora
	 * @return valore in punti della pecora
	 */
	public int getValue() {
		return value;
	}
}
