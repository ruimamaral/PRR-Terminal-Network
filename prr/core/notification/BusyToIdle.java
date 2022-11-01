package prr.core.notification;

import java.io.Serial;

public class BusyToIdle extends Notification {

	@Serial
	private static final long serialVersionUID = 202211011925L;

	public BusyToIdle(String key) {
		super(key);
	}

	@Override
	public String getType() {
		return "B2I";
	}
	
}
