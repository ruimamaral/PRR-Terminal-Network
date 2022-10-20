
package prr.core.terminal;

import java.io.Serial;

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
