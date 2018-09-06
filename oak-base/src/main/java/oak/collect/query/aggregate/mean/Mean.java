package oak.collect.query.aggregate.mean;

import oak.func.sup.AsLong;

import static java.util.Objects.requireNonNull;

public interface Mean extends AsLong {
  static <T> Mean withHashCode(final Iterable<T> some) {
    return new HashCodeMean<>(requireNonNull(some, "Some is null"));
  }
}
