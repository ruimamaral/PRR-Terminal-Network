package prr.app.terminal;

import prr.core.Network;
import prr.core.communication.Communication;
import prr.core.terminal.Terminal;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for showing the ongoing communication.
 */
class DoShowOngoingCommunication extends TerminalCommand {

	DoShowOngoingCommunication(Network context, Terminal terminal) {
		super(Label.SHOW_ONGOING_COMMUNICATION, context, terminal);
	}
	
	@Override
	protected final void execute() throws CommandException {
		Communication comm;
		try {
			comm = _receiver.getOngoing();
			StringMaker stringMaker = new StringMaker();
			comm.accept(stringMaker);
			_display.popup(stringMaker);
		} catch (IllegalAccessException e) {
			_display.popup(Message.noOngoingCommunication());
		}
	}
}
