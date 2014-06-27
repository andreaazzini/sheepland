package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel che contiene le carte in vendita nel market
 */
public class MarketPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String[] soldCard;
	private List<CardButton> cards;
	
	private JTextField textField;
	private JButton button;
	private JButton endSellTurn;
	private JButton endMarketTurn;
	
	private final ClientCommunication myClient;
	
	private static final Logger LOGGER = Logger.getLogger(MarketPanel.class.getName());
	
	/**
	 * Costruisce un nuovo MarketPanel senza carte al suo interno
	 */
	public MarketPanel(ClientCommunication client) {
		LOGGER.setUseParentHandlers(false);
		
		this.setBackground(new Color(33, 162, 246));
		this.setLayout(new FlowLayout());
		
		myClient = client;
		
		cards = new ArrayList<CardButton>();
		
		soldCard = new String[2];
	}
	/**
	 * Aggiunge alla GUI il button per stoppare
	 * il market turn
	 */
	public void addEndMarketButton() {
		endMarketTurn = new JButton("Stop buying");
		endMarketTurn.setPreferredSize(new Dimension(90, 20));
		endMarketTurn.setVisible(true);
		endMarketTurn.setEnabled(false);
		endMarketTurn.addActionListener(new ActionListener() {
			/**
			 * Azione eseguita al fire del button
			 */
			public void actionPerformed(ActionEvent arg0) {
				endMarketTurn.setEnabled(false);
				myClient.setChosenCard(0);
			}
		});
		this.add(endMarketTurn);
	}
	/**
	 * Aggiunge alla GUI i buttons utili nella fase
	 * di sell turn
	 */
	public void addEndSellTurnButtons() {
		textField = new JTextField();
		button = new JButton("OK");
		endSellTurn = new JButton("Stop selling");
		
		textField.setPreferredSize(new Dimension(45, 20));
		button.setPreferredSize(new Dimension(45, 20));
		endSellTurn.setPreferredSize(new Dimension(90, 20));
		
		textField.setEnabled(false);
		button.setEnabled(false);
		endSellTurn.setEnabled(false);
		
		textField.setVisible(true);
		button.setVisible(true);
		endSellTurn.setVisible(true);
		
		endSellTurn.addActionListener(new ActionListener() {
			/**
			 * Azione eseguita al fire del button
			 */
			public void actionPerformed(ActionEvent arg0) {
				removeAll();
				myClient.setSoldCard(null);
			}
		});
		
		this.add(textField);
		this.add(button);
		this.add(endSellTurn);
	}
	/**
	 * Aggiunge una carta alla lista delle carte in vendita
	 * @param type tipo di carta in vendita
	 */
	public void addSoldCardType(String type) {
		soldCard[0] = type;
	}
	/**
	 * Aggiunge un CardButton al MarketPanel
	 * @param type tipo della carta
	 * @param price prezzo assegnato alla carta
	 */
	public void addCardButton(int type, int price) {
		// Prende l'immagine della carta terreno corrispondente al tipo richiesto
		ImageIcon icon = Default.getCardButtonImage(type);
		// Crea la carta terreno
		CardButton cardButton = new CardButton(icon);
		// Modifica il prezzo e la aggiunge al panel
		cardButton.setNumber(price);
		cards.add(cardButton);
		this.add(cardButton);
	}
	/**
	 * Setta l'editabilità del textField e del button
	 * @param enabled
	 */
	public void enableAddingSoldCards() {
		textField.setEnabled(true);
		button.setEnabled(true);
		button.addActionListener(new ActionListener() {
			/**
			 * Azione eseguita al fire del button
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					int value = Integer.parseInt(textField.getText());
					if (value > 0) {
						soldCard[1] = String.valueOf(value);
						myClient.setSoldCard(soldCard);
						removeAll();
					} else {
						StaticGUI.getTextPanel().appendText("INVALID PRICE: choose a price > 0");
					}
				} catch (NumberFormatException ex) {
					LOGGER.log(Level.INFO, "Invalid price", ex);
					StaticGUI.getTextPanel().appendText("INVALID PRICE: choose a valid numerical price");
				}
			}
		});
	}
	/**
	 * Attiva i card listener del market
	 */
	public void activateCardListeners() {
		final List<CardButton> myCards = cards;
		endMarketTurn.setEnabled(true);
		for (Component card : this.getComponents()) {
			if (card instanceof CardButton) {
				final CardButton myCard = (CardButton) card;
				myCard.addActionListener(new ActionListener() {
					/**
					 * Azione eseguita al fire del button di una carta
					 */
					public void actionPerformed(ActionEvent e) {
						myClient.setChosenCard(myCards.indexOf(myCard) + 1);
					}
				});
			}
		}
	}
	/**
	 * Restituisce il button di fine sellTurn
	 * @return end
	 */
	public JButton getEndButton() {
		return endSellTurn;
	}
	/**
	 * Restituisce il button di fine marketTurn
	 * @return
	 */
	public JButton getEndMarketTurnButton() {
		return endMarketTurn;
	}
	
	@Override
	public Dimension getSize() {
		return Default.getMarketPanelDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getMarketPanelDimension();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return Default.getMarketPanelDimension();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return Default.getMarketPanelDimension();
	}
	/**
	 * Restituisce le carte all'interno del market
	 * @return carte del market
	 */
	public List<CardButton> getMarketCards() {
		return this.cards;
	}
}
