package oak.func;

import java.util.function.IntFunction;
import java.util.function.LongFunction;

@FunctionalInterface
public interface LongFunc<R> extends Func<Long, R>, LongFunction<R> {
  R applyLong(final long value);

  @Override
  default R apply(final Long value) {
    return applyLong(value);
  }

  @Override
  default R apply(final long value) {
    return applyLong(value);
  }
}
