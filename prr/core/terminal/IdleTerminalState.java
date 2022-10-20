package prr.core.terminal;

import java.io.Serial;

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
