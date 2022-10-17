package prr.core.client;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import prr.core.terminal.Terminal;

public class Client implements Serializable {

	@Serial
	private static final long serialVersionUID = 202210161323L;

	private String _key;

	private String _name;

	private int _taxId;

	private ClientStatus _status;

	private boolean _notificationsOn;
	
	private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();


	public abstract class ClientStatus implements Serializable {

		@Serial
		private static final long serialVersionUID = 202210161323L;

		public void changeStatus() {} 
	}

	public Client(String key, String name, int taxId) {
		this._key = key;
		this._name = name;
		this._notificationsOn = true; // default
		this._taxId = taxId;
		this._status = new NormalClientStatus(); // default
	}

	public void addTerminal(Terminal terminal, String terminalKey) {
		this._terminals.put(terminalKey, terminal);
	}
}
