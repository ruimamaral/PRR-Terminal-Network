package prr.app.client;

import prr.core.Network;
import prr.util.StringMaker;
import prr.util.Visitor;
import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

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
		String key = stringField("clientKey");
		Visitor<String> stringMaker = new StringMaker();

		try {
			super._display.popup(super._receiver.getClient(key)
					.accept(stringMaker));
		} catch (prr.core.exception.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(key);
		}
	}

}
