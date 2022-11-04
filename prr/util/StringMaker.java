package prr.util;

import java.util.Collection;

import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.notification.Notification;
import prr.core.terminal.Terminal;

public class StringMaker implements Visitor<Void> {

	private StringBuilder _text = new StringBuilder(500);

	// Format:	CLIENT|key|name|taxId|type|notifications|terminals|payments|debts
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
				.append(Math.round(client.getTotalPaid())).append("|")
				.append(Math.round(client.getDebt())).append("\n");
	
		return null;
	}
	// Format:	terminalType|terminalId|clientId|terminalStatus|balance-paid|balance-debts|friend1,...,friend
	//			terminalType|terminalId|clientId|terminalStatus|balance-paid|balance-debts
	public Void visit(Terminal terminal) {

		this._text.append(terminal.getTypeName()).append("|")
				.append(terminal.getKey()).append("|")
				.append(terminal.getClientKey()).append("|")
				.append(terminal.getStateName()).append("|")
				.append(Math.round(terminal.getTotalPaid())).append("|")
				.append(Math.round(terminal.getDebt()));
			
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
	// Format:	type|idCommunication|idSender|idReceiver|units|price|status
	public Void visit(Communication comm) {

		this._text.append(comm.getTypeName()).append("|")
				.append(comm.getKey()).append("|")
				.append(comm.getSender().getKey()).append("|")
				.append(comm.getReceiver().getKey()).append("|")
				.append(comm.getUnits()).append("|")
				.append(comm.getCost()).append("|");
		
		if (comm._isOngoing()) {
 			this._text.append("ONGOING");
		} else {
			this._text.append("FINISHED");
		}
		this._text.append("\n");
		return null;
	}
	// Format:	notification-type|idTerminal
	public Void visit(Notification notification) {
		this._text.append(notification.getTypeName()).append("|")
				.append(notification.getTerminalKey());
		this._text.append("\n");
		return null;
	}

	public int length() {
		return this._text.length();
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
