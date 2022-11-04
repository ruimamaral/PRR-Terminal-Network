package prr.app.client;

import prr.core.Network;
import prr.core.client.Client;
import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Enable client notifications.
 */
class DoEnableClientNotifications extends Command<Network> {

	DoEnableClientNotifications(Network receiver) {
		super(Label.ENABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("clientKey", Message.key());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String key = stringField("clientKey");

		try {
			Client client = _receiver.getClient(key);
			client.turnOnNotifications();
		} catch (prr.core.exception.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(key);
		} catch (IllegalAccessException e) {
			_display.popup(Message.clientNotificationsAlreadyEnabled());
		}
	}
}
