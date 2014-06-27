package it.polimi.deib.provaFinale2014.controller;

import it.polimi.deib.provaFinale2014.exceptions.ActionLimitExceededException;
import it.polimi.deib.provaFinale2014.model.Bank;
import it.polimi.deib.provaFinale2014.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestore del singolo turno di gioco e delle azioni eseguibili
 */
public class Turn {
	private List<Action> madeActions;
	private static final int ACTIONSPERTURN = 3;
	
	/**
	 * Crea nuovo turno di gioco
	 * @param Player giocatore che deve iniziare il turno
	 */
	public Turn() {
		// Inizialmente nessuna azione è stata eseguita
		madeActions = new ArrayList<Action>();
	}
	/**
	 * Restituisce la lista di azioni che è possibile
	 * eseguire dal giocatore durante il suo turno
	 * @return lista di azioni eseguibili
	 * @throws ActionLimitExceededException
	 */
	public List<Action> availableActions(Player player, Bank bank) throws ActionLimitExceededException {
		// Aggiunge alla lista tutte le azioni che è possibile eseguire
		List<Action> availableActions = checkAllActions(player, bank);
		
		// Controllo se il pastore si è mosso e in quale azione
		boolean shepherdMoved = false;
		int lastMove = 0;
		for (int i = 0; i < madeActions.size(); i++) {
			if (madeActions.get(i).equals(Action.MOVE_SHEPHERD)) {
				shepherdMoved = true;
				lastMove = i;
			}
		}
		
		if (madeActions.size() == ACTIONSPERTURN - 1 && !shepherdMoved) {
			List<Action> onlyAction = new ArrayList<Action>();
			onlyAction.add(Action.MOVE_SHEPHERD);
			
			return onlyAction;
		} else {
			int i;
			if (!shepherdMoved) {
	 			i = lastMove;
			} else {
				i = lastMove + 1;
			}
			for (; i < madeActions.size(); i++) {
				availableActions.remove(madeActions.get(i));
			}
		} return availableActions;
	}
		
	/**
	 * Marca un'azione come scelta e la inserisce nella
	 * lista di azioni eseguite
	 * @param action azione scelta
	 */
	public void setChosenAction(Action action) {
		madeActions.add(action);
	}
	/**
	 * Restituisce l'ultima azione scelta dal giocatore
	 * @return ultima azione scelta
	 */
	public Action getChosenAction() {
		return madeActions.get(madeActions.size() - 1);
	}
	
	private List<Action> checkAllActions (Player player, Bank bank) {
		List<Action> availableActions = new ArrayList<Action>();
		//è sempre possibile muovere un pastore
		availableActions.add(Action.MOVE_SHEPHERD);
		if (player.getMainShepherd().canMove()) 
			availableActions.add(Action.MOVE_SHEEP);
		if (player.getMainShepherd().canCouple())
			availableActions.add(Action.COUPLE);
		if (player.getMainShepherd().canCoupleSheeps()) 
			availableActions.add(Action.COUPLE_SHEEPS);
		if (player.getMainShepherd().canKill())
			availableActions.add(Action.KILL);
		if (bank.playerCanBuyCards(player)) {
			availableActions.add(Action.BUY_CARD);
		}
		return availableActions;
	}
}