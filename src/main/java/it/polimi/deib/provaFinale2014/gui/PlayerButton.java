package it.polimi.deib.provaFinale2014.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Player rappresentabile sul frame principale
 */
public class PlayerButton extends JButton {
	private static final long serialVersionUID = 1L;
	/**
	 * Costruisce il player
	 * @param icon icona del player
	 */
	public PlayerButton(ImageIcon icon) {
		super(icon);
		
		setBorder(null);
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
