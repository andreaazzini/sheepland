package it.polimi.deib.provaFinale2014.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;

/**
 * Utility class utilizzata per reperire tutti i dati costanti
 * interni alla GUI
 */
public class Default {
	private static int boardWidth = 450;
	private static int boardHeight = 646;
	private static int frameWidth = 450;
	private static int frameHeight = 646;
	static double scaleValue = .9;

	static int[][] roadHooks = {
		{129, 182},
		{158, 231},
		{85, 262},
		{158, 291},
		{158, 358},
		{120, 392},
		{162, 428},
		{127, 537},
		{194, 475},
		{231, 495},
		{192, 595},
		{267, 518},
		{304, 570},
		{305, 489},
		{334, 462},
		{380, 509},
		{376, 419},
		{437, 393},
		{393, 350},
		{393, 297},
		{449, 260},
		{400, 243},
		{418, 178},
		{361, 226},
		{331, 201},
		{351, 126},
		{297, 174},
		{260, 119},
		{255, 188},
		{206, 211},
		{244, 240},
		{225, 302},
		{190, 332},
		{227, 354},
		{235, 426},
		{270, 378}, 
		{305, 413},
		{306, 353},
		{338, 332},
		{301, 300},
		{315, 251},  
		{264, 274},
	};
	
	static int[][] sheepRegionHooks = {
		{70, 122},
		{113, 271},
		{183, 360},
		{76, 433},
		{172, 498},
		{257, 398},
		{252, 547},
		{328, 484},
		{327, 357},
		{402, 411},
		{418, 277},
		{336, 241},
		{438, 184},
		{406, 98},
		{281, 191},
		{308, 51},
		{141, 132},
		{183, 233},
		{250, 293}
	};

	static int[][] muttonRegionHooks = {
		{80, 176},
		{118, 320},
		{198, 410},
		{115, 459},
		{169, 537},
		{268, 445},
		{249, 588},
		{343, 537},
		{344, 398},
		{413, 447},
		{428, 321},
		{350, 280},
		{431, 218},
		{350, 142},
		{286, 222},
		{317, 116},
		{198, 159},
		{202, 258},
		{270, 326}
	};
	
	static int[][] lambRegionHooks = {
		{35, 168},
		{90, 345},
		{177, 397},
		{89, 497},
		{133, 573},
		{238, 456},
		{186, 631},
		{337, 587},
		{319, 420},
		{377, 460},
		{402, 312},
		{321, 287},
		{407, 211},
		{367, 181},
		{262, 234},
		{284, 106},
		{225, 130},
		{179, 279},
		{225, 315}
	};
	
	static int[][] blacksheepRegionHooks = {
		{107, 205},
		{96, 302},
		{180, 434},
		{113, 415},
		{203, 527},
		{270, 475},
		{218, 606},
		{318, 517},
		{361, 363},
		{389, 434},
		{416, 345},
		{365, 252},
		{424, 194},
		{403, 137},
		{261, 206},
		{343, 74},
		{187, 133},
		{169, 258},
		{248, 318}
	};
	
	static int[][] wolfRegionHooks = {
		{80, 221},
		{119, 354},
		{157, 386},
		{79, 472},
		{145, 517},
		{249, 428},
		{224, 579},
		{324, 563},
		{322, 393},
		{404, 481},
		{407, 275},
		{323, 266},
		{409, 196},
		{368, 140},
		{249, 207},
		{314, 86},
		{168, 166},
		{211, 234},
		{250, 347}
	};

	
	static Dimension actionButtonDimension = new Dimension(80, 80);
	
	static String gameBoardUrl = "Images/gameboard.png";
	
	static String[] regionUrls = {
		"Images/region-0.png",
		"Images/region-1.png",
		"Images/region-2.png",
		"Images/region-3.png",
		"Images/region-4.png",
		"Images/region-5.png",
		"Images/region-6.png",
		"Images/region-7.png",
		"Images/region-8.png",
		"Images/region-9.png",
		"Images/region-10.png",
		"Images/region-11.png",
		"Images/region-12.png",
		"Images/region-13.png",
		"Images/region-14.png",
		"Images/region-15.png",
		"Images/region-16.png",
		"Images/region-17.png",
		"Images/region-18.png"
	};
	
	static String sheeplandLogoUrl = "Images/sheepland_logo.png";
	
	static String moveShepherdButtonUrl = "Images/move_shepherd.png";
	static String moveSheepButtonUrl = "Images/move_sheep.png";
	static String coupleButtonUrl = "Images/couple.png";
	static String coupleSheepsButtonUrl = "Images/couple_sheeps.png";
	static String killButtonUrl = "Images/kill.png";
	static String buyCardButtonUrl = "Images/buy_card.png";
	
	static String hillCardButtonUrl = "Images/hill.png";
	static String plainCardButtonUrl = "Images/plain.png";
	static String lakeCardButtonUrl = "Images/lake.png";
	static String desertCardButtonUrl = "Images/desert.png";
	static String mountainCardButtonUrl = "Images/mountain.png";
	static String countryCardButtonUrl = "Images/country.png";
	
	static String sheepImageUrl = "Images/sheep.png";
	static String muttonImageUrl = "Images/mutton.png";
	static String lambImageUrl = "Images/lamb.png";
	static String blacksheepImageUrl = "Images/blacksheep.png";
	static String wolfImageUrl = "Images/wolf.png";
	static String sheepShadowUrl = "Images/sheep_shadow.png";
	
	static String blueShepherdUrl = "Images/blue-shepherd.png";
	static String greenShepherdUrl = "Images/green-shepherd.png";
	static String redShepherdUrl = "Images/red-shepherd.png";
	static String yellowShepherdUrl = "Images/yellow-shepherd.png";
	
	static String bluePlayerUrl = "Images/blue_player.png";
	static String greenPlayerUrl = "Images/green_player.png";
	static String redPlayerUrl = "Images/red_player.png";
	static String yellowPlayerUrl = "Images/yellow_player.png";
	
	static String fenceUrl = "Images/fence.png";
	static String fence2Url = "Images/fence2.png";
	static String moneyUrl = "Images/money.png";
	
	private Default() {
		// Non si vuole costruire un oggetto Default
	}
	/**
	 * Restituisce la dimensione del frame principale
	 * @return dimensione del frame principale
	 */
	public static Dimension getFrameDimension() {
		return new Dimension(frameWidth, frameHeight);
	}
	/**
	 * Restituisce la dimensione della plancia di gioco
	 * @return dimensione della plancia di gioco
	 */
	public static Dimension getBoardDimension() {
		return new Dimension(boardWidth, boardHeight);
	}
	/**
	 * Restituisce la dimensione di un ActionButton
	 * @return dimensione di un ActionButton
	 */
	public static Dimension getActionButtonDimension() {
		return new Dimension(40, 40);
	}
	/**
	 * Restituisce la dimensione di uno Shepherd
	 * @return dimensione di un Shepherd
	 */
	public static Dimension getShepherdDimension() {
		return new Dimension(32, 32);
	}
	/**
	 * Restituisce la dimensione di un CardButton
	 * @return dimensione di un CardButton
	 */
	public static Dimension getCardButtonDimension() {
		return new Dimension(40, 40);
	}
	/**
	 * Restituisce la dimensione dell'ActionButtonPanel
	 * @return dimensione dell'ActionButtonPanel
	 */
	public static Dimension getActionButtonPanelDimension() {
		return new Dimension(100, 150);
	}
	/**
	 * Restituisce la dimensione del BankPanel
	 * @return dimensione del BankPanel
	 */
	public static Dimension getCardButtonPanelDimension() {
		return new Dimension(100, 150);
	}
	/**
	 * Restituisce la dimensione del MarketPanel
	 * @return dimensione del MarketPanel
	 */
	public static Dimension getMarketPanelDimension() {
		return new Dimension(100, 300);
	}
	/**
	 * Restituisce la dimensione del TurnPanel
	 * @return dimensione del TurnPanel
	 */
	public static Dimension getTurnPanelDimension() {
		return new Dimension(boardWidth, 40);
	}
	/**
	 * Restituisce la dimensione del TextPanel
	 * @return dimensione del TextPanel
	 */
	public static Dimension getTextPanelDimension() {
		return new Dimension(320, boardHeight);
	}
	/**
	 * Restituisce la dimensione del box di un player
	 * @return
	 */
	public static Dimension getPlayerBoxDimension() {
		return new Dimension(120, 50);
	}
	/**
	 * Restituisce la dimensione del playerPanel
	 * @return
	 */
	public static Dimension getPlayerPanelDimension() {
		return new Dimension(130, boardHeight);
	}
	/**
	 * Restituisce il colore di backgruond della pnacia di gioco
	 * @return
	 */
	public static Color getBlue() {
		return new Color(35, 161, 246);
	}
	/**
	 * Restituisce l'immagine di un tipo di carta
	 * @param type indice del tipo di carta
	 * @return immagine del tipo di carta
	 */
	public static ImageIcon getCardButtonImage(int type) {
		/*
		 * Crea un array contenente le immagini delle carte e restituisce
		 * quella situtata all'indice desiderato
		 */
		return new ImageIcon[] {new ImageIcon(Default.hillCardButtonUrl),
				new ImageIcon(Default.plainCardButtonUrl),
				new ImageIcon(Default.lakeCardButtonUrl),
				new ImageIcon(Default.desertCardButtonUrl),
				new ImageIcon(Default.mountainCardButtonUrl),
				new ImageIcon(Default.countryCardButtonUrl)
		}[type];
	}
}
