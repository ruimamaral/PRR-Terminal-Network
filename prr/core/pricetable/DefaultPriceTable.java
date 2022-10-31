package prr.core.pricetable;

import java.io.Serial;
import java.util.function.DoubleFunction;

import prr.core.communication.*;

public class DefaultPriceTable implements PriceTable {

	@Serial
	private static final long serialVersionUID = 202210161305L;

	private double _videoRate;

	private double _voiceRate;

	private double _shortTextPrice;

	private double _mediumTextPrice;

	private DoubleFunction<Double> _longTextCostFormula;

	DefaultPriceTable(double videoRate,
			double voiceRate, double shortTextPrice,
			double mediumTextPrice, DoubleFunction<Double> longText) {

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
		return textComm.isFriendly() ? cost : cost / 2;
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

	
}
