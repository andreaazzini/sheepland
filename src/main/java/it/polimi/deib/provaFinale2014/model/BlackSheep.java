package it.polimi.deib.provaFinale2014.model;

/**
 * Pecora nera, un tipo particolare di pecora che vale più punti
 * di un ovino tradizionale e può muoversi sulla plancia senza
 * l'ausilio di un pastore
 */
public class BlackSheep extends Sheep {

	
	/**
	 * Costruisce una pecora nera, assegnandole di default
	 * il tipo di ovino SHEEP
	 */
	public BlackSheep() {
		// La pecora nera è di tipo pecora
		type = SheepType.SHEEP;
		value = 2;
	}
	
	/**
	 * Muove la pecora nera in maniera randomica
	 */
	public void moveBlackSheepRandmly() {
		int i = Dice.roll();
		
		for (Road r: this.position.adjacentRoads) {
			if (r.getValue() == i && !r.isOccupied()) {
				Region destination = r.obtainDestinationRegion(position);
				position.getAnimals().remove(this);
				position.setBlackSheep(false);
				destination.getAnimals().add(this);
				destination.setBlackSheep(true);
				position = destination;
			}
		}
	}
}
