package prr.app.client;

import prr.core.Network;
import prr.util.StringMaker;
import prr.util.Visitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show all clients.
 */
class DoShowAllClients extends Command<Network> {

	DoShowAllClients(Network receiver) {
		super(Label.SHOW_ALL_CLIENTS, receiver);
	}
	
	@Override
	protected final void execute() throws CommandException {

		final Visitor<Void> stringMaker = new StringMaker();

		if (_receiver.getClientCount() != 0) {
			_receiver.visitAll(stringMaker,
					_receiver.getAllClients(), client -> true);
			_display.popup(stringMaker);
		}
	}
}
