package prr.util;

import prr.core.client.Client;
import prr.core.communication.Communication;
import prr.core.terminal.Terminal;

public interface Visitor<T> {
	
	public abstract T visit(Client client);
	public abstract T visit(Terminal terminal);
	public abstract T visit(Communication communication);
}
