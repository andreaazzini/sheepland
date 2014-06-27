package it.polimi.deib.provaFinale2014.model;

/**
 * Tipo di terreno assegnabile ad una regione o ad una carta
 */
public enum TerrainType {
	HILL("Hill"), 
	PLAIN("Plain"), 
	LAKE("Lake"), 
	DESERT("Desert"), 
	MOUNTAIN("Mountain"), 
	COUNTRY("Country"), 
	SHEEPSBURG("Sheepsburg");
	
	private final String typeString;
	
	private TerrainType(String typeString) {
		this.typeString = typeString;
	}
	
	public String getString() {
		return typeString;
	}
}