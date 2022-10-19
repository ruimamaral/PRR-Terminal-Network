package prr.core.terminal;

import java.io.Serial;

public class OffTerminalState extends Terminal.TerminalState {
	
	@Serial
	private static final long serialVersionUID = 202210161925L;

	// FIXME add more functionality.

	@Override
	void turnOff() {}

	String getStateName() {
		return "OFF";
	}

	boolean canStartCommunication() {
		return false;
	}

	boolean canEndCurrentCommunication() {
		return false;
	}
}
