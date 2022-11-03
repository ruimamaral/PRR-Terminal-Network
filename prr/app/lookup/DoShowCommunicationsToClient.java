package prr.app.lookup;

import java.util.Comparator;

import prr.core.Network;
import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.exception.UnknownClientKeyException;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show communications to a client.
 */
class DoShowCommunicationsToClient extends Command<Network> {

	DoShowCommunicationsToClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_TO_CLIENT, receiver);
		addStringField("clientKey", Message.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		String clientKey = stringField("clientKey");
		final StringMaker stringMaker = new StringMaker();

		try {
			Client client = _receiver.getClient(clientKey);
			// comparator doesnt need to be case insensitive
			_receiver.visitAll(stringMaker,
					_receiver.getAllCommunications(),
					c -> c.getClient().equals(client),
					Comparator.comparing(Communication::getKey));
		} catch (UnknownClientKeyException e) {
			throw new prr.app.exception.UnknownClientKeyException(clientKey);
		}

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
