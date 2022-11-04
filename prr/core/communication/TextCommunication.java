package prr.core.communication;

import prr.core.pricetable.PriceTable;
import prr.core.terminal.Terminal;

import java.io.Serial;

public class TextCommunication extends Communication {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	private String _message;

	public TextCommunication(int key,
			Terminal sender, Terminal receiver, String message) {
		super(key, sender, receiver);
		this._message = message;
		this.setUnits(message.length());
	}

	@Override
	public void logCommunication(PriceTable priceTable) {
		double cost = priceTable.getCost(this);
		
		this.logCost(cost);
		this.getClient().sendTextCommunication();
	}

	@Override
	public String getTypeName() {
		return "TEXT";
	}
	
}
