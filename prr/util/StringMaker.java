package prr.util;

import prr.core.client.Client;
import prr.core.terminal.Terminal;

public class StringMaker implements Visitor<String> {

	public String visit(Client client) {
		StringBuilder text = new StringBuilder(100);
		return text.toString();
	}

	public String visit(Terminal terminal) {
		StringBuilder text = new StringBuilder(200);
		return text.toString();
	}
	
}
