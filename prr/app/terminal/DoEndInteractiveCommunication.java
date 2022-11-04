package prr.app.terminal;

import prr.core.Network;
import prr.core.terminal.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for ending communication.
 */
class DoEndInteractiveCommunication extends TerminalCommand {

	DoEndInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.END_INTERACTIVE_COMMUNICATION, context,
				terminal, receiver -> receiver.canEndCurrentCommunication());
		addIntegerField("duration", Message.duration());
	}
	
	@Override
	protected final void execute() throws CommandException {
		int duration = integerField("duration");
		double cost;

		try {
			cost = _receiver.endCurrentCommunication(duration);
			_display.popup(Message.communicationCost(Math.round(cost)));
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // Unexpected behaviour
		}
	}
}
