package it.polimi.deib.provaFinale2014.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe che gestisce le informazioni del client.
 * Utility per la gestione della riconnessione.
 */
public class ClientInfo {
	private String nickname;
	private Socket socket;
	private boolean Online;
	private BufferedReader inputStream;
	private PrintWriter outputStream;
	private int playerIndex;
	
	/**
	 * Costruisce un nuovo oggetto ClientInfo
	 * @param nickname nickname
	 * @param socket socket
	 * @throws IOException
	 */
	public ClientInfo(String nickname, Socket socket) throws IOException {
		this.nickname = nickname;
		this.socket = socket;
		this.Online = true;
		inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		outputStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
	}
	/**
	 * Imposta un socket
	 * @param socket
	 */
	public void setNewSocket (Socket socket) {
		this.socket = socket;
	}
	/**
	 * Imposta se il client è online
	 * @param status
	 */
	public void setOnline(boolean status) {
		this.Online = status;
	}
	/**
	 * Restituisce il socket
	 * @return
	 */
	public Socket getSocket() {
		return this.socket;
	}
	/**
	 * Verifica che il client sia online
	 * @return
	 */
	public boolean isOnline(){
		return this.Online;
	}
	/**
	 * Restituisce il nickname
	 * @return nickname
	 */
	public String getNickname() {
		return this.nickname;
	}
	/**
	 * Restituisce lo stream di input
	 * @return stream di input
	 */
	public BufferedReader getInputStream() {
		return this.inputStream;
	}
	/**
	 * Restituisce lo stream di output
	 * @return stream di output
	 */
	public PrintWriter getOutputStream() {
		return this.outputStream;
	}
	/**
	 * Imposta gli stream di input e output
	 * @throws IOException
	 */
	public void setStreams() throws IOException {
		inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		outputStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
	}
	/**
	 * Imposta il player index
	 * @param i
	 */
	public void setPlayerIndex (int i) {
		this.playerIndex=i;
	}
	/**
	 * Restituisce il player index
	 * @return player index
	 */
	public int getPlayerIndex () {
		return this.playerIndex;
	}
}
