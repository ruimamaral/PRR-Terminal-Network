package prr.core.terminal;

import java.io.Serial;

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
