package prr.app.terminal;

import prr.core.Network;
import prr.core.exception.ActionNotSupportedAtDestination;
import prr.core.exception.ActionNotSupportedAtOrigin;
import prr.core.exception.TargetBusyException;
import prr.core.exception.TargetOffException;
import prr.core.exception.TargetSilentException;
import prr.core.terminal.Terminal;
import prr.app.exception.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context,
				terminal, receiver -> receiver.canStartCommunication());
		addStringField("terminalKey", Message.terminalKey());
		addOptionField("commType", Message.commType(), "VIDEO", "VOICE");
	}
	
	@Override
	protected final void execute() throws CommandException {
		String terminalKey = stringField("terminalKey");
		String type = optionField("commType");

		try {
			_network.registerInteractiveCommunication(
					type, _receiver, terminalKey);
		} catch (prr.core.exception.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(terminalKey);
		} catch (TargetBusyException e) {
			_display.popup(Message.destinationIsBusy(terminalKey));
		} catch (TargetSilentException e) {
			_display.popup(Message.destinationIsSilent(terminalKey));
		} catch (TargetOffException e) {
			_display.popup(Message.destinationIsOff(terminalKey));
		} catch (ActionNotSupportedAtDestination e) {
			_display.popup(Message.unsupportedAtDestination(terminalKey, type));
		} catch (ActionNotSupportedAtOrigin e) {
			_display.popup(Message.unsupportedAtOrigin(_receiver.getKey(), type));
		}
	}
}
