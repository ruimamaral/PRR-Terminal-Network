package prr.core.client;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import prr.core.communication.Communication;
import prr.core.notification.DefaultMethodHolder;
import prr.core.notification.Notification;
import prr.core.notification.NotificationDeliveryMethod;
import prr.core.pricetable.DefaultPricing;
import prr.core.pricetable.PriceTable;
import prr.core.terminal.Terminal;
import prr.util.Visitable;
import prr.util.Visitor;

public class Client implements Serializable, Visitable {

	@Serial
	private static final long serialVersionUID = 202211022106L;

	private String _key;

	private String _name;

	private int _taxId;

	private ClientStatus _status;

	private boolean _notificationsOn;

	private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();

	private double _totalPaid;

	private double _debt;

	private NotificationDeliveryMethod _deliveryMethod;

	private List<Notification> _notifications = new ArrayList<Notification>();

	public Client(String key, String name, int taxId) {
		this._key = key;
		this._name = name;
		this._notificationsOn = true; // default
		this._taxId = taxId;
		this._status = new NormalClientStatus(); // default
		this._debt = 0;
		this._totalPaid = 0;
		this._deliveryMethod = DefaultMethodHolder.getDefaultMethod();
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Client) {
			return this.getKey().equalsIgnoreCase(((Client) other).getKey());
		}
		return false;
	}

	public String getKey() {
		return this._key;
	}
	public String getName() {
		return this._name;
	}
	public int getTaxId() {
		return this._taxId;
	}
	public boolean hasNotificationsEnabled() {
		return this._notificationsOn;
	}
	public String getStatusName() {
		return this._status.getName();
	}
	public int getTerminalCount() {
		return this._terminals.size();
	}
	public int getTotalPaid() {
		return (int) Math.round(this._totalPaid);
	}
	public int getDebt() {
		return (int) Math.round(this._debt);
	}
	public PriceTable getPriceTable() {
		return this._status.getPriceTable();
	}

	private double calculateBalance() {
		return this._totalPaid - this._debt;
	}

	public void pushNotification(Notification notification) {
		this._deliveryMethod.pushNotification(this, notification);
	}

	public void addTerminal(Terminal terminal) {
		this._terminals.put(terminal.getKey(), terminal);
	}

	public void addNotification(Notification notification) {
		this._notifications.add(notification);
	}

	public void addDebt(double amount) {
		this._debt += amount;
	}

	public void payAmount(double amount) {
		this._totalPaid += amount;
	}

	public void startVideoCommunication() {
		this._status.startVideoCommunication();
	}

	public void startVoiceCommunication() {
		this._status.startVoiceCommunication();
	}

	public void sendTextCommunication() {
		this._status.sendTextCommunication();
	}

	public abstract class ClientStatus implements Serializable {

		@Serial
		private static final long serialVersionUID = 202211022106L;

		private PriceTable _priceTable;

		private int _consecutiveVideoCommunications;

		private int _consecutiveTextCommunications;

		protected void setStatus(ClientStatus newStatus) {
			Client.this._status = newStatus;
		}

		protected ClientStatus(PriceTable priceTable) {
			this._priceTable = priceTable;
		}

		protected abstract String getName();

		protected int getConsecutiveVideoCommunications() {
			return this._consecutiveVideoCommunications;
		}

		protected int getConsecutiveTextCommunications() {
			return this._consecutiveTextCommunications;
		}

		protected PriceTable getPriceTable() {
			return this._priceTable;
		}

		protected abstract void updateStatus();

		public void startVideoCommunication() {
			this._consecutiveVideoCommunications += 1;
			this._consecutiveTextCommunications = 0;
			this.updateStatus();
		}

		public void startVoiceCommunication() {
			this._consecutiveVideoCommunications = 0;
			this._consecutiveTextCommunications = 0;
		}

		public void sendTextCommunication() {
			this._consecutiveVideoCommunications = 0;
			this._consecutiveTextCommunications += 1;
			this.updateStatus();
		}
	}

	public class NormalClientStatus extends Client.ClientStatus {

		@Serial
		private static final long serialVersionUID = 202211022106L;

		public NormalClientStatus() {
			super(DefaultPricing.getNormal());
		}
	
		public String getName() {
			return "NORMAL";
		}

		@Override
		protected void updateStatus() {
			if (Client.this.calculateBalance() > 500) {
				this.setStatus(new GoldClientStatus());
			}
		}
	}
	public class GoldClientStatus extends Client.ClientStatus {

		@Serial
		private static final long serialVersionUID = 202211022106L;

		public GoldClientStatus() {
			super(DefaultPricing.getNormal());
		}
	
		public String getName() {
			return "GOLD";
		}

		@Override
		protected void updateStatus() {
			if (Client.this.calculateBalance() < 0) {
				this.setStatus(new NormalClientStatus());
			} else if (this.getConsecutiveVideoCommunications() >= 5) {
				this.setStatus(new PlatinumClientStatus());
			}
		}
	}

	public class PlatinumClientStatus extends Client.ClientStatus {

		@Serial
		private static final long serialVersionUID = 202211022106L;

		public PlatinumClientStatus() {
			super(DefaultPricing.getPlatinum());
		}
	
		public String getName() {
			return "PLATINUM";
		}

		@Override
		protected void updateStatus() {
			if (Client.this.calculateBalance() < 0) {
				this.setStatus(new NormalClientStatus());
			} else if (this.getConsecutiveTextCommunications() >= 2) {
				this.setStatus(new GoldClientStatus());
			}
		}
	}
}
