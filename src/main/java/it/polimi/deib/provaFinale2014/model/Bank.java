package it.polimi.deib.provaFinale2014.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Banco del gioco, contiene recinti, carte e danari che non sono
 * ancora stati assegnati ad alcun giocatore
 */
public class Bank {
	/*
	 * Costanti del banco
	 */
	private static final int FENCES = 20;
	public static final int TYPES = 6;
	private static final int COSTS = 5;
	
	/**
	 * Recinti non ancora utilizzati
	 */
	private List<Fence> availableFences;
	/**
	 * Carte che non appartengono ad alcun giocatore
	 */
	private List<Card> availableCards;
	/**
	 * Carte che verranno distribuite ai giocatori nel
	 * primo turno di gioco
	 */
	private List<Card> initialCards;
	
	/**
	 * Costruisce un banco inizializzando la lista di
	 * recinti attualmente non inseriti nella plancia e
	 * la lista di carte ancora non assegnate ad alcun
	 * giocatore
	 */
	public Bank() {
		initializeFences();
		initializeCards();
	}
	/**
	 * Inizializza i recinti all'interno del banco
	 */
	private void initializeFences() {
		// Inizializza i recinti nel banco
		availableFences = new ArrayList<Fence>();
		for (int i = 0; i < FENCES; i++) {
			availableFences.add(new Fence());
		}
	}
	/**
	 * Inizializza le carte all'interno del banco
	 */
	private void initializeCards() {
		// Inizializza le carte nel banco
		availableCards = new ArrayList<Card>();
		initialCards = new ArrayList<Card>();
		
		// Inizializza le carte iniziali
		for (int i = 0; i < TYPES; i++) {
			initialCards.add(new Card(TerrainType.values()[i], 0, true));
		}
		
		// Inizializza le altre carte
		for (int i = 0; i < TYPES; i++) {
			for (int j = 0; j < COSTS; j++) {
				/*
				 * Ogni 5 carte viene assegnato un tipo terreno
				 * diverso
				 */
				availableCards.add(new Card(TerrainType.values()[i], j, false));
			}
		}
	}
	/**
	 * Restituisce la lista di carte distribuibili
	 * nel primo turno di gioco
	 * @return lista di carte iniziali
	 */
	public List<Card> getInitialCards() {
		return initialCards;
	}
	/**
	 * Restituisce la lista di carte ancora disponibili
	 * all'interno del banco
	 * @return lista di carte disponibili
	 */
	public List<Card> getAvailableCards() {
		return availableCards;
	}
	/**
	 * Restituisce la Carta più economica se disponibile
	 * @param type, tipo di terreno della carta
	 * @return card, se esiste ancora una carta di quel tipo nel banco,
	 * 			null altrimenti
	 */
	public Card getCheaperCard (TerrainType type) {
		for (Card card : availableCards) {
			if (card.getTerrainType().equals(type)) {
				return card;
			}
		}
		
		return null;
	}
	/**
	 * Restituisce il numero di recinti ancora non 
	 * utilizzati
	 * @return numero di recinti non ancora utilizzati
	 */
	public int getAvailableFenceNumber() {
		return availableFences.size();
	}
	/**
	 * Dà una carta di un certo tipo terreno ad un giocatore
	 * @param player giocatore a cui dare la carta
	 * @param type tipo terreno della carta
	 */
	public void giveCard(Player player, TerrainType type) {
		Card card = this.getCheaperCard(type);
		player.buyCard(card);
		availableCards.remove(card);
	}
	
	/**
	 * Verifica che un player possa acquistare delle carte 
	 * @param player giocatore
	 * @return true, se il player può acquistare carte; false altrimenti
	 */
	public boolean playerCanBuyCards(Player player) {
		for (int i = 0; i < this.getAvailableCards().size(); i++) {
			/*
			 * È possibile acquistare una carta se ne esiste una tra le disponibili
			 * che costi meno dei danari disponibili per il player, e se quella carta
			 * è di un tipo terreno coincidente con quello delle regioni adiacenti
			 * alla posizione del pastore controllato dal player
			 */
			if (this.getAvailableCards().get(i).getCost() < player.getAvailableMoney()
				&& (this.getAvailableCards().get(i).getTerrainType().equals(player.getMainShepherd().getPosition().getAdjacentRegions()[0].getType())
				||	this.getAvailableCards().get(i).getTerrainType().equals(player.getMainShepherd().getPosition().getAdjacentRegions()[1].getType())	)) {
				return true;
			}
		}
		
		return false;
	}
	/**
	 * Restituisce il primo recinto nella lista dei recinti disponibili
	 * @return recinto disponibile
	 */
	public Fence getFirstFence() {
		Fence fence;
		// Se ci sono ancora recinti disponibili
		if (!availableFences.isEmpty()) {
			// Ne prende uno
			fence = availableFences.get(0);
			// Rimuove il recinto dalla lista dei recinti disponibili
			availableFences.remove(fence);
		} else {
			// Altrimenti ne crea uno nuovo (recinti finali)
			fence = new Fence();
		}
		return fence;
	}
}
