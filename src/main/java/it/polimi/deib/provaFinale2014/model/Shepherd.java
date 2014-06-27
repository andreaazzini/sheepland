package it.polimi.deib.provaFinale2014.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pastore guidato da un giocatore.
 */
public class Shepherd {
	private Player controller;
	private Road position;
	
	/**
	 * Costruisce un pastore inizializzando il proprio
	 * controllore, la propria posizione e il proprio dado
	 * @param controller giocatore che controlla il pastore
	 */
	public Shepherd(Player controller) {
		this.controller = controller;
		position = null;
	}
	/**
	 * Muove il pastore sulla strada scelta
	 * @param road strada di destinazione
	 * @param fence 
	 */
	public void moveTo(Road road, Fence fence) {
		position.setFence(fence);
		position.removeOccupier();
		if (!position.getAdjacentRoads().contains(road)) {
			controller.spendMoney(1);
		}
		position = road;
		road.setOccupier(this);
		
	}
	/**
	 * Verifica se è possibile spostarsi nella strada di destinazione
	 * @param road strada di destinazione
	 * @return true; false altrimenti
	 */
	public boolean canMoveTo(Road road) {
		boolean hasSufficientMoney = true;
		if (!position.getAdjacentRoads().contains(road) && 
				controller.getAvailableMoney() == 0) {
			hasSufficientMoney = false;
		}
		return !road.hasFence() && hasSufficientMoney;
	}
	/**
	 * Restituisce il giocatore che controlla il pastore
	 * @return giocatore controllore
	 */
	public Player getController() {
		return controller;
	}
	/**
	 * @return strada sulla quale è posizionato il pastore
	 */
	public Road getPosition() {
		return position;
	}
	/**
	 * Accomppiamento 1: se in una regione sono posizionate due o più pecore,
	 * il pastore può eseguire un'azione di accoppiamento. Viene tirato un dado.
	 * Nel caso in cui il numero ottenuto equivalga a quello della cella sopra cui
	 * il pastore è posto, viene generata una nuova pecora e aggiunta alla regione
	 * @param region la regione nella quale eseguire l'azione di accoppiamento
	 */
	public boolean coupleSheeps(Region region) {
		if (Dice.roll() == position.getValue()) {
			Sheep newSheep = new Sheep(Sheep.SheepType.SHEEP);
			region.addAnimal(newSheep);
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Accoppiamento 2: se in una regione sono posizionati un montone e una pecora,
	 * il pastore può eseguire un'azione di accoppiamento. Viene tirato un dado.
	 * Nel caso in cui il numero ottenuto equivalga a quello della cella sopra cui
	 * il pastore è posto, viene generato un nuovo agnello e aggiunto alla regione
	 * @param region la regione nella quale eseguire l'azione di accoppiamento
	 */
	public boolean couple(Region region) {
		if (Dice.roll() == position.getValue()) {
			Sheep lamb = new Sheep(Sheep.SheepType.LAMB);
			region.addAnimal(lamb);
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Restituisce la possibilità di eseguire l'azione di Accoppiamento 1
	 * @return true, se è possibile; false altrimenti
	 */
	public boolean canCoupleSheeps() {
			return position.getAdjacentRegions()[0].getSheepTypeNumber() >= 2 ||
					position.getAdjacentRegions()[1].getSheepTypeNumber() >= 2;
		}
	
	/**
	 * Restituisce la possibilità di eseguire l'azione di Accoppiamento 2
	 *  in una delle regioni adiacenti al pastore
	 * @return true, se è possibile, false altrimenti
	 */
	public boolean canCouple() {
			return (position.getAdjacentRegions()[0].getSheepTypeNumber() >= 1 &&
					position.getAdjacentRegions()[0].getMuttonTypeNumber() >= 1)||
					(position.getAdjacentRegions()[1].getSheepTypeNumber() >= 1 &&
					position.getAdjacentRegions()[1].getMuttonTypeNumber() >= 1);
		} 

	
	/**
	 * Azione di abbattimento: il pastore rimuove un ovino a piacere da
	 * una regione a lui confinante se il numero del dado corrisponde al valore
	 * della strada su cui è posizionato. Tutti i pastori posizionati in strade
	 * limitrofe a quelle del pastore che esegue l'azione, tirano un dado, e
	 * nel caso ottengano un valore maggiore o uguale a 5 devono ricevere
	 * 2 danari
	 * @param region regione su cui si esegue l'abbattimento
	 * @param sheep ovino da abbattere
	 */
	public boolean kill(Region region, Sheep sheep) {
		if( Dice.roll() ==position.getValue()) {
			region.getAnimals().remove(sheep);
			return true;
		} return false;
	}
	/**
	 * Restituisce la lista di pastori che devono essere risarciti
	 * dopo un'azione di abbattimento
	 * @return lista di pastori
	 */
	public List<Shepherd> kill2() {
		List<Shepherd> rewarded= new ArrayList<Shepherd>();
		for (Road adjacentRoad : position.getAdjacentRoads()) {
			if (adjacentRoad.hasShepherd()) {
				/*
				 * Viene lanciato un dado. Se il risultato è maggiore o uguale
				 * a 5 allore il pastore deve essere risarcito.
				 */
				int roll = Dice.roll();
				if (roll >= 5) {
					giveMoney(adjacentRoad.getShepherd(), 2);
					rewarded.add(adjacentRoad.getShepherd());
				}
			}
		} 
		
		return rewarded;
	}
	
	/**
	 * Metodo di controllo sull'azione di abbattimento, che è possibile solo se sulla regione
	 * selezionata è presente almeno un ovino, e se il giocatore ha abbastanza denaro
	 * @param region, regione valida
	 * @return true se è possibile, false altrimenti
	 */
	public boolean canKill() {
		if (position.getAdjacentRegions()[0].hasBlackSheep() || position.getAdjacentRegions()[1].hasBlackSheep()) {
			return enoughMoneyToKill() && 
					((position.getAdjacentRegions()[0].getSheeps().size() + 
							position.getAdjacentRegions()[1].getSheeps().size() -1) > 0);
		} else {
			return enoughMoneyToKill() && 
				((position.getAdjacentRegions()[0].getSheeps().size() + 
						position.getAdjacentRegions()[1].getSheeps().size()) > 0);
		}
	}
	
	/**
	 * Metodo di controllo sull'azione di abbattimento, che è possibile solo
	 * nel caso in cui il pastore abbia abbastanza danari per pagare 
	 * eventualmente gli altri giocatori confinanti.
	 * @return true se è possibile eseguire l'abbattimento; false altrimenti
	 */
	public boolean enoughMoneyToKill() {
		int adjacentShepherds = 0;
		for (Road adjacentRoad : position.getAdjacentRoads()) {
			if (adjacentRoad.hasShepherd()) {
				adjacentShepherds++;
				
			}
		} return adjacentShepherds*2 < this.controller.getAvailableMoney();
	}
	/**
	 * Paga un altro pastore
	 * @param receiver pastore che riceve i danari
	 * @param money ammontare di danari
	 */
	public void giveMoney(Shepherd receiver, int money) {
		this.getController().spendMoney(money);
		receiver.getController().receiveMoney(money);
	}
	/**
	 * Muove una pecora da una regione adiacente all'altra
	 * @param startingRegion regione di partenza
	 * @param movingSheep pecora da muovere
	 * @throws InvalidRegionException
	 */
	public void moveSheep(Region startingRegion, Sheep movingSheep)  {
		Region destination = position.obtainDestinationRegion(startingRegion);
		startingRegion.getAnimals().remove(movingSheep);
		destination.getAnimals().add(movingSheep);
		movingSheep.setPosition(destination);
		if (movingSheep instanceof BlackSheep) {
			startingRegion.setBlackSheep(false);
			destination.setBlackSheep(true);
		}
	}
	
	/**
	 * Metodo di controllo se è possibile effettuare l'azione di muovere una ovino.
	 * Un pastore può muovere una pecora solo se nelle regioni adiacenti è presente un ovino.
	 * @return true se è possibile, false altrimenti
	 */
	public boolean canMove() {
		return !position.getAdjacentRegions()[0].getSheeps().isEmpty() ||
				!position.getAdjacentRegions()[1].getSheeps().isEmpty();	
	}


	/**
	 * Verifica che sia possibile acquistare una carta di un certo
	 * tipo terreno
	 * @param type tipo terreno
	 * @return true, se è possibile acquistare una carta di quel tipo terreno; false altrimenti
	 */
	public boolean canBuyCard(TerrainType type) {
		// L'acquisto è possibile solo se una delle due regioni adiacenti
		// corrisponde al tipo terreno della carta che si vuole acquistare
		return type.equals(position.getAdjacentRegions()[0].getType()) ||
				type.equals(position.getAdjacentRegions()[1].getType());
	}
	/**
	 * Imposta la posizione del pastore
	 * @param road strada occupata dal pastore
	 */
	public void setPosition(Road road) {
		position = road;
		road.setOccupier(this);
	}
	/**
	 * Controlla che il pastore sia adiacente ad una regione
	 * @param region regione
	 * @return true, se il pastore è adiacente alla regione; false altrimenti
	 */
	public boolean isAdjacent (Region region) {
		return position.getAdjacentRegions()[0] == region
				|| position.getAdjacentRegions()[1] == region;
	}
}
