package io.artoo.query.many;

import io.artoo.func.$2.Pred;
import io.artoo.query.Many;
import io.artoo.query.many.impl.Having;
import io.artoo.union.$2.Union;

import static io.artoo.type.Nullability.nonNullable;

@FunctionalInterface
public interface Grouping<K, T> extends Many<Grouping.Bag<K, T>> {
  default Many<Bag<K, T>> having(final Pred<? super K, ? super Many<T>> having) {
    return new Having<>(this, nonNullable(having, "having"));
  }

  default <R> Many<R> select(final io.artoo.func.$2.Func<? super K, ? super Many<T>, ? extends R> select) {
    return this.select(bag -> bag.as(select::apply));
  }

  interface Bag<K, T> extends Union<K, Many<T>> {}
}
