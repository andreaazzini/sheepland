package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ActionButtonListenerTest {
	private static ClientCommunication client;
	private static ActionButtonPanel actionPanel;
	
	@Parameters
	public static Collection<Object[]> data() {
		client = new ClientHandler();
		actionPanel = new ActionButtonPanel();
		return Arrays.asList(new Object[][] 
				{{0, new ActionButtonListener(client, actionPanel, 0)},
				{1, new ActionButtonListener(client, actionPanel, 1)},
				{2, new ActionButtonListener(client, actionPanel, 2)},
				{3, new ActionButtonListener(client, actionPanel, 3)},
				{4, new ActionButtonListener(client, actionPanel, 4)},
				{5, new ActionButtonListener(client, actionPanel, 5)},
				{6, new ActionButtonListener(client, actionPanel, 6)},
				});
	}
	
	public ActionButtonListenerTest(int action, ActionButtonListener abl) {
		abl.actionPerformed(new ActionEvent(abl, action, null));
	}

	@Test
	public void initialization() {
		for (ActionButton button : actionPanel.getActionButtons()) {
			assertTrue(!button.isEnabled());
		}
	}

}
