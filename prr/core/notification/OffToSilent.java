package prr.core.notification;

import java.io.Serial;

public class OffToSilent extends Notification {

	@Serial
	private static final long serialVersionUID = 202211011925L;

	public OffToSilent(String key) {
		super(key);
	}

	@Override
	public String getType() {
		return "O2S";
	}

	
}
