package it.polimi.deib.provaFinale2014.gui;

import java.awt.Color;

import javax.swing.JFrame;

/**
 * Frame personalizzato per la GUI di Sheepland
 */
public class SheeplandFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruisce un frame di Sheepland
	 * @param title titolo del frame
	 */
	public SheeplandFrame(String title) {
		super(title);
		this.setBackground(new Color(43, 139, 255));
		setSize(Default.getFrameDimension());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
}
