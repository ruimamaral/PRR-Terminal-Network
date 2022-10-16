package prr.core.terminal;

public class BasicTerminal extends Terminal {

	public BasicTerminal(int id, Client client) {
		super(id, client);
	}

	@Override
	public void setBusy() {
		// FIXME throw exception
	}

	@Override
	public void setSilence() {
		// FIXME throw exception
	}
	//FIXME add more functionality.
}
