package oak.query.many;

import oak.query.Queryable;

import static oak.type.Nullability.nonNullable;

public interface Joinable<T1> extends Queryable<T1> {
  default <T2> Joining<T1, T2> join(final Queryable<T2> second) {
    return new Join<>(this, nonNullable(second, "second"));
  }

  @SuppressWarnings("unchecked")
  default <T2> Joining<T1, T2> join(final T2... values) {
    return join(oak.query.Many.from(values));
  }
}

