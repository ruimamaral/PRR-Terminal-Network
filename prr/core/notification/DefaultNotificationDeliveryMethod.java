package prr.core.notification;

import prr.core.client.Client;

public class DefaultNotificationDeliveryMethod
		implements NotificationDeliveryMethod {

	public void pushNotification(Client client, Notification notification) {
		client.addNotification(notification);
	}
	
}
