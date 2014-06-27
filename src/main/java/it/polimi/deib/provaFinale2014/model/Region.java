package it.polimi.deib.provaFinale2014.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Regione che non possiede un TerrainType
 */
public class Region {
	private TerrainType type;
	/**
	 * Ovini che occupano la regione
	 */
	List<Animal> animals;
	/**
	 * Strade che delimitano la regione
	 */
	List<Road> adjacentRoads;
	
	/**
	 * Costruisce una regione assegnandole un tipo terreno
	 * @param type tipo terreno
	 */
	private boolean hasBlackSheep;
	private boolean hasWolf;
	
	/**
	 * Costruisce una regione inizializzando gli animali,
	 * le strade adiacenti alla regione e l'esistenza di pecora
	 * nera e lupo
	 */
	public Region() {
		animals = new ArrayList<Animal>();
		adjacentRoads = new ArrayList<Road>();
		hasBlackSheep = false;
		hasWolf = false;
	}
	/**
	 * Restituisce il riferimento alla pecora nera se presente
	 * all'interno della regione, altrimenti restituisce un
	 * riferimento a null
	 * @return riferimento a BlackSheep se presente nella lista
	 * di animali, oppure null
	 */
	public BlackSheep getBlackSheep() {
		for (Animal animal : animals) {
			if (animal instanceof BlackSheep) {
				return (BlackSheep) animal;
			}
		}
		return null;
	}
	/**
	 * Restituisce il riferimento al lupo se presente
	 * all'interno della regione, altrimenti restituisce un
	 * riferimento a null
	 * @return riferimento a Wolf se presente nella lista
	 * di animali, oppure null
	 */
	public Wolf getWolf() {
		for (Animal animal : animals) {
			if (animal instanceof Wolf) {
				return (Wolf) animal;
			}
		}
		return null;
	}
	/**
	 * Aggiunge un ovino all'interno della regione
	 * @param sheep ovino da aggiungere alla regione
	 */
	public void addAnimal(Animal animal) {
		animals.add(animal);
	}
	/**
	 * Elimina un ovino all'interno della regione
	 * @param index indice della pecora da rimuovere all'interno della lista
	 */
	public void removeSheep(int index) {
		animals.remove(index);
	}
	/**
	 * Elimina un ovino all'interno della regione
	 * @param pecora da rimuovere all'interno della lista
	 */
	public void removeSheep(Sheep sheep) {
		animals.remove(sheep);
	}
	/**
	 * Configura la strade adiacenti alla regione
	 * @param roads strade adiacenti alla regione
	 */
	public void configureAdjacentRoads(Road...roads) {
		for (Road road : roads) {
			adjacentRoads.add(road);
		}
	}
	/**
	 * @return true: ci sono pecore all'interno della Region; false altrimenti (pecore nere escluse)
	 */
	public boolean containsSheeps() {
		for (Animal animal : animals) {
			if (!(animal instanceof BlackSheep) && !(animal instanceof Wolf)) {
				return true;
			}
		} return false;
	}
	/**
	 * @return lista di pecore all'interno della regione
	 */
	public List<Animal> getAnimals() {
		return animals;
	}
	/**
	 * Restituisce la lista di pecore all'interno della regione
	 * @return lista di pecore nella regione
	 */
	public List<Sheep> getSheeps() {
		List<Sheep> sheeps = new ArrayList<Sheep>();
		
		for (Animal animal : animals) {
			if (animal instanceof Sheep) {
				sheeps.add((Sheep) animal);
			}
		}
		
		return sheeps;
	}
	/**
	 * Restituisce il numero di pecore che sono situate all'interno
	 * della regione
	 * @return numero di ovini di tipo pecora all'interno della regione
	 */
	public int getSheepTypeNumber() {
		int sheeps = 0;
		for (Sheep sheep : getSheeps()) {
			if (sheep.getType() == Sheep.SheepType.SHEEP && !(sheep instanceof BlackSheep)) {
				sheeps++;
			}
		}
		return sheeps;
	}
	/**
	 * Restituisce il numero di montoni che sono situati all'interno
	 * della regione
	 * @return numero di ovini di tipo montone all'interno della regione
	 */
	public int getMuttonTypeNumber() {
		int muttons = 0;
		for (Sheep sheep : getSheeps()) {
			if (sheep.getType() == Sheep.SheepType.MUTTON) {
				muttons++;
			}
		}
		return muttons;
	}
	/**
	 * Restituisce il numero di agnelli che sono situati all'interno
	 * della regione
	 * @return
	 */
	public int getLambTypeNumber() {
		int lambs = 0;
		for (Sheep sheep: getSheeps()) {
			if (sheep.getType() == Sheep.SheepType.LAMB) {
				lambs++;
			}
		}
		return lambs;
	}
	/**
	 * Restituisce il tipo terreno associato alla regione
	 * @return tipo terreno
	 */
	public TerrainType getType() {
		return type;
	}
	/**
	 * Imposta un tipo terreno associato alla regione
	 * @param type
	 */
	public void setType(TerrainType type) {
		this.type = type;
	}
	/**
	 * Seleziona una pecora dalla regione in base al suo indice
	 * @param index indice di scelta
	 * @return pecora all'interno della regione
	 */
	public Sheep pickSheep(int index) {
		return getSheeps().get(index);
	}
	/**
	 * Restituisce il valore di verità in merito all'esistenza della
	 * pecora nera all'interno della regione
	 * @return true, se la regione contiene la pecora nera; false altrimenti
	 */
	public boolean hasBlackSheep() {
		return hasBlackSheep;
	}
	/**
	 * Imposta l'esistenza della pecora nera all'interno della regione
	 * @param hasBlackSheep true, se la pecora nera esiste all'interno della regione
	 */
	public void setBlackSheep (boolean hasBlackSheep) {
		this.hasBlackSheep = hasBlackSheep;
		
	}
	/**
     * Verifica che sia presente un certo tipo di ovino in una data regione
     * @return Sheep ritorna il primo ovino di quel tipo, null se non sono presenti ovini 
     * di quel tipo
     */
	public Sheep typeInRegion (int typeIndex) {
		for (Sheep sheep: this.getSheeps()){
			if (sheep.getType().equals(Sheep.SheepType.values()[typeIndex]) && !(sheep instanceof BlackSheep)) {
				return sheep;
			}
		} return null;
	}
	/**
	 * Restituisce la possibilità di eseguire l'azione di Accoppiamento 2
	 * in una delle regioni adiacenti al pastore
	 * @return true, se è possibile, false altrimenti
	 */
	public boolean canCoupleHere() {
			return this.getSheepTypeNumber() >= 1 && this.getMuttonTypeNumber() >= 1;
		} 
	/**
	 * Restituisce il valore di verità in merito all'esistenza del lupo 
	 * all'interno della regione
	 * @return true, se il lupo si trova all'interno della regione; false, altrimenti
	 */
	public boolean hasWolf(){
		return hasWolf;
	}
	/**
	 * Imposta l'esistenza del lupo all'interno della regione
	 * @param hasWolf true, se il lupo si trova all'interno della regione
	 */
	public void setWolf(boolean hasWolf) {
		this.hasWolf = hasWolf;
	}
}
