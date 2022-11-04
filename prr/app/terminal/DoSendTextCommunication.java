package prr.app.terminal;

import prr.core.Network;
import prr.core.exception.TargetOffException;
import prr.core.terminal.Terminal;
import prr.app.exception.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

	DoSendTextCommunication(Network context, Terminal terminal) {
		super(Label.SEND_TEXT_COMMUNICATION, context,
				terminal, receiver -> receiver.canStartCommunication());
		addStringField("terminalKey", Message.terminalKey());
		addStringField("message", Message.textMessage());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String destinationKey = stringField("terminalKey");
		String message = stringField("message");

		try {
			_network.registerTextCommunication(
					_receiver, destinationKey, message);
		} catch (TargetOffException e) {
			_display.popup(Message.destinationIsOff(destinationKey));
		} catch (prr.core.exception.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(destinationKey);
		}

	}
} 
