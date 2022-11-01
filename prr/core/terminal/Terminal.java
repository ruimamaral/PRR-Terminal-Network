package prr.core.terminal;

import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.exception.TargetBusyException;
import prr.core.exception.TargetOffException;
import prr.core.exception.TargetSilentException;
import prr.core.pricetable.PriceTable;
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

	private Map<Integer, Communication> _sentComms =
			new TreeMap<Integer, Communication>();
	
	private Communication _ongoingCom;

	private boolean _isActive;

	private double _totalPaid;

	private double _debt;

	protected Terminal(String key,
			Client client) throws IllegalArgumentException {
		this.checkKey(key);
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
	public PriceTable getPriceTable() {
		return this._client.getPriceTable();
	}

	public void addDebt(double amount) {
		this._debt += amount;
		this._client.addDebt(amount);
	}

	public void turnOff() throws IllegalAccessException { // TODO catch exceptions
		this._state.turnOff();
	}
	public void setIdle() throws IllegalAccessException {
		this._state.setIdle();
	}
	public void setSilence() throws IllegalAccessException {
		this._state.setSilence();
	}

	private String checkKey(String key) throws IllegalArgumentException {
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

		if (this.isFriend(friendKey)) {
			throw new IllegalArgumentException();
		}
		this._friends.put(friendKey, friend);
	}

	public boolean isFriend(String key) {
		return this._friends.containsKey(key);
	}

	public void addCommunication(Communication communication) {
		this._sentComms.put(communication.getKey(), communication);
	}

	/**
	 * Checks if this terminal can end the current interactive communication.
	 *
	 * @return true if this terminal is busy (i.e., it has an active
	 * 		interactive communication) and it was the originator of
	 * 		this communication.
	 **/
	public boolean canEndCurrentCommunication()
			throws IllegalAccessException {

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

	public void startInteractiveCommunication(
			Communication comm)	throws IllegalAccessException {

		if(this.canStartCommunication()) {
			Terminal.this._ongoingCom = comm;
			Terminal.this.addCommunication(comm);
			this._state.setBusy(true);
		} else {
			throw new IllegalAccessException();
		}
	}

	public void receiveInteractiveCommunication(Communication comm)
			throws TargetOffException, TargetBusyException,
			TargetSilentException, IllegalAccessException {
			
		this._state.receiveInteractiveCommunication(comm);
	}

	public void sendTextCommunication(
			Communication comm) throws IllegalAccessException {

		if (this.canStartCommunication()) {
			Terminal.this.addCommunication(comm);
			comm.setCost(Terminal.this.getPriceTable());
		} else {
			throw new IllegalAccessException();
		}
	}

	public void receiveTextCommunication() throws TargetOffException {

		this._state.receiveTextCommunication();
	}

	public void endCurrentCommunication() throws IllegalAccessException {
		this._state.endCurrentCommunication();
	}

	// Class that manages terminal state dependent functionalities.
	public abstract class TerminalState implements Serializable {

		protected void setState(TerminalState state) {
			Terminal.this._state = state;
		}

		protected void turnOff() throws IllegalAccessException {
			setState(new OffTerminalState());
		}
		protected void setIdle() throws IllegalAccessException {
			setState(new IdleTerminalState());
		}
		protected void setBusy(boolean isSender) throws IllegalAccessException {
			if (isSender) {
				setState(new SenderBusyTerminalState(Terminal.this._state));
			} else {
				setState(new BusyTerminalState(Terminal.this._state));
			}
		}
		protected void setSilence() throws IllegalAccessException {
			setState(new SilenceTerminalState());
		}

		protected abstract String getStateName();

		protected  boolean canStartCommunication() {
			return false;
		}

		protected  boolean canEndCurrentCommunication() {
			return false;
		}

		protected void receiveTextCommunication() throws TargetOffException {}

		protected void receiveInteractiveCommunication(Communication comm)
			throws TargetOffException, TargetBusyException,
			TargetSilentException, IllegalAccessException {

			Terminal.this._ongoingCom = comm;
			this.setBusy(false);
		}

		protected Communication endCurrentCommunication()
				throws IllegalAccessException {

			throw new IllegalAccessException();
		}
	}

	public class BusyTerminalState extends Terminal.TerminalState {
	
		@Serial
		private static final long serialVersionUID = 202210161925L;

		private TerminalState _oldState;

		protected BusyTerminalState(TerminalState oldState) {
			this._oldState = oldState;
		}
	
		@Override
		protected void setBusy(boolean isSender) throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void setIdle() throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void setSilence() throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void turnOff() throws IllegalAccessException {
			throw new IllegalAccessException();
		}
	
		@Override
		protected String getStateName() {
			return "BUSY";
		}

		@Override
		protected void receiveInteractiveCommunication(
				Communication comm) throws TargetBusyException {

			throw new TargetBusyException();
		}

		@Override
		protected Communication endCurrentCommunication() {

			Communication comm = Terminal.this._ongoingCom;
			Terminal.this._ongoingCom = null;
			this.setState(this._oldState);
			return comm;
		}
	}

	public class SenderBusyTerminalState extends Terminal.BusyTerminalState {

		protected SenderBusyTerminalState(TerminalState oldState) {
			super(oldState);
		}

		@Override
		protected boolean canEndCurrentCommunication() {
			return true;
		}

		@Override
		protected Communication endCurrentCommunication() {
			Communication comm = super.endCurrentCommunication();
			comm.setCost(Terminal.this.getPriceTable());
			return comm;
		}
	}

	public class IdleTerminalState extends Terminal.TerminalState {
	
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		@Override
		protected void setIdle() throws IllegalAccessException {
			throw new IllegalAccessException();
		}
	
		@Override
		protected String getStateName() {
			return "IDLE";
		}
	
		@Override
		protected boolean canStartCommunication() {
			return true;
		}
	}
	
	public class OffTerminalState extends Terminal.TerminalState {
		
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		@Override
		protected void turnOff() throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void setBusy(boolean isSender) throws IllegalAccessException {
			throw new IllegalAccessException();
		}
	
		@Override
		protected String getStateName() {
			return "OFF";
		}

		@Override
		protected void receiveTextCommunication() throws TargetOffException {
			throw new TargetOffException();
		}

		@Override
		protected void receiveInteractiveCommunication(
				Communication comm) throws TargetOffException {

			throw new TargetOffException();
		}
	}

	public class SilenceTerminalState extends Terminal.TerminalState {
	
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		@Override
		protected void setSilence() throws IllegalAccessException {
			throw new IllegalAccessException();
		}
	
		@Override
		protected String getStateName() {
			return "SILENCE";
		}
	
		@Override
		protected boolean canStartCommunication() {
			return true;
		}

		@Override
		protected void receiveInteractiveCommunication(
				Communication comm) throws TargetSilentException {

			throw new TargetSilentException();
		}
	}
}
