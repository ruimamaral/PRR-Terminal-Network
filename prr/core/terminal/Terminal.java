package prr.core.terminal;

import prr.core.client.Client;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.io.Serial;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable /* FIXME maybe add more interfaces */{

	@Serial
	private static final long serialVersionUID = 202210161925L;

	private String _key;

	private Client _client;

	private TerminalState _state;

	private Map<String, Terminal> _friends = new TreeMap<String, Terminal>();

	private boolean _isActive;

	// Class that manages terminal state dependent functionalities.
	public abstract class TerminalState implements Serializable { // maybe set private

		void setState(TerminalState state) {
			Terminal.this._state = state;
		}
		void turnOff() {
			setState(new OffTerminalState());
		}
		void setIdle() {
			setState(new IdleTerminalState());
		}
		void setBusy() {
			setState(new BusyTerminalState());
		}
		void setSilence() {
			setState(new SilenceTerminalState());
		}

		abstract boolean canStartCommunication();

		abstract boolean canEndCurrentCommunication();
	}

	public Terminal(String key, Client client) {
		this._client = client;
		this._key = key;
		this._state = new IdleTerminalState();
		this._isActive = false;
	}

	public String getKey() {
		return this._key;
	}

	public void turnOff() {
		this._state.turnOff();
	}

	public void setIdle() {
		this._state.setIdle();
	}

	public void setBusy() {
		this._state.setBusy();
	}

	public void setSilence() {
		this._state.setSilence();
	}

	public static String checkKey(String key) {
		if (key.length() != 6) {
			throw new IllegalArgumentException();
		}
		try {
			Integer.parseInt(key);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		return key;
	}

	public void addFriend(Terminal friend) {
		this._friends.put(friend.getKey(), friend);
	}

	// FIXME
	public void startInteractiveCommunication() {
		this._isActive = true;
	}

	// FIXME
	public void receiveInteractiveCommunication() {
		this._isActive = true;
	}


	/**
	 * Checks if this terminal can end the current interactive communication.
 	 *
 	 * @return true if this terminal is busy (i.e., it has an active interactive communication) and
 	 *          it was the originator of this communication.
	 **/
	public boolean canEndCurrentCommunication() {
		return this._state.canEndCurrentCommunication();
	}
  
	/**
 	 * Checks if this terminal can start a new communication.
 	 *
 	 * @return true if this terminal is neither off neither busy, false otherwise.
	 **/
	public boolean canStartCommunication() {
		return this._state.canStartCommunication();
	}
}
