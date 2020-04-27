package artoo.query.many;

import artoo.func.$2.Pred;
import artoo.query.Many;
import artoo.query.many.impl.Having;
import artoo.union.$2.Union;

import static artoo.type.Nullability.nonNullable;

@FunctionalInterface
public interface Grouping<K, T> extends Many<Grouping.Bag<K, T>> {
  default Many<Bag<K, T>> having(final Pred<? super K, ? super Many<T>> having) {
    return new Having<>(this, nonNullable(having, "having"));
  }

  default <R> Many<R> select(final artoo.func.$2.Func<? super K, ? super Many<T>, ? extends R> select) {
    return this.select(bag -> bag.as(select::apply));
  }

  interface Bag<K, T> extends Union<K, Many<T>> {
  }
}
