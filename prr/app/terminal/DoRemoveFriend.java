package prr.app.terminal;

import prr.app.exception.UnknownTerminalKeyException;
import prr.core.Network;
import prr.core.terminal.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
		addStringField("terminalKey", Message.terminalKey());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String friendKey = stringField("terminalKey");

		try {
			_network.getTerminal(friendKey);
			_receiver.removeFriend(friendKey);
		} catch (prr.core.exception.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(friendKey);
		}
	}
}
