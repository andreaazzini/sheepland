package it.polimi.deib.provaFinale2014.controller;

import it.polimi.deib.provaFinale2014.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Mercato delle carte terreno
 */
public class Market {
	private List<Card> forSale;
	private List<Integer> sellers;
	
	/**
	 * Costruisce il mercato inizializzando la lista di carte
	 * in vendita
	 */
	public Market() {
		forSale = new ArrayList<Card>();
		sellers = new ArrayList<Integer>();
	}
	
	/**
	 * Aggiunge una carta alla lista di carte in vendita
	 * @param card carta da mettere in vendita
	 */
	public void addCard(Card card, int newPrice, int seller) {
		card.setPrice(newPrice);
		forSale.add(card);
		sellers.add(seller);
	}
	/**
	 * Compra una carta
	 * @param cardIndex indice della carta all'interno delle forSaleCards
	 * @return carta acquistata
	 */
	public Card buyCard(int cardIndex) {
		Card card = forSale.get(cardIndex);
		// Rimuove la carta dalle forSaleCards
		forSale.remove(card);
		// Rimuove il seller di quella carta dalla lista dei sellers
		sellers.remove(cardIndex);
		return card;
	}
	
	public List<Card> getCards() {
		return forSale;
	}
	
	public List<Integer> getSellers() {
		return sellers;
	}
	/**
	 * Restituisce il venditore di una determinata carta
	 * @param index indice della carta all'interno delle forSaleCards
	 * @return indice del giocatore che ha venduto la carta
	 */
	public int getSeller(int index){
		return sellers.get(index);
	}
	/**
	 * Restituisce una determinata carta
	 * @param index indice della carta all'interno delle forSaleCards
	 * @return carta venduta
	 */
	public Card getCard(int index) {
		return forSale.get(index);
	}
	
}
