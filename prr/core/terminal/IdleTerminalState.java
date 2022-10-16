package prr.core.terminal;

import java.io.Serial;

public class IdleTerminalState extends Terminal.TerminalState {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	// FIXME add more functionality.
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

	boolean canStartCommunication() {
		return true;
	}

	boolean canEndCurrentCommunication() {
		return false;
	}
}
