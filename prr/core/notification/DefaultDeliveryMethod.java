package prr.core.notification;

public class DefaultDeliveryMethod {

	private static NotificationDeliveryMethod method;

	public static NotificationDeliveryMethod getDefaultMethod() {
		if (method == null) {
			method = new DefaultNotificationDeliveryMethod();
		}
		return method;
	}
	
}
