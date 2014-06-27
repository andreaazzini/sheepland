package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel che contiene i CardButton
 */
public class BankPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private CardButton hillButton;
	private CardButton plainButton;
	private CardButton lakeButton;
	private CardButton desertButton;
	private CardButton mountainButton;
	private CardButton countryButton;
	
	private int chosenCard;

	/**
	 * Costruisce un nuovo BankPanel inserisce al suo interno
	 * i CardButton
	 */
	public BankPanel() {
		this.setBackground(Default.getBlue());
		this.setLayout(new FlowLayout());
		hillButton = new CardButton(new ImageIcon(Default.hillCardButtonUrl));
		plainButton = new CardButton(new ImageIcon(Default.plainCardButtonUrl));
		lakeButton = new CardButton(new ImageIcon(Default.lakeCardButtonUrl));
		desertButton = new CardButton(new ImageIcon(Default.desertCardButtonUrl));
		mountainButton = new CardButton(new ImageIcon(Default.mountainCardButtonUrl));
		countryButton = new CardButton(new ImageIcon(Default.countryCardButtonUrl));
		
		hillButton.setEnabled(false);
		plainButton.setEnabled(false);
		lakeButton.setEnabled(false);
		desertButton.setEnabled(false);
		mountainButton.setEnabled(false);
		countryButton.setEnabled(false);
		
		this.add(hillButton);
		this.add(plainButton);
		this.add(lakeButton);
		this.add(desertButton);
		this.add(mountainButton);
		this.add(countryButton);
	}
	/**
	 * Attiva i listener delle carte
	 */
	public void activateCardListeners(ClientCommunication client, BankPanel bank, TurnPanel turn) {
		hillButton.setEnabled(true);
		plainButton.setEnabled(true);
		lakeButton.setEnabled(true);
		desertButton.setEnabled(true);
		mountainButton.setEnabled(true);
		countryButton.setEnabled(true);
		
		hillButton.addActionListener(new CardListener(client, bank, 0));
		plainButton.addActionListener(new CardListener(client, bank, 1));
		lakeButton.addActionListener(new CardListener(client, bank, 2));
		desertButton.addActionListener(new CardListener(client, bank, 3));
		mountainButton.addActionListener(new CardListener(client, bank, 4));
		countryButton.addActionListener(new CardListener(client, bank, 5));
	}
	/**
	 * Restituisce il CardButton corrispondente all'indice
	 * del tipo terreno della carta
	 * @param buttonIndex indice del tipo terreno
	 * @return CardButton corrispondente
	 */
	public CardButton getCardButton(int buttonIndex) {
		CardButton returnButton;
		switch (buttonIndex) {
		case 0:
			returnButton = hillButton;
			break;
		case 1:
			returnButton = plainButton;
			break;
		case 2:
			returnButton = lakeButton;
			break;
		case 3:
			returnButton = desertButton;
			break;
		case 4:
			returnButton = mountainButton;
			break;
		case 5:
			returnButton = countryButton;
			break;
		default:
			returnButton = null;
			break;
		}
		return returnButton;
	}
	
	public void setCard(int card) {
		chosenCard = card;
	}
	
	public int getChosenCard() {
		return chosenCard;
	}
	
	@Override
	public Dimension getSize() {
		return Default.getCardButtonPanelDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getCardButtonPanelDimension();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return Default.getCardButtonPanelDimension();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return Default.getCardButtonPanelDimension();
	}
}
