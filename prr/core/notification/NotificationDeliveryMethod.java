package prr.core.notification;

import prr.core.client.Client;

public interface NotificationDeliveryMethod {
	public abstract void pushNotification(Client client, Notification notification);
}
