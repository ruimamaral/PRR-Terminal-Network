package prr.core.terminal;

import java.io.Serial;

import prr.core.client.Client;
import prr.core.communication.Communication;

public class FancyTerminal extends Terminal {

	@Serial
	private static final long serialVersionUID = 202210161925L;
	private Object _ongoingCom;

	public FancyTerminal(String key,
			Client client) throws IllegalArgumentException {
		super(key, client);
		this._ongoingCom = null;
	}

	@Override
	public boolean hasOngoingCom() {
		return this._ongoingCom != null;
	}

	@Override
	public String getTypeName() {
		return "FANCY";
	}

	public void startInteractiveCommunication(Communication communication) {
		// FIXME
	}

	public void receiveInteractiveCommunication(Communication communication) {
		this.addCommunication(communication);
		// FIXME
	}

	//FIXME add more functionality.
}
