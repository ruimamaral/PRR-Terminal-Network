package prr.core.communication;

import prr.core.terminal.Terminal;

public class TextCommunication extends Communication {

	private String _message;

	public TextCommunication(int key,
			Terminal sender, Terminal receiver, String message) {
		super(key, sender, receiver);
		this._message = message;
	}
	
}
