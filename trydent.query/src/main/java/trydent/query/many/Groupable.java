package trydent.query.many;

import trydent.NotImplementedYetException;
import trydent.func.$2.ConsInt;
import trydent.func.Func;
import trydent.query.Queryable;
import trydent.query.many.internal.GroupBy;

import static trydent.type.Nullability.nonNullable;

public interface Groupable<T> extends Queryable<T> {
  default <K> Grouping<K, T> groupBy(final Func<? super T, ? extends K> key) {
    return new GroupBy<>(this, ConsInt.nothing(), nonNullable(key, "key"));
  }

  default <K1, K2> trydent.query.$2.many.Grouping<K1, K2, T> groupBy(
    final Func<? super T, ? extends K1> key1,
    final Func<? super T, ? extends K2> key2
  ) {
    throw new NotImplementedYetException("groupBy two keys");
    //return new GroupBy2<>(this, key1, key2);
  }
}
