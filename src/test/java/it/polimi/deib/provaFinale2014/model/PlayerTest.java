package it.polimi.deib.provaFinale2014.model;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.model.Card;
import it.polimi.deib.provaFinale2014.model.Player;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	private Player player;
	
	@Before
	public void setUp() {
		player = new Player(false);
	}
	
	@Test
	public void initialization() {
		assertEquals(Player.PLAYERMONEY, player.getAvailableMoney());
	}
	
	@Test
	public void cards() {
		Card card = new Card(TerrainType.COUNTRY, 1,false);
		player.giveCard(card);
		assertSame(card, player.getCards().get(0));
	}
	
	@Test
	public void moneyTransactions() {
		assertEquals(Player.PLAYERMONEY, player.getAvailableMoney());
		int moneySpent = 12;
		player.spendMoney(moneySpent);
		assertEquals(Player.PLAYERMONEY - moneySpent, player.getAvailableMoney());
		int moneyReceived = 13;
		player.receiveMoney(moneyReceived);
		assertEquals(Player.PLAYERMONEY - moneySpent + moneyReceived,
				player.getAvailableMoney());
	}
	
	@Test
	public void buyCard() {
		Card card = new Card(TerrainType.COUNTRY, 1,false);
		int beforeBuyingMoney = player.getAvailableMoney();
		player.buyCard(card);
		assertEquals(beforeBuyingMoney - card.getCost(), player.getAvailableMoney());
		assertSame(card, player.getCards().get(0));
	}
	
	@Test
	public void activeness() {
		assertFalse(player.isActive());
		player.becomeActive();
		assertTrue(player.isActive());
	}
}
