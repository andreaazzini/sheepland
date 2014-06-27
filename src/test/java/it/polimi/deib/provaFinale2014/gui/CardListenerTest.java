package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;
import it.polimi.deib.provaFinale2014.model.TerrainType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CardListenerTest {
	private static ClientCommunication client;
	private static BankPanel bankPanel;
	private int action;
	private CardListener cardListener;
	
	@Parameters
	public static Collection<Object[]> data() {
		client = new ClientHandler();
		bankPanel = new BankPanel();
		return Arrays.asList(new Object[][] 
				{{0, new CardListener(client, bankPanel, 0)},
				{1, new CardListener(client, bankPanel, 1)},
				{2, new CardListener(client, bankPanel, 2)},
				{3, new CardListener(client, bankPanel, 3)},
				{4, new CardListener(client, bankPanel, 4)},
				{5, new CardListener(client, bankPanel, 5)},
				{6, new CardListener(client, bankPanel, 6)},
				});
	}
	
	public CardListenerTest(int action, CardListener cl) {
		this.cardListener = cl;
		this.action = action;
		cl.actionPerformed(new ActionEvent(cl, action, null));
	}

	@Test
	public void actionPerformed() {
		assertEquals(action, bankPanel.getChosenCard());
	}

	@Test
	public void getType() {
		if (action < 6) {
			assertEquals(String.valueOf(TerrainType.values()[action]), cardListener.getType());
		}
	}
}