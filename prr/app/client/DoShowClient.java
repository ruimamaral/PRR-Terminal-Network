package prr.app.client;

import prr.core.Network;
import prr.core.client.Client;
import prr.util.StringMaker;

import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
		addStringField("clientKey", Message.key());
	}

	@Override
	protected final void execute() throws CommandException {
		final StringMaker stringMaker = new StringMaker();
		String key = stringField("clientKey");
		Client client;

		try {
			client = _receiver.getClient(key);
		} catch (prr.core.exception.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(key);
		}
		client.accept(stringMaker);
		_receiver.visitAll(stringMaker,
				client.getNotifications(),
				n -> true);

		client.resetNotifications();
		_display.popup(stringMaker);
	}
}
