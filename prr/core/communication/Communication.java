package prr.core.communication;

import java.io.Serial;
import java.io.Serializable;

import prr.core.client.Client;
import prr.core.pricetable.PriceTable;
import prr.core.terminal.Terminal;
import prr.util.Visitable;
import prr.util.Visitor;

public abstract class Communication implements Visitable, Serializable {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	private int _units;

	private int _key;

	private boolean _isOngoing;

	private boolean _isPaid;

	private double _cost;

	private Terminal _sender;

	private Terminal _receiver;

	private boolean _friendly;

	protected Communication(int key, Terminal sender, Terminal receiver) {
		this._key = key;
		this._receiver = receiver;
		this._sender = sender;
		this._friendly = sender.isFriend(receiver.getKey());
		this._isPaid = false;
		this._isOngoing = true;
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	public int getKey() {
		return this._key;
	}
	public int getUnits() {
		return this._units;
	}
	public Client getClient() {
		return this._sender.getClient();
	}
	public Terminal getReceiver() {
		return this._receiver;
	}
	public Terminal getSender() {
		return this._sender;
	}
	public double getCost() {
		return this._cost;
	}
	public boolean isOngoing() {
		return this._isOngoing;
	}
	public abstract String getTypeName();

	public abstract void logCommunication(PriceTable priceTable);

	protected void logCost(double cost) {
		this._isOngoing = false;
		this._cost = cost;
		this._sender.addDebt(cost);
	}

	public void setUnits(int units) {
		this._units = units;
	}

	public void pay() throws IllegalAccessException {
		if (this._isOngoing || this._isPaid) {
			throw new IllegalAccessException();
		}
		this._sender.payAmount(this._cost);
		this._isPaid = true;
	}

	public boolean isFriendly() {
		return this._friendly;
	}
}
