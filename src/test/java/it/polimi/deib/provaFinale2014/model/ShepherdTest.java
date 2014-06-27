package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.Animal;
import it.polimi.deib.provaFinale2014.model.Player;
import it.polimi.deib.provaFinale2014.model.Region;
import it.polimi.deib.provaFinale2014.model.Road;
import it.polimi.deib.provaFinale2014.model.Sheep;
import it.polimi.deib.provaFinale2014.model.Sheep.SheepType;
import it.polimi.deib.provaFinale2014.model.Shepherd;

import org.junit.Before;
import org.junit.Test;

public class ShepherdTest {
	private Shepherd shepherd;
	private Player player;
	private Road road;
	private Road adjacentRoad;
	private Region region;
	
	@Before
	public void setUp() {
		player = new Player(false);
		shepherd = new Shepherd(player);
		region = new Region();
		// Crea una strada e posiziona il pastore su di essa
		road = new Road(1, region, new Region());
		shepherd.setPosition(road);
		// Aggiungi una strada adiacente alla strada appena creata
		adjacentRoad = new Road(2, new Region(), new Region());
		road.getAdjacentRoads().add(adjacentRoad);
	}
	
	@Test
	public void initialization() {
		assertSame(player, shepherd.getController());
	}
	
	@Test
	public void position() {
		assertSame(road, shepherd.getPosition());
	}
	
	@Test
	public void move() {
		Road road = new Road(1, new Region(), new Region());
		Fence fence;
		
		assertNotSame(road, shepherd.getPosition());
		assertEquals(20, shepherd.getController().getAvailableMoney());
		
		fence = new Fence();
		shepherd.moveTo(adjacentRoad, fence);
		assertSame(adjacentRoad, shepherd.getPosition());
		// Se un pastore si muove in una strada adiacente non paga
		assertEquals(20, shepherd.getController().getAvailableMoney());
		
		fence = new Fence();
		shepherd.moveTo(road, fence);
		assertSame(road, shepherd.getPosition());
		// Se un pastore si muove in una strada non adiacente paga
		assertEquals(19, shepherd.getController().getAvailableMoney());
	}
	
	@Test
	public void possibilityToMove() {
		Road road = new Road(1, new Region(), new Region());
		Road[] adjacentRoads = {road};
		// Si può sempre muovere in una strada adiacente
		assertTrue(shepherd.canMoveTo(adjacentRoad));
		// Si può muovere su una strada non adiacente se ha i soldi
		assertTrue(shepherd.canMoveTo(road));
		shepherd.getController().spendMoney(20);
		// Non si può muovere su una strada senza soldi
		assertFalse(shepherd.canMoveTo(road));
		this.road.configureAdjacentRoads(adjacentRoads);
		// Anche senza soldi si può muovere su una strada adiacente
		assertTrue(shepherd.canMoveTo(road));
		// Non si può muovere in una strada non adiacente senza soldi
		assertFalse(shepherd.canMoveTo(new Road(1, new Region(), new Region())));
	}
	
	@Test
	public void canCoupleSheeps() {
		// Inizialmente non è possibile eseguire un'azione
		// di accoppiamento tra pecore
		assertFalse(shepherd.canCoupleSheeps());
		// Aggiunge due pecore alla prima regione adiacente
		for (int i = 0; i < 2; i++) {
			shepherd.getPosition().getAdjacentRegions()[0].addAnimal(new Sheep(SheepType.SHEEP));
		}
		// Ora è possibile eseguire un'azione di accoppiamento
		assertTrue(shepherd.canCoupleSheeps());
	}
	
	@Test
	public void coupleSheep() {
		// Aggiunge due pecore alla prima regione adiacente
		for (int i = 0; i < 2; i++) {
			shepherd.getPosition().getAdjacentRegions()[0].addAnimal(new Sheep(SheepType.SHEEP));
		}
		// Esegue l'accoppiamento tra pecore
		shepherd.coupleSheeps(shepherd.getPosition().getAdjacentRegions()[0]);
		// Verifica che ora ci siano tre pecore all'interno della regione
		assertTrue(shepherd.getPosition().getAdjacentRegions()[0].getSheepTypeNumber() == 3 ||
				shepherd.getPosition().getAdjacentRegions()[0].getSheepTypeNumber() == 2);
	}
	
	@Test
	public void canCouple() {
		// Inizialmente non è possibile eseguire un'azione
		// di accoppiamento pecora-montone
		assertFalse(shepherd.canCouple());
		// Aggiunge una pecora e un montone alla seconda
		// regione adiacente
		Animal[] animals = {new Sheep(Sheep.SheepType.MUTTON),
							new Sheep(Sheep.SheepType.SHEEP)};
		for (Animal animal : animals) {
			shepherd.getPosition().getAdjacentRegions()[1].addAnimal(animal);
		}
		// Ora è possibile eseguire un'azione di accoppiamento
		assertTrue(shepherd.canCouple());
	}
	
	@Test
	public void couple() {
		// Aggiunge una pecora e un montone alla seconda
		// regione adiacente
		Animal[] animals = {new Sheep(Sheep.SheepType.MUTTON),
							new Sheep(Sheep.SheepType.SHEEP)};
		for (Animal animal : animals) {
			shepherd.getPosition().getAdjacentRegions()[1].addAnimal(animal);
		}
		// Esegue l'accoppiamento tra pecore
		shepherd.couple(shepherd.getPosition().getAdjacentRegions()[1]);
		// Verifica che ora ci sia un agnello all'interno della regione
		assertTrue(shepherd.getPosition().getAdjacentRegions()[1].getLambTypeNumber() == 1 ||
				shepherd.getPosition().getAdjacentRegions()[1].getLambTypeNumber() == 0);
	}
	
	@Test
	public void canKill() {
		shepherd.getPosition().getAdjacentRegions()[1].addAnimal(
				new Sheep(Sheep.SheepType.SHEEP));
		assertTrue(shepherd.enoughMoneyToKill());
		assertTrue(shepherd.canKill());
		Shepherd adjacentShepherd = new Shepherd(new Player(false));
		adjacentShepherd.setPosition(adjacentRoad);
		adjacentRoad.setOccupier(adjacentShepherd);
		assertTrue(shepherd.enoughMoneyToKill());
		assertTrue(shepherd.canKill());
		shepherd.getController().spendMoney(20);
		assertFalse(shepherd.enoughMoneyToKill());
		assertFalse(shepherd.canKill());
	}
	
	@Test
	public void kill() {
		// Aggiunge un pastore su una strada adiacente
		// alla posizione del pastore
		Player otherPlayer = new Player(false);
		shepherd.getPosition().getAdjacentRoads().get(0).setOccupier(new Shepherd(otherPlayer));
		// Aggiunge una pecora alla prima regione adiacente
		shepherd.getPosition().getAdjacentRegions()[0].addAnimal(new Sheep());
		// Esegue l'azione di abbattimento
		shepherd.kill(shepherd.getPosition().getAdjacentRegions()[0],
					shepherd.getPosition().getAdjacentRegions()[0].getSheeps().get(0));
		// Verifica che i danari dei giocatori siano compatibili
		shepherd.kill2();
		assertTrue(shepherd.getController().getAvailableMoney() == 20 || 
				shepherd.getController().getAvailableMoney() == 18);
		assertTrue(otherPlayer.getAvailableMoney() == 20 ||
				otherPlayer.getAvailableMoney() == 22);
	}
	
	@Test
	public void giveMoney() {
		Shepherd receiver = new Shepherd(new Player(false));
		int moneyExchanged = 15;
		shepherd.giveMoney(receiver, moneyExchanged);
		assertEquals(Player.PLAYERMONEY - moneyExchanged, shepherd.getController().getAvailableMoney());
		assertEquals(Player.PLAYERMONEY + moneyExchanged, receiver.getController().getAvailableMoney());
	}

	@Test
	public void sheepMovement() {
		Sheep sheep = new Sheep();
		assertFalse(shepherd.getPosition().getAdjacentRegions()[0].containsSheeps());
		assertFalse(shepherd.getPosition().getAdjacentRegions()[1].containsSheeps());
		region.addAnimal(sheep);
		assertTrue(shepherd.getPosition().getAdjacentRegions()[0].containsSheeps());
		shepherd.moveSheep(region, sheep);
		assertFalse(shepherd.getPosition().getAdjacentRegions()[0].containsSheeps());
		assertTrue(shepherd.getPosition().getAdjacentRegions()[1].containsSheeps());
	}
	
	@Test
	public void canBuyCard() {
		TerrainType type = TerrainType.COUNTRY;
		assertFalse(shepherd.canBuyCard(type));
		region.setType(type);
		assertTrue(shepherd.canBuyCard(type));
	}
	
	@Test
	public void coupleSheeps() {
		boolean coupleSheeps = false;
		while (!coupleSheeps) {
			coupleSheeps = shepherd.coupleSheeps(new Region());
		}
		assertTrue(coupleSheeps);
		while (coupleSheeps) {
			coupleSheeps = shepherd.couple(new Region());
		}
		assertFalse(coupleSheeps);
	}
}
