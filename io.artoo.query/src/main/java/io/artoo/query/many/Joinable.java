package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Join;

import static io.artoo.type.Nullability.nonNullable;

public interface Joinable<T1 extends Record> extends Queryable<T1> {
  default <T2 extends Record> Joining<T1, T2> join(final Queryable<T2> second) {
    return new Join<>(this, nonNullable(second, "second"));
  }

  @SuppressWarnings("unchecked")
  default <T2 extends Record> Joining<T1, T2> join(final T2... values) {
    return join(Many.from(values));
  }
}

