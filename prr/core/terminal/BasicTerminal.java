package prr.core.terminal;

import prr.core.client.Client;

public class BasicTerminal extends Terminal {

	public BasicTerminal(String key, Client client) {
		super(key, client);
	}

	@Override
	public void setBusy() {
		// FIXME throw exception
	}

	@Override
	public void setSilence() {
		// FIXME throw exception
	}
	//FIXME add more functionality.
}
