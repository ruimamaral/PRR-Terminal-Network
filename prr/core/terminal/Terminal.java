package prr.core.terminal;

import java.io.Serializable;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable /* FIXME maybe add more interfaces */{

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202210161925L;

	private int _id;

	private Client _client;

	private TerminalState _state;

	// Class that manages terminal state dependent functionalities.
	public abstract class TerminalState implements Serializable { // maybe set private

		void setState(TerminalState state) {
			Terminal.this._state = state;
		}
		abstract void turnOff();

		abstract void setIdle();
	
		abstract void setBusy();

		abstract void setSilence();

		abstract boolean canStartCommunication();

		abstract boolean canEndCurrentCommunication();
	}

	public Terminal(int id, Client client) {
		this._client = client;
		this._id = id;
		this._state = new IdleTerminalState();
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
