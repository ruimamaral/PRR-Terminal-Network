package prr.core.client;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import prr.core.notification.DefaultNotificationDeliveryMethod;
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

	// Set doesn't allow for duplicate notifications.
	private Set<Notification> _notifications = new LinkedHashSet<Notification>();

	public Client(String key, String name, int taxId) {
		this._key = key;
		this._name = name;
		this._notificationsOn = true; // default
		this._taxId = taxId;
		this._status = new NormalClientStatus(); // default
		this._debt = 0;
		this._totalPaid = 0;
		this._deliveryMethod =
				DefaultNotificationDeliveryMethod.getDefaultMethod();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Client) {
			return this.getKey().equalsIgnoreCase(((Client) other).getKey());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this._key.toUpperCase().hashCode();
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
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
	public double getTotalPaid() {
		return this._totalPaid;
	}
	public double getDebt() {
		return this._debt;
	}
	public PriceTable getPriceTable() {
		return this._status.getPriceTable();
	}
	public Collection<Notification> getNotifications() {
		return Collections.unmodifiableCollection(this._notifications);
	}

	public void resetNotifications() {
		this._notifications.clear();
	}

	public void turnOffNotifications() throws IllegalAccessException {
		if (!this.hasNotificationsEnabled()) {
			throw new IllegalAccessException();
		}
		this._notificationsOn = false;
	}

	public void turnOnNotifications() throws IllegalAccessException {
		if (this.hasNotificationsEnabled()) {
			throw new IllegalAccessException();
		}
		this._notificationsOn = true;
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
		this._debt -= amount;
		this._status.updateStatus();
	}

	public void endVideoCommunication() {
		this._status.endVideoCommunication();
	}

	public void endVoiceCommunication() {
		this._status.endVoiceCommunication();
	}

	public void sendTextCommunication() {
		this._status.sendTextCommunication();
	}

	/*
	 *
	 *  Inner Classes ------------------------------------------------
	 * 
	 */

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

		public void endVideoCommunication() {
			this._consecutiveVideoCommunications += 1;
			this._consecutiveTextCommunications = 0;
			this.updateStatus();
		}

		public void endVoiceCommunication() {
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
			if (Client.this.calculateBalance() > 500D) {
				this.setStatus(new GoldClientStatus());
			}
		}
	}
	public class GoldClientStatus extends Client.ClientStatus {

		@Serial
		private static final long serialVersionUID = 202211022106L;

		public GoldClientStatus() {
			super(DefaultPricing.getGold());
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
