package trydent.query.many;

import trydent.query.Many;
import trydent.query.Queryable;
import trydent.query.many.internal.Join;

import static trydent.type.Nullability.nonNullable;

public interface Joinable<T1> extends Queryable<T1> {
  default <T2> Joining<T1, T2> join(final Queryable<T2> second) {
    return new Join<>(this, nonNullable(second, "second"));
  }

  @SuppressWarnings("unchecked")
  default <T2> Joining<T1, T2> join(final T2... values) {
    return join(Many.from(values));
  }
}

