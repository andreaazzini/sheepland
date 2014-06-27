package it.polimi.deib.provaFinale2014.controller;

/**
 * Gestore dello scorrere dei turni di gioco
 */
public class TurnHandler {
	private Game game;
	private Turn actualTurn;
	private int remainingFences;
	private boolean initialTurn;
	private boolean sellTurn;
	private boolean marketTurn;
	private int firstPlayerMarket;
	private boolean needEndSignal;
	
	/**
	 * Costruisce un oggetto TurnHandler inizializzando il numero
	 * di recinti rimanenti e la partita di cui si occupa
	 * @param game partita di cui il TurnHandler si occupa
	 */
	public TurnHandler(Game game) {
		this.game = game;
		actualTurn = new Turn();
		remainingFences = this.game.getGameBank().getAvailableFenceNumber();
		initialTurn = true;
		needEndSignal=false;
	}
	/**
	 * Seleziona il giocatore cui toccherà giocare alla fine di un
	 * turno di un giocatore
	 */
public void changeTurn() {
		
		remainingFences = this.game.getGameBank().getAvailableFenceNumber();
		int index = game.getPlayers().indexOf(game.getActivePlayer());
		// Chiede e opera il restore dei pastori
		if (game.onlyTwoPlayers() && game.getActivePlayer().needRestore()) {
			game.getActivePlayer().restoreShepherds();
		}
		game.getActivePlayer().becomeInactive();
		// Se il giocatore attivo è l'ultimo del giro, fa ripartire il giro
			
		if (index == game.getPlayers().size() - 1) {
			if (!initialTurn) {
				if (marketTurn) {
					game.getPlayers().get(0).becomeActive();
					if (firstPlayerMarket == 0) {
						marketTurn = false;
						if (!game.getMarket().getCards().isEmpty()) {
						game.returnUnselledCards();
						}
					}
				} else if (!sellTurn && !marketTurn) {
					game.getPlayers().get(0).becomeActive();
					game.initializeMarket();
					sellTurn = true;
				} else if (sellTurn) {
					sellTurn = false;
					if (game.getMarket().getCards().size()>0) {
						marketTurn = true;
						firstPlayerMarket = (int)(Math.random()*game.getPlayers().size());
						game.getPlayers().get(firstPlayerMarket).becomeActive();
					} else {
						game.getPlayers().get(0).becomeActive();
					}
				}
			} else {
				game.getPlayers().get(0).becomeActive();
				disableInitialTurn();
			}
			
		} else {
			if (firstPlayerMarket == index+1 && marketTurn) {
				game.getPlayers().get(0).becomeActive();
				marketTurn = false;
				this.needEndSignal  = true;
				if (!game.getMarket().getCards().isEmpty()) {
					game.returnUnselledCards();
				}
			} else { 
				game.getPlayers().get(index + 1).becomeActive();
			}
		}
		
		if (!sellTurn && !marketTurn) {
			// Fa evolvere gli agnelli in montone o pecora
			if (!initialTurn) {
				game.evolveLambs();
			}
			// Reinizializza il turno di gioco
			actualTurn = new Turn();
		}	
	
	}
		
	/**
	 * Restituisce il turno attualmente in corso dal
	 * giocatore di turno
	 */
	public Turn getActualTurn() {
		return actualTurn;
	}
	/**
	 * Verifica se sono ancora disponibili recinti nel banco
	 * @return true, se ci sono recinti disponibili; false, altrimenti
	 */
	private boolean hasRemainingFences() {
		return remainingFences != 0;
	}
	/**
	 * Verifica che la partita sia ancora in corso
	 * @return true, se la partita è ancora in corso; false, altrimenti
	 */
	public boolean consentTurnChanging() {
		// Consente lo scorrere dei turni nel caso in cui
		// non ci siano più recinti rimanenti e il giro non
		// sia terminato
		return hasRemainingFences() ||
				(!hasRemainingFences() && 
				game.getPlayers().indexOf(game.getActivePlayer()) > 0);
	}
	/**
	 * Verifica se il turno di gioco è il primo
	 * @return true, se il turno di gioco è il primo; false altrimenti
	 */
	public boolean isInitialTurn() {
		return initialTurn;
	}
	/**
	 * Imposta il turno come non iniziale
	 */
	public void disableInitialTurn() {
		initialTurn = false;
	}
	/**
	 * Verifica se il turno di gioco è un Market Turn
	 * @return true se lo è, false altrimenti
	 */
	public boolean isMarketTurn() {
		return marketTurn;
	}
	/**
	 * Verifica se il turno di gioco è un Sell Turn prima del market
	 * @return true se lo è, false altrimenti
	 */
	public boolean isSellTurn() {
		return sellTurn;
	}
	/**
	 * Controlla se il turnHandler necessita di un segnale di uscita
	 * @return true se il turnHandler necessita di un segnale di uscita;
	 * false altrimenti
	 */
	public boolean needEndSignal() {
		return this.needEndSignal;
	}
	
}
