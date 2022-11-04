package prr.app.client;

import prr.core.Network;
import prr.core.client.Client;
import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show the payments and debts of a client.
 */
class DoShowClientPaymentsAndDebts extends Command<Network> {

	DoShowClientPaymentsAndDebts(Network receiver) {
		super(Label.SHOW_CLIENT_BALANCE, receiver);
		addStringField("clientKey", Message.key());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String key = stringField("clientKey");

		try {
			Client client = _receiver.getClient(key);
			_display.popup(Message.clientPaymentsAndDebts(key,
					Math.round(client.getTotalPaid()),
					Math.round(client.getDebt())));

		} catch (prr.core.exception.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(key);
		}
	}
}
