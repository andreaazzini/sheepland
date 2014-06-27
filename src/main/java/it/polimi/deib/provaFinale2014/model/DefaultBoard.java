package it.polimi.deib.provaFinale2014.model;

/**
 * Plancia di gioco standard per il gioco Sheepland
 */
public class DefaultBoard extends Board {
	/*
	 * Costanti valide per la plancia di default.
	 * Dichiarate friendly cosicché siano accessibili
	 * solo all'interno del package e static in modo
	 * da non dover creare un oggetto DefaultBoard per
	 * utilizzarle
	 */
	/**
	 * Numero di regioni all'interno della plancia di gioco standard
	 */
	public static final int REGIONS = 19;
	public static final int ROADS = 42;
	static final int REGIONSPERTYPE = 3;
	
	/**
	 * Costruisce una plancia standard per il gioco Sheepland, 
	 * inizializzando Sheepsburg, l'array di regioni e l'array 
	 * di strade che compongono la plancia
	 */
	public DefaultBoard() {
		regions = new Region[REGIONS];
		roads = new Road[ROADS];
		
		/*
		 * Inizializzo tutto
		 */
		DefaultBoardHardCoder coder = new DefaultBoardHardCoder();
		coder.initialize(regions, roads);
	}
	/**
	 * Controlla che l'indice della strada sia fra gli indici validi
	 * @param roadIndex indice della strada
	 * @return true, se l'indice della strada è valido; false altrimenti
	 */
	public boolean roadInBound (int roadIndex) {
		if (roadIndex >= 0 && roadIndex < ROADS) {
			return true;
		}
		return false;	
	}
	
	/**
	 * Controlla che l'indice della regione sia fra gli indici validi
	 * @param roadIndex indice della regione
	 * @return true, se l'indice della regione è valido; false altrimenti
	 */
	public boolean regionInBound (int regionIndex) {
		if (regionIndex >= 0 && regionIndex < REGIONS) {
			return true;
		}
		return false;	
	}
	/**
	 * Restituisce la regione corrispondente ad un certo indice
	 * @param regionIndex indice della regione
	 * @return regione della plancia di gioco standard
	 */
	public Region getRegion (int regionIndex) {
		return regions[regionIndex];
	}
	/**
	 * Restituisce la pecora nera
	 * @return pecora nera
	 */
	public Sheep getBlackSheep () {
		for (Region r: regions) {
			if (r.hasBlackSheep())
				return r.getBlackSheep();
		}
		return null;
	}
	/**
	 * Restituisce il lupo
	 * @return lupo
	 */
	public Wolf getWolf() {
		for (Region r: regions) {
			if (r.hasWolf())
				return r.getWolf();
		}
		return null;
		
	}
	/**
	 * Restituisce l'indice di una regione
	 * @param region regione
	 * @return indice della regione
	 */
	public int getRegionIndex(Region region) {
		for (int i=0; i<REGIONS; i++){
			if (regions[i]==region)
				return i;
		}
		return -1;
		
	}
}
