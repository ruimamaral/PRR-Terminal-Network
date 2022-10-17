package prr.util;

public interface Visitable {
	public abstract <T> T accept(Visitor<T> visitor);
}
