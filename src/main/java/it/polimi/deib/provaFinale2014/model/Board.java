package it.polimi.deib.provaFinale2014.model;

/**
 * Classe che definisce una plancia di gioco
 */
abstract class Board {
	/*
	 * Attributi friendly perché non devono essere
	 * visibili al di fuori del package
	 */
	
	/**
	 * Region della plancia
	 */
	Region[] regions;
	/**
	 * Road della plancia
	 */
	Road[] roads;
	
	/**
	 * Restituisce il riferimento a Sheepsburg
	 * @return sheepsburg
	 */
	public Region getSheepsburg() {
		return regions[regions.length - 1];
	}
	/**
	 * Restituisce l'array di regioni appartenenti alla plancia
	 * @return array di regioni
	 */
	public Region[] getRegions() {
		return regions;
	}
	/**
	 * Restituisce l'array di strade appartenenti alla plancia
	 * @return array di strade
	 */
	public Road[] getRoads() {
		return roads;
	}
}
