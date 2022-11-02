package prr.core.pricetable;

import java.io.Serial;
import java.io.Serializable;

public class DefaultPricing implements Serializable {

	@Serial
	private static final long serialVersionUID = 202210311305L;

	private static PriceTable _normal;
	private static PriceTable _gold;
	private static PriceTable _platinum;

	// lazy initialization
	public static PriceTable getNormal() {
		if (_normal == null) {
			_normal = new DefaultPriceTable(30, 20, 10, 16, x -> 2 * x); 
		}
		return _normal;
	}

	public static PriceTable getGold() {
		if (_gold == null) {
			_gold = new DefaultPriceTable(20, 10, 10, 10, x -> 2 * x); 
		}
		return _gold;
	}

	public static PriceTable getPlatinum() {
		if (_platinum == null) {
			_platinum = new DefaultPriceTable(10, 10, 0, 4, x -> (double) 4); 
		}
		return _platinum;
	}
}
