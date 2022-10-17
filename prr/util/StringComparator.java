package prr.util;

import java.util.Comparator;
import java.io.Serial;
import java.io.Serializable;
/*
 * Not really necessary since String implements Comparable
 * (TreeMaps/Sets will use String.compareTo() as default)
 */
public class StringComparator implements Comparator<String>, Serializable {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	public int compare(String str1, String str2) {
		return str1.compareTo(str2);
	}
}
