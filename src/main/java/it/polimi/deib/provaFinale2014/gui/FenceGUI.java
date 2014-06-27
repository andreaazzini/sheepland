package it.polimi.deib.provaFinale2014.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Fence rappresentabile sulla plancia di gioco
 */
public class FenceGUI extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private Image image;
	private int x, y;
	private int roadIndex;
	
	/**
	 * Costruisce un recinto all'interno della strada scelta
	 * @param roadIndex indice della strada scelta
	 */
	public FenceGUI(int roadIndex) {
		image = new ImageIcon(Default.fenceUrl).getImage();
		this.x = (int)(Default.roadHooks[roadIndex][0] * Default.scaleValue);
		this.y = (int)(Default.roadHooks[roadIndex][1] * Default.scaleValue);
		this.roadIndex = roadIndex;
	}
	
	public int getRoadIndex() {
		return roadIndex;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, x - 16, y - 16, null);
	}
}
