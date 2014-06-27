package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.gui.AnimalGUI.AnimalType;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Panel contenente la plancia di gioco e i component che
 * costituiscono gli elementi del gioco
 */
public class SheeplandPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(
			SheeplandPanel.class.getName());
	
	private ClientCommunication client;

	private List<RoadGUI> roads;
	private List<AnimalGUI> sheeps;	
	private List<ShepherdGUI> shepherds;
	private List<FenceGUI> fences;
	
	private boolean[] occupiedRoads;
	
	private String sheepType;

	/**
	 * Costruisce un nuovo SheeplandPanel inizializzando
	 * le liste di oggetti e settando il layout null
	 */
	public SheeplandPanel(ClientCommunication client) {
		LOGGER.setUseParentHandlers(false);
		
		this.client = client;
		
		roads = Collections.synchronizedList(new ArrayList<RoadGUI>());
		sheeps = Collections.synchronizedList(new ArrayList<AnimalGUI>());
		shepherds = Collections.synchronizedList(new ArrayList<ShepherdGUI>());
		fences = Collections.synchronizedList(new ArrayList<FenceGUI>());
		occupiedRoads = new boolean[Default.roadHooks.length];
		
		addRoads();
		
		this.setLayout(null);
	}
	
	private void addRoads() {
		for (int i = 0; i < Default.roadHooks.length; i++) {
			RoadGUI road = new RoadGUI(i);
			RoadListener listener = new RoadListener(client, this, road);
			road.addMouseListener(listener);
			road.addActionListener(listener);
			road.setEnabled(false);
			roads.add(road);
		}
	}
	/**
	 * Esegue una paint di un recinto all'interno di una certa
	 * strada
	 * @param roadIndex strada nella quale verrà rappresentato il recinto
	 */
	public void paintFence(int roadIndex) {
		FenceGUI fence = new FenceGUI(roadIndex);
		fences.add(fence);
	}
	/**
	 * Muove una pecora nella plancia di gioco
	 * @param sheep pecora da muovere
	 * @param destinationRegionIndex indice della regione di destinazione
	 */
	public void moveSheep(AnimalGUI sheep, int destinationRegionIndex) {
		if (!sheep.getType().equals(AnimalType.BLACKSHEEP)) {
			if (sheep.getSheepTypeNumber().getNumber() == 1) {
				// Rimuove la pecora dalla lista
				synchronized (sheeps) {
					sheeps.remove(sheep);
				}
			} else {
				sheep.getSheepTypeNumber().decreaseNumber();
			}
		} else {
			synchronized (sheeps) {
				sheeps.remove(sheep);
			}
		}
		paintAnimal(sheep.getType(), destinationRegionIndex);
	}
	
	private Point[] calculatePoints (AnimalGUI sheep, int destinationRegionIndex) {
		final Point startingRegion;
		final Point destinationRegion;
		if (sheep.getType().equals(AnimalType.SHEEP)) {
			startingRegion = new Point(
					(int)(Default.sheepRegionHooks[sheep.getRegionIndex()][0] * Default.scaleValue),
					(int)(Default.sheepRegionHooks[sheep.getRegionIndex()][1] * Default.scaleValue));
			destinationRegion = new Point(
					(int)(Default.sheepRegionHooks[destinationRegionIndex][0] * Default.scaleValue),
					(int)(Default.sheepRegionHooks[destinationRegionIndex][1] * Default.scaleValue));
		} else if (sheep.getType().equals(AnimalType.MUTTON)) {
			startingRegion = new Point(
					(int)(Default.muttonRegionHooks[sheep.getRegionIndex()][0] * Default.scaleValue),
					(int)(Default.muttonRegionHooks[sheep.getRegionIndex()][1] * Default.scaleValue));
			destinationRegion = new Point(
					(int)(Default.muttonRegionHooks[destinationRegionIndex][0] * Default.scaleValue),
					(int)(Default.muttonRegionHooks[destinationRegionIndex][1] * Default.scaleValue));
		} else if (sheep.getType().equals(AnimalType.LAMB)) {
			startingRegion = new Point(
					(int)(Default.lambRegionHooks[sheep.getRegionIndex()][0] * Default.scaleValue),
					(int)(Default.lambRegionHooks[sheep.getRegionIndex()][1] * Default.scaleValue));
			destinationRegion = new Point(
					(int)(Default.lambRegionHooks[destinationRegionIndex][0] * Default.scaleValue),
					(int)(Default.lambRegionHooks[destinationRegionIndex][1] * Default.scaleValue));
		} else if (sheep.getType().equals(AnimalType.BLACKSHEEP)) {
			startingRegion = new Point(
					(int)(Default.blacksheepRegionHooks[sheep.getRegionIndex()][0] * Default.scaleValue),
					(int)(Default.blacksheepRegionHooks[sheep.getRegionIndex()][1] * Default.scaleValue));
			destinationRegion = new Point(
					(int)(Default.blacksheepRegionHooks[destinationRegionIndex][0] * Default.scaleValue),
					(int)(Default.blacksheepRegionHooks[destinationRegionIndex][1] * Default.scaleValue));
		} else {
			startingRegion = null;
			destinationRegion = null;
		}
		
		return new Point[] {startingRegion, destinationRegion};
		
	}
	/**
	 * Muove le pecore con un'animazione
	 * @param sheep pecora
	 * @param destinationRegionIndex indice della regione di destinazione
	 */
	public void moveSheepWithAnimation (AnimalGUI sheep, int destinationRegionIndex) {
		Point[] points = calculatePoints(sheep, destinationRegionIndex);
		final Point destinationRegion = points[1];
		final AnimalGUI mySheep = sheep;
		final int destination = destinationRegionIndex;
		final ActionListener timerListener = new ActionListener() {
			private int counter = 50;
			/**
			 * Azione eseguita al fire del timer
			 */
		    public void actionPerformed(ActionEvent evt) {
		    	if (counter==0) {
		    		sheeps.remove(mySheep);
		    		paintAnimal(mySheep.getType(), destination);
		    		repaint();
		    		((Timer)evt.getSource()).stop();
		    	} else {
		    		double x = mySheep.getX() + ((destinationRegion.x - mySheep.getX())/ counter);
			    	double y = mySheep.getY() + ((destinationRegion.y - mySheep.getY())/ counter);
		    		counter--;
		    		sheeps.remove(mySheep);
			    	mySheep.setX((int)x);
					mySheep.setY((int)y);
					sheeps.add(mySheep);
					repaint();
		    	}
			}
		};

		if (!mySheep.getType().equals(AnimalType.BLACKSHEEP)) {
			if (mySheep.getSheepTypeNumber().getNumber() > 1) {
				AnimalGUI oldSheep = new AnimalGUI(mySheep.getType(), mySheep.getRegionIndex());
				for (int i = 0; i < mySheep.getSheepTypeNumber().getNumber() - 2; i++) {
					oldSheep.getSheepTypeNumber().increaseNumber();
				}
				sheeps.add(oldSheep);
				repaint();
			}
		}
		Timer timer = new Timer(20, timerListener);
		timer.start();
	}
	/**
	 * Rimuove una pecora da una certa regione
	 * @param sheep tipo di ovino
	 * @param regionIndex indice della regione
	 */
	public void removeSheep(AnimalType type, int regionIndex) {
		for (AnimalGUI sheep : sheeps) {
			if (sheep.getType().equals(type) && sheep.getRegionIndex() == regionIndex) {
				if (!type.equals(AnimalType.BLACKSHEEP) && !type.equals(AnimalType.WOLF) &&
						sheep.getSheepTypeNumber().getNumber() > 1) {
					sheep.getSheepTypeNumber().decreaseNumber();
				} else {
					synchronized (sheeps) {
						sheeps.remove(sheep);
					}
				}
				break;
			}
		}
	}
	/**
	 * Esegue una paint di un animale all'interno di una certa regione
	 * @param type tipo dell'animale da rappresentare
	 * @param regionIndex regione nella quale rappresentare l'animale
	 */
	public void paintAnimal(AnimalType type, int regionIndex) {
		final AnimalType animalType = type;
		final int index = regionIndex;
		boolean existing = false;
		synchronized (sheeps) {
			for (AnimalGUI sheep : sheeps) {
				if (sheep.getRegionIndex() == index && 
						sheep.getType().equals(animalType)) {
					if (!animalType.equals(AnimalType.WOLF) && 
							!animalType.equals(AnimalType.BLACKSHEEP)) {
						sheep.getSheepTypeNumber().increaseNumber();
					}
					existing = true;
					break;
				}
			}
		}
		
		if (!existing) {
			AnimalGUI sheep = new AnimalGUI(animalType, index);
			synchronized (sheeps) {
				sheeps.add(sheep);
			}
		}
	}
	/**
	 * Esegue una paint di un pastore all'interno di una certa strada
	 * @param color colore del pastore da rappresentare
	 * @param roadIndex indice della strada contenente il pastore
	 */
	public void paintShepherd(String color, int roadIndex) {
		ShepherdGUI shepherd = new ShepherdGUI(color, roadIndex);
		occupiedRoads[roadIndex] = true;
		shepherds.add(shepherd);
		this.add(shepherd);
	}
	/**
	 * Aggiunge i RoadListener sulle strade
	 */
	public void addRoadListeners() {
		for (RoadGUI road : roads) {
			road.setEnabled(false);
			this.add(road);
		}
	}
	/**
	 * Aggiunge gli SheepListener sulle pecore
	 */
	public void addSheepListeners() {
		for (int i = 0; i < sheeps.size(); i++) {
			AnimalGUI sheep = sheeps.get(i);
			this.remove(sheep);
			sheeps.remove(i);
			sheep.setListener(new SheepListener(client, this, sheep));
			sheeps.add(i, sheep);
			this.add(sheep, i);
		}
	}
	/**
	 * Rimuove gli SheepListener di ogni pecora dello SheeplandPanel
	 */
	public void reinitializeSheeps() {
		for (int i = 0; i < sheeps.size(); i++) {
			sheeps.get(i).removeListener();
		}
	}
	/**
	 * Restituisce la lista di strade dello SheeplandPanel
	 * @return lista di RoadGUI
	 */
	public List<RoadGUI> getRoads() {
		return roads;
	}
	/**
	 * Restituisce la lista di pastori dello SheeplandPanel
	 * @return lista di ShepherdGUI
	 */
	public List<ShepherdGUI> getShepherds() {
		return shepherds;
	}
	/**
	 * Imposta il valore di verità in merito all'occupazione
	 * di una strada dello SheeplandPanel
	 * @param roadIndex indice della strada
	 */
	public void setOccupiedRoad(int roadIndex) {
		occupiedRoads[roadIndex] = true;
	}
	/**
	 * Imposta il valore di verità in merito alla non occupazione
	 * di una strada dello SheeplandPanel
	 * @param roadIndex indice della strada
	 */
	public void setFreeRoad(int roadIndex) {
		occupiedRoads[roadIndex] = false;
		fences.add(new FenceGUI(roadIndex));
		roads.get(roadIndex).setHasFence();
	}
	/**
	 * Restituisce l'array contenente i valori di verità
	 * in merito all'occupazione delle strade dello SheeplandPanel
	 * @return array di strade occupate
	 */
	public boolean[] getOccupiedRoads() {
		return occupiedRoads;
	}
	/**
	 * Restituisce il pastore del giocatore cui appartiene la GUI
	 * @return pastore associato alla GUI
	 */
	public ShepherdGUI getMyShepherd() {
		synchronized (shepherds) {
			for (ShepherdGUI shepherd : shepherds) {
				if (shepherd.getColor().equals(StaticGUI.getColor())) {
					return shepherd;
				}
			}
		}
		return null;
	}
	/**
	 * Restituisce il pastore corrispondente ad un determinato colore
	 * @param color colore del pastore
	 * @return pastore associato al colore
	 */
	public ShepherdGUI getShepherd(String color) {
		for (ShepherdGUI shepherd : shepherds) {
			if (shepherd.getColor().equals(color)) {
				return shepherd;
			}
		}
		return null;
	}
	/**
	 * Restituisce la lista di animali sulla plancia di gioco
	 * @return lista di animali sulla plancia di gioco
	 */
	public List<AnimalGUI> getSheeps() {
		return sheeps;
	}
	/**
	 * Restituisce la pecora sulla plancia di gioco avente un 
	 * determinato tipo e occupante una determinata regione
	 * @param generalSheepType tipo della pecora
	 * @param regionIndex indice della regione occupata dalla pecora
	 * @return
	 */
	public AnimalGUI getSheep(String generalSheepType, int regionIndex) {
		for (AnimalGUI sheep : sheeps) {
			if (sheep.getType().name().equals(generalSheepType) &&
					sheep.getRegionIndex() == regionIndex) {
				return sheep;
			}
		}
		return null;
	}
	/**
	 * Imposta il tipo di pecora
	 * @param type tipo di pecora
	 */
	public void setSheepType(String type) {
		this.sheepType = type;
	}
	/**
	 * Restituisce il tipo di pecora
	 * @return tipo di pecora
	 */
	public String getSheepType() {
		return sheepType;
	}
	/**
	 * Restituisce i recinti della GUI
	 * @return recinti della GUI
	 */
	public List<FenceGUI> getFences() {
		return fences;
	}
	
	@Override
	public Dimension getSize() {
		return Default.getFrameDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getFrameDimension();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return Default.getFrameDimension();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return Default.getFrameDimension();
	}

	@Override
	public void update(Graphics g) {
		this.paintComponent(g);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Rappresenta la plancia di gioco
		Image gameBoard = new ImageIcon(Default.gameBoardUrl).getImage();
		g.drawImage(gameBoard, 0, 0, Default.getBoardDimension().width, Default.getBoardDimension().height, null);

		// Rappresenta i pastori
		synchronized (shepherds) {
			for (ShepherdGUI shepherd : shepherds) {
				shepherd.paintComponent(g);
			}
		}
		// Rappresenta le pecore
		synchronized (sheeps) {
			for (AnimalGUI sheep : sheeps) {
				sheep.paintComponent(g);
			}
		}
		// Rappresenta i recinti
		synchronized (fences) {
			for (FenceGUI fence : fences) {
				fence.paintComponent(g);
			}
		}
		// Rappresenta le strade
		synchronized (roads) {
			for (RoadGUI road : roads) {
				if (road.isEnabled()) {
					road.paintComponent(g);
				}
			}
		}
	}
}
