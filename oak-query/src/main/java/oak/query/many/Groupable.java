package oak.query.many;

import oak.NotImplementedYetException;
import oak.func.$2.IntCons;
import oak.func.Func;
import oak.query.Queryable;
import oak.query.many.internal.GroupBy;

import static oak.type.Nullability.nonNullable;

public interface Groupable<T> extends Queryable<T> {
  default <K> Grouping<K, T> groupBy(final Func<? super T, ? extends K> key) {
    return new GroupBy<>(this, IntCons.nothing(), nonNullable(key, "key"));
  }

  default <K1, K2> oak.query.$2.many.Grouping<K1, K2, T> groupBy(
    final Func<? super T, ? extends K1> key1,
    final Func<? super T, ? extends K2> key2
  ) {
    throw new NotImplementedYetException("groupBy two keys");
    //return new GroupBy2<>(this, key1, key2);
  }
}
