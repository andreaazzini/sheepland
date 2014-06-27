package it.polimi.deib.provaFinale2014.client;

/**
 * Intefaccia dei metodi di setting
 * invocabili lato Client dalle View.
 *
 */
public interface ClientCommunication {
	
	/**
	 * Imposta il valore dell'attributo cardType
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String cardType tipo della carta.
	 */
	void setCardType(String cardType);
	
	/**
	 * Imposta il valore dell'attributo sheepType
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String sheepType tipo dell'ovino.
	 */
	void setSheepType(String sheepType);
	
	/**
	 * Imposta il valore dell'attributo region
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String region, regione scelta.
	 */
	void setRegion(String region);
	
	/**
	 * Imposta il valore dell'attributo road
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String road, strada scelta.
	 */
	void setRoad(String road);
	
	/**
	 * Imposta il valore dell'attributo choice
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String choice, scelta effettuata.
	 */
	void setChoice(String choice);
	
	/**
	 * Imposta il valore dell'attributo chosenCard
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String chosen, numero della carta scelta.
	 */
	void setChosenCard(int index);
	
	/**
	 * Imposta il valore dell'attributo card
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param String[] card, attributi della carta scelta : tipo di carta e prezzo di vendita.
	 */
	void setSoldCard(String[] card);
	
	/**
	 * Imposta il valore dell'attributo chosenShepherd
	 * con il dato passato come parametro all'interno di un lock,
	 * successivamente notifica.
	 * @param int chosenShepherd, indice del pastore scelto.
	 */
	void setChosenShepherd(int chosenShepherd);
}
