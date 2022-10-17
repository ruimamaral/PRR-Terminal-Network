package prr.core;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import prr.core.exception.UnrecognizedEntryException;
import prr.core.terminal.BasicTerminal;
import prr.core.terminal.FancyTerminal;
import prr.core.terminal.Terminal;
import prr.core.exception.DuplicateTerminalKeyException;
import prr.core.exception.DuplicateClientKeyException;
import prr.core.client.Client;
import prr.core.exception.UnknownClientKeyException;
import prr.core.exception.UnknownTerminalKeyException;

import java.io.IOException;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202210161305L;

	private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();

	private Map<String, Client> _clients = new TreeMap<String, Client>();

	// FIXME define attributes
	// FIXME define contructor(s)
	// FIXME define methods

	/**
	 * Adds a new terminal to the app.
	 * 
	 * @param type Terminal type
	 * @param key Terminal key
	 * @param client Terminal's client's key
	 * @param state Terminals state
	 * @throws IllegalArgumentException
	 * @throws UnknownClientKeyException
	 */
	void registerTerminal(String type, String key, String client, String state)
			throws IllegalArgumentException, UnknownClientKeyException,
			DuplicateTerminalKeyException {
		Terminal newTerm;
		Terminal.checkKey(key);
		Client owner = this.getClient(client);

		switch(type) {
			case "BASIC" -> newTerm = new BasicTerminal(key, owner);
			case "FANCY" -> newTerm = new FancyTerminal(key, owner);
			default -> throw new IllegalArgumentException();
		}
		this.setTerminalState(newTerm, state);
		this.addTerminal(newTerm);
		owner.addTerminal(newTerm, key);
	}

	void setTerminalState(Terminal terminal, String state)
			throws IllegalArgumentException {

		switch(state) {
			case "IDLE" -> terminal.setIdle();
			case "OFF" -> terminal.turnOff();
			case "SILENCE" -> terminal.setSilence();
			default -> throw new IllegalArgumentException();
		}
	}

	Client getClient(String key) throws UnknownClientKeyException {
		Client client = this._clients.get(key);

		if (client == null) {
			throw new UnknownClientKeyException(key);
		}
		return client;
	}

	Terminal getTerminal(String key) throws UnknownTerminalKeyException {
		Terminal terminal = this._terminals.get(key);

		if (terminal == null) {
			throw new UnknownTerminalKeyException(key);
		}
		return terminal;
	}

	void addTerminal(Terminal terminal)  throws DuplicateTerminalKeyException {
		String key = terminal.getKey();

		if (this._terminals.keySet().contains(key)) {
			throw new DuplicateTerminalKeyException();
		} else {
			this._terminals.put(key, terminal);
		}
	}

	void addClient(Client client) throws DuplicateClientKeyException {
		String key = client.getKey();

		if (this._clients.keySet().contains(key)) {
			throw new DuplicateClientKeyException();
		} else {
			this._clients.put(key, client);
		}
	}

	void registerClient(String key, String name, int taxId) 
			throws DuplicateClientKeyException {
		Client newClient = new Client(key, name, taxId);

		this.addClient(newClient);
	}

	void addFriend(String terminalKey, String friendKey) 
			throws UnknownTerminalKeyException {
		Terminal terminal = getTerminal(terminalKey);
		Terminal friend = getTerminal(friendKey);

		terminal.addFriend(friend);
		// friend.addFriend(terminal); apparently not true
	}

}


