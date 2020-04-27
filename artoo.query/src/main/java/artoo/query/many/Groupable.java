package artoo.query.many;

import artoo.NotImplementedYetException;
import artoo.func.$2.ConsInt;
import artoo.func.Func;
import artoo.query.Queryable;
import artoo.query.many.impl.GroupBy;

import static artoo.type.Nullability.nonNullable;

public interface Groupable<T> extends Queryable<T> {
  default <K> Grouping<K, T> groupBy(final Func<? super T, ? extends K> key) {
    return new GroupBy<>(this, ConsInt.nothing(), nonNullable(key, "key"));
  }

  default <K1, K2> artoo.query.$2.many.Grouping<K1, K2, T> groupBy(
    final Func<? super T, ? extends K1> key1,
    final Func<? super T, ? extends K2> key2
  ) {
    throw new NotImplementedYetException("groupBy two keys");
    //return new GroupBy2<>(this, key1, key2);
  }
}
