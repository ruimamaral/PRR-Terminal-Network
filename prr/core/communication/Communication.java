package prr.core.communication;

import prr.core.terminal.Terminal;

public abstract class Communication {
	private int _units;

	private int _key;

	private boolean _isOngoing;

	private boolean _isPaid;

	private double _cost;

	private Terminal _sender;

	private Terminal _receiver;

	protected Communication(int key, Terminal sender, Terminal receiver) {
		this._key = key;
		this._isOngoing = true;
		this._isPaid = false;
		this._receiver = receiver;
		this._sender = sender;
	}

	public Integer getKey() {
		return this._key;
	}
}
