package prr.util;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.text.Collator;
/*
 * Case-insensitive String comparator.
 */
public class StringComparator implements Comparator<String>, Serializable {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	private transient Collator _collator;

	public StringComparator() {
		this.initCollator();
	}

	/**
	 * Creates collator using default system locale and sets it's
	 * strength to PRIMARY in order for it to be case-insensitive.
	 */
	private void initCollator() {
		this._collator = Collator.getInstance();
		this._collator.setStrength(Collator.PRIMARY);
	}

	/**
	 * Initializes Collator using default system locale when reading
	 * serialization file.
	 */
	public void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.initCollator();
	}

	@Override
	public int compare(String str1, String str2) {
		return this._collator.compare(str1, str2);
	}
}
