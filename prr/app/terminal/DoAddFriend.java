package prr.app.terminal;

import prr.app.exception.UnknownTerminalKeyException;
import prr.core.Network;
import prr.core.terminal.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
		addStringField("terminalKey", Message.terminalKey());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String friendKey = stringField("terminalKey");

		try {
			_receiver.addFriend(_network.getTerminal(friendKey));
		} catch (prr.core.exception.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(friendKey);
		}
	}
}
