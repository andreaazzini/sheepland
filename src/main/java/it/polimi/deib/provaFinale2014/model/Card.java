package it.polimi.deib.provaFinale2014.model;

/**
 * Carta Terreno, acquistabile durante la partita da un giocatore,
 *  ha un tipo e un costo in danari
 */
public class Card {
	private TerrainType type;
	private int cost;
	boolean isInitial;
	
	/**
	 * Costruisce una carta associandole un tipo terreno
	 * e un costo di acquisto
	 * @param type tipo terreno
	 * @param cost costo di acquisto
	 */
	public Card(TerrainType type, int cost, boolean initial) {
		this.type = type;
		this.cost = cost;
		this.isInitial = initial;
	}
	/**
	 * Restituisce il tipo terreno associato alla carta
	 * @return tipo terreno
	 */
	public TerrainType getTerrainType() {
		return type;
	}
	/**
	 * Restituisce il costo di acquisto della carta
	 * @return costo di acquisto
	 */
	public int getCost() {
		return cost;
	}
	/**
	 * Imposta il prezzo della carta
	 * @param price prezzo della carta
	 */
	public void setPrice(int price) {
		cost = price;
	}
	/**
	 * Restituisce true se la carta è iniziale
	 * @return true, se la carta è iniziale; false altrimenti
	 */
	public boolean isInitial() {
		return isInitial;
	}
}
