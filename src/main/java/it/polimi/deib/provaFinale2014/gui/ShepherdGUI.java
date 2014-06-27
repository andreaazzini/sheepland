package it.polimi.deib.provaFinale2014.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;

/**
 * Pastore rappresentabile sulla plancia di gioco
 */
public class ShepherdGUI extends DraggableImageComponent {
	private static final long serialVersionUID = 1L;
	private static final int SHIFTING = 20;
	
	private final String color;
	private int x, y;
	private int roadIndex;
	
	private static final String BLUE = "blue";
	private static final String GREEN = "green";
	private static final String RED = "red";
	private static final String YELLOW = "yellow";
	
	/**
	 * Costruisce uno ShepherdGUI in base al colore del player e
	 * all'indice della strada associata
	 * @param color colore associato al pastore
	 * @param roadIndex indice della strada del pastore
	 */
	public ShepherdGUI(String color, int roadIndex) {
		super();
		
		this.color = color;
		this.roadIndex = roadIndex;
		
		if (color.equals(BLUE)) {
			image = new ImageIcon(Default.blueShepherdUrl).getImage();
		} else if (color.equals(RED)) {
			image = new ImageIcon(Default.redShepherdUrl).getImage();
		} else if (color.equals(YELLOW)) {
			image = new ImageIcon(Default.yellowShepherdUrl).getImage();
		} else if (color.equals(GREEN)) {
			image = new ImageIcon(Default.greenShepherdUrl).getImage();
		}
		
		move(roadIndex);
		setBounds(x, y, getSize().width, getSize().height);
	}
	/**
	 * Muove il pastore nella strada di destinazione
	 * @param roadIndex strada di destinazione
	 */
	public void move(int roadIndex) {
		this.x = Default.roadHooks[roadIndex][0] - SHIFTING;
		this.y = Default.roadHooks[roadIndex][1] - SHIFTING;
		this.x = (int) (this.x * Default.scaleValue);
		this.y = (int) (this.y * Default.scaleValue);
		this.roadIndex = roadIndex;
	}
	/**
	 * Restituisce il colore associato al pastore
	 * @return colore associato al pastore
	 */
	public String getColor() {
		return color;
	}
	/**
	 * Restituisce l'indice della strada nella quale è situato il pastore
	 * @return indice della strada su cui è situato il pastore
	 */
	public int getRoadIndex() {
		return roadIndex;
	}
	
	public void setRoadIndex(int roadIndex) {
		this.roadIndex = roadIndex;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override public int getY() {
		return y;
	}
	
	@Override
	public Dimension getSize() {
		return Default.getShepherdDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getShepherdDimension();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return Default.getShepherdDimension();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return Default.getShepherdDimension();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, x + 2, y - 2, null);
	}
}
