package prr.app.terminal;

import prr.core.Network;
import prr.core.terminal.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Perform payment.
 */
class DoPerformPayment extends TerminalCommand {

	DoPerformPayment(Network context, Terminal terminal) {
		super(Label.PERFORM_PAYMENT, context, terminal);
		addIntegerField("commKey", Message.commKey());
	}
	
	@Override
	protected final void execute() throws CommandException {
		int commKey = integerField("commKey");

		try {
			_receiver.payCommunication(commKey);
		} catch (IllegalAccessException e) {
			_display.popup(Message.invalidCommunication());
		}
	}
}
