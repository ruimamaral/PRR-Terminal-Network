package prr.core.terminal;

import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.communication.TextCommunication;
import prr.core.communication.VideoCommunication;
import prr.core.communication.VoiceCommunication;
import prr.core.exception.ActionNotSupportedAtDestination;
import prr.core.exception.ActionNotSupportedAtOrigin;
import prr.core.exception.TargetBusyException;
import prr.core.exception.TargetOffException;
import prr.core.exception.TargetSilentException;
import prr.core.notification.BusyToIdle;
import prr.core.notification.Notification;
import prr.core.notification.OffToIdle;
import prr.core.notification.OffToSilent;
import prr.core.notification.SilentToIdle;
import prr.core.pricetable.PriceTable;
import prr.util.Visitable;
import prr.util.Visitor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
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

	// Set prevents duplicates
	private Set<Client> _clientsToNotify =
			new TreeSet<Client>(Comparator.comparing(Client::getKey));
	
	private Communication _ongoingCom;

	private double _totalPaid;

	private double _debt;

	protected Terminal(String key,
			Client client) throws IllegalArgumentException {
		this.checkKey(key);
		this._client = client;
		this._key = key;
		this._state = new IdleTerminalState();
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Terminal) {
			return this.getKey().equals(((Terminal) other).getKey());
		}
		return false;
	}

	public abstract String getTypeName();

	public String getKey() {
		return this._key;
	}
	public double getTotalPaid() {
		return this._totalPaid;
	}
	public double getDebt() {
		return this._debt;
	}
	public Client getClient() {
		return this._client;
	}
	public boolean isInactive() {
		return this._sentComms.size() == 0;
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
	public Communication getOngoing() {
		return this._ongoingCom;
	}

	public double calculateBalance() {
		return this._totalPaid - this._debt;
	}

	private void addDebt(double amount) {
		this._debt += amount;
		this._client.addDebt(amount);
	}

	private void payAmount(double amount) {
		this._totalPaid += amount;
		this._client.payAmount(amount);
	}

	public void payCommunication(int commKey) throws IllegalAccessException {
		if (!this._sentComms.containsKey(commKey)) {
			throw new IllegalArgumentException();
		}
		Communication comm = this._sentComms.get(commKey);
		double amount = comm.pay();
		this.payAmount(amount);
		this.addDebt(-amount);
	}

	public void turnOff() throws IllegalAccessException {
		this._state.turnOff();
	}
	public void setIdle() throws IllegalAccessException {
		this._state.setIdle();
	}
	public void setSilent() throws IllegalAccessException {
		this._state.setSilent();
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

	public void removeFriend(String key) throws IllegalArgumentException {
		if (!this.isFriend(key)) {
			throw new IllegalArgumentException();
		}
		this._friends.remove(key);
	}

	public boolean isFriend(String key) {
		return this._friends.containsKey(key);
	}

	private void addCommunication(Communication communication) {
		this._sentComms.put(communication.getKey(), communication);
	}

	private void logCommunicationAttempt(Communication comm) {
		Client client = comm.getClient();

		if (client.hasNotificationsEnabled()) {
			this._clientsToNotify.add(client);
		}
	}

	private void pushNotifications(Notification notification) {
		this._clientsToNotify.forEach(c -> c.pushNotification(notification));
		this._clientsToNotify.clear();
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
	 * @return true if this terminal is neither off neither busy,
	 * 		false otherwise.
	 **/
	public boolean canStartCommunication() {
		return this._state.canStartCommunication();
	}

	public abstract Communication startVideoCommunication(int key,
			Terminal sender, Terminal receiver) throws IllegalAccessException,
			TargetOffException, TargetBusyException, TargetSilentException,
			ActionNotSupportedAtDestination, ActionNotSupportedAtOrigin;

	protected abstract void receiveVideoCommunication(
			VideoCommunication comm) throws IllegalAccessException,
			TargetOffException, TargetBusyException, TargetSilentException,
			ActionNotSupportedAtDestination, ActionNotSupportedAtOrigin;

	public Communication startVoiceCommunication(
			int key, Terminal sender, Terminal receiver)
			throws IllegalAccessException,
			TargetOffException, TargetBusyException, TargetSilentException {

		// check not needed but in place in case method is misused	
		VoiceCommunication comm = new VoiceCommunication(key, sender, receiver);
		if (this.canStartCommunication()) {
			comm.getReceiver().receiveVoiceCommunication(comm);
			this.startInteractiveCommunication(comm);
			comm.getClient().startVoiceCommunication();
		} else {
			throw new IllegalAccessException();
		}
		return comm;
	}
		
	public void receiveVoiceCommunication(
			VoiceCommunication comm) throws IllegalAccessException,
			TargetOffException, TargetBusyException, TargetSilentException {

		this.receiveInteractiveCommunication(comm);
	}

	protected void startInteractiveCommunication(
			Communication comm)	throws IllegalAccessException {

			this._ongoingCom = comm;
			this.addCommunication(comm);
			this._state.setBusy(true);
	}

	protected void receiveInteractiveCommunication(
			Communication comm) throws IllegalAccessException,
			TargetBusyException, TargetSilentException, TargetOffException {
			
		this._state.receiveInteractiveCommunication(comm);
	}

	public Communication sendTextCommunication(
			int key, Terminal sender, Terminal receiver, String message)
			throws IllegalAccessException, TargetOffException {

		TextCommunication comm =
				new TextCommunication(key, sender, receiver, message);

		// check not needed but in place in case method is misused	
		if (this.canStartCommunication()) {
			comm.getReceiver().receiveTextCommunication(comm);
			this.addCommunication(comm);
			this.addDebt(comm.setCost(this.getPriceTable()));
			comm.getClient().sendTextCommunication();
		} else {
			throw new IllegalAccessException();
		}
		return comm;
	}

	protected void receiveTextCommunication(
			Communication comm) throws TargetOffException {

		this._state.receiveTextCommunication(comm);
	}

	public void endCurrentCommunication() throws IllegalAccessException {
		this._state.endCurrentCommunication();
	}

	/*
	 *
	 *  Inner Classes ------------------------------------------------
	 * 
	 */

	// Class that manages terminal state dependent functionalities.
	public abstract class TerminalState implements Serializable {

		protected void setState(TerminalState state) {
			Terminal.this._state = state;
		}

		protected void turnOff() throws IllegalAccessException {
			this.setState(new OffTerminalState());
		}
		protected void setIdle() throws IllegalAccessException {
			this.setState(new IdleTerminalState());
		}
		protected void setSilent() throws IllegalAccessException {
			this.setState(new SilenceTerminalState());
		}
		protected void setBusy(boolean isSender) throws IllegalAccessException {
			if (isSender) {
				this.setState(
						new SenderBusyTerminalState(Terminal.this._state));
			} else {
				this.setState(new BusyTerminalState(Terminal.this._state));
			}
		}

		protected abstract void restoreState() throws IllegalAccessException;

		protected abstract String getStateName();

		protected  boolean canStartCommunication() {
			return false;
		}

		protected  boolean canEndCurrentCommunication() {
			return false;
		}

		protected void receiveTextCommunication(
				Communication comm) throws TargetOffException {}

		protected abstract void receiveInteractiveCommunication(
			Communication comm) throws TargetOffException, TargetBusyException,
			TargetSilentException, IllegalAccessException;

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
		protected void setSilent() throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void turnOff() throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void restoreState() throws IllegalAccessException {
			throw new IllegalAccessException();
		}
	
		@Override
		protected String getStateName() {
			return "BUSY";
		}

		@Override
		protected void receiveInteractiveCommunication(
				Communication comm) throws TargetBusyException {
			
			Terminal.this.logCommunicationAttempt(comm);
			throw new TargetBusyException();
		}

		@Override
		protected Communication endCurrentCommunication()
				throws IllegalAccessException {

			Communication comm = Terminal.this._ongoingCom;
			Terminal.this._ongoingCom = null;
			this._oldState.restoreState();
			return comm;
		}
	}

	public class SenderBusyTerminalState extends Terminal.BusyTerminalState {

		@Serial
		private static final long serialVersionUID = 202210161925L;

		protected SenderBusyTerminalState(TerminalState oldState) {
			super(oldState);
		}

		@Override
		protected boolean canEndCurrentCommunication() {
			return true;
		}

		@Override
		protected Communication endCurrentCommunication()
				throws IllegalAccessException {

			Communication comm = super.endCurrentCommunication();
			comm.getReceiver().endCurrentCommunication();
			Terminal.this.addDebt(comm.setCost(Terminal.this.getPriceTable()));
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
		protected void restoreState() {
			this.setState(this);
			Terminal.this.pushNotifications(new BusyToIdle(Terminal.this._key));
		}
	
		@Override
		protected String getStateName() {
			return "IDLE";
		}
	
		@Override
		protected boolean canStartCommunication() {
			return true;
		}

		@Override
		protected void receiveInteractiveCommunication(Communication comm)
			throws TargetOffException, TargetBusyException,
			TargetSilentException, IllegalAccessException {

			Terminal.this._ongoingCom = comm;
			this.setBusy(false);
		}
	}
	
	public class OffTerminalState extends Terminal.TerminalState {
		
		@Serial
		private static final long serialVersionUID = 202210161925L;
	
		@Override
		protected void setIdle() throws IllegalAccessException {
			super.setIdle();
			Terminal.this.pushNotifications(new OffToIdle(Terminal.this._key));
		}
		@Override
		protected void turnOff() throws IllegalAccessException {
			throw new IllegalAccessException();
		}
		@Override
		protected void setSilent() throws IllegalAccessException {
			super.setSilent();
			Terminal.this.pushNotifications(
					new OffToSilent(Terminal.this._key));
		}

		@Override
		protected void setBusy(boolean isSender) throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void restoreState() throws IllegalAccessException {
			throw new IllegalAccessException();
		}
	
		@Override
		protected String getStateName() {
			return "OFF";
		}

		@Override
		protected void receiveTextCommunication(
				Communication comm) throws TargetOffException {
			
			Terminal.this.logCommunicationAttempt(comm);
			throw new TargetOffException();
		}

		@Override
		protected void receiveInteractiveCommunication(
				Communication comm) throws TargetOffException {

			Terminal.this.logCommunicationAttempt(comm);
			throw new TargetOffException();
		}
	}

	public class SilenceTerminalState extends Terminal.TerminalState {
	
		@Serial
		private static final long serialVersionUID = 202210161925L;

		@Override
		protected void setIdle() throws IllegalAccessException {
			super.setIdle();
			Terminal.this.pushNotifications(
					new SilentToIdle(Terminal.this._key));
		}
		@Override
		protected void setSilent() throws IllegalAccessException {
			throw new IllegalAccessException();
		}

		@Override
		protected void restoreState() {
			this.setState(this);
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

			Terminal.this.logCommunicationAttempt(comm);
			throw new TargetSilentException();
		}
	}
}
