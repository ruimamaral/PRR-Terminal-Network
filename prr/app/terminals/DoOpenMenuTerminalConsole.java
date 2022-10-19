package prr.app.terminals;

import prr.core.Network;
import prr.core.terminal.Terminal;
import prr.app.exception.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Open a specific terminal's menu.
 */
class DoOpenMenuTerminalConsole extends Command<Network> {

	DoOpenMenuTerminalConsole(Network receiver) {
		super(Label.OPEN_MENU_TERMINAL, receiver);
		addStringField("terminalKey", Message.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
		String key = stringField("terminalKey");
		try {
			Terminal terminal = _receiver.getTerminal(key);
			(new prr.app.terminal.Menu(_receiver, terminal)).open();
		} catch (prr.core.exception.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(key);
		}
	}
}
