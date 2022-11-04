package prr.app.client;

import prr.core.Network;
import prr.util.StringMaker;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show all clients.
 */
class DoShowAllClients extends Command<Network> {

	DoShowAllClients(Network receiver) {
		super(Label.SHOW_ALL_CLIENTS, receiver);
	}
	
	@Override
	protected final void execute() throws CommandException {
		final StringMaker stringMaker = new StringMaker();

		_receiver.visitAll(stringMaker,
				_receiver.getAllClients(),
				t -> true);

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
