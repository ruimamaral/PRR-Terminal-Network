package prr.app.terminals;

import prr.core.Network;
import prr.app.exception.DuplicateTerminalKeyException;
import prr.app.exception.InvalidTerminalKeyException;
import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
		addStringField("terminalKey", Message.terminalKey());
		addOptionField("terminalType",
				Message.terminalType(), "BASIC", "FANCY");
		addStringField("ownerKey", Message.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		String terminalKey = stringField("terminalKey");
		String terminalType = optionField("terminalType");
		String clientKey = stringField("clientKey");

		try {
			_receiver.registerTerminal(terminalType, terminalKey, clientKey);
		} catch (IllegalArgumentException e) {
			throw new InvalidTerminalKeyException(terminalKey);
		} catch (prr.core.exception.DuplicateTerminalKeyException e) {
			throw new DuplicateTerminalKeyException(terminalKey);
		} catch (prr.core.exception.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(clientKey);
		}
	}
}
