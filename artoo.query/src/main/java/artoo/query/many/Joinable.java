package artoo.query.many;

import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.many.impl.Join;

import static artoo.type.Nullability.nonNullable;

public interface Joinable<T1> extends Queryable<T1> {
  default <T2> Joining<T1, T2> join(final Queryable<T2> second) {
    return new Join<>(this, nonNullable(second, "second"));
  }

  @SuppressWarnings("unchecked")
  default <T2> Joining<T1, T2> join(final T2... values) {
    return join(Many.from(values));
  }
}

