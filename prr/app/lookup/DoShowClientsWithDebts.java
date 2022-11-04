package prr.app.lookup;

import java.util.Comparator;

import prr.core.Network;
import prr.core.client.Client;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with negative balance.
 */
class DoShowClientsWithDebts extends Command<Network> {

	DoShowClientsWithDebts(Network receiver) {
		super(Label.SHOW_CLIENTS_WITH_DEBTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		final StringMaker stringMaker = new StringMaker();

		_receiver.visitAll(stringMaker,
				_receiver.getAllClients(),
				c -> c.getDebt() > 0,
				Comparator.comparing(Client::getDebt).reversed()
						.thenComparing(Client::getKey));

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
