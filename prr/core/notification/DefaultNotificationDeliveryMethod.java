package prr.core.notification;

import prr.core.client.Client;

public class DefaultNotificationDeliveryMethod
		implements NotificationDeliveryMethod {

	private DefaultNotificationDeliveryMethod() {}

	private static NotificationDeliveryMethod _defaultMethod;
	
	public void pushNotification(Client client, Notification notification) {
		client.addNotification(notification);
	}

	public static NotificationDeliveryMethod getDefaultMethod() {
		if (_defaultMethod == null) {
			_defaultMethod = new DefaultNotificationDeliveryMethod();
		}
		return _defaultMethod;
	}
	
}
