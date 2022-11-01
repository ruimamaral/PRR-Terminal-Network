package prr.core.terminal;

import java.io.Serial;

import prr.core.client.Client;
import prr.core.communication.Communication;

public class FancyTerminal extends Terminal {

	@Serial
	private static final long serialVersionUID = 202210161925L;
	private Object _ongoingCom;

	public FancyTerminal(String key,
			Client client) throws IllegalArgumentException {
		super(key, client);
		this._ongoingCom = null;
	}

	@Override
	public boolean hasOngoingCom() {
		return this._ongoingCom != null;
	}

	@Override
	public String getTypeName() {
		return "FANCY";
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
			this._ongoingCom = comm;
			this.addCommunication(comm);
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
			this.addCommunication(comm);
			comm.setCost(this.getPriceTable());
		} else {
			throw new IllegalAccessException();
		}
	}

	public void receiveTextCommunication(
			Communication comm) throws TargetOffException {

		this._state.receiveTextCommunication(comm);
	}

	public void endCurrentCommunication() throws IllegalAccessException {
		this._state.endCurrentCommunication();
	}
	//FIXME add more functionality.
}
