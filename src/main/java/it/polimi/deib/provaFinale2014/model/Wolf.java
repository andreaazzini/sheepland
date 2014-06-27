package it.polimi.deib.provaFinale2014.model;

import it.polimi.deib.provaFinale2014.model.Sheep.SheepType;

/**
 * Lupo, tipo di animale presente nella versione
 * avanzata del gioco Sheepland
 */
public class Wolf extends Animal {
	/**
	 * Costruisce un lupo inizializzando la sua
	 * posizione
	 */
	public Wolf() {
		position = new Region();
	}
	/**
	 * Mangia una pecora a caso se all'interno della regione
	 * su cui è posizionato il lupo ne sono presenti
	 */
	public SheepType eatRandomSheep() {
		SheepType type = null;
		if (position.containsSheeps()) {
			int n = position.getSheeps().size();
			int index;
			Sheep sheepToEat;
			while (true) {
				index = (int) (Math.random() * n);
				sheepToEat = position.pickSheep(index);
				if (!(sheepToEat instanceof BlackSheep)) {
					type = sheepToEat.getType();
					break;
				}
			}
			position.removeSheep(sheepToEat);
		} return type;
	}
	/**
	 * Muove il lupo in maniera randomica
	 */
	public void moveRandomly() {
		int fences = 0;
		Road rightRoad = null;
		int i = Dice.roll();
		
		for (Road r : this.position.adjacentRoads) {
			if (r.hasFence()) {
				fences++;
			}
			if (r.getValue() == i && !r.hasShepherd()) {
				rightRoad = r;
			}
		}
		/*
		 * Se il lupo è circondato da recinti è possibile saltare
		 */
		if (rightRoad != null) {
			if ((fences == this.position.adjacentRoads.size()) || !rightRoad.hasFence()) {
				Region destination = rightRoad.obtainDestinationRegion(position);
				position.getAnimals().remove(this);
				position.setWolf(false);
				destination.getAnimals().add(this);
				destination.setWolf(true); 
				position = destination;
			}
		}
	}
}
