package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;
import it.polimi.deib.provaFinale2014.gui.AnimalGUI.AnimalType;

import org.junit.Before;
import org.junit.Test;

public class SheeplandPanelTest {
	private ClientCommunication client;
	private SheeplandPanel panel;

	@Before
	public void setUp() {
		client = new ClientHandler();
		panel = new SheeplandPanel(client);
		panel.paintAnimal(AnimalType.SHEEP, 0);
		panel.paintAnimal(AnimalType.SHEEP, 0);
		panel.paintAnimal(AnimalType.BLACKSHEEP, 1);
		panel.paintAnimal(AnimalType.MUTTON, 2);
	}

	@Test
	public void initialization() {
		assertNotNull(panel.getSheeps());
		assertNotNull(panel.getShepherds());
		assertNotNull(panel.getRoads());
		assertEquals(3, panel.getSheeps().size());
		assertEquals(0, panel.getShepherds().size());
		assertEquals(Default.roadHooks.length, panel.getRoads().size());
	}
	
	@Test
	public void moveSheep() {
		panel.moveSheep(panel.getSheeps().get(0), 1);
		assertEquals(4, panel.getSheeps().size());
		panel.moveSheep(panel.getSheeps().get(1), 3);
		assertEquals(4, panel.getSheeps().size());
		panel.moveSheep(panel.getSheeps().get(2),  6);
		assertEquals(4, panel.getSheeps().size());
	}
	
	@Test
	public void removeSheep() {
		panel.removeSheep(AnimalType.SHEEP, 0);
		assertEquals(3, panel.getSheeps().size());
		panel.removeSheep(AnimalType.SHEEP, 0);
		assertEquals(2, panel.getSheeps().size());
		panel.removeSheep(AnimalType.BLACKSHEEP, 1);
		assertEquals(1, panel.getSheeps().size());
		panel.removeSheep(AnimalType.MUTTON, 2);
		assertEquals(0, panel.getSheeps().size());
	}
	
	@Test
	public void paintAnimal() {
		panel.paintAnimal(AnimalType.MUTTON, 0);
		assertEquals(4, panel.getSheeps().size());
	}
	
	@Test
	public void paintShepherd() {
		assertFalse(panel.getOccupiedRoads()[0]);
		panel.paintShepherd("blue", 0);
		assertTrue(panel.getOccupiedRoads()[0]);
		assertEquals(1, panel.getShepherds().size());
	}

	@Test
	public void setOccupiedRoad() {
		int roadIndex = 0;
		assertFalse(panel.getOccupiedRoads()[roadIndex]);
		panel.setOccupiedRoad(roadIndex);
		assertTrue(panel.getOccupiedRoads()[roadIndex]);
	}
	
	@Test
	public void setFreeRoad() {
		int roadIndex = 0;
		panel.setOccupiedRoad(roadIndex);
		panel.setFreeRoad(roadIndex);
		assertFalse(panel.getOccupiedRoads()[roadIndex]);
	}
	
	@Test
	public void size() {
		assertEquals(Default.getFrameDimension(), panel.getSize());
		assertEquals(Default.getFrameDimension(), panel.getPreferredSize());
		assertEquals(Default.getFrameDimension(), panel.getMinimumSize());
		assertEquals(Default.getFrameDimension(), panel.getMaximumSize());
	}
	
	@Test
	public void paintFence() {
		panel.paintFence(0);
		assertEquals(panel.getFences().get(panel.getFences().size() - 1).getRoadIndex(), 
				new FenceGUI(0).getRoadIndex());
	}
	
	@Test
	public void addRoadListeners() {
		panel.addRoadListeners();
		for (RoadGUI road : panel.getRoads()) {
			assertFalse(road.isEnabled());
		}
	}
	
	@Test
	public void listeners() {
		int size = panel.getSheeps().size();
		panel.addSheepListeners();
		for (AnimalGUI sheep : panel.getSheeps()) {
			assertTrue(sheep.isEnabled());
		}
		assertEquals(size, panel.getSheeps().size());
		panel.reinitializeSheeps();
		for (AnimalGUI sheep : panel.getSheeps()) {
			assertEquals(0, sheep.getMouseListeners().length);
		}
	}
	
	@Test
	public void getShepherd() {
		ShepherdGUI shepherd = new ShepherdGUI("blue", 0);
		panel.paintShepherd("blue", 0);
		assertNull(panel.getMyShepherd());
		assertEquals(shepherd.getColor(), panel.getShepherd("blue").getColor());
	}
	
	@Test
	public void getSheep() {
		assertNotNull(panel.getSheep("SHEEP", 0));
		assertNull(panel.getSheep("LAMB", 0));
	}
	
	@Test
	public void sheepType() {
		panel.setSheepType("LAMB");
		assertEquals("LAMB", panel.getSheepType());
	}
}
