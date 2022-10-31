package prr.core.communication;

import prr.core.pricetable.PriceTable;
import prr.core.terminal.Terminal;
import prr.util.Visitable;
import prr.util.Visitor;

public abstract class Communication implements Visitable {
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
		this._isOngoing = true;
		this._isPaid = false;
		this._receiver = receiver;
		this._sender = sender;
		this._friendly = sender.isFriend(receiver.getKey());
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		visitor.visit(this);
	}

	public int getKey() {
		return this._key;
	}

	public int getUnits() {
		return this._key;
	}

	public double getCost() {
		return this._cost;
	}

	public double setCost(PriceTable priceTable) {
		double cost = this.calculateCost(priceTable);
		this._cost = cost;
		return cost;
	}

	protected abstract double calculateCost(PriceTable priceTable);

	public boolean isFriendly() {
		return this._friendly;
	}
}
