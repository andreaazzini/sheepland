package it.polimi.deib.provaFinale2014.RMIcommunication;

import it.polimi.deib.provaFinale2014.exceptions.NicknameAlreadyInUseException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia del server RMI
 */
public interface RMIServerInterface extends Remote {
	
	/**
	 * Connette un client al Server
	 * @param client
	 * @throws RemoteException
	 * @throws NicknameAlreadyInUseException
	 */
	void connect(RMIClientInterface client) throws RemoteException, NicknameAlreadyInUseException;

}
