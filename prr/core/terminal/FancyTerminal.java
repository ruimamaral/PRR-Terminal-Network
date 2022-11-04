package prr.core.terminal;

import java.io.Serial;

import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.communication.VideoCommunication;
import prr.core.exception.ActionNotSupportedAtDestination;
import prr.core.exception.ActionNotSupportedAtOrigin;
import prr.core.exception.TargetBusyException;
import prr.core.exception.TargetOffException;
import prr.core.exception.TargetSilentException;

public class FancyTerminal extends Terminal {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	public FancyTerminal(String key,
			Client client) throws IllegalArgumentException {
		super(key, client);
	}

	@Override
	public String getTypeName() {
		return "FANCY";
	}

	@Override
	public Communication startVideoCommunication(int key, Terminal sender,
			Terminal receiver) throws IllegalAccessException,
			TargetOffException, TargetBusyException, TargetSilentException,
			ActionNotSupportedAtDestination, ActionNotSupportedAtOrigin {
		
		VideoCommunication comm = new VideoCommunication(key, sender, receiver);

		// check not needed but in place in case method is misused	
		if (this.canStartCommunication()) {
			comm.getReceiver().receiveVideoCommunication(comm);
			this.startInteractiveCommunication(comm);
			comm.getClient().startVideoCommunication();
		} else {
			throw new IllegalAccessException();
		}
		return comm;
	}

	@Override
	protected void receiveVideoCommunication(
			VideoCommunication comm) throws IllegalAccessException,
			TargetOffException, TargetBusyException, TargetSilentException {

		this.receiveInteractiveCommunication(comm);
	}
}