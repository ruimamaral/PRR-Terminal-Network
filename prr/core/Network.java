package prr.core;

import java.io.Serializable;

import javax.management.BadAttributeValueExpException;

import java.io.IOException;
import prr.core.exception.UnrecognizedEntryException;
import prr.core.terminal.BasicTerminal;
import prr.core.terminal.Terminal;
import prr.core.exception.UnknownClientKeyException;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202210161305L;

	// FIXME define attributes
	// FIXME define contructor(s)
	// FIXME define methods

	/**
	* Read text input file and create corresponding domain entities.
	* 
	* @param filename name of the text input file
	* @throws UnrecognizedEntryException if some entry is not correct
	* @throws IOException if there is an IO erro while processing the text file
	*/
	void importFile(String filename) throws UnrecognizedEntryException, IOException /* FIXME maybe other exceptions */  {
	//FIXME implement method
	}

	void registerTerminal(String type, String id,
			String client, String state) 
			throws IllegalArgumentException, UnknownClientKeyException {

		Terminal newTerm;

		switch(type) {
			case "BASIC" -> newTerm = new BasicTerminal(id, getClient(client)); //getClient throws exception
			case "FANCY" -> newTerm = new FancyTerminal(id, getClient(client)); //maybe check id validity
			default -> throw new IllegalArgumentException();
		}
		switch(state) {
			case "IDLE" -> newTerm.setIdle();
			case "OFF" -> newTerm.turnOff();
			case "SILENCE" -> newTerm.setSilence();
			default -> throw new IllegalArgumentException();
		}
	}
}


