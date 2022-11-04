package prr.app.terminal;

import prr.core.Network;
import prr.core.terminal.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Turn on the terminal.
 */
class DoTurnOnTerminal extends TerminalCommand {

	DoTurnOnTerminal(Network context, Terminal terminal) {
		super(Label.POWER_ON, context, terminal);
	}
	
	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.setIdle();
		} catch (IllegalAccessException e) {
			_display.popup(Message.alreadyOn());
		}
	}
}
