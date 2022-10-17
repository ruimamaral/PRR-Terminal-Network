package prr.core.exception;

import java.io.Serial;

/**
 * Class that represents a problem with a terminal key
 * that is unknown in the app
 */
public class UnknownTerminalKeyException extends Exception {

	@Serial
	private static final long serialVersionUID = 202208091753L;

	private static final String ERROR_MESSAGE = "ID não corresponde a nenhum terminal: ";

	/**
	 * @param terminal terminal key
	 */
	public UnknownTerminalKeyException(String terminal) {
		super(ERROR_MESSAGE + terminal);
	}

	/**
	 * @param terminal terminal key
	 * @param cause exception that triggered this one
	 */
	public UnknownTerminalKeyException(String filename, Exception cause) {
		super(ERROR_MESSAGE + filename, cause);
	}
}