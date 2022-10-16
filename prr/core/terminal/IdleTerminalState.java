package prr.core.terminal;

import java.io.Serial;

public class IdleTerminalState extends Terminal.TerminalState {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	// FIXME add more functionality.

	@Override
	void setIdle() {
	}

	boolean canStartCommunication() {
		return true;
	}

	boolean canEndCurrentCommunication() {
		return false;
	}
}
