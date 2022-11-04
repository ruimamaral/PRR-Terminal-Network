package prr.core.notification;

import java.io.Serial;
import java.io.Serializable;

import prr.core.client.Client;

public class DefaultNotificationDeliveryMethod
		implements NotificationDeliveryMethod, Serializable{

	@Serial
	private static final long serialVersionUID = 202211042106L;

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
