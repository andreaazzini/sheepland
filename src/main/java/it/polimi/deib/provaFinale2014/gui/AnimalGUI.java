package it.polimi.deib.provaFinale2014.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Animale rappresentabile all'interno della plancia di gioco
 */
public class AnimalGUI extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	private AnimalType type;
	private int regionIndex;
	private int x, y;
	private Image image;
	private Number number;
	private SheepListener listener;
	
	/**
	 * Tipo di animale rappresentabile
	 */
	public enum AnimalType {
		SHEEP, MUTTON, LAMB, BLACKSHEEP, WOLF
	}
	/**
	 * Costruisce un animale di un tipo AnimalType e lo inserisce
	 * nella regione corrispondente
	 * @param type tipo dell'animale
	 * @param regionIndex indice della regione
	 */
	public AnimalGUI(AnimalType type, int regionIndex) {
		super();
		
		this.type = type;
		this.regionIndex = regionIndex;
		
		initializePosition();
		
		// Tutti gli ovini eccetto la pecora nera hanno il numero informativo
		// Il lupo non ha il numero informativo
		if (!type.equals(AnimalType.BLACKSHEEP) && !type.equals(AnimalType.WOLF)) {
			number = new Number(this);
		}
		
		initializeImage();
		
		setBounds(x, y, 20, 23);
	}
	
	private void initializePosition() {
		if (type.equals(AnimalType.SHEEP)) {
			this.x = Default.sheepRegionHooks[regionIndex][0];
			this.y = Default.sheepRegionHooks[regionIndex][1];
		} else if (type.equals(AnimalType.MUTTON)) {
			this.x = Default.muttonRegionHooks[regionIndex][0];
			this.y = Default.muttonRegionHooks[regionIndex][1];
		} else if (type.equals(AnimalType.LAMB)) {
			this.x = Default.lambRegionHooks[regionIndex][0];
			this.y = Default.lambRegionHooks[regionIndex][1];
		} else if (type.equals(AnimalType.BLACKSHEEP)) {
			this.x = Default.blacksheepRegionHooks[regionIndex][0];
			this.y = Default.blacksheepRegionHooks[regionIndex][1];
		} else {
			this.x = Default.wolfRegionHooks[regionIndex][0];
			this.y = Default.wolfRegionHooks[regionIndex][1];
		}
		this.x = (int) (this.x * Default.scaleValue);
		this.y = (int) (this.y * Default.scaleValue);
	}
	
	private void initializeImage() {
		if (type.equals(AnimalType.SHEEP)) {
			image = new ImageIcon(Default.sheepImageUrl).getImage();
		} else if (type.equals(AnimalType.MUTTON)) {
			image = new ImageIcon(Default.muttonImageUrl).getImage();
		} else if (type.equals(AnimalType.LAMB)) {
			image = new ImageIcon(Default.lambImageUrl).getImage();
		} else if (type.equals(AnimalType.BLACKSHEEP)) {
			image = new ImageIcon(Default.blacksheepImageUrl).getImage();
		} else {
			image = new ImageIcon(Default.wolfImageUrl).getImage();
		}
	}
	/**
	 * Restituisce l'AnimalType dell'animale
	 * @return tipo dell'animale
	 */
	public AnimalType getType() {
		return type;
	}
	/**
	 * Restituisce l'indice della regione dell'animale
	 * @return indice della regione
	 */
	public int getRegionIndex() {
		return regionIndex;
	}
	/**
	 * Restituisce il numero informativo associato alla pecora
	 * @return
	 */
	public Number getSheepTypeNumber() {
		return number;
	}
	/**
	 * Imposta un listener su una pecora
	 * @param listener SheepListener personalizzato
	 */
	public void setListener(SheepListener listener) {
		this.listener = listener;
		addMouseListener(listener);
		setEnabled(true);
	}
	/**
	 * Rimuove il listener dall'AnimalGUI
	 */
	public void removeListener() {
		removeMouseListener(listener);
		setEnabled(false);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public int getX() {
		return this.x;
	}
	
	@Override
	public int getY() {
		return this.y;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, getX(), getY(), null);
		if (!type.equals(AnimalType.BLACKSHEEP) && !type.equals(AnimalType.WOLF)) {
			number.paintComponent(g);
		}
	}
}
