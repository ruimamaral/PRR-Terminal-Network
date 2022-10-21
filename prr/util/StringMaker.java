package prr.util;

import java.util.Collection;

import prr.core.client.Client;
import prr.core.terminal.Terminal;

public class StringMaker implements Visitor<Void> {

	StringBuilder text = new StringBuilder(500);

	public Void visit(Client client) {

		text.append("CLIENT|").append(client.getKey()).append("|")
				.append(client.getName()).append("|")
				.append(client.getTaxId()).append("|")
				.append(client.getStatusName()).append("|");

		if (client.hasNotificationsEnabled()) {
			text.append("YES");
		} else {
			text.append("No");
		}
		text.append("|").append(client.getTerminalCount()).append("|")
				.append(client.getTotalPaid()).append("|")
				.append(client.getDebt()).append("\n");
	
		return null;
	}

	public Void visit(Terminal terminal) {

		text.append(terminal.getTypeName()).append("|")
				.append(terminal.getKey()).append("|")
				.append(terminal.getClientKey()).append("|")
				.append(terminal.getStateName()).append("|")
				.append(terminal.getTotalPaid()).append("|")
				.append(terminal.getDebt());
			
		Collection<String> friends = terminal.getFriendKeys();

		if (friends.size() != 0) {
			text.append("|");
			for (String friendKey : friends) {
				text.append(friendKey).append(",");
			}
			// Delete trailing comma
			text.deleteCharAt(text.length() - 1);
		}
		text.append("\n");

		return null;
	}


	@Override
	public String toString() {
		// Delete trailing newline character before returning
		if (!text.isEmpty()) {
			text.deleteCharAt(text.length() - 1);
		}
		String finished = text.toString();

		// Reset StringBuilder
		this.text.setLength(0);
		return finished;
	}
	
}
