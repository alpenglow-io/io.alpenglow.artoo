package io.artoo.query.many;


import io.artoo.query.Many;
import io.artoo.query.Queryable;

import java.util.function.BiPredicate;

import static io.artoo.type.Nullability.nonNullable;

@FunctionalInterface
public interface Grouping<T extends Record, K> extends Queryable<T> {
  default Many<T> having(final BiPredicate<? super T, ? super Many<K>> having) {
    return new Having<>(this, nonNullable(having, "having"));
  }
/*

  default <R> Many<R> select(final BiFunction<? super K, ? super Many<T>, ? extends R> select) {
    return this.select(bag -> bag.as(select::apply));
  }
*/

  interface Bag<K, T> {}
}
