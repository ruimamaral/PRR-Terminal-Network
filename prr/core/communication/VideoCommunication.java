package prr.core.communication;

import prr.core.pricetable.PriceTable;
import prr.core.terminal.Terminal;

import java.io.Serial;

public class VideoCommunication extends Communication {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	public VideoCommunication(int key, Terminal sender, Terminal receiver) {
		super(key, sender, receiver);
	}

	@Override
	public double logCommunication(PriceTable priceTable) {
		double cost = priceTable.getCost(this);
		this.setIsOngoing(false);
		this.setCost(cost);
		this.getClient().endVideoCommunication();
		return cost;
	}

	@Override
	public String getTypeName() {
		return "VIDEO";
	}
}
