package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MarketPanelTest {
	private ClientCommunication client;
	private Color color;
	private MarketPanel mp;
	
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] 
				{{0}, {1}, {2}, {3}, {4}, {5}});
	}
	
	public MarketPanelTest(int type) {
		this.client = new ClientHandler();
		color = new Color(33, 162, 246);
		mp = new MarketPanel(client);
	}

	@Test
	public void initialization() {
		assertEquals(color, mp.getBackground());
		assertEquals(Default.getMarketPanelDimension(), mp.getSize());
		assertEquals(Default.getMarketPanelDimension(), mp.getPreferredSize());
		assertEquals(Default.getMarketPanelDimension(), mp.getMinimumSize());
		assertEquals(Default.getMarketPanelDimension(), mp.getMaximumSize());
	}


}
