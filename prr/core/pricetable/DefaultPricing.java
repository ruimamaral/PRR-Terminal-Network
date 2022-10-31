package prr.core.pricetable;

import java.io.Serial;

public class DefaultPricing {

	@Serial
	private static final long serialVersionUID = 202210161305L;

	private static PriceTable normal;
	private static PriceTable gold;
	private static PriceTable platinum;

	public static PriceTable getNormal() {
		if (normal == null) {
			normal = new DefaultPriceTable(30, 20, 10, 16, x -> 2 * x); 
		}
		return normal;
	}

	public static PriceTable getGold() {
		if (gold == null) {
			gold = new DefaultPriceTable(20, 10, 10, 10, x -> 2 * x); 
		}
		return gold;
	}

	public static PriceTable getPlatinum() {
		if (platinum == null) {
			platinum = new DefaultPriceTable(10, 10, 0, 4, x -> (double) 4); 
		}
		return platinum;
	}
	
}
