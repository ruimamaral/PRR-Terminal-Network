package prr.core.terminal;

import java.io.Serial;

import prr.core.client.Client;

public class FancyTerminal extends Terminal {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	@Override
	public String getTypeName() {
		return "FANCY";
	}

	public FancyTerminal(String key, Client client) {
		super(key, client);
	}
	//FIXME add more functionality.
}
