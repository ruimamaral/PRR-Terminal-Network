package prr.core.notification;

import java.io.Serial;

public class OffToIdle extends Notification {

	@Serial
	private static final long serialVersionUID = 202211011925L;

	public OffToIdle(String key) {
		super(key);
	}

	@Override
	public String getType() {
		return "O2I";
	}
	
}
