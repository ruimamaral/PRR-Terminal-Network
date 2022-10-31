package prr.core.pricetable;

import prr.core.communication.*;

public interface PriceTable {

	public abstract double getCost(TextCommunication textComm);

	public abstract double getCost(VoiceCommunication voiceComm);

	public abstract double getCost(VideoCommunication videoComm);
}
