package it.polimi.deib.provaFinale2014.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Button relativo ad un'azione eseguibile nel gioco
 */
public class ActionButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruisce un ActionButton prendendo in ingresso l'immagine
	 * del button
	 * @param icon immagine del button
	 */
	public ActionButton(ImageIcon icon) {
		super(icon);
	}
	
	@Override
	public Dimension getSize() {
		return Default.getActionButtonDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getActionButtonDimension();
	}
}
