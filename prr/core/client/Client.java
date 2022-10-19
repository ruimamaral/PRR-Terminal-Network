package prr.core.client;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import prr.core.terminal.Terminal;
import prr.util.Visitable;
import prr.util.Visitor;

public class Client implements Serializable, Visitable {

	@Serial
	private static final long serialVersionUID = 202210161323L;

	private String _key;

	private String _name;

	private int _taxId;

	private ClientStatus _status;

	private boolean _notificationsOn;

	private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();

	private double _totalPaid;

	private double _debt;


	public abstract class ClientStatus implements Serializable {

		@Serial
		private static final long serialVersionUID = 202210161323L;

		public abstract String getName();

		// FIXME define changeStatus
		public void changeStatus() {} 
	}

	public Client(String key, String name, int taxId) {
		this._key = key;
		this._name = name;
		this._notificationsOn = true; // default
		this._taxId = taxId;
		this._status = new NormalClientStatus(); // default
		this._debt = 0;
		this._totalPaid = 0;
	}

	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	public String getKey() {
		return this._key;
	}
	public String getName() {
		return this._name;
	}
	public int getTaxId() {
		return this._taxId;
	}
	public boolean hasNotificationsEnabled() {
		return this._notificationsOn;
	}
	public String getStatusName() {
		return this._status.getName();
	}
	public int getTerminalCount() {
		return this._terminals.size();
	}
	public int getTotalPaid() {
		return (int) Math.round(this._totalPaid);
	}
	public int getDebt() {
		return (int) Math.round(this._debt);
	}


	public void addTerminal(Terminal terminal, String terminalKey) {
		this._terminals.put(terminalKey, terminal);
	}
}
