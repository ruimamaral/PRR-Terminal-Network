package prr.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import prr.core.terminal.BasicTerminal;
import prr.core.terminal.FancyTerminal;
import prr.core.terminal.Terminal;
import prr.util.Visitable;
import prr.util.Visitor;
import prr.core.exception.DuplicateTerminalKeyException;
import prr.core.exception.DuplicateClientKeyException;
import prr.core.client.Client;
import prr.core.exception.UnknownClientKeyException;
import prr.core.exception.UnknownTerminalKeyException;


/**
 * Class Network implements a network of terminals.
 * Class used to store and manage a network of
 * communication terminals and clients.
 */
public class Network implements Serializable {

	/* Serial number for serialization. */
	@Serial
	private static final long serialVersionUID = 202210161305L;

	/* Terminal map containing all the terminals in the network */
	private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();

	/* Client map containing all the clients in the network */
	private Map<String, Client> _clients = new TreeMap<String, Client>();

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
	public Terminal registerTerminal(String type, String key, String client)
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
		this.addTerminal(newTerm);
		owner.addTerminal(newTerm);
		return newTerm;
	}

	/**
	 * Fetches client that corresponds to given client key.
	 * 
	 * @param key Client key
	 * @return Client
	 * @throws UnknownClientKeyException
	 */
	public Client getClient(String key) throws UnknownClientKeyException {
		Client client = this._clients.get(key.toUpperCase());

		if (client == null) {
			throw new UnknownClientKeyException(key);
		}
		return client;
	}

	/**
	 * Fetches terminal that corresponds to given terminal key.
	 * 
	 * @param key terminal's key
	 * @return Terminal
	 * @throws UnknownTerminalKeyException
	 */
	public Terminal getTerminal(String key) throws UnknownTerminalKeyException {
		Terminal terminal = this._terminals.get(key);

		if (terminal == null) {
			throw new UnknownTerminalKeyException(key);
		}
		return terminal;
	}

	/**
	 * Returns a view on a collection of all clients on the network.
	 * 
	 * @return Collection<Client>
	 */
	public Collection<Client> getAllClients() {
		return Collections.unmodifiableCollection(this._clients.values());
	}

	/**
	 * Returns a view on a collection of all terminals on the network.
	 * 
	 * @return Collection<Terminal>
	 */
	public Collection<Terminal> getAllTerminals() {
		return Collections.unmodifiableCollection(this._terminals.values());
	}

	/**
	 * Returns the amount of clients on the network
	 * 
	 * @return int
	 */
	public int getClientCount() {
		return this._clients.size();
	}

	/**
	 * Returns the amount of terminals on the network
	 * 
	 * @return int
	 */
	public int getTerminalCount() {
		return this._terminals.size();
	}

	/**
	 * Adds given terminal to the terminal map.
	 * 
	 * @param terminal the Terminal
	 * @throws DuplicateTerminalKeyException
	 */
	private void addTerminal(Terminal terminal)
			throws DuplicateTerminalKeyException {
		String key = terminal.getKey();

		if (this._terminals.containsKey(key)) {
			throw new DuplicateTerminalKeyException();
		} else {
			this._terminals.put(key, terminal);
		}
	}

	/**
	 * Adds given client to the client map.
	 * 
	 * @param client the Client
	 * @throws DuplicateTerminalKeyException
	 */
	private void addClient(Client client) throws DuplicateClientKeyException {
		String key = client.getKey().toUpperCase();

		if (this._clients.containsKey(key)) {
			throw new DuplicateClientKeyException();
		} else {
			this._clients.put(key, client);
		}
	}

	/**
	 * Registers a new client on the network.
	 * 
	 * @param key client's key
	 * @param name client's name
	 * @param taxId client's tax ID
	 * @return Client
	 * @throws DuplicateClientKeyException
	 */
	public Client registerClient(String key, String name, int taxId) 
			throws DuplicateClientKeyException {
		Client newClient = new Client(key, name, taxId);

		this.addClient(newClient);
		return newClient;
	}

	/**
	 * Adds a terminal to another terminal's friend list.
	 * 
	 * @param terminalKey terminal's key
	 * @param friendKey friend terminal's key
	 * @throws UnknownTerminalKeyException
	 * @throws IllegalArgumentException
	 */
	public void addFriend(String terminalKey, String friendKey) 
			throws UnknownTerminalKeyException, IllegalArgumentException {
		if (terminalKey.equals(friendKey)) {
			throw new IllegalArgumentException();
		}
		Terminal terminal = getTerminal(terminalKey);
		Terminal friend = getTerminal(friendKey);

		terminal.addFriend(friend);
	}

	/**
	 * Visits elements that respect the given predicate from the
	 * given collection with a given visitor.
	 * 
	 * @param <T> visitor type
	 * @param <E> element type
	 * @param visitor the visitor
	 * @param col collection of <E> elements
	 * @param valid predicate for choosing elements
	 */
	public <T, E extends Visitable> void visitAll(
			Visitor<T> visitor, Collection<E> col, Predicate<E> valid) {

		for (E element : col) {
			if (valid.test(element)) {
				element.accept(visitor);
			}
		}
	}

}


