package prr.app.lookup;

import prr.core.Network;
import prr.core.client.Client;
import prr.core.exception.UnknownClientKeyException;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

	DoShowCommunicationsFromClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
		addStringField("clientKey", Message.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		String clientKey = stringField("clientKey");
		final StringMaker stringMaker = new StringMaker();

		try {
			Client client = _receiver.getClient(clientKey);
			_receiver.visitAll(stringMaker,
					_receiver.getAllCommunications(),
					c -> c.getClient().equals(client));
		} catch (UnknownClientKeyException e) {
			throw new prr.app.exception.UnknownClientKeyException(clientKey);
		}

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
