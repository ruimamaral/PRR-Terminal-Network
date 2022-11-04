package prr.core.notification;

import java.io.Serial;

public class OffToIdle extends Notification {

	@Serial
	private static final long serialVersionUID = 202211011925L;

	@Override
	public boolean equals(Object other) {
		if (other instanceof OffToIdle) {
			return (this.getTerminalKey())
					.equals(((Notification) other).getTerminalKey());
		}
		return false;
	}

	public OffToIdle(String key) {
		super(key);
	}

	@Override
	public String getType() {
		return "O2I";
	}
	
}
