package it.polimi.deib.provaFinale2014.gui;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action Listener personalizzato per le carte del bankPanel
 */
public class CardListener implements ActionListener {
	private ClientCommunication client;
	private BankPanel panel;
	private int card;
	private String type;

	private MarketPanel marketPanel;
	
	private boolean market;
	
	/**
	 * Costruisce un CardListener dati un ClientCommunication e una carta
	 * @param client gestore comunicazione lato client
	 * @param card indice della carta
	 */
	public CardListener(ClientCommunication client, BankPanel panel, int card) {
		this.client = client;
		this.panel = panel;
		this.card = card;
		market = false;
	}
	/**
	 * Costruisce un CardListener per la gestione del sellTurn durante
	 * la fase di market
	 * @param client gestore comunicazione lato client
	 * @param turnPanel turnPanel personale del giocatore
	 * @param marketPanel panel dedicato al market
	 * @param cardIndex indice della carta nella lista delle carte in vendita
	 * @param card indice della carta
	 */
	public CardListener(ClientCommunication client, MarketPanel marketPanel, int card) {
		this.client = client;
		this.marketPanel = marketPanel;
		this.card = card;
		market = true;
		this.marketPanel.getEndButton().setEnabled(true);
	}
	/**
	 * Invia l'informazione sulla carta selezionata tramite il ClientCommunication
	 */
	public void actionPerformed(ActionEvent e) {
		if (!market) {
			this.type = TerrainType.values()[this.card].name();
			client.setCardType(this.type);
			panel.setCard(card);
		} else {
			marketPanel.addSoldCardType(String.valueOf(this.card));
			marketPanel.enableAddingSoldCards();
		}
	}
	/**
	 * Restituisce il tipo di carta sotto forma di stringa
	 * @return stringa del tipo di carta
	 */
	public String getType() {
		return type;
	}

}
