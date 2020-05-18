package io.artoo.query.many;


import io.artoo.query.Many;

import java.util.function.BiPredicate;

import static io.artoo.type.Nullability.nonNullable;

@FunctionalInterface
public interface Grouping<K, T> extends Many<Grouping.Bag<K, T>> {
  default Many<Bag<K, T>> having(final BiPredicate<? super K, ? super Many<T>> having) {
    return new Having<>(this, nonNullable(having, "having"));
  }
/*

  default <R> Many<R> select(final BiFunction<? super K, ? super Many<T>, ? extends R> select) {
    return this.select(bag -> bag.as(select::apply));
  }
*/

  interface Bag<K, T> {}
}
