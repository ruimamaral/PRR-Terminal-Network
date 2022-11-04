package prr.core.notification;

import java.io.Serial;

public class SilentToIdle extends Notification {

	@Serial
	private static final long serialVersionUID = 202211011925L;

	@Override
	public boolean equals(Object other) {
		if (other instanceof SilentToIdle) {
			return (this.getTerminalKey())
					.equals(((Notification) other).getTerminalKey());
		}
		return false;
	}

	public SilentToIdle(String key) {
		super(key);
	}

	@Override
	public String getType() {
		return "S2I";
	}
	
}
