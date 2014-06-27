package it.polimi.deib.provaFinale2014.controller;

import java.io.Serializable;

/**
 * Messaggi di controllo
 */
public enum Message implements Serializable {
	ROAD_ALREADY_OCCUPIED("INVALID CHOICE: Road is already occupied.\n"),
	INVALID_VALUE("INVALID CHOICE:The value is not valid"),
	NO_MATCHING_TYPE("INVALID CHOICHE: There are no sheeps of the choosen type in the choosen region"),
	NOT_ENOUGH_MONEY("You don't have enough money to perfom the action"),
	CANNOT_COUPLE_HERE("INVALID CHOICE: There are not enough sheeps to perform a couplin in the chosen region"),
	NO_BLACKSHEEP_HERE("INVALID CHOICE: The black sheep is not here!"),
	NOT_ADJACENT_REGION("INVALID CHOICE: The chosen region is not adjacent to your shepherd"),
	NONE("Action performed correctly"),
	INVALID_PASSWORD("INVALID PASSWORD: password was not correct"),
	FAIL("You failed..."),
	SUCCESS("You did it!"),
	GAME_PAUSE("Game Paused"),
	NICK_ALREADY_IN_USE("Nickname is already in use");
	
	
	private String message;

	private Message(String message) {
		this.message = message;
	}
	/**
	 * Restituisce il message
	 * @return messaggio
	 */
	public String getMessage() {
		return message;
	}
	
}
