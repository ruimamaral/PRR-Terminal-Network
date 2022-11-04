package prr.core.communication;

import prr.core.pricetable.PriceTable;
import prr.core.terminal.Terminal;

import java.io.Serial;

public class VoiceCommunication extends Communication {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	public VoiceCommunication(int key, Terminal sender, Terminal receiver) {
		super(key, sender, receiver);
	}

	@Override
	public void logCommunication(PriceTable priceTable) {
		double cost = priceTable.getCost(this);

		this.logCost(cost);
		this.getClient().endVoiceCommunication();
	}

	@Override
	public String getTypeName() {
		return "VOICE";
	}
}
