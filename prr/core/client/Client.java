package prr.core.client;

import java.io.Serializable;

public class Client implements Serializable, Visitable {

	private static final long serialVersionUID = 202210161323L;

	private String _key;

	private String _name;

	private int _taxId;

	private ClientStatus _status;

	private boolean _notificationsOn;


	public abstract class ClientStatus implements Serializable, Visitable {

		public void changeStatus() throws SomeExceptions {}
	}



}
