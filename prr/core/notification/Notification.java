package prr.core.notification;

import java.io.Serial;
import java.io.Serializable;

import prr.util.Visitable;
import prr.util.Visitor;

public abstract class Notification implements Serializable, Visitable {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	private final String _terminalKey;
	
	@Override
	public int hashCode() {
		return this._terminalKey.hashCode();
	}

	@Override
	public abstract boolean equals(Object other);

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}
	
	protected Notification(String key) {
		this._terminalKey = key;
	}

	public String getTerminalKey() {
		return this._terminalKey;
	}

	public abstract String getTypeName();
	
}
