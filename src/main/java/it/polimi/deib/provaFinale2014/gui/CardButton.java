package it.polimi.deib.provaFinale2014.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * Button che rappresenta le carte presenti nel gioco
 */
public class CardButton extends JButton {
	private static final long serialVersionUID = 1L;
	private Number number;
	
	/**
	 * Costruisce un nuovo CardButton e inizializza a 0 il
	 * prezzo della carta di quel tipo
	 * @param icon immagine che identifica carta
	 */
	public CardButton(ImageIcon icon) {
		super(icon);
		
		setBorder(LineBorder.createGrayLineBorder());
		
		this.number = new Number(this);
		this.number.setPosition(31, 7);
		this.number.initialize();
	}
	/**
	 * Imposta il prezzo della carta più economica di quel
	 * tipo terreno
	 * @param number prezzo della carta più economica
	 */
	public void setNumber(int number) {
		for (int i = 0; i < number; i++) {
			this.number.increaseNumber();
		}
	}
	/**
	 * Restituisce il Number associato al CardButton
	 * @return Number associato
	 */
	public Number getNumber() {
		return number;
	}
	/**
	 * Crea un CardButton senza informazione numerica
	 */
	public void noNumber() {
		number = null;
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
	public Dimension getMinimumSize() {
		return Default.getActionButtonDimension();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return Default.getActionButtonDimension();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (number != null) {
			number.paintComponent(g);
		}
	}
}
