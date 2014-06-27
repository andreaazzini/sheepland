package it.polimi.deib.provaFinale2014.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

/**
 * Strada rappresentabile sulla plancia di gioco
 */
public class RoadGUI extends JButton {
	private static final long serialVersionUID = 1L;
	
	private int radius = 25;
	private int roadIndex;
	private int xCentre, yCentre;
	private boolean hasFence;

	/**
	 * Costruisce la strada prendendo in ingresso l'indice
	 * della strada
	 * @param roadIndex indice della strada
	 */
	public RoadGUI(int roadIndex) {
		super();
		
		this.roadIndex = roadIndex;
		this.xCentre = Default.roadHooks[roadIndex][0];
		this.yCentre = Default.roadHooks[roadIndex][1];
		
		this.setEnabled(false);
		
		this.xCentre = (int) (this.xCentre * Default.scaleValue);
		this.yCentre = (int) (this.yCentre * Default.scaleValue);
		this.radius = (int) (this.radius * Default.scaleValue);
		
		this.setBounds(xCentre - radius / 2, yCentre - radius / 2, radius, radius);
	}
	/**
	 * Restituisce l'indice della strada
	 * @return indice della strada
	 */
	public int getRoadIndex() {
		return roadIndex;
	}
	/**
	 * Imposta che la strada possiede un recinto
	 */
	public void setHasFence() {
		hasFence = true;
	}
	/**
	 * Restituisce il valore di verità in merito all'esistenza di un recinto
	 * sulla strada
	 * @return true, se la strada possiede un recinto; false altrimenti
	 */
	public boolean hasFence() {
		return hasFence;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (isEnabled()) {
			g.setColor(new Color(0, 0, 0, 80));
			g.fillOval(this.xCentre + 1 - radius / 2, this.yCentre + 1 - radius / 2, radius, radius);
			setVisible(true);
		}
	}
}