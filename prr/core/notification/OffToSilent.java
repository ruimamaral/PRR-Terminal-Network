package prr.core.notification;

import java.io.Serial;

public class OffToSilent extends Notification {

	@Serial
	private static final long serialVersionUID = 202211011925L;

	@Override
	public boolean equals(Object other) {
		if (other instanceof OffToSilent) {
			return (this.getTerminalKey())
					.equals(((Notification) other).getTerminalKey());
		}
		return false;
	}

	public OffToSilent(String key) {
		super(key);
	}

	@Override
	public String getTypeName() {
		return "O2S";
	}

	
}
