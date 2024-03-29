package prr.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import prr.core.terminal.BasicTerminal;
import prr.core.terminal.FancyTerminal;
import prr.core.terminal.Terminal;
import prr.util.Visitable;
import prr.util.Visitor;
import prr.core.exception.DuplicateTerminalKeyException;
import prr.core.exception.TargetBusyException;
import prr.core.exception.TargetOffException;
import prr.core.exception.TargetSilentException;
import prr.core.exception.ActionNotSupportedAtDestination;
import prr.core.exception.ActionNotSupportedAtOrigin;
import prr.core.exception.DuplicateClientKeyException;
import prr.core.client.Client;
import prr.core.communication.Communication;
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

	/* Communication map containing all the communications in the network */
	private Map<Integer, Communication> _communications =
			new TreeMap<Integer, Communication>();

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
	 * Registers a new client on the network.
	 * 
	 * @param key client's key
	 * @param name client's name
	 * @param taxId client's tax ID
	 * @return Client
	 * @throws DuplicateClientKeyException
	 */
	public Client registerClient(String key,
			String name, int taxId) throws DuplicateClientKeyException {

		Client newClient = new Client(key, name, taxId);

		this.addClient(newClient);
		return newClient;
	}

	public void registerInteractiveCommunication(String type, Terminal sender,
			String receiverKey) throws UnknownTerminalKeyException,
			TargetBusyException, TargetSilentException, TargetOffException,
			ActionNotSupportedAtDestination, ActionNotSupportedAtOrigin {

		Terminal receiver = this.getTerminal(receiverKey);
		int key = this._communications.size() + 1;
		if (receiver.equals(sender)) {
			throw new TargetBusyException();
		}
		Communication newComm;
		try {
			switch (type) {
				case "VIDEO" -> newComm = 
						sender.startVideoCommunication(key, sender, receiver);
				case "VOICE" -> newComm =
						sender.startVoiceCommunication(key, sender, receiver);
				default -> throw new IllegalArgumentException();
			}
			this.addCommunication(newComm);
		} catch (IllegalAccessException e) {
			e.printStackTrace();	// Unexpected behaviour
		}
	}

	public void registerTextCommunication(
			Terminal sender, String receiverKey, String message) 
			throws TargetOffException, UnknownTerminalKeyException {
		
		Terminal receiver = this.getTerminal(receiverKey);
		Communication newComm;
		try {
			newComm = sender.sendTextCommunication(
					this._communications.size() + 1, sender, receiver, message);
			this.addCommunication(newComm);
		} catch (IllegalAccessException e) {
			e.printStackTrace();	// Unexpected behaviour
		}
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
	 * Returns a view on a collection of all clients on the network
	 * sorted by their key (case-insensitive).
	 * 
	 * @return Collection<Client>
	 */
	public Collection<Client> getAllClients() {
		return Collections.unmodifiableCollection(this._clients.values());
	}

	/**
	 * Returns a view on a collection of all terminals on the network
	 * sorted by key.
	 * 
	 * @return Collection<Terminal>
	 */
	public Collection<Terminal> getAllTerminals() {
		return Collections.unmodifiableCollection(this._terminals.values());
	}

	/**
	 * Returns a view on a collection of all communications
	 * on the network sorted by key.
	 * 
	 * @return Collection<Communication>
	 */
	public Collection<Communication> getAllCommunications() {
		return Collections
				.unmodifiableCollection(this._communications.values());
	}

	public double getTotalPaid() {
		return this.getAllClients().stream()
				.map(Client::getTotalPaid).reduce(0D, Double::sum);
	}

	public double getDebt() {
		return this.getAllClients().stream()
				.map(Client::getDebt).reduce(0D, Double::sum);
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

	private void addCommunication(Communication comm) {
		this._communications.put(comm.getKey(), comm);
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
	 * @param valid predicate for filtering elements
	 */
	public <T, E extends Visitable> void visitAll(
			Visitor<T> visitor, Collection<E> col, Predicate<E> valid) {

		col.stream().filter(valid).forEach(e -> e.accept(visitor));
	}

	/**
	 * Visits elements that respect the given predicate from the
	 * given collection with a given visitor in a certain order
	 * determined by the comparator.
	 * 
	 * @param <T> visitor type
	 * @param <E> element type
	 * @param visitor the visitor
	 * @param col collection of <E> elements
	 * @param valid predicate for filtering elements
	 * @param order comparator for sorting elements
	 */
	public <T, E extends Visitable> void visitAllSorted(
			Visitor<T> visitor, Collection<E> col,
			Predicate<E> valid, Comparator<E> order) {

		col.stream().filter(valid)
				.sorted(order).forEach(e -> e.accept(visitor));
	}
}


