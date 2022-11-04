package prr.util;

import java.io.Serializable;
import java.util.function.DoubleFunction;

public interface SerializableDoubleFunction<R>
        extends Serializable, DoubleFunction<R> {
}
