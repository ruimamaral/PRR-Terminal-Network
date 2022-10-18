package prr.util;

import prr.core.client.Client;
import prr.core.terminal.Terminal;

public class StringMaker implements Visitor<String> {

	StringBuilder text = new StringBuilder(500);

	public String visit(Client client) {

		text.append("CLIENT|").append(client.getKey()).append("|")
				.append(client.getName()).append("|")
				.append(client.getTaxId()).append("|")
				.append(client.getStatusName()).append("|")
				.append(client.hasNotificationsEnabled()).append("|")
				.append(client.getTerminalCount()).append("|")
				.append(client.getTotalPaid()).append("|")
				.append(client.getDebt()).append("/n");
	
		return this.toString();
	}

	public String visit(Terminal terminal) {
		return this.toString();
	}

	@Override
	public String toString() {
		// Delete trailing newline character before returning
		return text.deleteCharAt(text.length() - 1).toString();
	}
	
}
