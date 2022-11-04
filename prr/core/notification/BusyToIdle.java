package prr.core.notification;

import java.io.Serial;

public class BusyToIdle extends Notification {

	@Serial
	private static final long serialVersionUID = 202211011925L;

	@Override
	public boolean equals(Object other) {
		if (other instanceof BusyToIdle) {
			return (this.getTerminalKey())
					.equals(((Notification) other).getTerminalKey());
		}
		return false;
	}

	public BusyToIdle(String key) {
		super(key);
	}

	@Override
	public String getTypeName() {
		return "B2I";
	}
	
}
