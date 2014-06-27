package it.polimi.deib.provaFinale2014.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Componente contenente un numero informativo
 */
public class Number extends JPanel {
	private static final long serialVersionUID = 1L;
	private int number;
	
	private static int radius = 15;
	private static int shifting = 2;
	private int xCentre, yCentre;
	
	/**
	 * Costruisce il componente e a sua volta viene associato
	 * il component che lo richiede
	 * @param component JComponent che richiede il Number
	 */
	public Number(JComponent component) {
		super();
		this.setLayout(null);
		
		this.number = 1;
		this.xCentre = component.getX() + 9 * shifting;
		this.yCentre = component.getY() - shifting;
		this.setBounds(xCentre - radius / 2, yCentre - radius / 2, radius, radius);
	}
	/**
	 * Inizializza a 0 il valore del Number
	 */
	public void initialize() {
		while (this.number > 0) {
			decreaseNumber();
		}
	}
	/**
	 * Imposta la posizione del Number
	 * @param x coordinata longitudinale
	 * @param y coordinata altitudinale
	 */
	public void setPosition(int x, int y) {
		this.xCentre = x;
		this.yCentre = y;
	}
	/**
	 * Aumenta di 1 il valore di Number
	 */
	public void increaseNumber() {
		this.number++;
	}
	/**
	 * Diminuisce di 1 il valore di Number
	 */
	public void decreaseNumber() {
		this.number--;
	}
	/**
	 * Restituisce il numero informativo
	 * @return
	 */
	public int getNumber() {
		return number;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(new Color(255, 0, 0, 180));
		g.fillOval(this.xCentre - radius / 2, this.yCentre - radius / 2, radius, radius);
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(number), this.xCentre - 2 * shifting, this.yCentre + 2 * shifting + 1);
	}
}
