package prr.core.notification;

import java.io.Serial;
import java.io.Serializable;

public abstract class Notification implements Serializable {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	private final String _terminalKey;
	
	protected Notification(String key) {
		this._terminalKey = key;
	}

	public String getTerminalKey() {
		return this._terminalKey;
	}

	public abstract String getType();
	
}
