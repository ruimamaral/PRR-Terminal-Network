package prr.core.terminal;

import prr.core.client.Client;
import prr.util.Visitable;
import prr.util.Visitor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.io.Serial;

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable, Visitable {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	private String _key;

	private Client _client;

	private TerminalState _state;

	private Map<String, Terminal> _friends = new TreeMap<String, Terminal>();

	private boolean _isActive;

	private double _totalPaid;

	private double _debt;

	// Class that manages terminal state dependent functionalities.
	public abstract class TerminalState implements Serializable {

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

		abstract String getStateName();

		abstract boolean canStartCommunication();

		abstract boolean canEndCurrentCommunication();
	}

	public Terminal(String key, Client client) {
		this._client = client;
		this._key = key;
		this._state = new IdleTerminalState();
		this._isActive = false;
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	public abstract String getTypeName();

	public abstract boolean hasOngoingCom();

	public String getKey() {
		return this._key;
	}
	public int getTotalPaid() {
		return (int) Math.round(this._totalPaid);
	}
	public int getDebt() {
		return (int) Math.round(this._debt);
	}
	public boolean hasActivity() {
		return this._isActive;
	}
	public String getClientKey() {
		return this._client.getKey();
	}
	public String getStateName() {
		return this._state.getStateName();
	}
	public Collection<String> getFriendKeys() {
		return Collections.unmodifiableSet(this._friends.keySet());
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
		String friendKey = friend.getKey();

		if (this._friends.containsKey(friendKey)) {
			throw new IllegalArgumentException();
		}
		this._friends.put(friendKey, friend);
	}

	public void startInteractiveCommunication() {
		this._isActive = true;
		// FIXME
	}

	public void receiveInteractiveCommunication() {
		this._isActive = true;
		// FIXME
	}

	/**
	 * Checks if this terminal can end the current interactive communication.
	 *
	 * @return true if this terminal is busy (i.e., it has an active
	 * 		interactive communication) and it was the originator of
	 * 		this communication.
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

	public class BusyTerminalState extends Terminal.TerminalState {
	
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		// FIXME add more functionality.
	
		@Override
		void setBusy() {}
	
		@Override
		String getStateName() {
			return "BUSY";
		}
	
		@Override
		boolean canStartCommunication() {
			return false;
		}
	
		@Override
		boolean canEndCurrentCommunication() {
			// FIXME check if terminal is originator.
			return true;
		}
	}

	public class IdleTerminalState extends Terminal.TerminalState {
	
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		// FIXME add more functionality.
	
		@Override
		void setIdle() {}
	
		@Override
		String getStateName() {
			return "IDLE";
		}
	
		@Override
		boolean canStartCommunication() {
			return true;
		}
	
		@Override
		boolean canEndCurrentCommunication() {
			return false;
		}
	}
	
	public class OffTerminalState extends Terminal.TerminalState {
		
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		// FIXME add more functionality.
	
		@Override
		void turnOff() {}
	
		@Override
		String getStateName() {
			return "OFF";
		}
	
		@Override
		boolean canStartCommunication() {
			return false;
		}
	
		@Override
		boolean canEndCurrentCommunication() {
			return false;
		}
	}

	public class SilenceTerminalState extends Terminal.TerminalState {
	
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		// FIXME add more functionality.
	
		@Override
		void setSilence() {}
	
		@Override
		String getStateName() {
			return "SILENCE";
		}
	
		@Override
		boolean canStartCommunication() {
			return false;
		}
	
		@Override
		boolean canEndCurrentCommunication() {
			return false;
		}
	}
}
