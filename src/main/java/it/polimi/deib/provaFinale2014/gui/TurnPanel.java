package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel contenente le icone dei player e altre icone
 * informative
 */
public class TurnPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private PlayerButton player1;
	private PlayerButton player2;
	private PlayerButton player3;
	private PlayerButton player4;
	
	private InfoButton remainingFences;
	private InfoButton availableMoney;
	
	private JPanel playerPanel;
	private JPanel infoPanel;
	private JPanel cardPanel;
	
	private List<Integer> types;
	
	/**
	 * Costruisce il TurnPanel inizializzando i buttons contenuti
	 * al suo interno
	 */
	public TurnPanel() {
		types = new ArrayList<Integer>();
		
		this.setBackground(new Color(37, 163, 252));
		this.setLayout(new BorderLayout());
		
		playerPanel = new JPanel();
		infoPanel = new JPanel();
		cardPanel = new JPanel();
		
		playerPanel.setLayout(new FlowLayout());
		infoPanel.setLayout(new FlowLayout());
		cardPanel.setLayout(new FlowLayout());
		
		player1 = new PlayerButton(new ImageIcon(Default.blueShepherdUrl));
		player2 = new PlayerButton(new ImageIcon(Default.greenShepherdUrl));
		player3 = new PlayerButton(new ImageIcon(Default.redShepherdUrl));
		player4 = new PlayerButton(new ImageIcon(Default.yellowShepherdUrl));
		
		remainingFences = new InfoButton(new ImageIcon(Default.fence2Url));
		availableMoney = new InfoButton(new ImageIcon(Default.moneyUrl));
		
		player1.setFocusable(false);
		player2.setFocusable(false);
		player3.setFocusable(false);
		player4.setFocusable(false);
		remainingFences.setFocusable(false);
		availableMoney.setFocusable(false);
		
		playerPanel.add(player1);
		playerPanel.add(player2);
		playerPanel.add(player3);
		playerPanel.add(player4);
		infoPanel.add(remainingFences);
		infoPanel.add(availableMoney);
		
		this.add(playerPanel, BorderLayout.WEST);
		this.add(cardPanel, BorderLayout.CENTER);
		this.add(infoPanel, BorderLayout.EAST);
	}
	/**
	 * Attiva il bottone del giocatore cui appartiene la GUI
	 * @param playerIndex indice del giocatore
	 */
	public void giveTurnTo(int playerIndex) {
		switch (playerIndex) {
		case 0:
			player1.setEnabled(true);
			player2.setEnabled(false);
			player3.setEnabled(false);
			player4.setEnabled(false);
			break;
		case 1:
			player1.setEnabled(false);
			player2.setEnabled(true);
			player3.setEnabled(false);
			player4.setEnabled(false);
			break;
		case 2:
			player1.setEnabled(false);
			player2.setEnabled(false);
			player3.setEnabled(true);
			player4.setEnabled(false);
			break;
		case 3:
			player1.setEnabled(false);
			player2.setEnabled(false);
			player3.setEnabled(false);
			player4.setEnabled(true);
			break;
		default:
			break;
		}
	}
	/**
	 * Concede il turno ad un pastore
	 * @param mainShepherdIndex indice del pastore principale
	 * @param optShepherdIndex indice del pastore opzionale
	 */
	public void giveTurnTo(int mainShepherdIndex, int optShepherdIndex) {
		if (mainShepherdIndex == 0 && optShepherdIndex == 1) {
			player1.setEnabled(true);
			player2.setEnabled(true);
			player3.setEnabled(false);
			player4.setEnabled(false);
		} else if (mainShepherdIndex == 2 && optShepherdIndex == 3) {
			player1.setEnabled(false);
			player2.setEnabled(false);
			player3.setEnabled(true);
			player4.setEnabled(true);
		}
	}
	/**
	 * Restituisce il button dei recinti rimanenti
	 * @return
	 */
	public InfoButton getRemainingFences() {
		return remainingFences;
	}
	/**
	 * Restituisce il button dei danari rimanenti
	 * @return
	 */
	public InfoButton getAvailableMoney() {
		return availableMoney;
	}
	
	public JPanel getCardPanel() {
		return cardPanel;
	}
	/**
	 * Aggiunge una carta al TurnPanel
	 * @param type indice del tipo di terreno della carta
	 */
	public void addCardButton(int type) {
		CardButton cardButton = new CardButton(Default.getCardButtonImage(type));
		// La carta non ha numemro informativo
		cardButton.noNumber();
		// Aggiunge la carta al panel
		types.add(type);
		cardPanel.add(cardButton);
	}
	/**
	 * Attiva i listener per scegliere il pastore da muovere
	 */
	public void addShepherdListeners(ClientCommunication client) {
		player1.addActionListener(new ShepherdListener(client, this, 0));
		player2.addActionListener(new ShepherdListener(client, this, 1));
		player3.addActionListener(new ShepherdListener(client, this, 2));
		player4.addActionListener(new ShepherdListener(client, this, 3));
	}
	/**
	 * Attiva i listener delle carte possedute dal player
	 * @param client gestione della comunicazione lato client
	 * @param market panel dedeicato al market
	 */
	public void activateCardListeners(ClientCommunication client, MarketPanel market) {
		int i = 0;
		for (Component component : cardPanel.getComponents()) {
			if (component instanceof CardButton ) {
				if (i>0) {
				((CardButton)component).addActionListener(new CardListener(client, market, types.get(i)));
				} else {
					((CardButton)component).setEnabled(false);
				}
				i++;
			}
		}
	}
	
	public List<Integer> getTypes() {
		return types;
	}
 }
