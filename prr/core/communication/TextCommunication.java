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

	public String getTypeName() {
		return "TEXT";
	}

	public double calculateCost(PriceTable priceTable) {
		return priceTable.getCost(this);
	}
	
}
