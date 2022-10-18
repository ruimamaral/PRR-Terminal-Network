package prr.core;

import java.io.IOException;
import java.io.FileNotFoundException;

import prr.core.exception.DuplicateClientKeyException;
import prr.core.exception.DuplicateTerminalKeyException;
import prr.core.exception.ImportFileException;
import prr.core.exception.MissingFileAssociationException;
import prr.core.exception.UnavailableFileException;
import prr.core.exception.UnrecognizedEntryException;
import prr.util.Visitor;
import prr.core.exception.UnknownClientKeyException;
import prr.core.exception.UnknownTerminalKeyException;

//FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Manage access to network and implement load/save operations.
 */
public class NetworkManager {

	/** The network itself. */
	private Network _network = new Network();
	//FIXME  addmore fields if needed

	public Network getNetwork() {
		return _network;
	}

	/**
	 * @param filename name of the file containing the serialized application's state
	 *        to load.
	 * @throws UnavailableFileException if the specified file does not exist or there is
	 *         an error while processing this file.
	 */
	public void load(String filename) throws UnavailableFileException {
		//FIXME implement serialization method
	}

	/**
	 * Saves the serialized application's state into the file associated to the current network.
	 *
	 * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
	 * @throws MissingFileAssociationException if the current network does not have a file.
	 * @throws IOException if there is some error while serializing the state of the network to disk.
	 */
	public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
		//FIXME implement serialization method
	}

	/**
	 * Saves the serialized application's state into the specified file. The current network is
	 * associated to this file.
	 *
	 * @param filename the name of the file.
	 * @throws FileNotFoundException if for some reason the file cannot be created or opened.
	 * @throws MissingFileAssociationException if the current network does not have a file.
	 * @throws IOException if there is some error while serializing the state of the network to disk.
	 */
	public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
		//FIXME implement serialization method
	}

	/**
	 * Read text input file and create domain entities..
	 * 
	 * @param filename name of the text input file
	 * @throws ImportFileException
	 */
	public void importFile(String filename) throws ImportFileException {
		Parser parser = new Parser(this);
	try {
		parser.parseFile(filename);
		} catch (IOException | UnrecognizedEntryException /* FIXME maybe other exceptions */ e) {
			throw new ImportFileException(filename, e);
		}
	}  

	public void registerTerminal(String type, String key,
			String client, String state) 
			throws IllegalArgumentException,
			UnknownClientKeyException, DuplicateTerminalKeyException {

		this._network.registerTerminal(type, key, client, state);
	}

	void registerClient(String key, String name, int taxId) 
			throws DuplicateClientKeyException {

		this._network.registerClient(key, name, taxId);
	}

	
	void addFriend(String terminalKey, String friendKey) 
			throws UnknownTerminalKeyException {

		this._network.addFriend(terminalKey, friendKey);
	}

	Visitor<String> getStringMaker() {
		
	}
}