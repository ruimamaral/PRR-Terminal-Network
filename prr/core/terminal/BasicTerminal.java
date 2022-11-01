package prr.core.terminal;

import java.io.Serial;

import prr.core.client.Client;

public class BasicTerminal extends Terminal {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	public BasicTerminal(String key,
			Client client) throws IllegalArgumentException {
		super(key, client);
	}

	@Override
	public boolean hasOngoingCom() {
		return false;
	}

	@Override
	public String getTypeName() {
		return "BASIC";
	}

	//TODO add exception overrides to fancyterminal only functionalities

	@Override
	public void setSilence() {
		// FIXME throw exception
	}
	//FIXME add more functionality.
}
