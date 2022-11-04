package prr.core.pricetable;

import java.io.Serializable;
import java.io.Serial;

import prr.core.communication.VideoCommunication;
import prr.core.communication.VoiceCommunication;
import prr.util.SerializableDoubleFunction;
import prr.core.communication.TextCommunication;

public class DefaultPricing implements PriceTable, Serializable {

	@Serial
	private static final long serialVersionUID = 202210161925L;

	private static PriceTable _normal;
	private static PriceTable _gold;
	private static PriceTable _platinum;

	private double _videoRate;

	private double _voiceRate;

	private double _shortTextPrice;

	private double _mediumTextPrice;

	private SerializableDoubleFunction<Double> _longTextCostFormula;

	private DefaultPricing(
			double videoRate, double voiceRate,
			double shortTextPrice, double mediumTextPrice,
			SerializableDoubleFunction<Double> longText) {

		this._videoRate = videoRate;
		this._voiceRate = voiceRate;
		this._shortTextPrice = shortTextPrice;
		this._mediumTextPrice = mediumTextPrice;
		this._longTextCostFormula = longText;
	}

	@Override
	public double getCost(TextCommunication textComm) {
		int units = textComm.getUnits();
		double cost;

		if (units < 50) {
			cost = _shortTextPrice;
		} else if (units < 100) {
			cost = _mediumTextPrice;
		} else {
			cost = this._longTextCostFormula.apply((double) units);
		}
		return cost;
	}

	@Override
	public double getCost(VoiceCommunication voiceComm) {
		double cost = this._voiceRate * voiceComm.getUnits();
		return voiceComm.isFriendly() ? cost : cost / 2;
	}

	@Override
	public double getCost(VideoCommunication videoComm) {
		double cost = this._videoRate * videoComm.getUnits();
		return videoComm.isFriendly() ? cost : cost / 2;
	}

	/*
	 * Default instance getters
	 */

	public static PriceTable getNormal() {
		if (_normal == null) {
			_normal = new DefaultPricing(30, 20, 10, 16, x -> 2 * x); 
		}
		return _normal;
	}

	public static PriceTable getGold() {
		if (_gold == null) {
			_gold = new DefaultPricing(20, 10, 10, 10, x -> 2 * x); 
		}
		return _gold;
	}

	public static PriceTable getPlatinum() {
		if (_platinum == null) {
			_platinum = new DefaultPricing(10, 10, 0, 4, x -> 4D); 
		}
		return _platinum;
	}
}
