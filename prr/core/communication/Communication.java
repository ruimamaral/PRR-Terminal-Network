package prr.core.communication;

import java.io.Serial;

import prr.core.client.Client;
import prr.core.pricetable.PriceTable;
import prr.core.terminal.Terminal;
import prr.util.Visitable;
import prr.util.Visitor;

public abstract class Communication implements Visitable {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	private int _units;

	private int _key;

	private boolean _isOngoing;

	private double _cost;

	private Terminal _sender;

	private Terminal _receiver;

	private boolean _friendly;

	protected Communication(int key, Terminal sender, Terminal receiver) {
		this._key = key;
		this._isOngoing = true;
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
		return this._units;
	}
	public double getCost() {
		return this._cost;
	}
	public Client getClient() {
		return this._sender.getClient();
	}

	public void setCost(PriceTable priceTable) {
		double cost = this.calculateCost(priceTable);
		this._cost = cost;
		this._sender.addDebt(cost);
	}

	protected abstract double calculateCost(PriceTable priceTable);

	public boolean isFriendly() {
		return this._friendly;
	}
}
