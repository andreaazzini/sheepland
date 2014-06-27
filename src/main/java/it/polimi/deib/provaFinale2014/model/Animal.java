package it.polimi.deib.provaFinale2014.model;

/**
 * Classe astratta che definisce un animale all'interno
 * del gioco Sheepland
 */
public abstract class Animal {
	 Region position;
	
	/**
	 * Metodo che restituisce la posizione dell'animale
	 * all'interno della plancia
	 * @return regione occupata sulla plancia
	 */
	public Region getPosition() {
		return position;
	}
	
	/**
	 * Imposta la posizione dell'animale all'interno
	 * di una regione
	 * @param position regione che la pecora nera andrà ad occupare
	 */
	public void setPosition(Region region) {
		position = region;
	}
}
