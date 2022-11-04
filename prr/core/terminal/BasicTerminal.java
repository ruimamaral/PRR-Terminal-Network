package prr.core.terminal;

import java.io.Serial;

import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.communication.VideoCommunication;
import prr.core.exception.ActionNotSupportedAtDestination;
import prr.core.exception.ActionNotSupportedAtOrigin;

public class BasicTerminal extends Terminal {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	public BasicTerminal(String key,
			Client client) throws IllegalArgumentException {
		super(key, client);
	}

	@Override
	public String getTypeName() {
		return "BASIC";
	}

	@Override
	public Communication startVideoCommunication(int key,Terminal sender,
			Terminal receiver) throws ActionNotSupportedAtOrigin {

		throw new ActionNotSupportedAtOrigin();
	}

	@Override
	public void receiveVideoCommunication(
			VideoCommunication comm) throws ActionNotSupportedAtDestination {

		throw new ActionNotSupportedAtDestination();
	}
		
}
