package prr.core.exception;

import java.io.Serial;

/**
 * Class that represents a problem with a client key
 * that is unknown in the app
 */
public class UnknownClientKeyException extends Exception {

	@Serial
	private static final long serialVersionUID = 202208091753L;

	private static final String ERROR_MESSAGE = "ID não corresponde a nenhum cliente: "; // FIXME sera que e necessaria mensagem?

	/**
	 * @param client client key
	 */
	public UnknownClientKeyException(String client) {
		super(ERROR_MESSAGE + client);
	}

	/**
	 * @param client client key
	 * @param cause exception that triggered this one
	 */
	public UnknownClientKeyException(String filename, Exception cause) {
		super(ERROR_MESSAGE + filename, cause);
	}
}
