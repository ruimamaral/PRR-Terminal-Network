package prr.app.client;

import prr.core.Network;
import prr.core.client.Client;
import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

	DoDisableClientNotifications(Network receiver) {
		super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("clientKey", Message.key());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String key = stringField("clientKey");

		try {
			Client client = _receiver.getClient(key);
			client.turnOffNotifications();
		} catch (prr.core.exception.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(key);
		} catch (IllegalAccessException e) {
			_display.popup(Message.clientNotificationsAlreadyDisabled());
		}
	}
}
