package prr.app.lookup;

import prr.core.Network;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with positive balance.
 */
class DoShowClientsWithoutDebts extends Command<Network> {

	DoShowClientsWithoutDebts(Network receiver) {
		super(Label.SHOW_CLIENTS_WITHOUT_DEBTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		final StringMaker stringMaker = new StringMaker();

		_receiver.visitAll(stringMaker,
				_receiver.getAllClients(),
				c -> c.getDebt() == 0);

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
