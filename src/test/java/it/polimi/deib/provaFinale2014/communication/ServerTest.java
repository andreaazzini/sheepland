package it.polimi.deib.provaFinale2014.communication;

import it.polimi.deib.provaFinale2014.communication.SheeplandServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {
	private SheeplandServer server;
	private List<Socket> sockets;
	
	@Before
	public void setUp() {
		sockets = new ArrayList<Socket>();
		// Inizializza e fa partire il server su un thread separato
		Thread serverThread = new Thread() {
			public void run() {
				server = new SheeplandServer();
				new Thread (server).start();
			}
		};
		serverThread.start();
	}
	
	@Test
	public synchronized void clientConnections() {
		try {
			// Tenta di stabilire 4 connessioni
			for (int i = 0; i < 4; i++) {
				sockets.add(new Socket("localhost", 3000));
			}
		} catch (Exception e) {
			// Non si vuole entrare in questo ramo
			// in fase di test
		}
	}
}
