package prr.core.notification;

public class DefaultMethodHolder {

	private static NotificationDeliveryMethod _method;

	// lazy initialization
	public static NotificationDeliveryMethod getDefaultMethod() {
		if (_method == null) {
			_method = new DefaultNotificationDeliveryMethod();
		}
		return _method;
	}
	
}
