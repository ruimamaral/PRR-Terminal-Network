package prr.util;

import java.util.Collection;

import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.terminal.Terminal;

public class StringMaker implements Visitor<Void> {

	private StringBuilder _text = new StringBuilder(500);

	public Void visit(Client client) {

		this._text.append("CLIENT|").append(client.getKey()).append("|")
				.append(client.getName()).append("|")
				.append(client.getTaxId()).append("|")
				.append(client.getStatusName()).append("|");

		if (client.hasNotificationsEnabled()) {
			this._text.append("YES");
		} else {
			this._text.append("No");
		}
		this._text.append("|").append(client.getTerminalCount()).append("|")
				.append(client.getTotalPaid()).append("|")
				.append(client.getDebt()).append("\n");
	
		return null;
	}

	public Void visit(Terminal terminal) {

		this._text.append(terminal.getTypeName()).append("|")
				.append(terminal.getKey()).append("|")
				.append(terminal.getClientKey()).append("|")
				.append(terminal.getStateName()).append("|")
				.append(terminal.getTotalPaid()).append("|")
				.append(terminal.getDebt());
			
		Collection<String> friends = terminal.getFriendKeys();

		if (friends.size() != 0) {
			this._text.append("|");
			for (String friendKey : friends) {
				this._text.append(friendKey).append(",");
			}
			// Delete trailing comma
			this._text.deleteCharAt(this._text.length() - 1);
		}
		this._text.append("\n");

		return null;
	}

	public Void visit(Communication comm) {
		// TODO make comm string
		return null;
	}

	@Override
	public String toString() {
		// Delete trailing newline character before returning
		if (!this._text.isEmpty()) {
			this._text.deleteCharAt(this._text.length() - 1);
		}
		String finished = this._text.toString();

		// Reset StringBuilder
		this._text.setLength(0);
		return finished;
	}
	
}
