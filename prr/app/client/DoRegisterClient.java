package prr.app.client;

import prr.core.Network;
import prr.app.exception.DuplicateClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

	DoRegisterClient(Network receiver) {
		super(Label.REGISTER_CLIENT, receiver);
		addStringField("clientKey", Message.key());
		addStringField("name", Message.name());
		addIntegerField("taxId", Message.taxId());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String clientKey = stringField("clientKey");
		String name = stringField("name");
		int taxId = integerField("taxId");

		try {
			_receiver.registerClient(clientKey, name, taxId);
		} catch (prr.core.exception.DuplicateClientKeyException e) {
			throw new DuplicateClientKeyException(clientKey);
		}
	}
}
