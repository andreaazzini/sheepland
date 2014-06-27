package it.polimi.deib.provaFinale2014.gui;

import static org.junit.Assert.*;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import it.polimi.deib.provaFinale2014.client.ClientCommunication;
import it.polimi.deib.provaFinale2014.client.ClientHandler;
import it.polimi.deib.provaFinale2014.gui.AnimalGUI.AnimalType;

import org.junit.Test;

public class SheepListenerTest {
	ClientCommunication client = new ClientHandler();
	SheeplandPanel panel = new SheeplandPanel(client);
	AnimalGUI sheep = new AnimalGUI(AnimalType.SHEEP, 0);
	AnimalGUI wolf = new AnimalGUI(AnimalType.WOLF, 0);
	AnimalGUI blacksheep = new AnimalGUI(AnimalType.BLACKSHEEP, 0);
	SheepListener sl = new SheepListener(client, panel, sheep);

	@Test
	public void mouse() {
		StaticGUI.setKill(false);
		sl.mouseEntered(new MouseEvent(sheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(Cursor.HAND_CURSOR, sheep.getCursor().getType());
		sl.mouseExited(new MouseEvent(sheep, 0, 0, 0, 0, 0, 0, false));
		assertNotEquals(Cursor.HAND_CURSOR, sheep.getCursor().getType());
		StaticGUI.setKill(true);
		sl.mouseEntered(new MouseEvent(sheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(Cursor.HAND_CURSOR, sheep.getCursor().getType());
		sl.mouseExited(new MouseEvent(sheep, 0, 0, 0, 0, 0, 0, false));
		assertNotEquals(Cursor.HAND_CURSOR, sheep.getCursor().getType());
		
		StaticGUI.setKill(false);
		sl.mouseEntered(new MouseEvent(wolf, 0, 0, 0, 0, 0, 0, false));
		assertEquals(Cursor.DEFAULT_CURSOR, wolf.getCursor().getType());
		sl.mouseExited(new MouseEvent(wolf, 0, 0, 0, 0, 0, 0, false));
		assertNotEquals(Cursor.HAND_CURSOR, wolf.getCursor().getType());
		StaticGUI.setKill(true);
		sl.mouseEntered(new MouseEvent(wolf, 0, 0, 0, 0, 0, 0, false));
		assertEquals(Cursor.DEFAULT_CURSOR, wolf.getCursor().getType());
		sl.mouseExited(new MouseEvent(wolf, 0, 0, 0, 0, 0, 0, false));
		assertNotEquals(Cursor.HAND_CURSOR, wolf.getCursor().getType());
		
		StaticGUI.setKill(false);
		sl.mouseEntered(new MouseEvent(blacksheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(Cursor.DEFAULT_CURSOR, blacksheep.getCursor().getType());
		sl.mouseExited(new MouseEvent(blacksheep, 0, 0, 0, 0, 0, 0, false));
		assertNotEquals(Cursor.HAND_CURSOR, blacksheep.getCursor().getType());
		StaticGUI.setKill(true);
		sl.mouseEntered(new MouseEvent(blacksheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(Cursor.DEFAULT_CURSOR, blacksheep.getCursor().getType());
		sl.mouseExited(new MouseEvent(blacksheep, 0, 0, 0, 0, 0, 0, false));
		assertNotEquals(Cursor.HAND_CURSOR, blacksheep.getCursor().getType());
	}

	@Test
	public void actionPerformed() {
		StaticGUI.setKill(false);
		sl.mouseClicked(new MouseEvent(sheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(sheep.getType().name(), panel.getSheepType());
		StaticGUI.setKill(true);
		sl.mouseClicked(new MouseEvent(sheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(sheep.getType().name(), panel.getSheepType());
		
		StaticGUI.setKill(false);
		sl.mouseClicked(new MouseEvent(wolf, 0, 0, 0, 0, 0, 0, false));
		assertEquals(wolf.getType().name(), panel.getSheepType());
		StaticGUI.setKill(true);
		sl.mouseClicked(new MouseEvent(wolf, 0, 0, 0, 0, 0, 0, false));
		assertEquals(wolf.getType().name(), panel.getSheepType());
		
		StaticGUI.setKill(false);
		sl.mouseClicked(new MouseEvent(blacksheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(blacksheep.getType().name(), panel.getSheepType());
		StaticGUI.setKill(true);
		sl.mouseClicked(new MouseEvent(blacksheep, 0, 0, 0, 0, 0, 0, false));
		assertEquals(blacksheep.getType().name(), panel.getSheepType());
	}
}
