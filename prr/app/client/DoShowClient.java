package prr.app.client;

import prr.core.Network;
import prr.util.StringMaker;
import prr.util.Visitor;
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

	private final Visitor<Void> _stringMaker = new StringMaker();
	
	@Override
	protected final void execute() throws CommandException {
		String key = stringField("clientKey");

		try {
			_receiver.getClient(key).accept(this._stringMaker);
			_display.popup(this._stringMaker);
		} catch (prr.core.exception.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(key);
		}
	}

}
