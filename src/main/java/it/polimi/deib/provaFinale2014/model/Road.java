package it.polimi.deib.provaFinale2014.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Strada di una plancia
 */
public class Road {
	private static final int ADJACENTREGIONS = 2;
	private int value;
	private Fence fence;
	private Shepherd shepherd;
	private Region[] adjacentRegions;
	private List<Road> adjacentRoads;
	
	/**
	 * Costruisce una strada, assegnandole un valore numerico e 
	 * le due regioni a lei adiacenti
	 * @param value valore della strada
	 * @param region1 prima regione adiacente
	 * @param region2 seconda regione adiacente
	 */
	public Road(int value, Region region1, Region region2) {
		this.value = value;
		fence = null;
		shepherd = null;
		// Inizializzo la coppia di regioni adiacenti
		adjacentRegions = new Region[ADJACENTREGIONS];
		adjacentRegions[0] = region1;
		adjacentRegions[1] = region2;
		
		adjacentRoads = new ArrayList<Road>();
	}
	/**
	 * Configura le strade adiacenti alla strada
	 * @param roads strade adiacenti alla strada
	 */
	public void configureAdjacentRoads(Road[] roads) {
		for (Road road : roads){
			adjacentRoads.add(road);
		}
	}
	/**
	 * Mette un recinto sulla strada
	 * @param fence2 
	 */
	public void setFence(Fence fence) {
		this.fence = fence;
	}
	/**
	 * Restituisce il valore di verità riguardo l'avere
	 * assegnato un recinto
	 * @return true, se alla strada è stato assegnato un recinto;
	 * false, altrimenti
	 */
	public boolean hasFence() {
		return fence != null;
	}
	/**
	 * Restituisce il valore di verità riguardo l'avere
	 * un pastore occupante
	 * @return true, se alla strada è stato assegnato un pastore;
	 * false, altrimenti
	 */
	public boolean hasShepherd() {
		return shepherd != null;
	}
	/**
	 * Imposta il pastore che occupa la strada
	 * @param occupier pastore occupante
	 */
	public void setOccupier(Shepherd occupier) {
		shepherd = occupier;
	}
	/**
	 * Rimuove il pastore occupante dalla strada
	 */
	public void removeOccupier() {
		shepherd = null;
	}
	
	public Shepherd getShepherd() {
		return shepherd;
	}
	
	public int getValue() {
		return value;
	}
	
	public Region[] getAdjacentRegions() {
		return adjacentRegions;
	}
	
	public List<Road> getAdjacentRoads() {
		return adjacentRoads;
	}
	/**
	 * Imposta se la strada è occupata da un pastore o da un recinto
	 * @return true, se la strada è occupata da un pastore o da un recinto;
	 * false altrimenti
	 */
	public boolean isOccupied() {
		return hasShepherd() || hasFence();
	}
	/**
	 * Ottiene la regione di destinazione di una pecora che si muove
	 * partendo da una regione di partenza
	 * @param startingRegion regione di partenza
	 * @return regione di arrivo
	 * @throws InvalidRegionException
	 */
	public Region obtainDestinationRegion(Region startingRegion)  {
		if (startingRegion.equals(adjacentRegions[0])) {
			return adjacentRegions[1];
		} else {
			return adjacentRegions[0];
		} 
	}
}
