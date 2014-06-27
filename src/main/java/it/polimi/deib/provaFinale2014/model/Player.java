package it.polimi.deib.provaFinale2014.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Giocatore della partita, ha assegnati uno o due pastori, delle carte
 * e dei danari e può prendere decisioni ad ogni turno
 */
public class Player {
	/**
	 * Danari disponibili per il giocatore all'inizio
	 * della partita
	 */
	static final int PLAYERMONEY = 20;
	static final int PLAYERMONEYTWOPLAYERS = 30;

	private List<Card> cards;
	private List<Shepherd> shepherds;
	private int availableMoney;
	private boolean active;
	private boolean restore;
	
	/**
	 * Costruisce un giocatore inizializzando
	 * le sue carte, pastori e danari
	 */
	public Player(boolean twoPlayers) {
		cards = new ArrayList<Card>();
		shepherds = new ArrayList<Shepherd>();
		initializeShepherds();
		if (twoPlayers) {
			availableMoney = PLAYERMONEYTWOPLAYERS;
		} else {
			availableMoney = PLAYERMONEY;
		}
		active = false;
		restore = false;
	}
	
	private void initializeShepherds() {
		for (int i = 0; i < 2; i++) {
			shepherds.add(new Shepherd(this));
		}
	}
	/**
	 * Ritorna la lista di carte attualmente possedute
	 * dal giocatore
	 * @return lista di carte possedute
	 */
	public List<Card> getCards() {
		return cards;
	}
	
	/**
	 * Ritorna il pastore principale controllato
	 * dal giocatore
	 * @return pastore principale
	 */
	public Shepherd getMainShepherd() {
		return shepherds.get(0);
	}
	
	/**
	 * Ritorna il pastore opzionale controllato
	 * dal giocatore
	 * @return pastore opzionale
	 */
	public Shepherd getOptionalShepherd() {
		return shepherds.get(1);
	}
	
	/**
	 * Ritorna l'ammontare di danari disponibli
	 * per il giocatore
	 * @return danari disponibili
	 */
	public int getAvailableMoney() {
		return availableMoney;
	}
	
	/**
	 * Assegna una carta al giocatore
	 * @param card carta da assegnare al giocatore
	 */
	public void giveCard(Card card) {
		cards.add(card);
	}
	/**
	 * Compra una carta se il giocatore ha soldi a sufficienza
	 * @param card carta da acquistare
	 * @throws Exception
	 */
	public void buyCard(Card card) {
		availableMoney -= card.getCost();
		cards.add(card);
	}
	/**
	 * Vende una carta
	 * @param card carta da vendere
	 */
	public void sellCard(Card card) {
		availableMoney += card.getCost();
		cards.remove(card);
	}
	/**
	 * Verifica che sia possibile comprare una carta
	 * @param card carta da acquistare
	 * @return true, se i danari sono sufficienti; false altrimenti
	 */
	public boolean hasMoneyForBuyingCard(Card card) {
		return availableMoney > card.getCost();
	}
	/**
	 * Aumenta il saldo attuale di danari disponibile
	 * di una quantità money
	 * @param money danari ricevuti
	 */
	public void receiveMoney(int money) {
		this.availableMoney += money;
	}
	
	/**
	 * Diminuisce il saldo attuale di danari
	 * di una quantità money
	 * @param money danari spesi
	 */
	public void spendMoney(int money) {
		this.availableMoney -= money;
	}
	/**
	 * Imposta il giocatore come attivo
	 */
	public void becomeActive() {
		active = true;
	}
	/**
	 * Imposta il giocatore come inattivo
	 */
	public void becomeInactive() {
		active = false;
	}
	/**
	 * Restituisce una valore di verità nel caso in cui
	 * il giocatore sia attivo
	 * @return true, se il giocatore è attivo; false altrimenti
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * Imposta il pastore principale
	 * @param shepherd 0 per il primo pastore, 1 per il secondo
	 */
	public void setUsedShepherd(int shepherd) {
		if (shepherd == 1) {
			// Pastore secondario da impostare come principale
			Shepherd temporaryShepherd = getOptionalShepherd();
			shepherds.add(0, temporaryShepherd);
			restore = true;
		}
	}
	/**
	 * Chiede se i due pastori devono essere ripristinati
	 * @return true, se i due pastori devono essere ripristinati; false altrimenti
	 */
	public boolean needRestore() {
		return restore;
	}
	/**
	 * Reimposta la lista dei pastori
	 */
	public void restoreShepherds() {
		// Rimuove il pastore opzionale che era stato
		// posizionato come primo della lista dei pastori
		shepherds.remove(0);
		restore = false;
	}
	
}
