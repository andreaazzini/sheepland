package it.polimi.deib.provaFinale2014.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Button con unico scopo informativo
 */
public class InfoButton extends JButton {
	private static final long serialVersionUID = 1L;
	private Number number;
	
	/**
	 * Costruisce un InfoButton inserendo l'immagine che lo
	 * contraddistingue e impostando a 0 il numero associato
	 * @param icon immagine dell'InfoButton
	 */
	public InfoButton(ImageIcon icon) {
		super(icon);
		
		setBorder(null);
		
		this.number = new Number(this);
		this.number.setPosition(31, 7);
		this.number.initialize();
	}
	/**
	 * Restituisce il Number component associato all'InfoButton
	 * @return Number component associato
	 */
	public Number getNumber() {
		return number;
	}
	/**
	 * Imposta il numero informativo
	 * @param number numero informativo
	 */
	public void setNumber(int number) {
		this.number.initialize();
		for (int i = 0; i < number; i++) {
			this.number.increaseNumber();
		}
	}
	
	@Override
	public Dimension getSize() {
		return Default.getActionButtonDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getActionButtonDimension();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		number.paintComponent(g);
	}
}
